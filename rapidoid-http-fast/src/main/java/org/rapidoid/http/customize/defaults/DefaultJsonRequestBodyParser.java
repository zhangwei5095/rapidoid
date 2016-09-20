package org.rapidoid.http.customize.defaults;

/*
 * #%L
 * rapidoid-http-fast
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

import org.rapidoid.RapidoidThing;
import org.rapidoid.annotation.Authors;
import org.rapidoid.annotation.Since;
import org.rapidoid.http.Req;
import org.rapidoid.http.customize.Customization;
import org.rapidoid.http.customize.JsonRequestBodyParser;

import java.util.Map;

@Authors("Nikolche Mihajlovski")
@Since("5.2.0")
public class DefaultJsonRequestBodyParser extends RapidoidThing implements JsonRequestBodyParser {

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, ?> parseJsonBody(Req req, byte[] body) throws Exception {
		return Customization.of(req).jackson().readValue(body, Map.class);
	}

}
