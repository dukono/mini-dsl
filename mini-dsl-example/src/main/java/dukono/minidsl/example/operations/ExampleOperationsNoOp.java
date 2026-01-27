package dukono.minidsl.example.operations;

import dukono.minidsl.annotation.DslDomain;
import dukono.minidsl.annotation.DslField;
import dukono.minidsl.annotation.OperationDefinition;
import dukono.minidsl.annotation.OperationType;

/**
 * Ejemplo: Operaciones de tipo NO_OPERATION
 * 
 * Añade el campo sin operador: fieldName Ejemplo: userId (sin operador ni
 * valor)
 */
// @formatter:off
@DslDomain(
	name = "ExampleOperationsNoOp",
	fields = {
		@DslField(javaName = "USER_ID", value = "userId"),
		@DslField(javaName = "SESSION_ID", value = "sessionId"),
		@DslField(javaName = "REQUEST_ID", value = "requestId")
	},
	operationsEnum = ExampleOperationsNoOp.NoOpOperations.class
)
// @formatter:on
public class ExampleOperationsNoOp {

	/**
	 * Operaciones sin operador
	 */
	public enum NoOpOperations implements OperationDefinition {
		JUST_ADD("justAdd", null, OperationType.NO_OP_NO_VALUE, "Add field without operation");

		private final String name;
		private final String operator;
		private final OperationType type;
		private final String description;

		NoOpOperations(final String name, final String operator, final OperationType type, final String description) {
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
