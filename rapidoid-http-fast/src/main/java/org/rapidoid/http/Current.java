package org.rapidoid.http;

/*
 * #%L
 * rapidoid-http-fast
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

import org.rapidoid.RapidoidThing;
import org.rapidoid.annotation.Authors;
import org.rapidoid.annotation.Since;
import org.rapidoid.ctx.Ctx;
import org.rapidoid.ctx.Ctxs;
import org.rapidoid.ctx.UserInfo;
import org.rapidoid.u.U;

import java.util.Set;

@Authors("Nikolche Mihajlovski")
@Since("5.1.0")
public class Current extends RapidoidThing {

	public static boolean hasContext() {
		return Ctxs.get() != null;
	}

	public static UserInfo user() {
		Ctx ctx = Ctxs.get();
		UserInfo user = ctx != null ? ctx.user() : null;
		return U.or(user, UserInfo.ANONYMOUS);
	}

	public static boolean isLoggedIn() {
		return user().username != null;
	}

	public static String username() {
		return user().username;
	}

	public static Set<String> roles() {
		return user().roles;
	}

	public static Req request() {
		return Ctxs.required().exchange();
	}

}
