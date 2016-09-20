package org.rapidoid.validation;

import org.rapidoid.RapidoidThing;
import org.rapidoid.annotation.Authors;
import org.rapidoid.annotation.Since;
import org.rapidoid.u.U;

import javax.validation.*;
import java.util.Set;

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

@Authors("Nikolche Mihajlovski")
@Since("5.1.0")
public class Validators extends RapidoidThing {

	private static final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();

	private static final Validator validator = factory.getValidator();

	public static ValidatorFactory factory() {
		return factory;
	}

	public static Validator get() {
		return validator;
	}

	public static <T> Set<ConstraintViolation<T>> getViolations(T bean) {
		return validator.validate(bean);
	}

	public static void validate(Object bean) {
		Set<ConstraintViolation<Object>> violations = getViolations(bean);

		if (U.notEmpty(violations)) {
			throw new ConstraintViolationException(violations);
		}
	}

}
