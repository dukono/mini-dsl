package dukono.minidsl.example.prefixsuffix;

import dukono.minidsl.annotation.DslDomain;
import dukono.minidsl.annotation.OperationDefinition;
import dukono.minidsl.annotation.OperationType;

/**
 * Ejemplo: Eliminar prefijo "FIELD_" de nombres de constantes
 * 
 * Sin transformación: - javaName: FIELD_ORDER_ID -> FIELD_ORDER_ID - value:
 * orderId -> orderId
 * 
 * Con fieldNamePrefix = "FIELD_": - javaName: FIELD_ORDER_ID -> ORDER_ID -
 * value: orderId -> orderId
 */
// @formatter:off
@DslDomain(
	name = "ExampleFieldNamePrefix", 
	fieldsConstants = ExampleFieldNamePrefix.OrderFieldConstantsWithPrefix.class,
	fieldNamePrefix = "FIELD_",
	operationsEnum = ExampleFieldNamePrefix.SimpleOperations.class
)
// @formatter:on
public class ExampleFieldNamePrefix {

	public static class OrderFieldConstantsWithPrefix {
		public static final String FIELD_ORDER_ID = "orderId";
		public static final String FIELD_CUSTOMER_NAME = "customerName";
		public static final String FIELD_TOTAL_AMOUNT = "totalAmount";
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
