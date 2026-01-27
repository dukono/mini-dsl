package dukono.minidsl.example.fields;

import dukono.minidsl.annotation.DslDomain;
import dukono.minidsl.annotation.OperationDefinition;
import dukono.minidsl.annotation.OperationType;

/**
 * Ejemplo: Definir fields usando una clase con constantes
 * 
 * El nombre de la constante se usa como javaName y su valor como value. Es útil
 * cuando ya tienes una clase de constantes existente.
 */
// @formatter:off
@DslDomain(
	name = "ExampleFieldsWithConstants",
	fieldsConstants = ExampleFieldsWithConstants.CustomerFields.class,
	operationsEnum = ExampleFieldsWithConstants.SimpleOperations.class
)
// @formatter:on
public class ExampleFieldsWithConstants {

	/**
	 * Clase con constantes que definen los campos
	 */
	public static class CustomerFields {
		public static final String CUSTOMER_ID = "customerId";
		public static final String FIRST_NAME = "firstName";
		public static final String LAST_NAME = "lastName";
		public static final String EMAIL = "email";
		public static final String PHONE = "phone";
		public static final String ADDRESS = "address";
		public static final String REGISTRATION_DATE = "registrationDate";
	}

	/**
	 * Enum de operaciones
	 */
	public enum SimpleOperations implements OperationDefinition {
		EQUAL_TO("equalTo", "eq", OperationType.WITH_ARG, "Equality comparison"), LIKE("like", "like",
				OperationType.WITH_ARG, "Pattern matching"), NOT_NULL("isNotNull", "is_not_null",
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
