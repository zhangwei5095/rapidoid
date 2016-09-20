package org.rapidoid.http;

/*
 * #%L
 * rapidoid-integration-tests
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

import org.junit.After;
import org.junit.Before;
import org.rapidoid.annotation.Authors;
import org.rapidoid.annotation.Since;
import org.rapidoid.commons.Arr;
import org.rapidoid.config.Conf;
import org.rapidoid.crypto.Crypto;
import org.rapidoid.data.JSON;
import org.rapidoid.fluent.Do;
import org.rapidoid.io.IO;
import org.rapidoid.ioc.IoC;
import org.rapidoid.jpa.JPA;
import org.rapidoid.jpa.JPAUtil;
import org.rapidoid.log.Log;
import org.rapidoid.log.LogLevel;
import org.rapidoid.reverseproxy.ProxyMapping;
import org.rapidoid.reverseproxy.Reverse;
import org.rapidoid.scan.ClasspathUtil;
import org.rapidoid.setup.*;
import org.rapidoid.sql.JDBC;
import org.rapidoid.test.RapidoidIntegrationTest;
import org.rapidoid.test.TestCommons;
import org.rapidoid.u.U;
import org.rapidoid.util.Msc;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

@Authors("Nikolche Mihajlovski")
@Since("5.1.0")
public abstract class IntegrationTestCommons extends TestCommons {

	// FIXME HEAD
	private static final List<String> HTTP_VERBS = U.list("GET", "DELETE", "OPTIONS", "TRACE", "POST", "PUT", "PATCH");

	public static final int DEFAULT_PORT = 8888;

	public static final String LOCALHOST = "http://localhost:8888";

	@Before
	public void openContext() {
		Msc.reset();

		TimeZone.setDefault(TimeZone.getTimeZone("CET"));

		ClasspathUtil.setRootPackage("some.nonexisting.app");

		TimeZone.setDefault(TimeZone.getTimeZone("CET"));

		System.out.println("--- STARTING SERVER ---");

		My.reset();
		JPAUtil.reset();
		Conf.ROOT.setPath(getTestName());
		Log.setLogLevel(LogLevel.INFO);
		IoC.reset();

		App.resetGlobalState();
		OnChanges.ignore();

		On.setup().listen();
		On.setup().reload();

		JDBC.execute(RapidoidIntegrationTest.HSQL_TRUNCATE);

		System.out.println("--- SERVER STARTED ---");

		verifyNoRoutes();
	}

	@After
	public void closeContext() {
		System.out.println("--- STOPPING SERVER ---");

		if (Admin.setup().isRunning()) {
			if (Admin.setup().port() == On.setup().port()) {
				Admin.setup().reset();
			} else {
				Admin.setup().shutdown();
			}
		}

		System.out.println("--- SERVER STOPPED ---");
	}

	protected String localhost(String uri) {
		return localhost(DEFAULT_PORT, uri);
	}

	protected String localhost(int port, String uri) {
		return "http://localhost:" + port + uri;
	}

	protected void defaultServerSetup() {
		On.get("/echo").serve((Req x) -> {
			x.response().contentType(MediaType.PLAIN_TEXT_UTF_8);
			return x.verb() + ":" + x.path() + ":" + x.query();
		});

		On.get("/hello").html("Hello");

		On.post("/upload").plain((Req x) -> {
			Log.info("Uploaded files", "files", x.files().keySet());

			boolean hasF3 = x.files().containsKey("f3");

			return U.join(":", x.cookies().get("foo"), x.cookies().get("COOKIE1"), x.posted().get("a"), x.files().size(),
				Crypto.md5(x.file("f1").content()),
				Crypto.md5(x.files().get("f2").get(0).content()),
				Crypto.md5(hasF3 ? x.file("f3").content() : new byte[0]));
		});

		On.req((Req x) -> x.response().html(U.join(":", x.verb(), x.path(), x.query())));
	}

	protected String resourceMD5(String filename) throws IOException, URISyntaxException {
		return Crypto.md5(IO.loadBytes(filename));
	}

	protected String get(String uri) {
		return HTTP.get(localhost(uri)).fetch();
	}

	protected byte[] getBytes(String uri) {
		return HTTP.get(localhost(uri)).execute().bodyBytes();
	}

	protected void onlyGet(String uri) {
		onlyGet(DEFAULT_PORT, uri);
	}

	protected void onlyGet(int port, String uri) {
		onlyReq(port, "GET", uri, null, null);
	}

	protected void onlyPost(String uri) {
		onlyPost(DEFAULT_PORT, uri, null);
	}

	protected void onlyPost(String uri, Map<String, ?> data) {
		onlyPost(DEFAULT_PORT, uri, data);
	}

	protected void onlyPost(int port, String uri, Map<String, ?> data) {
		onlyReq(port, "POST", uri, data, null);
	}

	protected void onlyPost(String uri, String json) {
		onlyReq(DEFAULT_PORT, "POST", uri, null, json);
	}

	protected void onlyPut(String uri) {
		onlyPut(DEFAULT_PORT, uri, null);
	}

	protected void onlyPut(int port, String uri, Map<String, ?> data) {
		onlyReq(port, "PUT", uri, data, null);
	}

	protected void onlyPut(String uri, Map<String, ?> data) {
		onlyPut(DEFAULT_PORT, uri, data);
	}

	protected void onlyPut(String uri, String json) {
		onlyReq(DEFAULT_PORT, "PUT", uri, null, json);
	}

	protected void onlyDelete(String uri) {
		onlyDelete(DEFAULT_PORT, uri);
	}

	protected void onlyDelete(int port, String uri) {
		onlyReq(port, "DELETE", uri, null, null);
	}

	protected void getAndPost(String uri) {
		getAndPost(DEFAULT_PORT, uri);
	}

	protected void getAndPost(int port, String uri) {
		testReq(port, "GET", uri, null, null);
		testReq(port, "POST", uri, null, null);
		notFoundExcept(port, uri, "GET", "POST");
	}

	protected void getReq(String uri) {
		testReq(DEFAULT_PORT, "GET", uri, null, null);
	}

	protected void postData(String uri, Map<String, ?> data) {
		testReq(DEFAULT_PORT, "POST", uri, data, null);
	}

	protected void postJson(String uri, Map<String, ?> data) {
		testReq(DEFAULT_PORT, "POST", uri, null, JSON.stringify(data));
	}

	protected void postData(int port, String uri, Map<String, ?> data) {
		testReq(port, "POST", uri, data, null);
	}

	protected void putData(String uri, Map<String, ?> data) {
		testReq(DEFAULT_PORT, "PUT", uri, data, null);
	}

	protected void putData(int port, String uri, Map<String, ?> data) {
		testReq(port, "PUT", uri, data, null);
	}

	protected void patchData(String uri, Map<String, ?> data) {
		testReq(DEFAULT_PORT, "PATCH", uri, data, null);
	}

	protected void patchData(int port, String uri, Map<String, ?> data) {
		testReq(port, "PATCH", uri, data, null);
	}

	private void onlyReq(int port, String verb, String uri, Map<String, ?> data, String json) {
		testReq(port, verb, uri, data, json);
		notFoundExcept(port, uri, verb);
	}

	protected void deleteReq(String uri) {
		testReq(DEFAULT_PORT, "DELETE", uri, null, null);
	}

	protected void notFoundExcept(String uri, String... exceptVerbs) {
		notFoundExcept(DEFAULT_PORT, uri, exceptVerbs);
	}

	protected void notFoundExcept(int port, String uri, String... exceptVerbs) {
		for (String verb : HTTP_VERBS) {
			if (Arr.indexOf(exceptVerbs, verb) < 0) {
				notFound(port, verb, uri);
			}
		}
	}

	protected void notFound(String uri) {
		notFound(DEFAULT_PORT, uri);
	}

	protected void notFound(int port, String uri) {
		notFoundExcept(port, uri);
	}

	protected void notFound(int port, String verb, String uri) {
		String resp = fetch(port, verb, uri, null, null);

		String notFound = U.notNull(IO.load("404-not-found.txt"), "404-not-found");
		String notFound2 = U.notNull(IO.load("404-not-found-json.txt"), "404-not-found-json");

		if (!httpResultsMatch(resp, notFound) && !httpResultsMatch(resp, notFound2)) {
			eq(resp, "!!! Expected (404 Not Found) HTTP response as HTML or JSON !!!!");
		}
	}

	private void testReq(int port, String verb, String uri, Map<String, ?> data, String json) {
		String resp = fetch(port, verb, uri, data, json);
		String reqName = reqName(port, verb, uri);

		verifyCase(port + " " + verb + " " + uri, resp, reqName);
	}

	protected String fetch(int port, String verb, String uri, Map<String, ?> data, String json) {
		HttpReq req = HTTP.verb(HttpVerb.from(verb)).url(localhost(port, uri)).data(data).body(json != null ? json.getBytes() : null);
		return exec(req);
	}

	protected String fetch(HttpClient client, int port, String verb, String uri, Map<String, ?> data) {
		return exec(client.req().verb(HttpVerb.from(verb)).url(localhost(port, uri)).data(data));
	}

	private String exec(HttpReq req) {
		req.raw(true);

		String resp = new String(req.execute().raw());
		resp = resp.replaceFirst("Date: .*? GMT", "Date: XXXXX GMT");

		req.raw(false);
		return resp;
	}

	protected String fetch(HttpClient client, String verb, String uri, Map<String, ?> data) {
		return fetch(client, DEFAULT_PORT, verb, uri, data);
	}

	protected String fetch(HttpClient client, String verb, String uri) {
		return fetch(client, DEFAULT_PORT, verb, uri, null);
	}

	protected String fetch(String verb, String uri) {
		return fetch(DEFAULT_PORT, verb, uri, null, null);
	}

	protected String fetch(String verb, String uri, Map<String, ?> data) {
		return fetch(DEFAULT_PORT, verb, uri, data, null);
	}

	private String reqName(int port, String verb, String uri) {
		String req = verb + uri.replace("/", "_").replace("?", "-");
		if (port != DEFAULT_PORT) {
			req = port + "__" + req;
		}

		return req;
	}

	protected static Map<String, Object> reqResp(Req req, Resp resp) {
		return U.map("verb", req.verb(), "uri", req.uri(), "data", req.data(), "code", resp.code());
	}

	protected String appRoutes() {
		List<String> routes = Do.map(On.routes().all()).to((Route r) -> r.toString());
		Collections.sort(routes);
		return U.join("\n", routes);
	}

	protected void verifyRoutes() {
		verify("routes", appRoutes());
	}

	protected void verifyRoutes(String name) {
		verify("routes-" + name, appRoutes());
	}

	protected void verifyNoRoutes() {
		isTrue(On.routes().all().isEmpty());
	}

	protected void tx(Runnable action) {
		JPA.transaction(action);
	}

	protected ProxyMapping proxy(String match, String upstreams) {
		return Reverse.proxy().map(match).to(upstreams);
	}

}
