/**
 * Copyright (C) 2014 Ontotext AD (info@ontotext.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.data.semantic.core;

import javax.annotation.PreDestroy;

import org.openrdf.repository.Repository;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.util.StringUtils;

public class SemanticDatabaseFactoryBean implements
		FactoryBean<SemanticDatabase> {

	private String url;

	private String username;

	private String password;

	private int maxConnections;

	private Repository repo;

	private String configFile;

	private SemanticDatabase semanticDB;
	
	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url
	 *            the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username
	 *            the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password
	 *            the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the maxConnections
	 */
	public int getMaxConnections() {
		return maxConnections;
	}

	/**
	 * @param maxConnections
	 *            the maxConnections to set
	 */
	public void setMaxConnections(int maxConnections) {
		this.maxConnections = maxConnections;
	}

	@Override
	public SemanticDatabase getObject() throws Exception {
		if (semanticDB == null) {
			semanticDB = getInstance();
		}
		return semanticDB;
	}

	private SemanticDatabase getInstance() {
		if (repo == null) {
			repo = getRepository();
		}
		SemanticDatabase db = new PooledSemanticDatabase(repo, maxConnections);
		return db;
	}
	
	private Repository getRepository(){
		if(StringUtils.hasText(username) && StringUtils.hasText(password)){
			return SemanticDatabaseManager.getRepository(url, username, password, this.configFile);
		}
		else{
			return SemanticDatabaseManager.getRepository(url, this.configFile);
		}
	}
	
	

	@Override
	public Class<?> getObjectType() {
		return SemanticDatabase.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

	@PreDestroy
	public void shutdown() {
		if (semanticDB != null) {
			semanticDB.shutdown();
		}
	}

	/**
	 * @return the configFile
	 */
	public String getConfigFile() {
		return configFile;
	}

	/**
	 * @param configFile
	 *            the configFile to set
	 */
	public void setConfigFile(String configFile) {
		this.configFile = configFile;
	}
	

}
