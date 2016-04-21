/*
 * Copyright 2011-2015 by the original author(s).
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
package org.springframework.data.mongodb.core.index;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.dao.DataAccessException;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.CollectionCallback;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoException;

/**
 * Integration tests for index handling.
 * 
 * @author Oliver Gierke
 * @author Christoph Strobl
 * @author Jordi Llach
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:infrastructure.xml")
public class IndexingIntegrationTests {

	@Autowired MongoOperations operations;
	@Autowired MongoDbFactory mongoDbFactory;
	@Autowired ConfigurableApplicationContext context;

	@After
	public void tearDown() {
		operations.dropCollection(IndexedPerson.class);
	}

	/**
	 * @see DATAMONGO-237
	 */
	@Test
	@DirtiesContext
	public void createsIndexWithFieldName() {

		operations.getConverter().getMappingContext().getPersistentEntity(IndexedPerson.class);

		assertThat(hasIndex("_firstname", IndexedPerson.class), is(true));
	}

	/**
	 * @see DATAMONGO-1163
	 */
	@Test
	@DirtiesContext
	public void createsIndexFromMetaAnnotation() {

		operations.getConverter().getMappingContext().getPersistentEntity(IndexedPerson.class);

		assertThat(hasIndex("_lastname", IndexedPerson.class), is(true));
	}

	@Target({ ElementType.FIELD })
	@Retention(RetentionPolicy.RUNTIME)
	@Indexed
	@interface IndexedFieldAnnotation {
	}

	@Document
	class IndexedPerson {

		@Field("_firstname") @Indexed String firstname;
		@Field("_lastname") @IndexedFieldAnnotation String lastname;
	}

	/**
	 * Returns whether an index with the given name exists for the given entity type.
	 * 
	 * @param indexName
	 * @param entityType
	 * @return
	 */
	private boolean hasIndex(final String indexName, Class<?> entityType) {

		return operations.execute(entityType, new CollectionCallback<Boolean>() {
			public Boolean doInCollection(DBCollection collection) throws MongoException, DataAccessException {
				for (DBObject indexInfo : collection.getIndexInfo()) {
					if (indexName.equals(indexInfo.get("name"))) {
						return true;
					}
				}
				return false;
			}
		});
	}
}
