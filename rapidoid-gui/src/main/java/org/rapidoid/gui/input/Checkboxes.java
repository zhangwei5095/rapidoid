package org.rapidoid.gui.input;

import org.rapidoid.annotation.Authors;
import org.rapidoid.annotation.Since;
import org.rapidoid.gui.GUI;
import org.rapidoid.gui.base.AbstractOptions;
import org.rapidoid.u.U;

import java.util.Collection;
import java.util.List;

/*
 * #%L
 * rapidoid-gui
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
public class Checkboxes extends AbstractOptions<Checkboxes> {

	protected Collection<?> initial;

	@Override
	protected Object render() {
		U.notNull(options, "options");
		List<Object> checkboxes = U.list();

		for (Object opt : options) {
			boolean checked = initial != null && initial.contains(opt);
			Checkbox ch = GUI.checkbox().name(_name()).value(opt).checked(checked).label(str(opt));
			checkboxes.add(ch);
		}

		return checkboxes;
	}

	public Collection<?> initial() {
		return initial;
	}

	public Checkboxes initial(Collection<?> initial) {
		this.initial = initial;
		return this;
	}
}
