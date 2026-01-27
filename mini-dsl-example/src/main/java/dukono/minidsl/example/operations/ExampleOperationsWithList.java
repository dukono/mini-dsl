package dukono.minidsl.example.operations;

import dukono.minidsl.annotation.DslDomain;
import dukono.minidsl.annotation.DslField;
import dukono.minidsl.annotation.OperationDefinition;
import dukono.minidsl.annotation.OperationType;

/**
 * Ejemplo: Operaciones de tipo WITH_LIST
 * 
 * Operaciones que reciben una lista de valores: fieldName + operation + [val1,
 * val2, ...] Ejemplo: status in ["ACTIVE", "PENDING"]
 */
// @formatter:off
@DslDomain(
	name = "ExampleOperationsWithList",
	fields = {
		@DslField(javaName = "PRODUCT_ID", value = "productId"),
		@DslField(javaName = "CATEGORY", value = "category"),
		@DslField(javaName = "STATUS", value = "status"),
		@DslField(javaName = "TAG", value = "tag")
	},
	operationsEnum = ExampleOperationsWithList.ListOperations.class
)
// @formatter:on
public class ExampleOperationsWithList {

	/**
	 * Operaciones con lista de valores
	 */
	public enum ListOperations implements OperationDefinition {
		IN_VALUES("inValues", "in", OperationType.WITH_LIST, " ", "[]", "Check if value is in list"), NOT_IN_VALUES(
				"notInValues", "not_in", OperationType.WITH_LIST, " ", "[]", "Check if value is not in list"), BETWEEN(
						"between", "between", OperationType.WITH_LIST, " ", "()", "Check if value is between range");

		private final String name;
		private final String operator;
		private final OperationType type;
		private final String listDelimiter;
		private final String listBrackets;
		private final String description;

		ListOperations(final String name, final String operator, final OperationType type, final String listDelimiter,
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
