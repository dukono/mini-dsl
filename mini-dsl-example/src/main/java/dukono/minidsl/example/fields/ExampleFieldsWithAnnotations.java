package dukono.minidsl.example.fields;

import dukono.minidsl.annotation.DslDomain;
import dukono.minidsl.annotation.DslField;
import dukono.minidsl.annotation.OperationDefinition;
import dukono.minidsl.annotation.OperationType;

/**
 * Ejemplo: Definir fields usando anotaciones @DslField
 * 
 * Esta es la forma más explícita de definir fields, permite especificar tanto
 * el nombre Java como el valor para cada field individualmente.
 */
// @formatter:off
@DslDomain(
	name = "ExampleFieldsWithAnnotations",
	fields = {
		@DslField(javaName = "ORDER_ID", value = "orderId"),
		@DslField(javaName = "CUSTOMER_NAME", value = "customerName"),
		@DslField(javaName = "TOTAL_AMOUNT", value = "totalAmount"),
		@DslField(javaName = "ORDER_DATE", value = "orderDate"),
		@DslField(javaName = "STATUS", value = "status")
	},
	operationsEnum = ExampleFieldsWithAnnotations.SimpleOperations.class
)
// @formatter:on
public class ExampleFieldsWithAnnotations {

	/**
	 * Enum simple de operaciones
	 */
	public enum SimpleOperations implements OperationDefinition {
		EQUAL_TO("equalTo", "eq", OperationType.WITH_ARG, "Equality comparison"), NOT_NULL("isNotNull", "is_not_null",
				OperationType.NO_VALUE, "Check if field is not null");

		private final String name;
		private final String operator;
		private final OperationType type;
		private final String description;

		SimpleOperations(final String name, final String operator, final OperationType type, final String description) {
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
