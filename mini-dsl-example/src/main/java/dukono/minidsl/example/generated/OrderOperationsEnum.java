package dukono.minidsl.example.generated;

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
	EQUAL_TO("equalTo", "eq", OperationType.WITH_ARG, "Matches exact value"),

	NOT_EQUAL_TO("notEqualTo", "ne", OperationType.WITH_ARG, "Matches any value except the given one"),

	GREATER_THAN("greaterThan", "gt", OperationType.WITH_ARG, "Matches values greater than the given value"),

	GREATER_THAN_OR_EQUAL("greaterThanOrEqual", "gte", OperationType.WITH_ARG,
			"Matches values greater than or equal to the given value"),

	LESS_THAN("lessThan", "lt", OperationType.WITH_ARG, "Matches values less than the given value"),

	LESS_THAN_OR_EQUAL("lessThanOrEqual", "lte", OperationType.WITH_ARG,
			"Matches values less than or equal to the given value"),

	LIKE("like", "like", OperationType.WITH_ARG, "Pattern matching (case sensitive)"),

	ILIKE("ilike", "ilike", OperationType.WITH_ARG, "Pattern matching (case insensitive)"),

	CONTAINS("contains", "contains", OperationType.WITH_ARG, "Checks if field contains the given substring"),

	STARTS_WITH("startsWith", "starts_with", OperationType.WITH_ARG, "Checks if field starts with the given prefix"),

	ENDS_WITH("endsWith", "ends_with", OperationType.WITH_ARG, "Checks if field ends with the given suffix"),

	// ========== WITH_LIST: Membership checks ==========
	IN_VALUES("inValues", "in", OperationType.WITH_LIST, "Checks if field value is in the given list"),

	NOT_IN_VALUES("notInValues", "nin", OperationType.WITH_LIST, "Checks if field value is not in the given list"),

	// ========== WITH_EMPTY: Null checks and boolean flags ==========
	IS_NULL("isNull", "is_null", OperationType.WITH_EMPTY, "Checks if field is null"),

	IS_NOT_NULL("isNotNull", "is_not_null", OperationType.WITH_EMPTY, "Checks if field is not null"),

	IS_EMPTY("isEmpty", "is_empty", OperationType.WITH_EMPTY, "Checks if collection/string field is empty"),

	IS_NOT_EMPTY("isNotEmpty", "is_not_empty", OperationType.WITH_EMPTY,
			"Checks if collection/string field is not empty"),

	// ========== JUST_ADD: Include without filtering ==========
	INCLUDE("include", null, OperationType.JUST_ADD, "Includes the field in the query without any filter"),

	SELECT("select", null, OperationType.JUST_ADD, "Selects the field for projection");

	private final String name;
	private final String operator;
	private final OperationType type;
	private final String description;

	OrderOperationsEnum(final String name, final String operator, final OperationType type, final String description) {
		this.name = name;
		this.operator = operator;
		this.type = type;
		this.description = description;
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
}
