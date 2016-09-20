package org.rapidoid.net.impl;

import org.rapidoid.RapidoidThing;
import org.rapidoid.annotation.Authors;
import org.rapidoid.annotation.Since;
import org.rapidoid.buffer.Buf;
import org.rapidoid.buffer.BufGroup;
import org.rapidoid.data.JSON;
import org.rapidoid.expire.Expiring;
import org.rapidoid.net.Protocol;
import org.rapidoid.net.abstracts.Channel;
import org.rapidoid.net.abstracts.IRequest;
import org.rapidoid.u.U;
import org.rapidoid.util.Constants;
import org.rapidoid.util.Resetable;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.concurrent.atomic.AtomicLong;

/*
 * #%L
 * rapidoid-net
 * %%
 * Copyright (C) 2014 - 2016 Nikolche Mihajlovski and contributors
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

@Authors("Nikolche Mihajlovski")
@Since("2.0.0")
public class RapidoidConnection extends RapidoidThing implements Resetable, Channel, Expiring, Constants {

	private static final CtxListener IGNORE = new IgnorantConnectionListener();

	private static final AtomicLong ID_N = new AtomicLong();

	public final RapidoidWorker worker;

	public final Buf input;

	public final Buf output;

	private final ConnState state = new ConnState();

	private volatile boolean waitingToWrite = false;

	public volatile SelectionKey key;

	private volatile boolean closeAfterWrite = false;

	public volatile boolean closed = true;

	public volatile boolean closing = false;

	volatile int completedInputPos;

	private volatile CtxListener listener;

	private final long id = ID_N.incrementAndGet();

	private volatile boolean initial;

	private volatile boolean async;

	volatile boolean done;

	private volatile boolean isClient;

	private volatile Protocol protocol;

	volatile long requestId;

	volatile IRequest request;

	private volatile long expiresAt;

	public RapidoidConnection(RapidoidWorker worker, BufGroup bufs) {
		this.worker = worker;
		this.input = bufs.newBuf("input#" + connId());
		this.output = bufs.newBuf("output#" + connId());
		reset();
	}

	@Override
	public synchronized void reset() {
		IRequest req = request;
		if (req != null) {
			req.stop();
			request = null;
		}

		key = null;
		closed = true;
		closing = false;
		input.clear();
		output.clear();
		closeAfterWrite = false;
		waitingToWrite = false;
		completedInputPos = 0;
		listener = IGNORE;
		initial = true;
		async = false;
		done = false;
		isClient = false;
		protocol = null;
		requestId = 0;
		expiresAt = 0;
		state.reset();
	}

	@Override
	public void log(String msg) {
		state().log(msg);
	}

	@Override
	public synchronized InetSocketAddress getAddress() {
		SocketChannel socketChannel = (SocketChannel) key.channel();
		SocketAddress addr = socketChannel.socket().getRemoteSocketAddress();
		if (addr instanceof InetSocketAddress) {
			InetSocketAddress address = (InetSocketAddress) addr;
			return address;
		} else {
			throw new IllegalStateException("Cannot get remote address!");
		}
	}

	@Override
	public synchronized Channel write(String s) {
		output.append(s);
		return this;
	}

	@Override
	public synchronized Channel writeln(String s) {
		output.append(s);
		output.append(CR_LF);
		return this;
	}

	@Override
	public synchronized Channel write(byte[] bytes) {
		return write(bytes, 0, bytes.length);
	}

	@Override
	public synchronized Channel write(byte[] bytes, int offset, int length) {
		output.append(bytes, offset, length);
		return this;
	}

	@Override
	public synchronized Channel write(ByteBuffer buf) {
		output.append(buf);
		return this;
	}

	@Override
	public synchronized Channel write(File file) {
		try {
			FileInputStream stream = new FileInputStream(file);
			FileChannel fileChannel = stream.getChannel();
			output.append(fileChannel);
			stream.close();
		} catch (IOException e) {
			throw U.rte(e);
		}

		return this;
	}

	@Override
	public Channel writeJSON(Object value) {
		JSON.stringify(value, output.asOutputStream());
		return this;
	}

	public boolean closeAfterWrite() {
		return closeAfterWrite;
	}

	@Override
	public Channel done() {
		done(null);
		return this;
	}

	public synchronized void done(Object tag) {
		async = false;
		if (!done) {
			done = true;
			askToSend();

//			if (tag != null) {
//				listener().onDone(this, tag);
//			}
		}
	}

	@Override
	public Channel send() {
		askToSend();
		return this;
	}

	public void error() {
		askToSend();
	}

	private synchronized void askToSend() {
		if (!waitingToWrite && output.size() > 0) {
			waitingToWrite = true;
			worker.wantToWrite(this);
		}
	}

	public synchronized void close(boolean waitToWrite) {
		if (waitToWrite) {
			done();
		}

		if (waitToWrite && waitingToWrite) {
			closeAfterWrite = true;
		} else {
			worker.close(this);
		}
	}

	public synchronized void wrote(boolean complete) {
		if (complete) {
			waitingToWrite = false;
		}

		input.deleteBefore(completedInputPos);
		completedInputPos = 0;
	}

	@Override
	public Buf input() {
		return input;
	}

	@Override
	public Buf output() {
		return output;
	}

	@Override
	public boolean onSameThread() {
		return worker.onSameThread();
	}

	@Override
	public RapidoidHelper helper() {
		return worker.helper;
	}

	public CtxListener listener() {
		return listener;
	}

	public void setListener(CtxListener listener) {
		this.listener = listener;
	}

	@Override
	public String address() {
		return getAddress().getAddress().getHostAddress();
	}

	@Override
	public Channel close() {
		close(true);
		return this;
	}

	@Override
	public Channel closeIf(boolean condition) {
		if (condition) {
			close();
		}
		return this;
	}

	@Override
	public String readln() {
		return input().readLn();
	}

	@Override
	public String readN(int count) {
		return input().readN(count);
	}

	@Override
	public ConnState state() {
		return state;
	}

	@Override
	public boolean isInitial() {
		return initial;
	}

	@Override
	public String toString() {
		return "conn#" + connId();
	}

	public void setInitial(boolean initial) {
		this.initial = initial;
	}

	@Override
	public synchronized Channel async() {
		this.async = true;
		this.done = false;
		return this;
	}

	@Override
	public synchronized boolean isAsync() {
		return async;
	}

	public boolean isClient() {
		return isClient;
	}

	public void setClient(boolean isClient) {
		this.isClient = isClient;
	}

	public void setProtocol(Protocol protocol) {
		this.protocol = protocol;
	}

	public Protocol getProtocol() {
		return protocol;
	}

	@Override
	public boolean isClosing() {
		return closing;
	}

	@Override
	public boolean isClosed() {
		return closed;
	}

	@Override
	public void waitUntilClosing() {
		if (!isClosing()) {
			throw Buf.INCOMPLETE_READ;
		}
	}

	@Override
	public long connId() {
		return id;
	}

	@Override
	public long requestId() {
		return requestId;
	}

	@Override
	public void setRequest(IRequest request) {
		this.request = request;
	}

	@Override
	public void setExpiresAt(long expiresAt) {
		this.expiresAt = expiresAt;
	}

	@Override
	public long getExpiresAt() {
		return expiresAt;
	}

	@Override
	public void expire() {
		close(false);
	}

}
