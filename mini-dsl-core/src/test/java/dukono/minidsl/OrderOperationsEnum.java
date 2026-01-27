package dukono.minidsl;

import dukono.minidsl.annotation.OperationDefinition;
import dukono.minidsl.annotation.OperationType;

/**
 * Enum that defines operations for the Order domain.
 * 
 * This is an alternative to using @DslOperation[] in @DslDomain annotation.
 * 
 * Benefits: - Type-safe - More concise than annotation array - Easier to
 * maintain - Reusable across different DSL definitions
 * 
 * Each enum constant represents an operation with: - name: method name (e.g.,
 * "equalTo") - operator: query operator (e.g., "eq") - type: operation type
 * (WITH_ARG, WITH_LIST, WITH_EMPTY, JUST_ADD) - description: optional
 * description
 */
public enum OrderOperationsEnum implements OperationDefinition {

	// ========== WITH_ARG: Standard comparisons ==========
	EQUAL_TO("equalTo", ",", OperationType.WITH_ARG, "Matches exact value"), NO_OP_VALS("noOpVals", null,
			OperationType.NO_OP_WITH_ARG, "Adds field with value but without operator"),

	// ========== WITH_LIST: Membership checks ==========
	IN_VALUES("inValues", "in", OperationType.WITH_LIST, "Checks if field value is in the given list", "|", "[]"),

	// ========== NO_OP_WITH_LIST: Operations with list but without operator
	NO_OP_LIST("noOpList", null, OperationType.NO_OP_WITH_LIST, "Adds field with list values but without operator", "$",
			"[]"),

	// ========== NO_VALUE: Null checks and boolean flags ==========
	IS_NULL("isNull", "is_null", OperationType.NO_VALUE, "Checks if field is null"),

	// ========== NO_OP_NO_VALUE: Include without filtering (no operator, no value)
	INCLUDE("include", null, OperationType.NO_OP_NO_VALUE, "Includes the field in the query without any filter");

	private final String name;
	private final String operator;
	private final OperationType type;
	private final String description;
	private final String listDelimiter;
	private final String listBrackets;

	OrderOperationsEnum(final String name, final String operator, final OperationType type, final String description) {
		this(name, operator, type, description, " ", "");
	}

	OrderOperationsEnum(final String name, final String operator, final OperationType type, final String description,
			final String listDelimiter, final String listBrackets) {
		this.name = name;
		this.operator = operator;
		this.type = type;
		this.description = description;
		this.listDelimiter = listDelimiter;
		this.listBrackets = listBrackets;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public String getOperator() {
		return this.operator;
	}

	@Override
	public OperationType getType() {
		return this.type;
	}

	@Override
	public String getDescription() {
		return this.description;
	}

	@Override
	public String getListDelimiter() {
		return this.listDelimiter;
	}

	@Override
	public String getListBrackets() {
		return this.listBrackets;
	}
}
