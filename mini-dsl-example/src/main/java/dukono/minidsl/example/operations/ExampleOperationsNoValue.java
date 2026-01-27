package dukono.minidsl.example.operations;

import dukono.minidsl.annotation.DslDomain;
import dukono.minidsl.annotation.DslField;
import dukono.minidsl.annotation.OperationDefinition;
import dukono.minidsl.annotation.OperationType;

/**
 * Ejemplo: Operaciones de tipo NO_VALUE
 * 
 * Operaciones sin valor: fieldName + operation Ejemplo: email is_not_null
 */
// @formatter:off
@DslDomain(
	name = "ExampleOperationsNoValue",
	fields = {
		@DslField(javaName = "EMAIL", value = "email"),
		@DslField(javaName = "PHONE", value = "phone"),
		@DslField(javaName = "ADDRESS", value = "address"),
		@DslField(javaName = "DESCRIPTION", value = "description")
	},
	operationsEnum = ExampleOperationsNoValue.NoValueOperations.class
)
// @formatter:on
public class ExampleOperationsNoValue {

	/**
	 * Operaciones sin valor
	 */
	public enum NoValueOperations implements OperationDefinition {
		IS_NOT_NULL("isNotNull", "is_not_null", OperationType.NO_VALUE, "Check if field is not null"), IS_NULL("isNull",
				"is_null", OperationType.NO_VALUE, "Check if field is null"), IS_EMPTY("isEmpty", "is_empty",
						OperationType.NO_VALUE, "Check if field is empty"), IS_NOT_EMPTY("isNotEmpty", "is_not_empty",
								OperationType.NO_VALUE, "Check if field is not empty");

		private final String name;
		private final String operator;
		private final OperationType type;
		private final String description;

		NoValueOperations(final String name, final String operator, final OperationType type,
				final String description) {
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
