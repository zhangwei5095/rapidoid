package org.rapidoid.web;

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
import org.rapidoid.annotation.Required;
import org.rapidoid.annotation.Since;
import org.rapidoid.gui.GUI;
import org.rapidoid.http.IntegrationTestCommons;
import org.rapidoid.setup.On;
import org.rapidoid.u.U;

@Authors("Nikolche Mihajlovski")
@Since("5.1.0")
public class BeanFormRenderingTest extends IntegrationTestCommons {

	@Test
	public void testBeanFormBindingAndRendering() {
		On.page("/").html(() -> GUI.edit(new Dude("unknown", 100)));

		getAndPost("/");
		getAndPost("/?name=foo&age=12345"); // URL params are ignored

		postData("/?bad-age", U.map("name", "Mozart", "age", "123f"));
		postData("/?name=hey&age=77", U.map("name", "Bach"));
	}
}

class Dude {
	@Required
	String name;

	int age;

	Integer x;

	String desc;

	public Dude(String name, int age) {
		this.name = name;
		this.age = age;
	}
}
