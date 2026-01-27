package dukono.minidsl.example.fields;

import dukono.minidsl.annotation.DslDomain;
import dukono.minidsl.annotation.OperationDefinition;
import dukono.minidsl.annotation.OperationType;

/**
 * Ejemplo: Definir fields usando un Enum
 * 
 * El nombre del enum se usa como javaName y el valor del enum como value. Es
 * una forma limpia y type-safe de definir fields.
 */
// @formatter:off
@DslDomain(
	name = "ExampleFieldsWithEnum",
	fieldsEnum = ExampleFieldsWithEnum.ProductFields.class,
	operationsEnum = ExampleFieldsWithEnum.SimpleOperations.class
)
// @formatter:on
public class ExampleFieldsWithEnum {

	/**
	 * Enum que define los campos disponibles
	 */
	public enum ProductFields {
		PRODUCT_ID("productId"), PRODUCT_NAME("productName"), CATEGORY("category"), PRICE("price"), STOCK_QUANTITY(
				"stockQuantity"), BRAND("brand");

		private final String value;

		ProductFields(final String value) {
			this.value = value;
		}

		public String getValue() {
			return this.value;
		}
	}

	/**
	 * Enum de operaciones
	 */
	public enum SimpleOperations implements OperationDefinition {
		EQUAL_TO("equalTo", "eq", OperationType.WITH_ARG, "Equality comparison"), IN_VALUES("inValues", "in",
				OperationType.WITH_LIST, "Check if value is in list"), NOT_NULL("isNotNull", "is_not_null",
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
