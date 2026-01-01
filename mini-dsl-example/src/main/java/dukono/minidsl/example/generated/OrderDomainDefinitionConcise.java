package dukono.minidsl.example.generated;

import dukono.minidsl.annotation.DslDomain;

/**
 * Alternative version using BOTH fieldsConstants AND operationsEnum.
 * 
 * This is the MOST CONCISE approach - no verbose annotations, just references
 * to external classes.
 * 
 * PACKAGE INFERENCE: Notice that packageName is NOT specified! It will be
 * automatically inferred from fieldsConstants (OrderFieldConstants.class
 * package).
 * 
 * Compare:
 * 
 * OLD (verbose): fields = { @DslField(...), @DslField(...), ... } operations =
 * { @DslOperation(...), @DslOperation(...), ... }
 * 
 * NEW (concise): fieldsConstants = OrderFieldConstants.class operationsEnum =
 * OrderOperationsEnum.class // packageName is automatically inferred!
 * 
 * This makes the @DslDomain annotation extremely clean and readable!
 */
@DslDomain(name = "Order",
		// packageName is NOT specified - it will be inferred from OrderFieldConstants!
		dtoClass = "dukono.minidsl.example.dto.OrderDto", fieldsConstants = OrderFieldConstants.class, operationsEnum = OrderOperationsEnum.class)
public class OrderDomainDefinitionConcise {
	// This class only holds the annotation
	// All implementation is generated at compile-time

	/**
	 * BENEFITS OF USING ENUM FOR OPERATIONS:
	 * 
	 * 1. Type-safe - Enum provides compile-time safety 2. Concise - One line per
	 * operation instead of full annotation 3. Reusable - The enum can be used in
	 * multiple DSL definitions 4. Easier to maintain - All operations in one place
	 * 5. Better IDE support - Autocomplete and navigation work better 6. No syntax
	 * errors - Enum catches typos at compile time
	 * 
	 * COMPARISON:
	 * 
	 * OLD WAY (verbose): operations = { @DslOperation(name = "equalTo", operator =
	 * "eq", type = OperationType.WITH_ARG), @DslOperation(name = "notEqualTo",
	 * operator = "ne", type = OperationType.WITH_ARG), @DslOperation(name =
	 * "greaterThan", operator = "gt", type = OperationType.WITH_ARG), ... }
	 * 
	 * NEW WAY (concise): operationsEnum = OrderOperationsEnum.class
	 * 
	 * And in OrderOperationsEnum.java: EQUAL_TO("equalTo", "eq",
	 * OperationType.WITH_ARG), NOT_EQUAL_TO("notEqualTo", "ne",
	 * OperationType.WITH_ARG), GREATER_THAN("greaterThan", "gt",
	 * OperationType.WITH_ARG), ...
	 * 
	 * This annotation now has only 5 lines instead of 60+!
	 */
}
