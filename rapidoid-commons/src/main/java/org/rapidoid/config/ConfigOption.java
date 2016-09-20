package org.rapidoid.config;

import org.rapidoid.RapidoidThing;

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

/**
 * @author Nikolche Mihajlovski
 * @since 5.0.2
 */
public class ConfigOption extends RapidoidThing {

	public final String name;

	public final String desc;

	public final Object defaultValue;

	public ConfigOption(String name, String desc, Object defaultValue) {
		this.name = name;
		this.desc = desc;
		this.defaultValue = defaultValue;
	}

	public String getName() {
		return name;
	}

	public String getDesc() {
		return desc;
	}

	public Object getDefaultValue() {
		return defaultValue;
	}

}
