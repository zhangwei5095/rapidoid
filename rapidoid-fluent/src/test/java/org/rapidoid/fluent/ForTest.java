package org.rapidoid.fluent;

import org.junit.Test;
import org.rapidoid.test.TestCommons;

import java.util.List;

/*
 * #%L
 * rapidoid-fluent
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
 * @since 5.0.0
 */
public class ForTest extends TestCommons {

	@Test
	public void testListOps() {
		List<String> items = New.list("a", "bbb", "cc");

		List<String> target = New.list();
		For.each(items).withNonNull(String::length).where(s -> s.length() < 3).run(target::add);

		eq(target, New.list("a", "cc"));
	}

}
