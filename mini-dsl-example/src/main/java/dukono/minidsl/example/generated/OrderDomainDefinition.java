package dukono.minidsl.example.generated;

import dukono.minidsl.annotation.DslDomain;
import dukono.minidsl.annotation.DslField;
import dukono.minidsl.annotation.DslOperation;
import dukono.minidsl.annotation.OperationType;

/**
 * Example showing all 4 operation types in action.
 * 
 * This example demonstrates real-world scenarios for each operation type:
 * 
 * 1. WITH_ARG - Standard comparisons (equal, greater, less, like, etc.) 2.
 * WITH_LIST - Membership checks (in, not in) 3. WITH_EMPTY - Null checks and
 * boolean flags (isNull, isNotNull, isActive) 4. JUST_ADD - Include field
 * without filtering (useful for projections)
 */
@DslDomain(name = "Order", packageName = "dukono.minidsl.example.generated.order", dtoClass = "dukono.minidsl.example.dto.OrderDto", fields = {
		@DslField(value = "orderId", type = "String"), @DslField(value = "customerName", type = "String"),
		@DslField(value = "totalAmount", type = "BigDecimal"), @DslField(value = "status", type = "String"),
		@DslField(value = "createdDate", type = "LocalDateTime"),
		@DslField(value = "items", type = "List<String>")}, operations = {
				// ========== WITH_ARG: Standard comparisons ==========

				@DslOperation(name = "equalTo", operator = "eq", type = OperationType.WITH_ARG, description = "Matches exact value"),

				@DslOperation(name = "notEqualTo", operator = "ne", type = OperationType.WITH_ARG, description = "Matches any value except the given one"),

				@DslOperation(name = "greaterThan", operator = "gt", type = OperationType.WITH_ARG, description = "Matches values greater than the given value"),

				@DslOperation(name = "greaterThanOrEqual", operator = "gte", type = OperationType.WITH_ARG, description = "Matches values greater than or equal to the given value"),

				@DslOperation(name = "lessThan", operator = "lt", type = OperationType.WITH_ARG, description = "Matches values less than the given value"),

				@DslOperation(name = "lessThanOrEqual", operator = "lte", type = OperationType.WITH_ARG, description = "Matches values less than or equal to the given value"),

				@DslOperation(name = "like", operator = "like", type = OperationType.WITH_ARG, description = "Pattern matching (case sensitive)"),

				@DslOperation(name = "ilike", operator = "ilike", type = OperationType.WITH_ARG, description = "Pattern matching (case insensitive)"),

				@DslOperation(name = "contains", operator = "contains", type = OperationType.WITH_ARG, description = "Checks if field contains the given substring"),

				@DslOperation(name = "startsWith", operator = "starts_with", type = OperationType.WITH_ARG, description = "Checks if field starts with the given prefix"),

				@DslOperation(name = "endsWith", operator = "ends_with", type = OperationType.WITH_ARG, description = "Checks if field ends with the given suffix"),

				// ========== WITH_LIST: Membership checks ==========

				@DslOperation(name = "inValues", operator = "in", type = OperationType.WITH_LIST, description = "Checks if field value is in the given list"),

				@DslOperation(name = "notInValues", operator = "nin", type = OperationType.WITH_LIST, description = "Checks if field value is not in the given list"),

				// ========== WITH_EMPTY: Null checks and boolean flags ==========

				@DslOperation(name = "isNull", operator = "is_null", type = OperationType.WITH_EMPTY, description = "Checks if field is null"),

				@DslOperation(name = "isNotNull", operator = "is_not_null", type = OperationType.WITH_EMPTY, description = "Checks if field is not null"),

				@DslOperation(name = "isEmpty", operator = "is_empty", type = OperationType.WITH_EMPTY, description = "Checks if collection/string field is empty"),

				@DslOperation(name = "isNotEmpty", operator = "is_not_empty", type = OperationType.WITH_EMPTY, description = "Checks if collection/string field is not empty"),

				// ========== JUST_ADD: Include without filtering ==========

				@DslOperation(name = "include", type = OperationType.JUST_ADD, description = "Includes the field in the query without any filter"),

				@DslOperation(name = "select", type = OperationType.JUST_ADD, description = "Selects the field for projection")}, generateApi = true)
public class OrderDomainDefinition {
	// This class only holds the annotation
	// All implementation is generated at compile-time

	/**
	 * USAGE EXAMPLES:
	 * 
	 * // Example 1: Find high-value orders OrderApi.from() .field(f ->
	 * f.TOTAL_AMOUNT).greaterThan(new BigDecimal("1000.00")) .and() .field(f ->
	 * f.STATUS).inValues(List.of("PENDING", "PROCESSING")) .getDto();
	 * 
	 * // Example 2: Find orders with null customer OrderApi.from() .field(f ->
	 * f.CUSTOMER_NAME).isNull() .or() .field(f -> f.CUSTOMER_NAME).isEmpty()
	 * .getDto();
	 * 
	 * // Example 3: Search by customer name pattern OrderApi.from() .field(f ->
	 * f.CUSTOMER_NAME).like("%John%") .and() .field(f ->
	 * f.STATUS).notEqualTo("CANCELLED") .getDto();
	 * 
	 * // Example 4: Select specific fields (projection) OrderApi.from() .field(f ->
	 * f.ORDER_ID).include() .and() .field(f -> f.TOTAL_AMOUNT).include() .and()
	 * .field(f -> f.STATUS).equalTo("COMPLETED") .getDto();
	 * 
	 * // Example 5: Complex query with all operation types OrderApi.from() .field(f
	 * -> f.TOTAL_AMOUNT).greaterThanOrEqual(new BigDecimal("100.00")) // WITH_ARG
	 * .and() .field(f -> f.STATUS).inValues(List.of("PENDING", "PROCESSING")) //
	 * WITH_LIST .and() .field(f -> f.CUSTOMER_NAME).isNotNull() // WITH_EMPTY
	 * .and() .field(f -> f.ORDER_ID).select() // JUST_ADD .getDto();
	 */
}
