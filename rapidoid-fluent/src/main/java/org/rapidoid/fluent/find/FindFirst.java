package org.rapidoid.fluent.find;

/*
 * #%L
 * rapidoid-fluent
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

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * @author Nikolche Mihajlovski
 * @since 5.0.0
 */
public class FindFirst<T> {

	private final Stream<T> stream;

	public FindFirst(Stream<T> stream) {
		this.stream = stream;
	}

	public Optional<T> where(Predicate<? super T> predicate) {
		return stream.filter(predicate).findFirst();
	}

	public <R> Optional<T> withNonNull(Function<? super T, R> transformation) {
		return where(x -> transformation.apply(x) != null);
	}

	public <R> Optional<T> withNull(Function<? super T, R> transformation) {
		return where(x -> transformation.apply(x) == null);
	}

}
