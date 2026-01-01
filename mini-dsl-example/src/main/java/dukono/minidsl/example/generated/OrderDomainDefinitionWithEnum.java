package dukono.minidsl.example.generated;

/**
 * Alternative version using fieldsEnum instead of fields array.
 * 
 * This is much more concise and easier to maintain than the array-based
 * approach.
 * 
 * Compare: - OLD: @DslField(value = "orderId", javaName = "ORDER_ID") - NEW:
 * Just define OrderFields enum with: ORDER_ID("orderId")
 */
// @DslDomain(name = "Order", packageName =
// "dukono.minidsl.example.generated.order", dtoClass =
// "dukono.minidsl.example.dto.OrderDto", fieldsEnum = OrderFields.class, // <--
// // Using
// // Enum
// // instead
// // of
// // fields
// // array
// withActions = true, withLogical = true, withList = true, operations = {
// // ========== WITH_ARG: Standard comparisons ==========
//
// @DslOperation(name = "equalTo", operator = "eq", type =
// OperationType.WITH_ARG, description = "Matches exact value"),
//
// @DslOperation(name = "notEqualTo", operator = "ne", type =
// OperationType.WITH_ARG, description = "Matches any value except the given
// one"),
//
// @DslOperation(name = "greaterThan", operator = "gt", type =
// OperationType.WITH_ARG, description = "Matches values greater than the given
// value"),
//
// @DslOperation(name = "greaterThanOrEqual", operator = "gte", type =
// OperationType.WITH_ARG, description = "Matches values greater than or equal
// to the given value"),
//
// @DslOperation(name = "lessThan", operator = "lt", type =
// OperationType.WITH_ARG, description = "Matches values less than or equal to
// the given value"),
//
// @DslOperation(name = "lessThanOrEqual", operator = "lte", type =
// OperationType.WITH_ARG, description = "Matches values less than or equal to
// the given value"),
//
// @DslOperation(name = "like", operator = "like", type =
// OperationType.WITH_ARG, description = "Pattern matching (case sensitive)"),
//
// @DslOperation(name = "ilike", operator = "ilike", type =
// OperationType.WITH_ARG, description = "Pattern matching (case insensitive)"),
//
// @DslOperation(name = "contains", operator = "contains", type =
// OperationType.WITH_ARG, description = "Checks if field contains the given
// substring"),
//
// @DslOperation(name = "startsWith", operator = "starts_with", type =
// OperationType.WITH_ARG, description = "Checks if field starts with the given
// prefix"),
//
// @DslOperation(name = "endsWith", operator = "ends_with", type =
// OperationType.WITH_ARG, description = "Checks if field ends with the given
// suffix"),
//
// // ========== WITH_LIST: Membership checks ==========
//
// @DslOperation(name = "inValues", operator = "in", type =
// OperationType.WITH_LIST, description = "Checks if field value is in the given
// list"),
//
// @DslOperation(name = "notInValues", operator = "nin", type =
// OperationType.WITH_LIST, description = "Checks if field value is not in the
// given list"),
//
// // ========== WITH_EMPTY: Null checks and boolean flags ==========
//
// @DslOperation(name = "isNull", operator = "is_null", type =
// OperationType.WITH_EMPTY, description = "Checks if field is null"),
//
// @DslOperation(name = "isNotNull", operator = "is_not_null", type =
// OperationType.WITH_EMPTY, description = "Checks if field is not null"),
//
// @DslOperation(name = "isEmpty", operator = "is_empty", type =
// OperationType.WITH_EMPTY, description = "Checks if collection/string field is
// empty"),
//
// @DslOperation(name = "isNotEmpty", operator = "is_not_empty", type =
// OperationType.WITH_EMPTY, description = "Checks if collection/string field is
// not empty"),
//
// // ========== JUST_ADD: Include without filtering ==========
//
// @DslOperation(name = "include", type = OperationType.JUST_ADD, description =
// "Includes the field in the query without any filter"),
//
// @DslOperation(name = "select", type = OperationType.JUST_ADD, description =
// "Selects the field for projection")}, generateApi = true)
public class OrderDomainDefinitionWithEnum {
	// This class only holds the annotation
	// All implementation is generated at compile-time

	/**
	 * BENEFITS OF USING ENUM:
	 * 
	 * 1. More concise - Just one line per field instead of full annotation 2.
	 * Type-safe - Enum provides compile-time safety 3. Easier to maintain -
	 * Refactoring is simpler 4. Less verbose - Compare 6 lines of @DslField vs 1
	 * line in enum 5. Reusable - The enum can be used in other places if needed
	 * 
	 * OLD WAY (verbose): fields = { @DslField(value = "orderId", javaName =
	 * "ORDER_ID"), @DslField(value = "customerName", javaName = "CUSTOMER_NAME"),
	 * ... }
	 * 
	 * NEW WAY (concise): fieldsEnum = OrderFields.class
	 * 
	 * And in OrderFields.java: ORDER_ID("orderId"), CUSTOMER_NAME("customerName"),
	 * ...
	 */
}
