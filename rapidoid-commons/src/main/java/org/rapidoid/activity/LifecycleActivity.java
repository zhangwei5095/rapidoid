package org.rapidoid.activity;

import org.rapidoid.annotation.Authors;
import org.rapidoid.annotation.Since;
import org.rapidoid.u.U;

import java.util.concurrent.atomic.AtomicBoolean;

/*
 * #%L
 * rapidoid-commons
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
public abstract class LifecycleActivity<T> extends NamedActivity<T> {

	private final AtomicBoolean active = new AtomicBoolean(false);

	public LifecycleActivity(String name) {
		super(name);
	}

	@SuppressWarnings("unchecked")
	@Override
	public T start() {
		checkActive(false);
		active.set(true);
		return (T) this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T halt() {
		checkActive(true);
		active.set(false);
		return (T) this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T shutdown() {
		checkActive(true);
		active.set(false);
		return (T) this;
	}

	@Override
	public boolean isActive() {
		return active.get();
	}

	protected void checkActive(boolean active) {
		if (active) {
			U.must(isActive(), "The activity is not active!");
		} else {
			U.must(!isActive(), "The activity is already active!");
		}
	}

}
