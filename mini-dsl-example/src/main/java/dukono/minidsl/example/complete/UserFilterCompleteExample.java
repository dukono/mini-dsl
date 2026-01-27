package dukono.minidsl.example.complete;

import dukono.minidsl.annotation.DslDomain;
import dukono.minidsl.annotation.DslField;
import dukono.minidsl.annotation.OperationDefinition;
import dukono.minidsl.annotation.OperationType;

/**
 * Ejemplo completo: Sistema de filtrado de usuarios
 * 
 * Este ejemplo combina: - Fields definidos con anotaciones @DslField -
 * Operaciones completas (WITH_ARG, WITH_LIST, NO_VALUE) - Delimitadores
 * personalizados para listas
 */
// @formatter:off
@DslDomain(
	name = "UserFilterSystem",
	fields = {
		@DslField(javaName = "USER_ID", value = "userId"),
		@DslField(javaName = "USERNAME", value = "username"),
		@DslField(javaName = "EMAIL", value = "email"),
		@DslField(javaName = "FIRST_NAME", value = "firstName"),
		@DslField(javaName = "LAST_NAME", value = "lastName"),
		@DslField(javaName = "AGE", value = "age"),
		@DslField(javaName = "COUNTRY", value = "country"),
		@DslField(javaName = "ROLES", value = "roles"),
		@DslField(javaName = "STATUS", value = "status"),
		@DslField(javaName = "LAST_LOGIN", value = "lastLogin")
	},
	operationsEnum = UserFilterCompleteExample.UserOperations.class
)
// @formatter:on
public class UserFilterCompleteExample {

	/**
	 * Operaciones completas para filtrado de usuarios
	 */
	public enum UserOperations implements OperationDefinition {
		// Comparaciones exactas
		EQUALS("equalsTo", "eq", OperationType.WITH_ARG, "Equals"), NOT_EQUALS("notEquals", "ne", OperationType.WITH_ARG,
				"Not equals"),

		// Comparaciones numéricas
		GREATER_THAN("greaterThan", "gt", OperationType.WITH_ARG, "Greater than"), LESS_THAN("lessThan", "lt",
				OperationType.WITH_ARG, "Less than"), GREATER_OR_EQUAL("greaterOrEqual", "gte", OperationType.WITH_ARG,
						"Greater or equal"), LESS_OR_EQUAL("lessOrEqual", "lte", OperationType.WITH_ARG,
								"Less or equal"),

		// Búsqueda de texto
		LIKE("like", "like", OperationType.WITH_ARG, "Pattern matching"), CONTAINS("contains", "contains",
				OperationType.WITH_ARG, "Contains substring"), STARTS_WITH("startsWith", "starts",
						OperationType.WITH_ARG,
						"Starts with"), ENDS_WITH("endsWith", "ends", OperationType.WITH_ARG, "Ends with"),

		// Listas con diferentes delimitadores
		IN_LIST("inList", "in", OperationType.WITH_LIST, ",", "[]", "In list (comma separated)"), IN_ROLES("inRoles",
				"has_role", OperationType.WITH_LIST, "|", "{}",
				"Has any role (pipe separated)"), IN_COUNTRIES("inCountries", "in_country", OperationType.WITH_LIST,
						";", "()", "In countries (semicolon separated)"),

		// Nullability
		IS_NOT_NULL("isNotNull", "is_not_null", OperationType.NO_VALUE, "Is not null"), IS_NULL("isNull", "is_null",
				OperationType.NO_VALUE, "Is null"), IS_EMPTY("isEmpty", "is_empty", OperationType.NO_VALUE, "Is empty"),

		// Sin operador
		SELECT_FIELD("selectField", null, OperationType.NO_OP_NO_VALUE, "Select field");

		private final String name;
		private final String operator;
		private final OperationType type;
		private final String listDelimiter;
		private final String listBrackets;
		private final String description;

		UserOperations(final String name, final String operator, final OperationType type, final String description) {
			this(name, operator, type, " ", "[]", description);
		}

		UserOperations(final String name, final String operator, final OperationType type, final String listDelimiter,
				final String listBrackets, final String description) {
			this.name = name;
			this.operator = operator;
			this.type = type;
			this.listDelimiter = listDelimiter;
			this.listBrackets = listBrackets;
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
		public String getListDelimiter() {
			return this.listDelimiter;
		}

		@Override
		public String getListBrackets() {
			return this.listBrackets;
		}

		@Override
		public String getDescription() {
			return this.description;
		}
	}
}
