package org.rapidoid.collection;

import org.rapidoid.annotation.Authors;
import org.rapidoid.annotation.Since;
import org.rapidoid.lambda.Mapper;
import org.rapidoid.u.U;

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
@Since("5.1.0")
public class AutoExpandingMap<K, V> extends AbstractMapDecorator<K, V> {

	private final Mapper<K, V> valueFactory;

	public AutoExpandingMap(Mapper<K, V> valueFactory) {
		super(Coll.<K, V>synchronizedMap());
		this.valueFactory = valueFactory;
	}

	@SuppressWarnings("unchecked")
	@Override
	public V get(Object key) {
		V val = decorated.get(key);

		if (val == null) {
			synchronized (decorated) {
				val = decorated.get(key);

				if (val == null) {
					try {
						val = valueFactory.map((K) key);
					} catch (Exception e) {
						throw U.rte(e);
					}

					decorated.put((K) key, val);
				}

				return val;
			}
		}

		return val;
	}

	public AutoExpandingMap<K, V> copy() {
		return new AutoExpandingMap<K, V>(valueFactory);
	}

}
