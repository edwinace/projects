/*
 * Copyright 2015 the original author or authors.
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
package org.springframework.data.mongodb.core.aggregation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.util.Assert;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

/**
 * An enum of supported {@link AggregationExpression}s in aggregation pipeline stages.
 * 
 * @author Thomas Darimont
 * @author Oliver Gierke
 * @since 1.10
 */
public enum AggregationFunctionExpressions {

	SIZE;

	/**
	 * Returns an {@link AggregationExpression} build from the current {@link Enum} name and the given parameters.
	 * 
	 * @param parameters must not be {@literal null}
	 * @return
	 */
	public AggregationExpression of(Object... parameters) {

		Assert.notNull(parameters, "Parameters must not be null!");
		return new FunctionExpression(name().toLowerCase(), parameters);
	}

	/**
	 * An {@link AggregationExpression} representing a function call.
	 * 
	 * @author Thomas Darimont
	 * @author Oliver Gierke
	 * @since 1.10
	 */
	static class FunctionExpression implements AggregationExpression {

		private final String name;
		private final List<Object> values;

		/**
		 * Creates a new {@link FunctionExpression} for the given name and values.
		 * 
		 * @param name must not be {@literal null} or empty.
		 * @param values must not be {@literal null}.
		 */
		public FunctionExpression(String name, Object[] values) {

			Assert.hasText(name, "Name must not be null!");
			Assert.notNull(values, "Values must not be null!");

			this.name = name;
			this.values = Arrays.asList(values);
		}

		/*
		 * (non-Javadoc)
		 * @see org.springframework.data.mongodb.core.aggregation.Expression#toDbObject(org.springframework.data.mongodb.core.aggregation.AggregationOperationContext)
		 */
		@Override
		public DBObject toDbObject(AggregationOperationContext context) {

			List<Object> args = new ArrayList<Object>(values.size());

			for (Object value : values) {
				args.add(unpack(value, context));
			}

			return new BasicDBObject("$" + name, args);
		}

		private static Object unpack(Object value, AggregationOperationContext context) {

			if (value instanceof AggregationExpression) {
				return ((AggregationExpression) value).toDbObject(context);
			}

			if (value instanceof Field) {
				return context.getReference((Field) value).toString();
			}

			return value;
		}
	}
}
