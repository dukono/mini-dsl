package dukono.minidsl.example.prefixsuffix;

import dukono.minidsl.annotation.DslDomain;
import dukono.minidsl.annotation.OperationDefinition;
import dukono.minidsl.annotation.OperationType;

/**
 * Ejemplo: Eliminar prefijo "order." de valores
 * 
 * Útil cuando los valores vienen con un namespace o path predefinido
 * 
 * Sin transformación: - javaName: CUSTOMER_ID -> CUSTOMER_ID - value:
 * order.customerId -> order.customerId
 * 
 * Con fieldValuePrefix = "order.": - javaName: CUSTOMER_ID -> CUSTOMER_ID -
 * value: order.customerId -> customerId
 */
// @formatter:off
@DslDomain(
	name = "ExampleFieldValuePrefix", 
	fieldsConstants = ExampleFieldValuePrefix.CustomerFieldConstantsWithValuePrefix.class,
	fieldValuePrefix = "order.",
	operationsEnum = ExampleFieldValuePrefix.SimpleOperations.class
)
// @formatter:on
public class ExampleFieldValuePrefix {

	public static class CustomerFieldConstantsWithValuePrefix {
		public static final String CUSTOMER_ID = "order.customerId";
		public static final String CUSTOMER_NAME = "order.customerName";
		public static final String EMAIL = "order.email";
	}

	/**
	 * Enum simple de operaciones
	 */
	public enum SimpleOperations implements OperationDefinition {
		EQUAL_TO("equalTo", "eq", OperationType.WITH_ARG, "");

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
