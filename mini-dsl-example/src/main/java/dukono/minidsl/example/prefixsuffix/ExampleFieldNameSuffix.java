package dukono.minidsl.example.prefixsuffix;

import dukono.minidsl.annotation.DslDomain;
import dukono.minidsl.annotation.OperationDefinition;
import dukono.minidsl.annotation.OperationType;

/**
 * Ejemplo: Eliminar sufijo "_FIELD" de nombres de constantes
 * 
 * Sin transformación: - javaName: PRODUCT_ID_FIELD -> PRODUCT_ID_FIELD - value:
 * productId -> productId
 * 
 * Con fieldNameSuffix = "_FIELD": - javaName: PRODUCT_ID_FIELD -> PRODUCT_ID -
 * value: productId -> productId
 */
// @formatter:off
@DslDomain(
	name = "ExampleFieldNameSuffix", 
	fieldsConstants = ExampleFieldNameSuffix.ProductFieldConstantsWithSuffix.class,
	fieldNameSuffix = "_FIELD",
	operationsEnum = ExampleFieldNameSuffix.SimpleOperations.class
)
// @formatter:on
public class ExampleFieldNameSuffix {

	public static class ProductFieldConstantsWithSuffix {
		public static final String PRODUCT_ID_FIELD = "productId";
		public static final String PRODUCT_NAME_FIELD = "productName";
		public static final String PRICE_FIELD = "price";
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
