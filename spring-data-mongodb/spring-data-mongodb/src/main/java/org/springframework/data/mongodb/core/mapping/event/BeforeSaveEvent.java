/*
 * Copyright (c) 2011-2015 by the original author(s).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.data.mongodb.core.mapping.event;

import com.mongodb.DBObject;

/**
 * {@link MongoMappingEvent} triggered before save of a document.
 * 
 * @author Jon Brisbin <jbrisbin@vmware.com>
 * @author Christoph Strobl
 */
public class BeforeSaveEvent<E> extends MongoMappingEvent<E> {

	private static final long serialVersionUID = 1L;

	/**
	 * Creates new {@link BeforeSaveEvent}.
	 * 
	 * @param source must not be {@literal null}.
	 * @param dbo can be {@literal null}.
	 * @deprecated since 1.8. Please use {@link #BeforeSaveEvent(Object, DBObject, String)}.
	 */
	@Deprecated
	public BeforeSaveEvent(E source, DBObject dbo) {
		super(source, dbo);
	}

	/**
	 * Creates new {@link BeforeSaveEvent}.
	 * 
	 * @param source must not be {@literal null}.
	 * @param dbo can be {@literal null}.
	 * @param collectionName can be {@literal null}.
	 * @since 1.8
	 */
	public BeforeSaveEvent(E source, DBObject dbo, String collectionName) {
		super(source, dbo, collectionName);
	}

}
