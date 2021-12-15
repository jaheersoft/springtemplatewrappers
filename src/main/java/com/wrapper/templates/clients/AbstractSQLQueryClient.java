package com.wrapper.templates.clients;

import java.util.List;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import com.wrapper.templates.callers.SQLDatabaseCaller;
import com.wrapper.templates.exceptions.ApplicationException;
import com.wrapper.templates.exceptions.FaultError;

public abstract class AbstractSQLQueryClient<R> {

	private SQLDatabaseCaller caller;

	private Class<R> className;

	private List<R> responses;

	private R response;

	private String query;

	private String[] queryInputs;

	private RowMapper<R> rowMapper;

	private ResultSetExtractor<R> extractor;

	public AbstractSQLQueryClient(SQLDatabaseCaller caller) {
		this.caller = caller;
	}

	public AbstractSQLQueryClient<R> sqlQuery(String query) {
		this.query = query;
		return this;
	}

	public AbstractSQLQueryClient<R> withQueryInputs(String[] queryInputs) {
		this.queryInputs = queryInputs;
		return this;
	}

	public AbstractSQLQueryClient<R> withResultsProcessor(RowMapper<R> rowMapper) {
		this.rowMapper = rowMapper;
		return this;
	}

	public AbstractSQLQueryClient<R> withResultsProcessor(ResultSetExtractor<R> extractor) {
		this.extractor = extractor;
		return this;
	}

	public AbstractSQLQueryClient<R> withResultsProcessor(Class<R> className) {
		this.className = className;
		return this;
	}

	public AbstractSQLQueryClient<R> execute() throws ApplicationException,FaultError {
		if (className != null) {
			response = caller().executeSQLQuery(query, className);
		}
		if (rowMapper != null) {
			responses = caller().executeSQLQuery(query, rowMapper);
		}
		if (extractor != null) {
			response = caller().executeSQLQuery(query, extractor);
		}
		return this;
	}

	public SQLDatabaseCaller caller() {
		return caller;
	}

	public List<R> responses() {
		return responses;
	}

	public R response() {
		return response;
	}

	public String query() {
		return query;
	}

	public String[] queryInputs() {
		return queryInputs;
	}

	public RowMapper<R> rowMapper() {
		return rowMapper();
	}

	public ResultSetExtractor<R> extractor() {
		return extractor();
	}

	public Class<R> className() {
		return className;
	}
}
