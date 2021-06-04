package com.wrapper.templates.clients;

import static com.wrapper.templates.utilities.Collections.enumsToCommaDelimitedStringWithQuotes;
import static com.wrapper.templates.utilities.Collections.toCommaSeparatedStringWithQuotes;
import static java.sql.Types.NUMERIC;
import static java.sql.Types.VARCHAR;
import java.sql.Array;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.support.AbstractSqlTypeValue;
import org.springframework.jdbc.core.support.SqlLobValue;
import com.wrapper.templates.callers.SQLDatabaseCaller;
import com.wrapper.templates.exceptions.IncompleteExternalCallOutputException;
import com.wrapper.templates.utilities.Dates;

public abstract class AbstractStoredProcedureClient<R> {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractStoredProcedureClient.class);
	
	private List<SqlParameter> inputParameters = new ArrayList<>();
	private List<SqlOutParameter> outputParameters = new ArrayList<>();
	private Map<String,Object> arguments = new TreeMap<>();
	private Map<String, Object> results;
	
	private SQLDatabaseCaller caller;
	
	private R response;
	
	private String packageName;
	
	private String storedProcedureName;
	
	private String outputParamName;
	
	private int cursorType;
	
	private int numberType;
	
	private int clobType;
	
	private int arrayType;
	
	public AbstractStoredProcedureClient(SQLDatabaseCaller caller) {
		this.caller = caller;
	}

	public AbstractStoredProcedureClient<R> packageName(String packageName) {
		this.packageName = packageName;
		return this;
	}
	
	public AbstractStoredProcedureClient<R> storedProcedureName(String storedProcedureName) {
		this.storedProcedureName = storedProcedureName;
		return this;
	}
	
	public AbstractStoredProcedureClient<R> stringInput(String name, String value) {
		this.inputParam(name, VARCHAR, value);
		return this;
	}

	public AbstractStoredProcedureClient<R> arrayInput(String name, Array reportsArray) {
		this.inputParam(name, arrayType(), reportsArray);
		return this;
	}

	public AbstractStoredProcedureClient<R> stringsInput(String name, Collection<String> values) {
		this.inputParam(name, VARCHAR, toCommaSeparatedStringWithQuotes(values));
		return this;
	}

	public <E extends Enum<E>> AbstractStoredProcedureClient<R> enumsInput(String name, Collection<E> values,
			Function<E, String> function) {
		this.inputParam(name, VARCHAR, enumsToCommaDelimitedStringWithQuotes(values, function));
		return this;
	}

	public AbstractStoredProcedureClient<R> numericInput(String name, Number value) {
		this.inputParam(name, NUMERIC, value);
		return this;
	}

	public AbstractStoredProcedureClient<R> dateInput(String name, LocalDate date) {
		String value = date == null ? null : Dates.toString(date, Dates.DD_MMM_YYYY);
		this.inputParam(name, VARCHAR, value);
		return this;
	}

	/*public AbstractStoredProcedureClient<R> dateInput(String name, Date dates) {
		String value = dates == null ? null : dates.toString();
		this.inputParam(name, java.sql.Types.DATE, value);
		return this;
	}*/

	public AbstractStoredProcedureClient<R> dateInput(String name, Date date) {
		String value = date == null ? null : Dates.toFormatMMddyyyy(date);
		this.inputParam(name, VARCHAR, value);
		return this;
	}

	public AbstractStoredProcedureClient<R> clobInput(String name, String value) {
		LOGGER.debug("CLOB Input: {} = {}", name, value);
		this.inputParameters.add(new SqlParameter(name, clobType()));
		this.arguments.put(name, new SqlLobValue(value));
		return this;
	}

	public AbstractStoredProcedureClient<R> collectionInput(String name, Collection value) {
		final Object[] arrayInput = value == null ? null : value.toArray(new Object[] {});
		this.inputParameters.add(new SqlParameter(name, Types.ARRAY, "ARRAY_TABLE"));

		// Add input value
		this.arguments.put(name, new AbstractSqlTypeValue() {
			@Override
			protected Object createTypeValue(Connection connection, int sqlType, String typeName) throws SQLException {
				return connection.createArrayOf(typeName, arrayInput);
			}
		});
		return this;
	}

	private AbstractStoredProcedureClient<R> inputParam(String name, Integer type, Object value) {
		this.inputParameters.add(new SqlParameter(name, type));
		this.arguments.put(name, value);
		return this;
	}

	public AbstractStoredProcedureClient<R> cursorOutput(final String outputParamNameLocal,
			RowMapper<?> rowMapper) {
		this.outputParamName = outputParamNameLocal;
		this.outputParameters.add(new SqlOutParameter(this.outputParamName, cursorType(), rowMapper));
		return this;
	}

	public AbstractStoredProcedureClient<R> cursorOutput(final String outputParamNameLocal,
			RowCallbackHandler rowCallbackHandler) {
		this.outputParamName = outputParamNameLocal;
		this.outputParameters.add(new SqlOutParameter(this.outputParamName, cursorType(), rowCallbackHandler));
		return this;
	}

	public AbstractStoredProcedureClient<R> cursorOutput(final String outputParamNameLocal,
			ResultSetExtractor<R> resultSetExtractor) {
		this.outputParamName = outputParamNameLocal;
		this.outputParameters.add(new SqlOutParameter(this.outputParamName, cursorType(), resultSetExtractor));
		return this;
	}

	public AbstractStoredProcedureClient<R> stringOutput(final String outputParamNameLocal,
			RowMapper<?> rowMapper) {
		this.outputParamName = outputParamNameLocal;
		this.outputParameters.add(new SqlOutParameter(this.outputParamName, VARCHAR, rowMapper));
		return this;
	}
	
	public AbstractStoredProcedureClient<R> stringOutput(final String outputParamNameLocal) {
		this.outputParamName = outputParamNameLocal;
		this.outputParameters.add(new SqlOutParameter(this.outputParamName, VARCHAR));
		return this;
	}
	
	public AbstractStoredProcedureClient<R> numericOutput(final String outputParamNameLocal) {
		this.outputParamName = outputParamNameLocal;
		this.outputParameters.add(new SqlOutParameter(this.outputParamName, numberType()));
		return this;
	}
	
	public AbstractStoredProcedureClient<R> call() throws IncompleteExternalCallOutputException {
		try {
			results = caller().executeStoredProcedure(packageName(), storedProcedureName(), outputParameters(), inputParameters(), arguments());
			response = handleResults(results);
		} catch(Exception e) {
			throw new IncompleteExternalCallOutputException.Builder(e).build();
		}
		return this;
	}
	
	protected abstract R handleResults(Map<String,Object> results);
	
	public int cursorType() {
		return cursorType;
	}
	
	public int numberType() {
		return numberType;
	}
	
	public int clobType() {
		return clobType;
	}
	
	public int arrayType() {
		return arrayType;
	}
	
	public void setCursorType(int cursorType) {
		this.cursorType = cursorType;
	}
	
	public void setNumberType(int numberType) {
		this.numberType = numberType;
	}
	
	public void setClobType(int clobType) {
		this.clobType = clobType;
	}
	
	public void setArrayType(int arrayType) {
		this.arrayType = arrayType;
	}
	
	public SQLDatabaseCaller caller() {
		return caller;
	}
	
	public String packageName() {
		return packageName;
	}
	
	public String storedProcedureName() {
		return storedProcedureName;
	}
	
	public List<SqlParameter> inputParameters() {
		return inputParameters;
	}
	
	public List<SqlOutParameter> outputParameters() {
		return outputParameters;
	}
	
	public Map<String,Object> arguments() {
		return arguments;
	}
	
	public Map<String,?> results() {
		return results;
	}
	
	public String outputParamName() {
		return outputParamName;
	}
	
	public R response() {
		return response;
	}
}
