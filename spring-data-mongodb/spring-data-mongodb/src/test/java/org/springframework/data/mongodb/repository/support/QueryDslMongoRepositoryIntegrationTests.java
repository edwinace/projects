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
package org.springframework.data.mongodb.repository.support;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.repository.Person;
import org.springframework.data.mongodb.repository.QPerson;
import org.springframework.data.mongodb.repository.query.MongoEntityInformation;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.querydsl.core.types.Predicate;

/**
 * Integration test for {@link QueryDslMongoRepository}.
 * 
 * @author Thomas Darimont
 */
@ContextConfiguration(
		locations = "/org/springframework/data/mongodb/repository/PersonRepositoryIntegrationTests-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class QueryDslMongoRepositoryIntegrationTests {

	@Autowired MongoOperations operations;
	QueryDslMongoRepository<Person, String> repository;

	Person dave, oliver, carter;
	QPerson person;

	@Before
	public void setup() {

		MongoRepositoryFactory factory = new MongoRepositoryFactory(operations);
		MongoEntityInformation<Person, String> entityInformation = factory.getEntityInformation(Person.class);
		repository = new QueryDslMongoRepository<Person, String>(entityInformation, operations);

		operations.dropCollection(Person.class);

		dave = new Person("Dave", "Matthews", 42);
		oliver = new Person("Oliver August", "Matthews", 4);
		carter = new Person("Carter", "Beauford", 49);

		person = new QPerson("person");

		repository.save(Arrays.asList(oliver, dave, carter));
	}

	/**
	 * @see DATAMONGO-1146
	 */
	@Test
	public void shouldSupportExistsWithPredicate() throws Exception {

		assertThat(repository.exists(person.firstname.eq("Dave")), is(true));
		assertThat(repository.exists(person.firstname.eq("Unknown")), is(false));
		assertThat(repository.exists((Predicate) null), is(true));
	}

	/**
	 * @see DATAMONGO-1167
	 */
	@Test
	public void shouldSupportFindAllWithPredicateAndSort() {

		List<Person> users = repository.findAll(person.lastname.isNotNull(), new Sort(Direction.ASC, "firstname"));

		assertThat(users, hasSize(3));
		assertThat(users.get(0).getFirstname(), is(carter.getFirstname()));
		assertThat(users.get(2).getFirstname(), is(oliver.getFirstname()));
		assertThat(users, hasItems(carter, dave, oliver));
	}
}
