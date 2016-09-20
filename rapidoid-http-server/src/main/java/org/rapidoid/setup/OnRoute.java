package org.rapidoid.setup;

import org.rapidoid.RapidoidThing;
import org.rapidoid.annotation.Authors;
import org.rapidoid.annotation.Since;
import org.rapidoid.annotation.TransactionMode;
import org.rapidoid.http.*;
import org.rapidoid.http.handler.HttpHandlers;
import org.rapidoid.http.impl.RouteOptions;
import org.rapidoid.lambda.*;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.Callable;

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
@Since("4.3.0")
public class OnRoute extends RapidoidThing {

	private final FastHttp http;

	private final HttpRoutes routes;

	private final String verb;

	private final String path;

	private final RouteOptions options;

	public OnRoute(FastHttp http, RouteOptions defaults, HttpRoutes routes, String verb, String path) {
		this.http = http;
		this.routes = routes;
		this.verb = verb;
		this.path = path;
		this.options = defaults.copy();
	}

	/* GENERIC */

	public void serve(String response) {
		if (options.managed()) {
			HttpHandlers.registerPredefined(http, routes, verb, path, options, response);
		} else {
			HttpHandlers.registerStatic(http, routes, verb, path, options, response.getBytes());
		}
	}

	public void serve(byte[] response) {
		if (options.managed()) {
			HttpHandlers.registerPredefined(http, routes, verb, path, options, response);
		} else {
			HttpHandlers.registerStatic(http, routes, verb, path, options, response);
		}
	}

	public <T> void serve(Callable<T> handler) {
		HttpHandlers.register(http, routes, verb, path, options, handler);
	}

	public void serve(Method method, Object instance) {
		HttpHandlers.register(http, routes, verb, path, options, method, instance);
	}

	public void serve(ReqHandler handler) {
		HttpHandlers.register(http, routes, verb, path, options, handler);
	}

	public void serve(ReqRespHandler handler) {
		HttpHandlers.register(http, routes, verb, path, options, handler);
	}

	public void serve(OneParamLambda<?, ?> handler) {
		HttpHandlers.register(http, routes, verb, path, options, handler);
	}

	public void serve(TwoParamLambda<?, ?, ?> handler) {
		HttpHandlers.register(http, routes, verb, path, options, handler);
	}

	public void serve(ThreeParamLambda<?, ?, ?, ?> handler) {
		HttpHandlers.register(http, routes, verb, path, options, handler);
	}

	public void serve(FourParamLambda<?, ?, ?, ?, ?> handler) {
		HttpHandlers.register(http, routes, verb, path, options, handler);
	}

	public void serve(FiveParamLambda<?, ?, ?, ?, ?, ?> handler) {
		HttpHandlers.register(http, routes, verb, path, options, handler);
	}

	public void serve(SixParamLambda<?, ?, ?, ?, ?, ?, ?> handler) {
		HttpHandlers.register(http, routes, verb, path, options, handler);
	}

	public void serve(SevenParamLambda<?, ?, ?, ?, ?, ?, ?, ?> handler) {
		HttpHandlers.register(http, routes, verb, path, options, handler);
	}

	/* HTML */

	public void html(String response) {
		HttpHandlers.registerPredefined(http, routes, verb, path, htmlOpts(), response);
	}

	public void html(byte[] response) {
		HttpHandlers.registerPredefined(http, routes, verb, path, htmlOpts(), response);
	}

	public <T> void html(Callable<T> handler) {
		HttpHandlers.register(http, routes, verb, path, htmlOpts(), handler);
	}

	public void html(Method method, Object instance) {
		HttpHandlers.register(http, routes, verb, path, htmlOpts(), method, instance);
	}

	public void html(ReqHandler handler) {
		HttpHandlers.register(http, routes, verb, path, htmlOpts(), handler);
	}

	public void html(ReqRespHandler handler) {
		HttpHandlers.register(http, routes, verb, path, htmlOpts(), handler);
	}

	public void html(OneParamLambda<?, ?> handler) {
		HttpHandlers.register(http, routes, verb, path, htmlOpts(), handler);
	}

	public void html(TwoParamLambda<?, ?, ?> handler) {
		HttpHandlers.register(http, routes, verb, path, htmlOpts(), handler);
	}

