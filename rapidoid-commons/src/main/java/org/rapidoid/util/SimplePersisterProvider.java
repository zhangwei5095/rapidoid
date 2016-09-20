package org.rapidoid.util;

import org.rapidoid.RapidoidThing;
import org.rapidoid.ctx.Ctx;
import org.rapidoid.ctx.PersisterProvider;
import org.rapidoid.log.Log;

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

public class SimplePersisterProvider extends RapidoidThing implements PersisterProvider {

	private final Object persistor;

	public SimplePersisterProvider(Object persistor) {
		this.persistor = persistor;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <P> P openPersister(Ctx ctx) {
		return (P) persistor;
	}

	@Override
	public void closePersister(Ctx ctx, Object persister) {
		Log.info("Closing persister", "persister", persister);
	}

}
