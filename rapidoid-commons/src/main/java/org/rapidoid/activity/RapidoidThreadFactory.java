package org.rapidoid.activity;

import org.rapidoid.RapidoidThing;
import org.rapidoid.annotation.Authors;
import org.rapidoid.annotation.Since;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicLong;

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
@Since("4.1.0")
public class RapidoidThreadFactory extends RapidoidThing implements ThreadFactory {

	private final String name;

	private final boolean daemons;

	private final AtomicLong counter = new AtomicLong();

	public RapidoidThreadFactory(String name, boolean daemons) {
		this.name = name;
		this.daemons = daemons;
	}

	@Override
	public Thread newThread(Runnable runnable) {
		RapidoidThread thread = new RapidoidThread(runnable);

		thread.setName(name + counter.incrementAndGet());
		thread.setDaemon(daemons);

		return thread;
	}

}
