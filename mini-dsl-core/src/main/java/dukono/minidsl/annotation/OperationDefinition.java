package dukono.minidsl.annotation;

/**
 * Interface that operation enums must implement to provide operation metadata.
 * 
 * This allows users to define operations in a type-safe enum instead of using
 * {@code @DslOperation[]} array.
 * 
 * Example:
 * 
 * <pre>
 * public enum OrderOperations implements OperationDefinition {
 * 	EQUAL_TO("equalTo", "eq", OperationType.WITH_ARG), NOT_EQUAL_TO("notEqualTo", "ne",
 * 			OperationType.WITH_ARG), IN_VALUES("inValues", "in", OperationType.WITH_LIST);
 * 
 * 	private final String name;
 * 	private final String operator;
 * 	private final OperationType type;
 * 
 * 	OrderOperations(String name, String operator, OperationType type) {
 * 		this.name = name;
 * 		this.operator = operator;
 * 		this.type = type;
 * 	}
 * 
 * 	public String getName() {
 * 		return name;
 * 	}
 * 
 * 	public String getOperator() {
 * 		return operator;
 * 	}
 * 
 * 	public OperationType getType() {
 * 		return type;
 * 	}
 * 
 * 	public String getDescription() {
 * 		return "";
 * 	}
 * }
 * </pre>
 */
public interface OperationDefinition {

	/**
	 * The method name for this operation.
	 * 
	 * @return the method name (e.g., "equalTo")
	 */
	String getName();

	/**
	 * The operator string used in queries.
	 * 
	 * @return the operator (e.g., "eq")
	 */
	String getOperator();

	/**
	 * The type of operation (WITH_ARG, WITH_LIST, NO_VALUE, NO_OP_NO_VALUE).
	 * 
	 * @return the operation type
	 */
	OperationType getType();

	/**
	 * Optional description for the operation.
	 * 
	 * @return the description, or empty string if none
	 */
	default String getDescription() {
		return "";
	}

	/**
	 * Delimiter to use when formatting list values. Only applicable for WITH_LIST
	 * and NO_OP_WITH_LIST types.
	 * 
	 * @return the delimiter (default: " " - space)
	 */
	default String getListDelimiter() {
		return " ";
	}

	/**
	 * Brackets/parentheses to wrap list values. Only applicable for WITH_LIST and
	 * NO_OP_WITH_LIST types.
	 * 
	 * @return the brackets (default: "" - no wrapping)
	 */
	default String getListBrackets() {
		return "";
	}
}
