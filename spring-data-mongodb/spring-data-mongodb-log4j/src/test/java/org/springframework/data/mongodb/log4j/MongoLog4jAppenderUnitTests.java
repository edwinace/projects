/*
 * Copyright 2013 the original author or authors.
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
package org.springframework.data.mongodb.log4j;

import org.junit.Test;

/**
 * Unit tests for {@link MongoLog4jAppender}.
 * 
 * @author Oliver Gierke
 */
public class MongoLog4jAppenderUnitTests {

	/**
	 * @see DATAMONGO-641
	 */
	@Test
	public void closesWithoutMongoInstancePresent() {
		new MongoLog4jAppender().close();
	}
}
