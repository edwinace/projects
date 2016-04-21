/*
 * Copyright 2014-2015 the original author or authors.
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
package org.springframework.data.mongodb.repository.query;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.springframework.data.mongodb.core.query.IsTextQuery.*;

import java.lang.reflect.Method;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.convert.DbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.convert.MongoConverter;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Person;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.projection.SpelAwareProxyProjectionFactory;
import org.springframework.data.repository.core.support.DefaultRepositoryMetadata;

import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBObject;
import com.mongodb.util.JSONParseException;

/**
 * Unit tests for {@link PartTreeMongoQuery}.
 * 
 * @author Christoph Strobl
 * @author Oliver Gierke
 * @author Thomas Darimont
 */
@RunWith(MockitoJUnitRunner.class)
public class PartTreeMongoQueryUnitTests {

	@Mock MongoOperations mongoOperationsMock;

	MongoMappingContext mappingContext;

	public @Rule ExpectedException exception = ExpectedException.none();

	@Before
	public void setUp() {

		mappingContext = new MongoMappingContext();
		DbRefResolver dbRefResolver = new DefaultDbRefResolver(mock(MongoDbFactory.class));
		MongoConverter converter = new MappingMongoConverter(dbRefResolver, mappingContext);

		when(mongoOperationsMock.getConverter()).thenReturn(converter);
	}

	/**
	 * @see DATAMOGO-952
	 */
	@Test
	public void rejectsInvalidFieldSpecification() {

		exception.expect(IllegalStateException.class);
		exception.expectMessage("findByLastname");

		deriveQueryFromMethod("findByLastname", new Object[] { "foo" });
	}

	/**
	 * @see DATAMOGO-952
	 */
	@Test
	public void singleFieldJsonIncludeRestrictionShouldBeConsidered() {

		org.springframework.data.mongodb.core.query.Query query = deriveQueryFromMethod("findByFirstname",
				new Object[] { "foo" });

		assertThat(query.getFieldsObject(), is(new BasicDBObjectBuilder().add("firstname", 1).get()));
	}

	/**
	 * @see DATAMOGO-952
	 */
	@Test
	public void multiFieldJsonIncludeRestrictionShouldBeConsidered() {

		org.springframework.data.mongodb.core.query.Query query = deriveQueryFromMethod("findByFirstnameAndLastname",
				new Object[] { "foo", "bar" });

		assertThat(query.getFieldsObject(), is(new BasicDBObjectBuilder().add("firstname", 1).add("lastname", 1).get()));
	}

	/**
	 * @see DATAMOGO-952
	 */
	@Test
	public void multiFieldJsonExcludeRestrictionShouldBeConsidered() {

		org.springframework.data.mongodb.core.query.Query query = deriveQueryFromMethod("findPersonByFirstnameAndLastname",
				new Object[] { "foo", "bar" });

		assertThat(query.getFieldsObject(), is(new BasicDBObjectBuilder().add("firstname", 0).add("lastname", 0).get()));
	}

	/**
	 * @see DATAMOGO-973
	 */
	@Test
	public void shouldAddFullTextParamCorrectlyToDerivedQuery() {

		org.springframework.data.mongodb.core.query.Query query = deriveQueryFromMethod("findPersonByFirstname",
				new Object[] { "text", TextCriteria.forDefaultLanguage().matching("search") });

		assertThat(query, isTextQuery().searchingFor("search").where(new Criteria("firstname").is("text")));
	}

	/**
	 * @see DATAMONGO-1180
	 */
	@Test
	public void propagatesRootExceptionForInvalidQuery() {

		exception.expect(IllegalStateException.class);
		exception.expectCause(is(org.hamcrest.Matchers.<Throwable> instanceOf(JSONParseException.class)));

		deriveQueryFromMethod("findByAge", new Object[] { 1 });
	}

	/**
	 * @see DATAMONGO-1345
	 */
	@Test
	public void doesNotDeriveFieldSpecForNormalDomainType() {
		assertThat(deriveQueryFromMethod("findPersonBy", new Object[0]).getFieldsObject(), is(nullValue()));
	}