	public void html(ThreeParamLambda<?, ?, ?, ?> handler) {
		HttpHandlers.register(http, routes, verb, path, htmlOpts(), handler);
	}

	public void html(FourParamLambda<?, ?, ?, ?, ?> handler) {
		HttpHandlers.register(http, routes, verb, path, htmlOpts(), handler);
	}

	public void html(FiveParamLambda<?, ?, ?, ?, ?, ?> handler) {
		HttpHandlers.register(http, routes, verb, path, htmlOpts(), handler);
	}

	public void html(SixParamLambda<?, ?, ?, ?, ?, ?, ?> handler) {
		HttpHandlers.register(http, routes, verb, path, htmlOpts(), handler);
	}

	public void html(SevenParamLambda<?, ?, ?, ?, ?, ?, ?, ?> handler) {
		HttpHandlers.register(http, routes, verb, path, htmlOpts(), handler);
	}

	/* JSON */

	public void json(String response) {
		HttpHandlers.registerPredefined(http, routes, verb, path, jsonOpts(), response);
	}

	public void json(byte[] response) {
		HttpHandlers.registerPredefined(http, routes, verb, path, jsonOpts(), response);
	}

	public <T> void json(Callable<T> handler) {
		HttpHandlers.register(http, routes, verb, path, jsonOpts(), handler);
	}

	public void json(Method method, Object instance) {
		HttpHandlers.register(http, routes, verb, path, jsonOpts(), method, instance);
	}

	public void json(ReqHandler handler) {
		HttpHandlers.register(http, routes, verb, path, jsonOpts(), handler);
	}

	public void json(ReqRespHandler handler) {
		HttpHandlers.register(http, routes, verb, path, jsonOpts(), handler);
	}

	public void json(OneParamLambda<?, ?> handler) {
		HttpHandlers.register(http, routes, verb, path, jsonOpts(), handler);
	}

	public void json(TwoParamLambda<?, ?, ?> handler) {
		HttpHandlers.register(http, routes, verb, path, jsonOpts(), handler);
	}

	public void json(ThreeParamLambda<?, ?, ?, ?> handler) {
		HttpHandlers.register(http, routes, verb, path, jsonOpts(), handler);
	}

	public void json(FourParamLambda<?, ?, ?, ?, ?> handler) {
		HttpHandlers.register(http, routes, verb, path, jsonOpts(), handler);
	}

	public void json(FiveParamLambda<?, ?, ?, ?, ?, ?> handler) {
		HttpHandlers.register(http, routes, verb, path, jsonOpts(), handler);
	}

	public void json(SixParamLambda<?, ?, ?, ?, ?, ?, ?> handler) {
		HttpHandlers.register(http, routes, verb, path, jsonOpts(), handler);
	}

	public void json(SevenParamLambda<?, ?, ?, ?, ?, ?, ?, ?> handler) {
		HttpHandlers.register(http, routes, verb, path, jsonOpts(), handler);
	}

	/* PLAIN */

	public void plain(String response) {
		HttpHandlers.registerPredefined(http, routes, verb, path, plainOpts(), response);
	}

	public void plain(byte[] response) {
		HttpHandlers.registerPredefined(http, routes, verb, path, plainOpts(), response);
	}

	public <T> void plain(Callable<T> handler) {
		HttpHandlers.register(http, routes, verb, path, plainOpts(), handler);
	}

	public void plain(Method method, Object instance) {
		HttpHandlers.register(http, routes, verb, path, plainOpts(), method, instance);
	}

	public void plain(ReqHandler handler) {
		HttpHandlers.register(http, routes, verb, path, plainOpts(), handler);
	}

	public void plain(ReqRespHandler handler) {
		HttpHandlers.register(http, routes, verb, path, plainOpts(), handler);
	}

	public void plain(OneParamLambda<?, ?> handler) {
		HttpHandlers.register(http, routes, verb, path, plainOpts(), handler);
	}

	public void plain(TwoParamLambda<?, ?, ?> handler) {
		HttpHandlers.register(http, routes, verb, path, plainOpts(), handler);
	}

	public void plain(ThreeParamLambda<?, ?, ?, ?> handler) {
		HttpHandlers.register(http, routes, verb, path, plainOpts(), handler);
	}

