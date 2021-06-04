package com.wrapper.templates.callers;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.StoredProcedure;
import org.springframework.stereotype.Component;
import com.wrapper.templates.exceptions.FaultError;
import com.wrapper.templates.exceptions.FaultError.ErrorType;

@Component
public class SQLDatabaseCaller {

	@Autowired
	private JdbcTemplate template;

	public <R> List<R> executeSQLQuery(String fullyFormedQuery, RowMapper<R> rowMapper)
			throws FaultError, SQLException {
		try {
			return template.query(fullyFormedQuery, rowMapper);
		} catch (Exception e) {
			throw new FaultError.Builder(e).theError(ErrorType.SQLQUERY_ERROR).withOccurredWhileCalling(template.getDataSource().getConnection().getSchema()).build();
		}
	}

	public <R> R executeSQLQuery(String fullyFormedQuery, Class<R> className) throws FaultError, SQLException {
		try {
			return template.queryForObject(fullyFormedQuery, className);
		} catch (Exception e) {
			throw new FaultError.Builder(e).theError(ErrorType.SQLQUERY_ERROR).withOccurredWhileCalling(template.getDataSource().getConnection().getSchema()).build();
		}
	}

	public <R> R executeSQLQuery(String fullyFormedQuery, ResultSetExtractor<R> extractor)
			throws FaultError, SQLException {
		try {
			return template.query(fullyFormedQuery, extractor);
		} catch (Exception e) {
			throw new FaultError.Builder(e).theError(ErrorType.SQLQUERY_ERROR).withOccurredWhileCalling(template.getDataSource().getConnection().getSchema()).build();
		}
	}

	public Map<String, Object> executeStoredProcedure(String packageName, String storedProcName,
			List<SqlOutParameter> outputParameters, List<SqlParameter> inputParameters, Map<String, ?> inputs)
			throws FaultError, SQLException {
		return new StoredProcedureCaller().call(packageName, storedProcName, outputParameters, inputParameters, inputs);
	}

	private final class StoredProcedureCaller extends StoredProcedure {
		public Map<String, Object> call(String packageName, String storedProcName,
				List<SqlOutParameter> outputParameters, List<SqlParameter> inputParameters, Map<String, ?> inputs)
				throws FaultError, SQLException {
			try {
				setJdbcTemplate(template);
				setSql(packageName + storedProcName);
				setParameters(inputParameters.toArray(new SqlParameter[] {}));
				setParameters(outputParameters.toArray(new SqlOutParameter[] {}));
				compile();
				return super.execute(inputs);
			} catch (Exception e) {
				throw new FaultError.Builder(e).theError(ErrorType.STOREDPROC_ERROR).withOccurredWhileCalling(template.getDataSource().getConnection().getSchema()).build();
			}
		}
	}

	public JdbcTemplate jdbcTemplate() {
		return template;
	}
}
