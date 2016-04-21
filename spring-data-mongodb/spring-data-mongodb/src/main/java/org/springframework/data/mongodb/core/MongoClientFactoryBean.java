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
package org.springframework.data.mongodb.core;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.config.AbstractFactoryBean;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.support.PersistenceExceptionTranslator;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;

/**
 * Convenient factory for configuring MongoDB.
 * 
 * @author Christoph Strobl
 * @since 1.7
 */
public class MongoClientFactoryBean extends AbstractFactoryBean<Mongo> implements PersistenceExceptionTranslator {

	private static final PersistenceExceptionTranslator DEFAULT_EXCEPTION_TRANSLATOR = new MongoExceptionTranslator();

	private MongoClientOptions mongoClientOptions;
	private String host;
	private Integer port;
	private List<ServerAddress> replicaSetSeeds;
	private List<MongoCredential> credentials;

	private PersistenceExceptionTranslator exceptionTranslator = DEFAULT_EXCEPTION_TRANSLATOR;

	/**
	 * Set the {@link MongoClientOptions} to be used when creating {@link MongoClient}.
	 * 
	 * @param mongoClientOptions
	 */
	public void setMongoClientOptions(MongoClientOptions mongoClientOptions) {
		this.mongoClientOptions = mongoClientOptions;
	}

	/**
	 * Set the list of credentials to be used when creating {@link MongoClient}.
	 * 
	 * @param credentials can be {@literal null}.
	 */
	public void setCredentials(MongoCredential[] credentials) {
		this.credentials = filterNonNullElementsAsList(credentials);
	}

	/**
	 * Set the list of {@link ServerAddress} to build up a replica set for.
	 * 
	 * @param replicaSetSeeds can be {@literal null}.
	 */
	public void setReplicaSetSeeds(ServerAddress[] replicaSetSeeds) {
		this.replicaSetSeeds = filterNonNullElementsAsList(replicaSetSeeds);
	}

	/**
	 * Configures the host to connect to.
	 * 
	 * @param host
	 */
	public void setHost(String host) {
		this.host = host;
	}

	/**
	 * Configures the port to connect to.
	 * 
	 * @param port
	 */
	public void setPort(int port) {
		this.port = port;
	}

	/**
	 * Configures the {@link PersistenceExceptionTranslator} to use.
	 * 
	 * @param exceptionTranslator
	 */
	public void setExceptionTranslator(PersistenceExceptionTranslator exceptionTranslator) {
		this.exceptionTranslator = exceptionTranslator == null ? DEFAULT_EXCEPTION_TRANSLATOR : exceptionTranslator;
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.beans.factory.FactoryBean#getObjectType()
	 */
	public Class<? extends Mongo> getObjectType() {
		return Mongo.class;
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.dao.support.PersistenceExceptionTranslator#translateExceptionIfPossible(java.lang.RuntimeException)
	 */
	public DataAccessException translateExceptionIfPossible(RuntimeException ex) {
		return exceptionTranslator.translateExceptionIfPossible(ex);
	}

	/* 
	 * (non-Javadoc)
	 * @see org.springframework.beans.factory.config.AbstractFactoryBean#createInstance()
	 */
	@Override
	protected Mongo createInstance() throws Exception {

		if (mongoClientOptions == null) {
			mongoClientOptions = MongoClientOptions.builder().build();
		}

		if (credentials == null) {
			credentials = Collections.emptyList();
		}

		return createMongoClient();
	}

	/* 
	 * (non-Javadoc)
	 * @see org.springframework.beans.factory.config.AbstractFactoryBean#destroyInstance(java.lang.Object)
	 */
	@Override
	protected void destroyInstance(Mongo instance) throws Exception {
		instance.close();
	}

	private MongoClient createMongoClient() throws UnknownHostException {

		if (!CollectionUtils.isEmpty(replicaSetSeeds)) {
			return new MongoClient(replicaSetSeeds, credentials, mongoClientOptions);
		}

		return new MongoClient(createConfiguredOrDefaultServerAddress(), credentials, mongoClientOptions);
	}

	private ServerAddress createConfiguredOrDefaultServerAddress() throws UnknownHostException {

		ServerAddress defaultAddress = new ServerAddress();

		return new ServerAddress(StringUtils.hasText(host) ? host : defaultAddress.getHost(),
				port != null ? port.intValue() : defaultAddress.getPort());
	}

	/**
	 * Returns the given array as {@link List} with all {@literal null} elements removed.
	 * 
	 * @param elements the elements to filter <T>, can be {@literal null}.
	 * @return a new unmodifiable {@link List#} from the given elements without {@literal null}s.
	 */
	private static <T> List<T> filterNonNullElementsAsList(T[] elements) {

		if (elements == null) {
			return Collections.emptyList();
		}

		List<T> candidateElements = new ArrayList<T>();

		for (T element : elements) {
			if (element != null) {
				candidateElements.add(element);
			}
		}

		return Collections.unmodifiableList(candidateElements);
	}
}
