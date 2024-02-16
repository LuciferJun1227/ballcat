/*
 * Copyright 2023-2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.ballcat.common.core.jackson;

import java.io.IOException;
import java.io.Serializable;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

/**
 * 空数组序列化处理器 如果 Array 为 null，则序列化为 []
 *
 * @author Hccake 2019/10/17 23:17
 */
public class NullArrayJsonSerializer extends JsonSerializer<Object> implements Serializable {

	private static final long serialVersionUID = 1L;

	@Override
	public void serialize(Object value, JsonGenerator jsonGenerator, SerializerProvider provider) throws IOException {
		if (value == null) {
			jsonGenerator.writeStartArray();
			jsonGenerator.writeEndArray();
		}
		else {
			jsonGenerator.writeObject(value);
		}
	}

}
