package org.rapidoid.config;

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

import org.rapidoid.annotation.Authors;
import org.rapidoid.annotation.Since;
import org.rapidoid.collection.Coll;
import org.rapidoid.log.Log;
import org.rapidoid.u.U;

import java.util.Map;

@Authors("Nikolche Mihajlovski")
@Since("5.2.0")
public class ConfigBase extends RapidoidInitializer {

	private final String defaultFilenameBase;

	private final boolean useBuiltInDefaults;

	final Map<String, Object> properties = Coll.synchronizedMap();

	final Map<String, Object> args = Coll.synchronizedMap();

	volatile boolean initializing;

	volatile boolean initialized;

	volatile String path = "";

	volatile String filenameBase;

	public ConfigBase(String defaultFilenameBase, boolean useBuiltInDefaults) {
		this.defaultFilenameBase = defaultFilenameBase;
		this.filenameBase = defaultFilenameBase;
		this.useBuiltInDefaults = useBuiltInDefaults;
	}

	synchronized void reset() {
		this.properties.clear();
		this.args.clear();

		this.filenameBase = this.defaultFilenameBase;
		this.path = "";

		this.initialized = false;
		this.initializing = false;
	}

	synchronized void invalidate() {
		this.properties.clear();

		this.initialized = false;
		this.initializing = false;
	}

	String getFilenameBase() {
		return filenameBase;
	}

	synchronized boolean setFilenameBase(String filenameBase) {

		if (U.neq(this.filenameBase, filenameBase)) {
			Log.info("Changing configuration filename base", "!from", this.filenameBase, "!to", filenameBase);

			this.filenameBase = filenameBase;
			return true;
		}

		return false;
	}

	boolean setPath(String path) {

		if (U.neq(this.path, path)) {
			Log.info("Changing configuration path", "!from", this.path, "!to", path);

			this.path = path;
			return true;
		}

		return false;
	}

	String getPath() {
		return path;
	}

	public boolean useBuiltInDefaults() {
		return useBuiltInDefaults;
	}

	void putArg(String name, Object value) {
		args.put(name, value);
	}

	void applyArgsTo(Config config) {
		config.updateNested(args);
	}

}
