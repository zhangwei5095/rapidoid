package org.rapidoid.jpa;

import org.hibernate.proxy.HibernateProxy;
import org.rapidoid.RapidoidThing;
import org.rapidoid.annotation.Authors;
import org.rapidoid.annotation.Since;
import org.rapidoid.cls.Cls;
import org.rapidoid.ctx.Ctx;
import org.rapidoid.ctx.Ctxs;
import org.rapidoid.log.Log;
import org.rapidoid.u.U;
import org.rapidoid.util.Msc;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.metamodel.EntityType;
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
public class JPAUtil extends RapidoidThing {

	static volatile EntityManagerFactory emf;

	static final List<String> entities = U.list();

	static final List<Class<?>> entityJavaTypes = U.list();

	public static void reset() {
		emf = null;
		entities.clear();
		entityJavaTypes.clear();
	}

	public static EntityManager em() {
		Ctx ctx = Ctxs.get();
		if (ctx != null) {
			return ctx.persister();
		} else {
			EntityManagerFactory emf = JPAUtil.emf;
			U.notNull(emf, "JPA.emf");
			return emf.createEntityManager();
		}
	}

	public static EntityManager currentEntityManager() {
		return Ctxs.required().persister();
	}

	public static void bootstrap(String[] path, Class<?>... providedEntities) {
		if (Cls.exists("org.hibernate.cfg.Configuration") && (emf() == null)) {
			Msc.logSection("Bootstrapping JPA (Hibernate)...");

			List<String> entityTypes = EMFUtil.createEMF(path, providedEntities);

			if (entityTypes.isEmpty()) {
				Log.info("Didn't find JPA entities, canceling JPA/Hibernate setup!");
				return;
			}

//			Msc.logSection("Hibernate properties:");
			Properties props = EMFUtil.hibernateProperties();
//			Msc.logProperties(props);

			Msc.logSection("Starting Hibernate:");

			CustomHibernatePersistenceProvider provider = new CustomHibernatePersistenceProvider();
			provider.names().addAll(entityTypes);

			EntityManagerFactory emf = provider.createEntityManagerFactory("rapidoid", props);
			emf(emf);

			Msc.logSection("JPA (Hibernate) is ready.");
		}
	}

	public static boolean isEntity(Object obj) {
		if (obj == null) {
			return false;
		}

		if (entities.contains(obj.getClass().getName())) {
			return true;
		}

		for (Class<?> type : entityJavaTypes) {
			if (type.isAssignableFrom(obj.getClass())) {
				return true;
			}
		}

		return false;
	}

	public static <T> T unproxy(T entity) {
		return Cls.exists("org.hibernate.proxy.HibernateProxy") ? _unproxy(entity) : entity;
	}

	private static <T> T _unproxy(T entity) {
		if (Cls.exists("org.hibernate.proxy.HibernateProxy") && entity instanceof HibernateProxy) {
			entity = (T) ((HibernateProxy) entity).getHibernateLazyInitializer().getImplementation();
		}

		return entity;
	}

	public static void emf(EntityManagerFactory emf) {
		U.notNull(emf, "emf");

		reset();
		JPAUtil.emf = emf;

		for (EntityType<?> entityType : emf.getMetamodel().getEntities()) {
			Class<?> type = entityType.getJavaType();
			entityJavaTypes.add(type);
			entities.add(type.getName());
		}
	}

	public static EntityManagerFactory emf() {
		return emf;
	}

	static <T> List<T> getPage(Query q, int start, int length) {
		q.setFirstResult(start);
		q.setMaxResults(length);

		return q.getResultList();
	}

}
