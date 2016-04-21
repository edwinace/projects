/*
 * Copyright 2015-2016 the original author or authors.
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
package org.springframework.data.mongodb.core;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.BulkOperationException;
import org.springframework.data.mongodb.core.BulkOperations.BulkMode;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.util.Pair;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.mongodb.BasicDBObject;
import com.mongodb.BulkWriteResult;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.WriteConcern;

/**
 * Integration tests for {@link DefaultBulkOperations}.
 * 
 * @author Tobias Trelle
 * @author Oliver Gierke
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:infrastructure.xml")
public class DefaultBulkOperationsIntegrationTests {

	static final String COLLECTION_NAME = "bulk_ops";

	@Autowired MongoOperations operations;

	DBCollection collection;

	@Before
	public void setUp() {

		this.collection = this.operations.getCollection(COLLECTION_NAME);
		this.collection.remove(new BasicDBObject());
	}

	/**
	 * @see DATAMONGO-934
	 */
	@Test(expected = IllegalArgumentException.class)
	public void rejectsNullMongoOperations() {
		new DefaultBulkOperations(null, null, COLLECTION_NAME, null);
	}

	/**
	 * @see DATAMONGO-934
	 */
	@Test(expected = IllegalArgumentException.class)
	public void rejectsNullCollectionName() {
		new DefaultBulkOperations(operations, null, null, null);
	}

	/**
	 * @see DATAMONGO-934
	 */
	@Test(expected = IllegalArgumentException.class)
	public void rejectsEmptyCollectionName() {
		new DefaultBulkOperations(operations, null, "", null);
	}

	/**
	 * @see DATAMONGO-934
	 */
	@Test
	public void insertOrdered() {

		List<BaseDoc> documents = Arrays.asList(newDoc("1"), newDoc("2"));

		assertThat(createBulkOps(BulkMode.ORDERED).insert(documents).execute().getInsertedCount(), is(2));
	}

	/**
	 * @see DATAMONGO-934
	 */
	@Test
	public void insertOrderedFails() {

		List<BaseDoc> documents = Arrays.asList(newDoc("1"), newDoc("1"), newDoc("2"));

		try {
			createBulkOps(BulkMode.ORDERED).insert(documents).execute();
			fail();
		} catch (BulkOperationException e) {
			assertThat(e.getResult().getInsertedCount(), is(1)); // fails after first error
			assertThat(e.getErrors(), notNullValue());
			assertThat(e.getErrors().size(), is(1));
		}
	}

	/**
	 * @see DATAMONGO-934
	 */
	@Test
	public void insertUnOrdered() {

		List<BaseDoc> documents = Arrays.asList(newDoc("1"), newDoc("2"));

		assertThat(createBulkOps(BulkMode.UNORDERED).insert(documents).execute().getInsertedCount(), is(2));
	}

	/**
	 * @see DATAMONGO-934
	 */
	@Test
	public void insertUnOrderedContinuesOnError() {

		List<BaseDoc> documents = Arrays.asList(newDoc("1"), newDoc("1"), newDoc("2"));

		try {
			createBulkOps(BulkMode.UNORDERED).insert(documents).execute();
			fail();
		} catch (BulkOperationException e) {
			assertThat(e.getResult().getInsertedCount(), is(2)); // two docs were inserted
			assertThat(e.getErrors(), notNullValue());
			assertThat(e.getErrors().size(), is(1));
		}
	}

	/**
	 * @see DATAMONGO-934
	 */
	@Test
	public void upsertDoesUpdate() {

		insertSomeDocuments();

		BulkWriteResult result = createBulkOps(BulkMode.ORDERED).//
				upsert(where("value", "value1"), set("value", "value2")).//
				execute();

		assertThat(result, notNullValue());
		assertThat(result.getMatchedCount(), is(2));
		assertThat(result.getModifiedCount(), is(2));
		assertThat(result.getInsertedCount(), is(0));
		assertThat(result.getUpserts(), is(notNullValue()));
		assertThat(result.getUpserts().size(), is(0));
	}

	/**
	 * @see DATAMONGO-934
	 */
	@Test
	public void upsertDoesInsert() {

		BulkWriteResult result = createBulkOps(BulkMode.ORDERED).//
				upsert(where("_id", "1"), set("value", "v1")).//
				execute();

		assertThat(result, notNullValue());
		assertThat(result.getMatchedCount(), is(0));
		assertThat(result.getModifiedCount(), is(0));
		assertThat(result.getUpserts(), is(notNullValue()));
		assertThat(result.getUpserts().size(), is(1));
	}

	/**
	 * @see DATAMONGO-934
	 */
	@Test
	public void updateOneOrdered() {
		testUpdate(BulkMode.ORDERED, false, 2);
	}

	/**
	 * @see DATAMONGO-934
	 */
	@Test
	public void updateMultiOrdered() {
		testUpdate(BulkMode.ORDERED, true, 4);
	}

	/**
	 * @see DATAMONGO-934
	 */
	@Test
	public void updateOneUnOrdered() {
		testUpdate(BulkMode.UNORDERED, false, 2);
	}

	/**
	 * @see DATAMONGO-934
	 */
	@Test
	public void updateMultiUnOrdered() {
		testUpdate(BulkMode.UNORDERED, true, 4);
	}

	/**
	 * @see DATAMONGO-934
	 */
	@Test
	public void removeOrdered() {
		testRemove(BulkMode.ORDERED);
	}

	/**
	 * @see DATAMONGO-934
	 */
	@Test
	public void removeUnordered() {
		testRemove(BulkMode.UNORDERED);
	}

	/**
	 * If working on the same set of documents, only an ordered bulk operation will yield predictable results.
	 * 
	 * @see DATAMONGO-934
	 */
	@Test
	public void mixedBulkOrdered() {

		BulkWriteResult result = createBulkOps(BulkMode.ORDERED).insert(newDoc("1", "v1")).//
				updateOne(where("_id", "1"), set("value", "v2")).//
				remove(where("value", "v2")).//
				execute();

		assertThat(result, notNullValue());
		assertThat(result.getInsertedCount(), is(1));
		assertThat(result.getModifiedCount(), is(1));
		assertThat(result.getRemovedCount(), is(1));
	}

	/**
	 * If working on the same set of documents, only an ordered bulk operation will yield predictable results.
	 */
	@Test
	@SuppressWarnings("unchecked")
	public void mixedBulkOrderedWithList() {

		List<BaseDoc> inserts = Arrays.asList(newDoc("1", "v1"), newDoc("2", "v2"), newDoc("3", "v2"));
		List<Pair<Query, Update>> updates = Arrays.asList(Pair.of(where("value", "v2"), set("value", "v3")));
		List<Query> removes = Arrays.asList(where("_id", "1"));

		BulkWriteResult result = createBulkOps(BulkMode.ORDERED).insert(inserts).updateMulti(updates).remove(removes)
				.execute();

		assertThat(result, notNullValue());
		assertThat(result.getInsertedCount(), is(3));
		assertThat(result.getModifiedCount(), is(2));
		assertThat(result.getRemovedCount(), is(1));
	}

	private void testUpdate(BulkMode mode, boolean multi, int expectedUpdates) {

		BulkOperations bulkOps = createBulkOps(mode);

		insertSomeDocuments();

		List<Pair<Query, Update>> updates = new ArrayList<Pair<Query, Update>>();
		updates.add(Pair.of(where("value", "value1"), set("value", "value3")));
		updates.add(Pair.of(where("value", "value2"), set("value", "value4")));

		int modifiedCount = multi ? bulkOps.updateMulti(updates).execute().getModifiedCount()
				: bulkOps.updateOne(updates).execute().getModifiedCount();

		assertThat(modifiedCount, is(expectedUpdates));
	}

	private void testRemove(BulkMode mode) {

		insertSomeDocuments();

		List<Query> removes = Arrays.asList(where("_id", "1"), where("value", "value2"));

		assertThat(createBulkOps(mode).remove(removes).execute().getRemovedCount(), is(3));
	}

	private BulkOperations createBulkOps(BulkMode mode) {

		DefaultBulkOperations operations = new DefaultBulkOperations(this.operations, mode, COLLECTION_NAME, null);
		operations.setDefaultWriteConcern(WriteConcern.ACKNOWLEDGED);

		return operations;
	}

	private void insertSomeDocuments() {

		final DBCollection coll = operations.getCollection(COLLECTION_NAME);

		coll.insert(rawDoc("1", "value1"));
		coll.insert(rawDoc("2", "value1"));
		coll.insert(rawDoc("3", "value2"));
		coll.insert(rawDoc("4", "value2"));
	}

	private static BaseDoc newDoc(String id) {

		BaseDoc doc = new BaseDoc();
		doc.id = id;

		return doc;
	}

	private static BaseDoc newDoc(String id, String value) {

		BaseDoc doc = newDoc(id);
		doc.value = value;

		return doc;
	}

	private static Query where(String field, String value) {
		return new Query().addCriteria(Criteria.where(field).is(value));
	}

	private static Update set(String field, String value) {
		return new Update().set(field, value);
	}

	private static DBObject rawDoc(String id, String value) {
		return new BasicDBObject("_id", id).append("value", value);
	}
}
