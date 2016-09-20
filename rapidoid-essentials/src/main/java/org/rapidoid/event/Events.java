package org.rapidoid.event;

/*
 * #%L
 * rapidoid-essentials
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

/**
 * @author Nikolche Mihajlovski
 * @since 5.2.0
 */
public enum Events implements Event {

	LOG_TRACE, LOG_DEBUG, LOG_INFO, LOG_WARN, LOG_ERROR,

	SESSION_LOAD, SESSION_SAVE, SESSION_SERIALIZE, SESSION_DESERIALIZE, SESSION_CONCURRENT_ACCESS;

	private volatile EventListener listener;

	@Override
	public EventListener listener() {
		return listener;
	}

	@Override
	public void listener(EventListener listener) {
		this.listener = listener;
	}

	public static void reset() {
		for (Events ev : Events.values()) {
			ev.listener(null);
		}
	}
}
