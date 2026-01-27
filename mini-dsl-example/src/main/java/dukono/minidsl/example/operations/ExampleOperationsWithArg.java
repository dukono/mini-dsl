package dukono.minidsl.example.operations;

import dukono.minidsl.annotation.DslDomain;
import dukono.minidsl.annotation.DslField;
import dukono.minidsl.annotation.OperationDefinition;
import dukono.minidsl.annotation.OperationType;

/**
 * Ejemplo: Operaciones básicas de tipo WITH_ARG
 * 
 * Operaciones que reciben un argumento: fieldName + operation + value Ejemplo:
 * userId eq "john123"
 */
// @formatter:off
@DslDomain(
	name = "ExampleOperationsWithArg",
	fields = {
		@DslField(javaName = "USER_ID", value = "userId"),
		@DslField(javaName = "USERNAME", value = "username"),
		@DslField(javaName = "AGE", value = "age"),
		@DslField(javaName = "SALARY", value = "salary")
	},
	operationsEnum = ExampleOperationsWithArg.BasicOperations.class
)
// @formatter:on
public class ExampleOperationsWithArg {

	/**
	 * Operaciones básicas con argumento
	 */
	public enum BasicOperations implements OperationDefinition {
		EQUAL_TO("equalTo", "eq", OperationType.WITH_ARG, "Equal to"), NOT_EQUAL("notEqual", "ne",
				OperationType.WITH_ARG, "Not equal to"), GREATER_THAN("greaterThan", "gt", OperationType.WITH_ARG,
						"Greater than"), GREATER_OR_EQUAL("greaterOrEqual", "gte", OperationType.WITH_ARG,
								"Greater or equal"), LESS_THAN("lessThan", "lt", OperationType.WITH_ARG,
										"Less than"), LESS_OR_EQUAL("lessOrEqual", "lte", OperationType.WITH_ARG,
												"Less or equal"), LIKE("like", "like", OperationType.WITH_ARG,
														"Pattern matching"), STARTS_WITH("startsWith", "starts",
																OperationType.WITH_ARG, "Starts with pattern");

		private final String name;
		private final String operator;
		private final OperationType type;
		private final String description;

		BasicOperations(final String name, final String operator, final OperationType type, final String description) {
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
}
