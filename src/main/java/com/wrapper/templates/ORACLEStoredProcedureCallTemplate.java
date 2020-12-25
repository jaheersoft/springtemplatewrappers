package com.wrapper.templates;

import static com.wrapper.templates.utilities.Collections.enumsToCommaDelimitedStringWithQuotes;
import static com.wrapper.templates.utilities.Collections.toCommaSeparatedStringWithQuotes;
import static com.wrapper.templates.utilities.Strings.join;
import static java.sql.Types.NUMERIC;
import static java.sql.Types.VARCHAR;
import static oracle.jdbc.OracleTypes.CURSOR;
import static org.springframework.util.CollectionUtils.isEmpty;
import java.math.BigDecimal;
import java.sql.Array;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.jdbc.core.support.AbstractSqlTypeValue;
import org.springframework.jdbc.core.support.SqlLobValue;

import com.wrapper.templates.exceptions.DataAccessException;
import com.wrapper.templates.exceptions.FaultException;
import com.wrapper.templates.exceptions.FaultException.ErrorType;
import com.wrapper.templates.utilities.Dates;
import oracle.jdbc.OracleTypes;

public class ORACLEStoredProcedureCallTemplate<RESPONSE> {

	private static final Logger LOGGER = LoggerFactory.getLogger(ORACLEStoredProcedureCallTemplate.class);

	private static final String ERROR_CODE_PARAM = "oStatusCode";
	private static final String ERROR_MESSAGE_PARAM = "oStatusDesc";
	private static final String FAILED_RECORDS_PARAM = "oFailedRecords";
	private static final String EXECUTED_SQL_PARAM = "oExecutedSQL";

	private JdbcTemplate jdbcTemplate;
	private String packageName;
	private String schemaName;
	private String storedProcName;
	private String outputParamName;
	private List<SqlOutParameter> outputParameters = new ArrayList<>();
	private List<SqlParameter> inputParameters = new ArrayList<>();
	private Map<String, Object> arguments = new TreeMap<>();
	private Map<String, Object> results;

	public ORACLEStoredProcedureCallTemplate() {
		this.outputParameters.add(new SqlOutParameter(ERROR_CODE_PARAM, NUMERIC));
		this.outputParameters.add(new SqlOutParameter(ERROR_MESSAGE_PARAM, VARCHAR));
		this.outputParameters.add(new SqlOutParameter(FAILED_RECORDS_PARAM, VARCHAR));
		this.outputParameters.add(new SqlOutParameter(EXECUTED_SQL_PARAM, VARCHAR));
	}

	public ORACLEStoredProcedureCallTemplate<RESPONSE> jdbcTemplate(final JdbcTemplate jdbcTemplateLocal) {
		this.jdbcTemplate = jdbcTemplateLocal;
		return this;
	}

	public ORACLEStoredProcedureCallTemplate<RESPONSE> packageName(final String packageNameLocal) {
		this.packageName = packageNameLocal;
		return this;
	}

	public ORACLEStoredProcedureCallTemplate<RESPONSE> schemaName(final String schemaNameLocal) {
		this.schemaName = schemaNameLocal;
		return this;
	}

	public ORACLEStoredProcedureCallTemplate<RESPONSE> storedProcName(final String storedProcNameLocal) {
		this.storedProcName = storedProcNameLocal;
		return this;
	}

	public ORACLEStoredProcedureCallTemplate<RESPONSE> stringInput(String name, String value) {
		this.inputParam(name, VARCHAR, value);
		return this;
	}

	public ORACLEStoredProcedureCallTemplate<RESPONSE> arrayInput(String name, Array reportsArray) {
		this.inputParam(name, oracle.jdbc.OracleTypes.ARRAY, reportsArray);
		return this;
	}

	public ORACLEStoredProcedureCallTemplate<RESPONSE> stringsInput(String name, Collection<String> values) {
		this.inputParam(name, VARCHAR, toCommaSeparatedStringWithQuotes(values));
		return this;
	}

	public <E extends Enum<E>> ORACLEStoredProcedureCallTemplate<RESPONSE> enumsInput(String name, Collection<E> values,
			Function<E, String> function) {
		this.inputParam(name, VARCHAR, enumsToCommaDelimitedStringWithQuotes(values, function));
		return this;
	}

	public ORACLEStoredProcedureCallTemplate<RESPONSE> numericInput(String name, Number value) {
		this.inputParam(name, NUMERIC, value);
		return this;
	}

