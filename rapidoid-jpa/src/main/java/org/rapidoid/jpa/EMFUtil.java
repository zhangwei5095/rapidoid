package org.rapidoid.jpa;

import org.rapidoid.RapidoidThing;
import org.rapidoid.annotation.Authors;
import org.rapidoid.annotation.Since;
import org.rapidoid.config.Conf;
import org.rapidoid.log.Log;
import org.rapidoid.scan.Scan;

import javax.persistence.Entity;
import java.util.List;
import java.util.Properties;

/*
 * #%L
 * rapidoid-jpa
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
public class EMFUtil extends RapidoidThing {

	public static synchronized List<String> createEMF(String path[], Class<?>... entities) {

		List<String> entityTypes = Scan.annotated(Entity.class).in(path).getAll();

		for (Class<?> entity : entities) {
			String type = entity.getName();
			if (!entityTypes.contains(type)) {
				entityTypes.add(type);
			}
		}

		if (!entityTypes.isEmpty()) {
			Log.info("!Found " + entityTypes.size() + " JPA Entities");
		}

		return entityTypes;
	}

	public static Properties hibernateProperties() {
		return Conf.HIBERNATE.toProperties();
	}

}
