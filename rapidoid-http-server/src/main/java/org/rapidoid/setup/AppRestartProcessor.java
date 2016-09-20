package org.rapidoid.setup;

/*
 * #%L
 * rapidoid-http-server
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

import org.rapidoid.annotation.Authors;
import org.rapidoid.annotation.Since;
import org.rapidoid.data.BufRange;
import org.rapidoid.data.BufRanges;
import org.rapidoid.http.processor.AbstractHttpProcessor;
import org.rapidoid.http.processor.HttpProcessor;
import org.rapidoid.net.abstracts.Channel;

@Authors("Nikolche Mihajlovski")
@Since("5.1.0")
public class AppRestartProcessor extends AbstractHttpProcessor {

	public AppRestartProcessor(Setup setup, HttpProcessor next) {
		super(next);
	}

	@Override
	public void onRequest(Channel channel, boolean isGet, boolean isKeepAlive, BufRange body,
	                      BufRange verb, BufRange uri, BufRange path, BufRange query, BufRange protocol, BufRanges headers) {

		App.restartIfDirty();

		next.onRequest(channel, isGet, isKeepAlive, body, verb, uri, path, query, protocol, headers);
	}

}
