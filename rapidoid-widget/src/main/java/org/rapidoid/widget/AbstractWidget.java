package org.rapidoid.widget;

/*
 * #%L
 * rapidoid-pages
 * %%
 * Copyright (C) 2014 - 2015 Nikolche Mihajlovski
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

import org.rapidoid.annotation.Authors;
import org.rapidoid.annotation.Since;
import org.rapidoid.html.TagWidget;
import org.rapidoid.http.HttpExchange;
import org.rapidoid.util.AppCtx;
import org.rapidoid.util.Constants;
import org.rapidoid.util.U;

@Authors("Nikolche Mihajlovski")
@Since("2.0.0")
public abstract class AbstractWidget extends BootstrapWidgets implements TagWidget<HttpExchange>, Constants {

	private int widgetNum = getWidgetNumber(this);

	private HttpExchange x;

	protected HttpExchange exchange() {
		U.notNull(x, "HTTP exchange");
		return x;
	}

	protected abstract Object render();

	@Override
	public final Object render(HttpExchange x) {
		this.x = x;
		return render();
	}

	public String widgetId() {
		return getClass().getSimpleName() + widgetNum;
	}

	private static int getWidgetNumber(AbstractWidget widget) {
		HttpExchange x = AppCtx.exchange();
		U.notNull(x, "HTTP exchange");

		String extrName = "widget_counter_" + widget.getClass().getSimpleName();
		Integer counter = U.or((Integer) x.extra(extrName), 1);
		x.extra(extrName, counter + 1);

		return counter;
	}

}