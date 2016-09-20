package org.rapidoid.config;

import org.rapidoid.RapidoidThing;
import org.rapidoid.annotation.Authors;
import org.rapidoid.annotation.Since;
import org.rapidoid.beany.Beany;
import org.rapidoid.cls.Cls;
import org.rapidoid.collection.Coll;
import org.rapidoid.commons.Arr;
import org.rapidoid.commons.Env;
import org.rapidoid.log.Log;
import org.rapidoid.u.U;
import org.rapidoid.value.Value;
import org.rapidoid.value.Values;

import java.util.*;

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

@Authors("Nikolche Mihajlovski")
@Since("4.1.0")
public class ConfigImpl extends RapidoidThing implements Config {

	private final List<String> baseKeys;

	private final ConfigImpl root;

	private final ConfigBase base;

	private final boolean isRoot;

	public ConfigImpl() {
		this(null, false);
	}

	public ConfigImpl(String defaultFilenameBase) {
		this(defaultFilenameBase, false);
	}

	public ConfigImpl(String defaultFilenameBase, boolean useBuiltInDefaults) {
		this.base = new ConfigBase(defaultFilenameBase, useBuiltInDefaults);
		this.root = this;
		this.baseKeys = U.list();
		this.isRoot = true;
	}

	private ConfigImpl(ConfigBase base, List<String> baseKeys, ConfigImpl root) {
		this.base = base;
		this.root = root;
		this.baseKeys = Collections.unmodifiableList(U.list(baseKeys));
		this.isRoot = false;
	}

	@Override
	public synchronized void reset() {
		clear();
		base.reset();
	}

	@Override
	public void invalidate() {
		clear();
		base.invalidate();
	}

	@Override
	public Value<Object> entry(String key) {
		return Values.wrap(new ConfigValueStore<Object>(this, key));
	}

	private List<String> keyChain(Iterator<String> keys) {
		List<String> keyChain = U.list(this.baseKeys);

		while (keys.hasNext()) {
			String key = keys.next();
			U.notNull(key, "config key");
			Collections.addAll(keyChain, key.split("\\."));
		}

		return keyChain;
	}

	@Override
	@SuppressWarnings("unchecked")
	public Config sub(String... keys) {
		U.must(U.notEmpty(keys), "Keys must be specified!");
		return new ConfigImpl(base, keyChain(U.iterator(keys)), root());
	}

	@Override
	public Config sub(List<String> keys) {
		U.must(U.notEmpty(keys), "Keys must be specified!");
		return new ConfigImpl(base, keyChain(keys.iterator()), root());
	}

	@Override
	public Object get(String key) {
		makeSureIsInitialized();

		Object value;

		synchronized (base.properties) {
			value = asMap().get(key);
		}

		return value != null ? value : global(key);
	}

	private String global(String key) {
		String fullKey = fullKey(key, ".");

		String value = System.getProperty(fullKey);

		if (value == null) {
			value = System.getenv(fullKey);
		}

		if (value == null) {
			value = System.getenv(fullKey(key, "_").toUpperCase());
		}

		if (value == null) {
			value = System.getenv(fullKey(key, "_").toLowerCase());
		}

		return value;
	}

	private String fullKey(String key, String separator) {
		return U.join(separator, baseKeys) + separator + key;
	}

	@Override
	public boolean has(String key) {
		makeSureIsInitialized();

		synchronized (base.properties) {
			return asMap().containsKey(key);
		}
	}

	@Override
	public boolean is(String key) {
		makeSureIsInitialized();

		Object value;

		synchronized (base.properties) {
			value = asMap().get(key);
		}

		return Boolean.TRUE.equals(Cls.convert(value, Boolean.class));
	}

	@Override
	public Map<String, Object> toMap() {
		makeSureIsInitialized();

		return Collections.unmodifiableMap(asMap());
	}

	private Map<String, Object> asMap() {
		makeSureIsInitialized();

		if (isRoot) {
			return base.properties;

		} else {
			synchronized (base.properties) {
				Map<String, Object> props = base.properties;

				for (String key : baseKeys) {
					Object value = props.get(key);

					if (value == null) {
						value = Coll.synchronizedMap();
						props.put(key, value);
					}

					if (value instanceof Map<?, ?>) {
						props = (Map<String, Object>) value;
					} else {
						throw U.rte("Expected a Map for configuration section '%s', but found value of type: %s",
							sectionTo(key), value.getClass().getSimpleName());
					}
				}

				return props;
			}
		}
	}

	private String sectionTo(String toKey) {
		String section = "";

		for (String key : baseKeys) {
			if (!section.isEmpty()) {
				section += ".";
			}

			section += key;

			if (key.equals(toKey)) {
				break;
			}
		}

		return section;
	}

	@Override
	public void clear() {
		if (isRoot) {
			base.properties.clear();
		} else {
			synchronized (base.properties) {
				parent().remove(lastBaseKey());
			}
		}
	}

	private String lastBaseKey() {
		return baseKeys.get(baseKeys.size() - 1);
	}

	@Override
	public void set(String key, Object value) {
		makeSureIsInitialized();

		synchronized (base.properties) {
			asMap().put(key, value);
		}
	}

	@Override
	public void remove(String key) {
		makeSureIsInitialized();

		synchronized (base.properties) {
			asMap().remove(key);
		}
	}

