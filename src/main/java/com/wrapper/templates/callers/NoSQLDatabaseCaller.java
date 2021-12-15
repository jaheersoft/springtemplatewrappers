package com.wrapper.templates.callers;

import static com.wrapper.templates.exceptions.ErrorType.*;
import static com.wrapper.templates.exceptions.Process.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;
import com.wrapper.templates.exceptions.FaultError;

@Component
public class NoSQLDatabaseCaller {

	@Autowired
	private MongoTemplate mongoTemplate;
	
	public <R> R save(R value,String collectionName) throws FaultError {
		try {
			return mongoTemplate.insert(value, collectionName);
		} catch(Exception e) {
			throw new FaultError.Builder(e).the(NOSQL_ERROR).occurredWhile(SAVING_OBJECT).build();
		}
	}
	
	public <R> R update(R value,String collectionName) throws FaultError {
		try {
			return mongoTemplate.insert(value, collectionName);
		} catch(Exception e) {
			throw new FaultError.Builder(e).the(NOSQL_ERROR).occurredWhile(UPDATING_OBJECT).build();
		}
	}
	
	public <R> R find(R value,String collectionName) throws FaultError {
		try {
			return mongoTemplate.insert(value, collectionName);
		} catch(Exception e) {
			throw new FaultError.Builder(e).the(NOSQL_ERROR).occurredWhile(FINDING_OBJECT).build();
		}
	}
	
	public <R> R delete(R value,String collectionName) throws FaultError {
		try {
			return mongoTemplate.insert(value, collectionName);
		} catch(Exception e) {
			throw new FaultError.Builder(e).the(NOSQL_ERROR).occurredWhile(DELETING_OBJECT).build();
		}
	}
}