	public void plain(FourParamLambda<?, ?, ?, ?, ?> handler) {
		HttpHandlers.register(http, routes, verb, path, plainOpts(), handler);
	}

	public void plain(FiveParamLambda<?, ?, ?, ?, ?, ?> handler) {
		HttpHandlers.register(http, routes, verb, path, plainOpts(), handler);
	}

	public void plain(SixParamLambda<?, ?, ?, ?, ?, ?, ?> handler) {
		HttpHandlers.register(http, routes, verb, path, plainOpts(), handler);
	}

	public void plain(SevenParamLambda<?, ?, ?, ?, ?, ?, ?, ?> handler) {
		HttpHandlers.register(http, routes, verb, path, plainOpts(), handler);
	}

	/* MVC */

	public void mvc(String response) {
		HttpHandlers.registerPredefined(http, routes, verb, path, mvcOpts(), response);
	}

	public void mvc(Collection<?> response) {
		HttpHandlers.registerPredefined(http, routes, verb, path, mvcOpts(), response);
	}

	public void mvc(Map<?, ?> response) {
		HttpHandlers.registerPredefined(http, routes, verb, path, mvcOpts(), response);
	}

	public <T> void mvc(Callable<T> handler) {
		HttpHandlers.register(http, routes, verb, path, mvcOpts(), handler);
	}

	public void mvc(Method method, Object instance) {
		HttpHandlers.register(http, routes, verb, path, mvcOpts(), method, instance);
	}

	public void mvc(ReqHandler handler) {
		HttpHandlers.register(http, routes, verb, path, mvcOpts(), handler);
	}

	public void mvc(ReqRespHandler handler) {
		HttpHandlers.register(http, routes, verb, path, mvcOpts(), handler);
	}

	public void mvc(OneParamLambda<?, ?> handler) {
		HttpHandlers.register(http, routes, verb, path, mvcOpts(), handler);
	}

	public void mvc(TwoParamLambda<?, ?, ?> handler) {
		HttpHandlers.register(http, routes, verb, path, mvcOpts(), handler);
	}

	public void mvc(ThreeParamLambda<?, ?, ?, ?> handler) {
		HttpHandlers.register(http, routes, verb, path, mvcOpts(), handler);
	}

	public void mvc(FourParamLambda<?, ?, ?, ?, ?> handler) {
		HttpHandlers.register(http, routes, verb, path, mvcOpts(), handler);
	}

	public void mvc(FiveParamLambda<?, ?, ?, ?, ?, ?> handler) {
		HttpHandlers.register(http, routes, verb, path, mvcOpts(), handler);
	}

	public void mvc(SixParamLambda<?, ?, ?, ?, ?, ?, ?> handler) {
		HttpHandlers.register(http, routes, verb, path, mvcOpts(), handler);
	}

	public void mvc(SevenParamLambda<?, ?, ?, ?, ?, ?, ?, ?> handler) {
		HttpHandlers.register(http, routes, verb, path, mvcOpts(), handler);
	}
	
	/* CONTENT TYPE */

	private RouteOptions htmlOpts() {
		return opts(MediaType.HTML_UTF_8);
	}

	private RouteOptions jsonOpts() {
		return opts(MediaType.JSON);
	}

	private RouteOptions plainOpts() {
		return opts(MediaType.PLAIN_TEXT_UTF_8);
	}

	private RouteOptions mvcOpts() {
		return opts(MediaType.HTML_UTF_8).mvc(true);
	}

	private RouteOptions opts(MediaType contentType) {
		options.contentType(contentType);
		return options;
	}

	/* ROUTE OPTIONS */

	public OnRoute wrappers(HttpWrapper... wrappers) {
		options.wrappers(wrappers);
		return this;
	}

	public OnRoute roles(String... roles) {
		options.roles(roles);
		return this;
	}

	public OnRoute view(String viewName) {
		options.view(viewName);
		return this;
	}

	public OnRoute contentType(MediaType contentType) {
		options.contentType(contentType);
		return this;
	}

	public OnRoute tx(TransactionMode txMode) {
		options.transactionMode(txMode);
		return this;
	}

	public OnRoute tx() {
		return tx(TransactionMode.AUTO);
	}

	public OnRoute zone(String zone) {
		options.zone(zone);
		return this;
	}

	public OnRoute managed(boolean managed) {
		options.managed(managed);
		return this;
	}

}
