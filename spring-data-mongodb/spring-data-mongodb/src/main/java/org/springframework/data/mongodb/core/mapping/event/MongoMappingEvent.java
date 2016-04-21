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

import org.springframework.context.ApplicationEvent;

import com.mongodb.DBObject;

/**
 * Base {@link ApplicationEvent} triggered by Spring Data MongoDB.
 * 
 * @author Jon Brisbin <jbrisbin@vmware.com>
 * @author Christoph Strobl
 */
public class MongoMappingEvent<T> extends ApplicationEvent {

	private static final long serialVersionUID = 1L;
	private final DBObject dbo;
	private final String collectionName;

	/**
	 * Creates new {@link MongoMappingEvent}.
	 * 
	 * @param source must not be {@literal null}.
	 * @param dbo can be {@literal null}.
	 * @deprecated since 1.8. Please use {@link #MongoMappingEvent(Object, DBObject, String)}.
	 */
	@Deprecated
	public MongoMappingEvent(T source, DBObject dbo) {
		this(source, dbo, null);
	}

	/**
	 * Creates new {@link MongoMappingEvent}.
	 * 
	 * @param source must not be {@literal null}.
	 * @param dbo can be {@literal null}.
	 * @param collectionName can be {@literal null}.
	 */
	public MongoMappingEvent(T source, DBObject dbo, String collectionName) {

		super(source);
		this.dbo = dbo;
		this.collectionName = collectionName;
	}

	/**
	 * @return {@literal null} if not set.
	 */
	public DBObject getDBObject() {
		return dbo;
	}

	/**
	 * Get the collection the event refers to.
	 * 
	 * @return {@literal null} if not set.
	 * @since 1.8
	 */
	public String getCollectionName() {
		return collectionName;
	}

	/*
	 * (non-Javadoc)
	 * @see java.util.EventObject#getSource()
	 */
	@SuppressWarnings({ "unchecked" })
	@Override
	public T getSource() {
		return (T) super.getSource();
	}
}