	/**
	 * @see DATAMONGO-1345
	 */
	@Test
	public void restrictsQueryToFieldsRequiredForProjection() {

		DBObject fieldsObject = deriveQueryFromMethod("findPersonProjectedBy", new Object[0]).getFieldsObject();

		assertThat(fieldsObject.get("firstname"), is((Object) 1));
		assertThat(fieldsObject.get("lastname"), is((Object) 1));
	}

	/**
	 * @see DATAMONGO-1345
	 */
	@Test
	public void restrictsQueryToFieldsRequiredForDto() {

		DBObject fieldsObject = deriveQueryFromMethod("findPersonDtoByAge", new Object[] { 42 }).getFieldsObject();

		assertThat(fieldsObject.get("firstname"), is((Object) 1));
		assertThat(fieldsObject.get("lastname"), is((Object) 1));
	}

	/**
	 * @see DATAMONGO-1345
	 */
	@Test
	public void usesDynamicProjection() {

		DBObject fields = deriveQueryFromMethod("findDynamicallyProjectedBy", ExtendedProjection.class).getFieldsObject();

		assertThat(fields.get("firstname"), is((Object) 1));
		assertThat(fields.get("lastname"), is((Object) 1));
		assertThat(fields.get("age"), is((Object) 1));
	}

	private org.springframework.data.mongodb.core.query.Query deriveQueryFromMethod(String method, Object... args) {

		Class<?>[] types = new Class<?>[args.length];

		for (int i = 0; i < args.length; i++) {
			types[i] = args[i].getClass();
		}

		PartTreeMongoQuery partTreeQuery = createQueryForMethod(method, types);

		MongoParameterAccessor accessor = new MongoParametersParameterAccessor(partTreeQuery.getQueryMethod(), args);
		return partTreeQuery.createQuery(new ConvertingParameterAccessor(mongoOperationsMock.getConverter(), accessor));
	}

	private PartTreeMongoQuery createQueryForMethod(String methodName, Class<?>... paramTypes) {

		try {

			Method method = Repo.class.getMethod(methodName, paramTypes);
			ProjectionFactory factory = new SpelAwareProxyProjectionFactory();
			MongoQueryMethod queryMethod = new MongoQueryMethod(method, new DefaultRepositoryMetadata(Repo.class), factory,
					mappingContext);

			return new PartTreeMongoQuery(queryMethod, mongoOperationsMock);
		} catch (NoSuchMethodException e) {
			throw new IllegalArgumentException(e.getMessage(), e);
		} catch (SecurityException e) {
			throw new IllegalArgumentException(e.getMessage(), e);
		}
	}

	interface Repo extends MongoRepository<Person, Long> {

		@Query(fields = "firstname")
		Person findByLastname(String lastname);

		@Query(fields = "{ 'firstname' : 1 }")
		Person findByFirstname(String lastname);

		@Query(fields = "{ 'firstname' : 1, 'lastname' : 1 }")
		Person findByFirstnameAndLastname(String firstname, String lastname);

		@Query(fields = "{ 'firstname' : 0, 'lastname' : 0 }")
		Person findPersonByFirstnameAndLastname(String firstname, String lastname);

		Person findPersonByFirstname(String firstname, TextCriteria fullText);

		@Query(fields = "{ 'firstname }")
		Person findByAge(Integer age);

		Person findPersonBy();

		PersonProjection findPersonProjectedBy();

		PersonDto findPersonDtoByAge(Integer age);

		<T> T findDynamicallyProjectedBy(Class<T> type);
	}

	interface PersonProjection {

		String getFirstname();

		String getLastname();
	}

	interface ExtendedProjection extends PersonProjection {

		int getAge();
	}

	static class PersonDto {

		public String firstname, lastname;

		public PersonDto(String firstname, String lastname) {

			this.firstname = firstname;
			this.lastname = lastname;
		}
	}
}
