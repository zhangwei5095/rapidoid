package org.rapidoid.httpfast;

import org.junit.Test;
import org.rapidoid.annotation.Authors;
import org.rapidoid.annotation.Since;
import org.rapidoid.fluent.Flow;
import org.rapidoid.http.IntegrationTestCommons;
import org.rapidoid.setup.On;
import org.rapidoid.u.U;
import org.rapidoid.util.Msc;

/*
 * #%L
 * rapidoid-integration-tests
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
@Since("5.1.7")
public class InvalidUrlDecodedParamsTest extends IntegrationTestCommons {

	@Test
	public void testWithInvalidEncoding() {
		On.get("/").json(req -> U.map("uri", req.uri(), "query", req.query(), "data", req.data()));

		String resp = Msc.connect("localhost", 8888, (in, out) -> {
			out.writeBytes("GET /?a=[%A%]&b=bb!&c=%&d=%% HTTP/1.0\n\n");
			return Flow.of(in.lines()).findLast().get();
		});

		verify(resp);
	}

}
