package org.rapidoid.webapp1;

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

import org.junit.Test;
import org.rapidoid.annotation.Authors;
import org.rapidoid.annotation.Run;
import org.rapidoid.annotation.Since;
import org.rapidoid.http.IntegrationTestCommons;
import org.rapidoid.setup.App;
import org.rapidoid.setup.On;

@Authors("Nikolche Mihajlovski")
@Since("5.1.0")
public class HttpMainEntryTest extends IntegrationTestCommons {

	@Test
	public void testSequentialControllerRegistration() {
		App.path(path());
		App.scan();
		On.get("/a").plain("A");

		onlyGet("/a");
		onlyGet("/b");
	}

}

@Run
class EntryPoint {
	public static void main(String[] args) {
		On.get("/b").plain("B");
	}
}
