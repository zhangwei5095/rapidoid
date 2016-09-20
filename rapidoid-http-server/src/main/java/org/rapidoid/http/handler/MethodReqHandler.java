package org.rapidoid.http.handler;

import org.rapidoid.annotation.Authors;
import org.rapidoid.annotation.Since;
import org.rapidoid.cls.Cls;
import org.rapidoid.http.FastHttp;
import org.rapidoid.http.HttpRoutes;
import org.rapidoid.http.Req;
import org.rapidoid.http.handler.lambda.NParamMethodHandler;
import org.rapidoid.http.impl.RouteOptions;
import org.rapidoid.net.abstracts.Channel;
import org.rapidoid.u.U;

import java.lang.reflect.Method;

/*
 * #%L
 * rapidoid-http-server
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
public class MethodReqHandler extends NParamMethodHandler {

	private final Object instance;

	public MethodReqHandler(FastHttp http, HttpRoutes routes, RouteOptions options, Method method, Object instance) {
		super(http, routes, options, method, null);
		this.instance = instance;
	}

	@Override
	protected Object handleReq(Channel channel, boolean isKeepAlive, Req req, Object extra) throws Exception {
		Object result = Cls.invoke(method, instance, args(req));

		if (method.getReturnType() == void.class) {
			U.must(result == null);
			result = req;
		}

		return result;
	}

	@Override
	public String toString() {
		return contentTypeInfo(method.getDeclaringClass().getSimpleName() + "#" + method.getName() + paramsToString());
	}

}
