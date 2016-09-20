package org.rapidoid.concurrent;

import org.rapidoid.RapidoidThing;
import org.rapidoid.concurrent.impl.FutureImpl;
import org.rapidoid.lambda.Mapper;
import org.rapidoid.u.U;

import java.util.concurrent.TimeoutException;

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
 * @since 4.1.0
 */
public class Futures extends RapidoidThing {

	public static <FROM, TO> Future<TO> mapping(final Future<FROM> future, final Mapper<FROM, TO> mapper) {
		return new FutureImpl<TO>() {

			@Override
			public TO get(long timeoutMs, long sleepingIntervalMs) throws TimeoutException {
				try {
					return mapper.map(future.get(timeoutMs, sleepingIntervalMs));
				} catch (Exception e) {
					throw U.rte(e);
				}
			}

		};
	}

}
