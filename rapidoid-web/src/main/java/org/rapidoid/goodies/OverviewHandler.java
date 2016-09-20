package org.rapidoid.goodies;

import org.rapidoid.annotation.Authors;
import org.rapidoid.annotation.Since;
import org.rapidoid.commons.Env;
import org.rapidoid.gui.GUI;
import org.rapidoid.scan.ClasspathUtil;
import org.rapidoid.setup.App;
import org.rapidoid.setup.On;
import org.rapidoid.u.U;

import java.util.List;
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
@Since("5.1.0")
public class OverviewHandler extends GUI implements Callable<Object> {

	@Override
	public Object call() throws Exception {
		List<Object> info = U.list();

		info.add(h3(center("Application info:")));

		Map<String, Object> appInfo = U.map();

		appInfo.put("Application JAR", ClasspathUtil.appJar());
		appInfo.put("Application path (root packages)", App.path());
		appInfo.put("Active profiles", Env.profiles());
		appInfo.put("Command line arguments", Env.args());

		info.add(grid(appInfo));

		info.add(h3(center("System metrics:")));
		info.add(GraphsHandler.graphs(3));

		info.add(h3(center("Application routes:")));

		info.add(RoutesHandler.routesOf(On.setup().routes().allNonAdmin(), false));

		return multi(info);
	}

}
