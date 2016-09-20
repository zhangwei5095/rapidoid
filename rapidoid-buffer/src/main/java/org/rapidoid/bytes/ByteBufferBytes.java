package org.rapidoid.bytes;

import org.rapidoid.RapidoidThing;
import org.rapidoid.annotation.Authors;
import org.rapidoid.annotation.Since;

import java.nio.ByteBuffer;

/*
 * #%L
 * rapidoid-buffer
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
@Since("2.0.0")
public class ByteBufferBytes extends RapidoidThing implements Bytes {

	private ByteBuffer buf;

	public ByteBufferBytes() {
	}

	public ByteBufferBytes(ByteBuffer buf) {
		this.buf = buf;
	}

	@Override
	public byte get(int position) {
		return buf.get(position);
	}

	@Override
	public int limit() {
		return buf.limit();
	}

	public void setBuf(ByteBuffer buf) {
		this.buf = buf;
	}

}
