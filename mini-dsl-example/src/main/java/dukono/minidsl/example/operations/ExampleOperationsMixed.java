package dukono.minidsl.example.operations;

import dukono.minidsl.annotation.DslDomain;
import dukono.minidsl.annotation.DslField;
import dukono.minidsl.annotation.OperationDefinition;
import dukono.minidsl.annotation.OperationType;

/**
 * Ejemplo: Operaciones combinadas - todos los tipos
 * 
 * Un DSL puede combinar diferentes tipos de operaciones: - WITH_ARG: con
 * argumento - WITH_LIST: con lista de valores - NO_VALUE: sin valor -
 * NO_OPERATION: sin operador - NO_OP_WITH_ARG: sin operador pero con argumento
 * - NO_OP_WITH_LIST: sin operador pero con lista
 */
// @formatter:off
@DslDomain(
	name = "ExampleOperationsMixed",
	fields = {
		@DslField(javaName = "ID", value = "id"),
		@DslField(javaName = "NAME", value = "name"),
		@DslField(javaName = "STATUS", value = "status"),
		@DslField(javaName = "TAGS", value = "tags"),
		@DslField(javaName = "CREATED_BY", value = "createdBy")
	},
	operationsEnum = ExampleOperationsMixed.MixedOperations.class
)
// @formatter:on
public class ExampleOperationsMixed {

	/**
	 * Operaciones mezcladas de diferentes tipos
	 */
	public enum MixedOperations implements OperationDefinition {
		// WITH_ARG
		EQUAL_TO("equalTo", "eq", OperationType.WITH_ARG, "Equal to"), LIKE("like", "like", OperationType.WITH_ARG,
				"Pattern matching"),

		// WITH_LIST
		IN_VALUES("inValues", "in", OperationType.WITH_LIST, ",", "[]", "Check if value is in list"),

		// NO_VALUE
		IS_NOT_NULL("isNotNull", "is_not_null", OperationType.NO_VALUE, "Check if field is not null"),

		// NO_OP_NO_VALUE
		JUST_ADD("justAdd", null, OperationType.NO_OP_NO_VALUE, "Add field without operation"),

		// NO_OP_WITH_ARG
		DIRECT_VALUE("directValue", null, OperationType.NO_OP_WITH_ARG, "Add field with direct value"),

		// NO_OP_WITH_LIST
		VALUES_LIST("valuesList", null, OperationType.NO_OP_WITH_LIST, "|", "()", "Add field with list of values");

		private final String name;
		private final String operator;
		private final OperationType type;
		private final String listDelimiter;
		private final String listBrackets;
		private final String description;

		MixedOperations(final String name, final String operator, final OperationType type, final String description) {
			this(name, operator, type, " ", "[]", description);
		}

		MixedOperations(final String name, final String operator, final OperationType type, final String listDelimiter,
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
