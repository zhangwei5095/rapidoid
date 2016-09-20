package org.rapidoid.http.impl;

import org.rapidoid.RapidoidThing;
import org.rapidoid.annotation.Authors;
import org.rapidoid.annotation.Since;
import org.rapidoid.annotation.TransactionMode;
import org.rapidoid.collection.Coll;
import org.rapidoid.http.HttpWrapper;
import org.rapidoid.http.MediaType;
import org.rapidoid.http.RouteConfig;
import org.rapidoid.u.U;

import java.util.Arrays;
import java.util.Set;

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

@Authors("Nikolche Mihajlovski")
@Since("5.1.0")
public class RouteOptions extends RapidoidThing implements RouteConfig {

	private volatile MediaType contentType = MediaType.HTML_UTF_8;

	private volatile String view;

	private volatile boolean mvc;

	private volatile String zone;

	private volatile boolean managed = true;

	private volatile TransactionMode transactionMode = TransactionMode.NONE;

	private final Set<String> roles = Coll.synchronizedSet();

	private volatile HttpWrapper[] wrappers;

	@Override
	public String toString() {
		String prefix = mvc ? "MVC" : "";
		return prefix + "{" +
			(contentType != null ? "contentType=" + contentType.info() : "") +
			(view != null ? ", view='" + view + '\'' : "") +
			(transactionMode != null ? ", transactionMode='" + transactionMode + '\'' : "") +
			(U.notEmpty(roles) ? ", roles=" + roles : "") +
			(U.notEmpty(wrappers) ? ", wrappers=" + wrappers : "") +
			'}';
	}

	@Override
	public MediaType contentType() {
		return contentType;
	}

	@Override
	public RouteOptions contentType(MediaType contentType) {
		this.contentType = contentType;
		return this;
	}

	@Override
	public String view() {
		return view;
	}

	@Override
	public RouteOptions view(String view) {
		this.view = view;
		return this;
	}

	@Override
	public boolean mvc() {
		return mvc;
	}

	@Override
	public RouteOptions mvc(boolean mvc) {
		this.mvc = mvc;
		return this;
	}

	@Override
	public TransactionMode transactionMode() {
		return transactionMode;
	}

	@Override
	public RouteOptions transactionMode(TransactionMode transactionMode) {
		this.transactionMode = transactionMode;
		return this;
	}

	@Override
	public Set<String> roles() {
		return roles;
	}

	@Override
	public RouteOptions roles(String... roles) {
		Coll.assign(this.roles, roles);
		return this;
	}

	@Override
	public HttpWrapper[] wrappers() {
		return wrappers;
	}

	@Override
	public RouteOptions wrappers(HttpWrapper... wrappers) {
		this.wrappers = wrappers;
		return this;
	}

	@Override
	public String zone() {
		return zone;
	}

	@Override
	public RouteOptions zone(String zone) {
		this.zone = zone;
		return this;
	}

	@Override
	public boolean managed() {
		return managed;
	}

	@Override
	public RouteOptions managed(boolean managed) {
		this.managed = managed;
		return this;
	}

	public RouteOptions copy() {
		RouteOptions copy = new RouteOptions();

		copy.contentType(contentType());
		copy.view(view());
		copy.mvc(mvc());
		copy.transactionMode(transactionMode());
		copy.roles(U.arrayOf(String.class, roles));
		copy.wrappers(wrappers());
		copy.zone(zone());
		copy.managed(managed());

		return copy;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		RouteOptions that = (RouteOptions) o;

		if (mvc != that.mvc) return false;
		if (managed != that.managed) return false;
		if (contentType != null ? !contentType.equals(that.contentType) : that.contentType != null) return false;
		if (view != null ? !view.equals(that.view) : that.view != null) return false;
		if (zone != null ? !zone.equals(that.zone) : that.zone != null) return false;
		if (transactionMode != that.transactionMode) return false;
		if (roles != null ? !roles.equals(that.roles) : that.roles != null) return false;
		return Arrays.equals(wrappers, that.wrappers);

	}

	@Override
	public int hashCode() {
		int result = contentType != null ? contentType.hashCode() : 0;
		result = 31 * result + (view != null ? view.hashCode() : 0);
		result = 31 * result + (mvc ? 1 : 0);
		result = 31 * result + (zone != null ? zone.hashCode() : 0);
		result = 31 * result + (managed ? 1 : 0);
		result = 31 * result + (transactionMode != null ? transactionMode.hashCode() : 0);
		result = 31 * result + (roles != null ? roles.hashCode() : 0);
		result = 31 * result + Arrays.hashCode(wrappers);
		return result;
	}
}
