package org.rapidoid.datamodel;

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

import org.rapidoid.RapidoidThing;
import org.rapidoid.annotation.Authors;
import org.rapidoid.annotation.Since;

import java.util.Iterator;
import java.util.List;

@Authors("Nikolche Mihajlovski")
@Since("5.1.0")
public class PagingIterator extends RapidoidThing implements Iterator {

	private final DataItems items;

	private List<?> page;

	private int offset;
	private int pageLength;
	private int index;

	public PagingIterator(DataItems items) {
		this(items, 100);
	}

	public PagingIterator(DataItems items, int pageLength) {
		this.items = items;
		this.pageLength = pageLength;
	}

	@Override
	public boolean hasNext() {
		if (page == null || index >= page.size()) {
			page = items.page(offset, pageLength);
			offset += page.size();
			index = 0;
		}

		return index < page.size();
	}

	@Override
	public Object next() {
		return page.get(index++);
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException("remove");
	}

}
