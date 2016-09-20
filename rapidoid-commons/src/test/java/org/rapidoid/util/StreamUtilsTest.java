package org.rapidoid.util;

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

import org.junit.Test;
import org.rapidoid.annotation.Authors;
import org.rapidoid.annotation.Since;
import org.rapidoid.test.TestCommons;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Authors("Nikolche Mihajlovski")
@Since("5.2.0")
public class StreamUtilsTest extends TestCommons {

	@Test
	public void testUri() throws IOException {
		check(123, 2, 34);
		check(0, -1, -44);
		check(Long.MAX_VALUE, 100023, Long.MIN_VALUE + 1);
		check(12345, -125521209, Long.MIN_VALUE);

		for (int i = 0; i < 1000000; i++) {
			check(RND.nextLong(), RND.nextLong(), RND.nextLong());
		}
	}

	public void check(long n1, long n2, long n3) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();

		StreamUtils.putNumAsText(out, n1);
		out.write(',');
		StreamUtils.putNumAsText(out, n2);
		out.write(':');
		StreamUtils.putNumAsText(out, n3);

		String s = new String(out.toByteArray());
		eq(s, n1 + "," + n2 + ":" + n3);
	}

}
