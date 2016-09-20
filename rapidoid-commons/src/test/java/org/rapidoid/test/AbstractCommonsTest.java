package org.rapidoid.test;

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

import org.junit.Before;
import org.rapidoid.config.Conf;
import org.rapidoid.ctx.Ctxs;
import org.rapidoid.data.JSON;
import org.rapidoid.u.U;
import org.rapidoid.util.Msc;

public abstract class AbstractCommonsTest extends TestCommons {

	@Before
	public final void resetContext() {
		Msc.reset();

		Ctxs.reset();
		U.must(Ctxs.get() == null);

		Conf.ROOT.setPath(getTestName());
	}

	protected void verify(String name, Object actual) {
		super.verifyCase(name, JSON.prettify(actual), name);
	}

}