	@Override
	public void assign(Map<String, Object> entries) {
		makeSureIsInitialized();

		synchronized (base.properties) {
			clear();
			update(entries);
		}
	}

	@Override
	public boolean isEmpty() {
		makeSureIsInitialized();

		synchronized (base.properties) {
			return asMap().isEmpty();
		}
	}

	@Override
	public void update(Map<String, ?> entries) {
		makeSureIsInitialized();

		update(entries, false);
	}

	@Override
	@SuppressWarnings("unchecked")
	public void update(Map<String, ?> entries, boolean overridenByEnv) {
		makeSureIsInitialized();

		synchronized (base.properties) {
			Map<String, Object> conf = asMap();

			for (Map.Entry<String, ?> e : entries.entrySet()) {
				String name = e.getKey();
				Object value = e.getValue();

				if (value instanceof Map<?, ?>) {
					sub(name).update((Map<String, ?>) value, overridenByEnv);

				} else {
					if (overridenByEnv) {
						value = U.or(global(name), value);
					}

					conf.put(name, value);
				}
			}
		}
	}

	@Override
	public String toString() {
		makeSureIsInitialized();

		synchronized (base.properties) {
			return asMap().toString();
		}
	}

	@Override
	public void args(List<String> args) {
		mustBeRoot();
		makeSureIsInitialized();

		if (U.notEmpty(args)) {
			for (String arg : args) {
				if (!arg.contains("->")) {
					String[] parts = arg.split("=", 2);
					String name = parts[0];

					if (parts.length > 1) {
						String value = parts[1];

						if (name.equals("config")) {
							setFilenameBase(value);
						}

						base.putArg(name, value);
					} else {
						base.putArg(name, true);
					}
				}
			}
		}
	}

	private void mustBeRoot() {
		U.must(isRoot, "Must be Config's root!");
	}

	@Override
	public void setNested(String key, Object value) {
		String[] keys = key.split("\\.");
		Config cfg = keys.length > 1 ? sub(Arr.sub(keys, 0, -1)) : this;
		cfg.set(U.last(keys), value);
	}

	@Override
	public ConfigImpl root() {
		return root;
	}

	@Override
	public Config parent() {
		return isRoot ? null : root.sub(baseKeys.subList(0, baseKeys.size() - 1));
	}

	@Override
	public List<String> keys() {
		return baseKeys;
	}

	@Override
	public Map<String, String> toFlatMap() {
		makeSureIsInitialized();

		Map<String, String> flatMap = U.map();
		Map<String, Object> map = toMap();

		traverseToFlat(map, U.list(keys()), flatMap);

		return flatMap;
	}

	private static void traverseToFlat(Map<String, Object> map, List<String> keys, Map<String, String> flatMap) {
		for (Map.Entry<String, Object> e : map.entrySet()) {
			String key = e.getKey();
			Object val = e.getValue();

			if (val instanceof Map<?, ?>) {
				Map<String, Object> mapVal = (Map<String, Object>) val;
				List<String> keys2 = U.list(keys);
				keys2.add(key);
				traverseToFlat(mapVal, keys2, flatMap);

			} else {
				flatMap.put(U.join(".", keys) + "." + key, String.valueOf(val));
			}
		}
	}

	@Override
	public Properties toProperties() {
		makeSureIsInitialized();

		Properties props = new Properties();
		props.putAll(toFlatMap());
		return props;
	}

	@Override
	public ConfigAlternatives or(Config alternative) {
		return new ConfigAlternatives(this, alternative);
	}

	@Override
	public String getFilenameBase() {
		return base.getFilenameBase();
	}

	@Override
	public Config setFilenameBase(String filenameBase) {

		if (base.setFilenameBase(filenameBase)) {
			invalidate(); // clear to apply changes
		}

		return this;
	}

	@Override
	public String getPath() {
		return base.getPath();
	}

	@Override
	public Config setPath(String path) {

		if (base.setPath(path)) {
			invalidate(); // clear to apply changes
		}

		return this;
	}

	@Override
	public void applyTo(Object target) {
		makeSureIsInitialized();
		Beany.update(target, toMap());
	}

	private void makeSureIsInitialized() {
		if (base.initializing) {
			return;
		}

		if (!base.initialized) {
			synchronized (this) {
				if (!base.initialized) {
					base.initializing = true;
					root.initialize();
					base.initialized = true;
				}
			}
		}
	}

	protected synchronized void initialize() {
		mustBeRoot();

		List<List<String>> detached = ConfigUtil.untrack();
		List<String> loaded = U.list();

		args(Env.args());
		base.applyArgsTo(this);

		if (useBuiltInDefaults()) {
			ConfigLoaderUtil.loadBuiltInConfig(this, loaded);
		}

		ConfigLoaderUtil.loadConfig(this, detached, loaded);

		base.applyArgsTo(this);
		Conf.applyConfig(this);

		if (!loaded.isEmpty()) {
			Log.info("Loaded configuration", "!files", loaded);
		} else {
			Log.warn("Didn't find any configuration files", "path", getPath());
		}
	}

	@Override
	public boolean useBuiltInDefaults() {
		return base.useBuiltInDefaults();
	}

	@Override
	public void updateNested(Map<String, Object> properties) {
		for (Map.Entry<String, Object> prop : properties.entrySet()) {
			setNested(prop.getKey(), prop.getValue());
		}
	}

}
