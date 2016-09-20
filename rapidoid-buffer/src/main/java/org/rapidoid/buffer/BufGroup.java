package org.rapidoid.buffer;

import org.rapidoid.RapidoidThing;
import org.rapidoid.annotation.Authors;
import org.rapidoid.annotation.Since;
import org.rapidoid.pool.Pool;
import org.rapidoid.pool.Pools;

import java.nio.ByteBuffer;
import java.util.concurrent.Callable;

/*
 * #%L
 * rapidoid-buffer
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
public class BufGroup extends RapidoidThing {

	private final int factor;

	private final int capacity;

	private final Pool<ByteBuffer> pool;

	private final boolean synchronizedBuffers;

	public BufGroup(int factor, boolean synchronizedBuffers) {
		this.synchronizedBuffers = synchronizedBuffers;
		this.factor = factor;
		this.capacity = (int) Math.pow(2, factor);

		pool = Pools.create("buffers", new Callable<ByteBuffer>() {
			@Override
			public ByteBuffer call() {
				return ByteBuffer.allocateDirect(capacity);
			}
		}, 1000);
	}

	public BufGroup(int factor) {
		this(factor, true);
	}

	public Buf newBuf(String name) {
		Buf buf = new MultiBuf(pool, factor, name);

		if (synchronizedBuffers) {
			buf = new SynchronizedBuf(buf);
		}

		return buf;
	}

	public Buf newBuf() {
		return newBuf("no-name");
	}

	public Buf from(String s, String name) {
		return from(ByteBuffer.wrap(s.getBytes()), name);
	}

	public Buf from(ByteBuffer bbuf, String name) {
		Buf buf = newBuf(name);
		buf.append(bbuf);
		return buf;
	}

	public int instances() {
		return pool.objectsCreated();
	}

	public void clear() {
		pool.clear();
	}

}
