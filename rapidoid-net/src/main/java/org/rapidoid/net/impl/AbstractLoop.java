package org.rapidoid.net.impl;

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

import org.rapidoid.activity.LifecycleActivity;
import org.rapidoid.annotation.Authors;
import org.rapidoid.annotation.Since;
import org.rapidoid.log.Log;
import org.rapidoid.u.U;

@Authors("Nikolche Mihajlovski")
@Since("2.0.0")
public abstract class AbstractLoop<T> extends LifecycleActivity<T> implements Runnable {

	protected volatile Thread ownerThread;

	protected volatile LoopStatus status = LoopStatus.INIT;

	public AbstractLoop(String name) {
		super(name);
	}

	@Override
	public void run() {
		this.ownerThread = Thread.currentThread();

		Log.debug("Starting event loop", "name", name);

		setStatus(LoopStatus.BEFORE_LOOP);

		try {
			beforeLoop();
		} catch (Throwable e) {
			Log.error("Error occured before loop is started", "name", name, "error", e);
			setStatus(LoopStatus.FAILED);
			return;
		}

		setStatus(LoopStatus.LOOP);

		while (status == LoopStatus.LOOP) {
			if (Thread.currentThread().isInterrupted()) {
				break;
			}

			try {
				insideLoop();
			} catch (Throwable e) {
				Log.error("Event loop exception in " + name, e);
			}
		}

		setStatus(LoopStatus.AFTER_LOOP);

		afterLoop();

		setStatus(LoopStatus.STOPPED);

		Log.debug("Stopped event loop", "name", name);
	}

	private void setStatus(LoopStatus status) {
		this.status = status;
	}

	protected synchronized void stopLoop() {
		Log.debug("Stopping event loop", "name", name);

		while (status == LoopStatus.INIT || status == LoopStatus.BEFORE_LOOP) {
			try {
				Thread.sleep(100);
				Log.debug("Waiting for event loop to initialize...", "name", name);
			} catch (InterruptedException e) {
				// ignore it, stopping anyway
			}
		}

		if (status == LoopStatus.LOOP) {
			status = LoopStatus.STOPPED;
		}

		Log.debug("Stopped event loop", "name", name);
	}

	protected void beforeLoop() {

	}

	protected abstract void insideLoop();

	protected void afterLoop() {

	}

	protected void assertStatus(LoopStatus expected) {
		if (status != expected) {
			throw new IllegalStateException("Expected status=" + expected + " for event loop: " + name);
		}
	}

	protected boolean onSameThread() {
		return ownerThread == Thread.currentThread();
	}

	protected void checkOnSameThread() {
		if (!onSameThread()) {
			throw U.rte("Not on the owner thread, expected %s, but found: %s", ownerThread, Thread.currentThread());
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public T start() {
		super.start();

		waitToStart();

		return (T) this;
	}

	public void waitToStart() {
		// wait for the event loop to activate
		while (status == LoopStatus.INIT || status == LoopStatus.BEFORE_LOOP) {
			U.sleep(50);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public T shutdown() {
		super.shutdown();

		waitToStop();

		return (T) this;
	}

	public void waitToStop() {
		// wait for the event loop to stop
		while (status != LoopStatus.STOPPED && status != LoopStatus.FAILED) {
			U.sleep(50);
		}
	}

}
