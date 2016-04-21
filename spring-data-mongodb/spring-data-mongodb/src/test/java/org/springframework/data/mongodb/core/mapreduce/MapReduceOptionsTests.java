/*
 * Copyright 2010-2015 the original author or authors.
 *
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
 */
package org.springframework.data.mongodb.core.mapreduce;

import static org.junit.Assert.*;
import static org.springframework.data.mongodb.test.util.IsBsonObject.*;

import org.junit.Test;

/**
 * @author Mark Pollack
 * @author Oliver Gierke
 * @author Christoph Strobl
 */
public class MapReduceOptionsTests {

	@Test
	public void testFinalize() {
		new MapReduceOptions().finalizeFunction("code");
	}

	/**
	 * @see DATAMONGO-1334
	 */
	@Test
	public void limitShouldBeIncludedCorrectly() {

		MapReduceOptions options = new MapReduceOptions();
		options.limit(10);

		assertThat(options.getOptionsObject(), isBsonObject().containing("limit", 10));
	}

	/**
	 * @see DATAMONGO-1334
	 */
	@Test
	public void limitShouldNotBePresentInDboWhenNotSet() {
		assertThat(new MapReduceOptions().getOptionsObject(), isBsonObject().notContaining("limit"));
	}
}
