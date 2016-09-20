package org.rapidoid.goodies;

import org.rapidoid.RapidoidThing;
import org.rapidoid.annotation.Authors;
import org.rapidoid.annotation.Since;
import org.rapidoid.commons.Env;
import org.rapidoid.commons.RapidoidInfo;
import org.rapidoid.io.Res;
import org.rapidoid.scan.ClasspathUtil;
import org.rapidoid.u.U;
import org.rapidoid.util.Msc;

import java.io.File;
import java.util.Map;
import java.util.concurrent.Callable;

/*
 * #%L
 * rapidoid-web
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
@Since("5.2.3")
public class StatusHandler extends RapidoidThing implements Callable<Object> {

	@Override
	public Map<String, ?> call() throws Exception {
		return U.map(
			"id", Msc.id(),
			"root", Res.root(),
			"jar", ClasspathUtil.appJar(),
			"jarExists", U.notEmpty(ClasspathUtil.appJar()) && new File(ClasspathUtil.appJar()).exists(),
			"version", Msc.maybeMasked(RapidoidInfo.version()),
			"notes", RapidoidInfo.notes(),
			"mode", Env.mode(),
			"profiles", Env.profiles(),
			"uptime", Msc.maybeMasked((RapidoidInfo.uptime() / 1000) + "s")
		);
	}

}