	public ORACLEStoredProcedureCallTemplate<RESPONSE> dateInput(String name, LocalDate date) {
		String value = date == null ? null : Dates.toString(date, Dates.DD_MMM_YYYY);
		this.inputParam(name, VARCHAR, value);
		return this;
	}

	public ORACLEStoredProcedureCallTemplate<RESPONSE> dateInput(String name, java.sql.Date dates) {
		String value = dates == null ? null : dates.toString();
		this.inputParam(name, java.sql.Types.DATE, value);
		return this;
	}

	public ORACLEStoredProcedureCallTemplate<RESPONSE> dateInput(String name, Date date) {
		String value = date == null ? null : Dates.toFormatMMddyyyy(date);
		this.inputParam(name, VARCHAR, value);
		return this;
	}

	public ORACLEStoredProcedureCallTemplate<RESPONSE> clobInput(String name, String value) {
		LOGGER.debug("CLOB Input: {} = {}", name, value);
		this.inputParameters.add(new SqlParameter(name, OracleTypes.CLOB));
		this.arguments.put(name, new SqlLobValue(value));
		return this;
	}

	public ORACLEStoredProcedureCallTemplate<RESPONSE> collectionInput(String name, Collection value) {
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

	private ORACLEStoredProcedureCallTemplate<RESPONSE> inputParam(String name, Integer type, Object value) {
		this.inputParameters.add(new SqlParameter(name, type));
		this.arguments.put(name, value);
		return this;
	}

	public ORACLEStoredProcedureCallTemplate<RESPONSE> cursorOutput(final String outputParamNameLocal,
			RowMapper<?> rowMapper) {
		this.outputParamName = outputParamNameLocal;
		this.outputParameters.add(new SqlOutParameter(this.outputParamName, CURSOR, rowMapper));
		return this;
	}

	public ORACLEStoredProcedureCallTemplate<RESPONSE> cursorOutput(final String outputParamNameLocal,
			RowCallbackHandler rowCallbackHandler) {
		this.outputParamName = outputParamNameLocal;
		this.outputParameters.add(new SqlOutParameter(this.outputParamName, CURSOR, rowCallbackHandler));
		return this;
	}

	public ORACLEStoredProcedureCallTemplate<RESPONSE> cursorOutput(final String outputParamNameLocal,
			ResultSetExtractor<RESPONSE> resultSetExtractor) {
		this.outputParamName = outputParamNameLocal;
		this.outputParameters.add(new SqlOutParameter(this.outputParamName, CURSOR, resultSetExtractor));
		return this;
	}

	public ORACLEStoredProcedureCallTemplate<RESPONSE> stringOutput(final String outputParamNameLocal,
			RowMapper<?> rowMapper) {
		this.outputParamName = outputParamNameLocal;
		this.outputParameters.add(new SqlOutParameter(this.outputParamName, VARCHAR, rowMapper));
		return this;
	}

	@SuppressWarnings({ "unchecked", "PMD.AvoidCatchingGenericException" })
	public RESPONSE call() throws FaultException, DataAccessException {
		SimpleJdbcCall jdbcCall = new SimpleJdbcCall(this.jdbcTemplate).withSchemaName(this.schemaName)
				.withCatalogName(this.packageName).withProcedureName(this.storedProcName)
				.declareParameters(this.outputParameters.toArray(new SqlOutParameter[] {}));

		if (!isEmpty(this.inputParameters)) {
			jdbcCall.declareParameters(this.inputParameters.toArray(new SqlParameter[] {}));
		}

		try {
			LOGGER.debug("{}", beforeSpCallLogMessage());

			this.results = jdbcCall.execute(this.arguments);

			LOGGER.debug("{}", afterSpCallLogMessage());
		} catch (Exception e) {
			String message = join("Exception invoking SP ", this.packageName, ".", this.storedProcName,
					" : Exception: ", e.getMessage());
			LOGGER.error("{}", message, e);
			throw new FaultException.Builder(e).consumerInputs(message).withServiceCalledURL(jdbcCall.getCallString())
					.forErrorType(ErrorType.ORACLE_ERROR).build();
		}
		checkForErrors();
		LOGGER.debug("{}", this.results);
		return (RESPONSE) this.results.get(this.outputParamName);
	}

	@SuppressWarnings("PMD.AvoidDuplicateLiterals")
	protected String beforeSpCallLogMessage() {
		// Perform this complex string concatenation only if debug is enabled
		if (LOGGER.isDebugEnabled()) {
			String message = join("CALL ", this.schemaName, ".", this.packageName, ".", this.storedProcName, " ( ");

			StringBuilder inputParams = new StringBuilder();
			for (SqlParameter parameter : this.inputParameters) {
				inputParams.append(parameter.getName()).append(" IN, ");
			}

			String outputParams = join(this.outputParamName, " OUT, ", this.ERROR_CODE_PARAM, " OUT, ",
					this.ERROR_MESSAGE_PARAM, " OUT, ", this.EXECUTED_SQL_PARAM, " OUT ) \n");

			StringBuilder inputValues = new StringBuilder();
			for (Map.Entry<String, Object> entry : this.arguments.entrySet()) {
				String value = entry.getValue() == null ? "null" : entry.getValue().toString();
				inputValues.append(entry.getKey()).append(" = ").append(value).append(" \n");
			}

			return join(message, inputParams, outputParams, inputValues);
		}
		return "";
	}

	protected String afterSpCallLogMessage() {
		// Perform this complex string concatenation only if debug is enabled
		if (LOGGER.isDebugEnabled()) {
			StringBuilder outputMessage = new StringBuilder().append("Results for ").append(this.packageName)
					.append(".").append(this.storedProcName).append(" () \n");

			if (!isEmpty(this.results)) {
				outputMessage.append(ERROR_CODE_PARAM).append(" = ").append(getErrorCode()).append("\n");
				outputMessage.append(ERROR_MESSAGE_PARAM).append(" = ").append(getErrorMessage()).append("\n");
				outputMessage.append(FAILED_RECORDS_PARAM).append(" = ").append(getFailedRecords()).append("\n");
				outputMessage.append(EXECUTED_SQL_PARAM).append(" = ").append(getExecutedSQL()).append("\n");
			}

			return outputMessage.toString();
		}
		return "";
	}

	/**
	 * Check for errors and throw exception
	 * @throws com.wrapper.templates.exceptions.DataAccessException 
	 */
	protected void checkForErrors() throws DataAccessException {
		if (hasError()) {
			String message = join("Exception invoking SP ", this.packageName, ".", this.storedProcName,
					" : Error Code: ", getErrorCode(), " - Error Msg: ", getErrorMessage());
			Map<String, String> failedRecords = getFailedRecords();
			LOGGER.error("{} : \nFailed Records = {}", message, failedRecords);
			throw new DataAccessException(message, failedRecords);
		}
	}

	/**
	 * Checks if the SP invocation has errors
	 * 
	 * @return true, if the SP invocation has errors
	 */
	protected boolean hasError() {
		if (this.results != null) {
			Object errorCode = results.get(ERROR_CODE_PARAM);
			if (errorCode != null && !BigDecimal.ZERO.equals(errorCode)) {
				return true;
			}

			Object failedRecords = results.get(FAILED_RECORDS_PARAM);
			if (failedRecords != null) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Return the SP output parameter 'oStatusCode' value
	 * 
	 * @return value from output parameter 'oStatusCode'
	 */
	public Object getErrorCode() {
		return this.results.get(ERROR_CODE_PARAM);
	}

	/**
	 * Return the SP output parameter 'oStatusDesc' value
	 * 
	 * @return value from output parameter 'oStatusDesc'
	 */
	public Object getErrorMessage() {
		return this.results.get(ERROR_MESSAGE_PARAM);
	}

	/**
	 * Return the SP output parameter 'oExecutedSQL' value
	 * 
	 * @return value from output parameter 'oExecutedSQL'
	 */
	public Object getExecutedSQL() {
		return this.results.get(EXECUTED_SQL_PARAM);
	}

	/**
	 * Translates the SP output parameter failedRecords into a map of failed row id
	 * and error message
	 * 
	 * @return map of failed row id and error message
	 */
	public Map<String, String> getFailedRecords() {
		Map<String, String> idVsErrorMsg = new HashMap<>();

		String failedRecordString = (String) this.results.get(FAILED_RECORDS_PARAM);
		if (failedRecordString != null) {
			String[] failedRecords = failedRecordString.split("\\|");

			for (String failedRecord : failedRecords) {
				String[] idAndError = failedRecord.split("=");

				if ((idAndError != null) && (idAndError.length == 2)) {
					idVsErrorMsg.put(idAndError[0], idAndError[1]);
				}
			}
		}
		return idVsErrorMsg;
	}

}
