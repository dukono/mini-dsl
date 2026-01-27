package dukono.minidsl.annotation;

/**
 * Enum representing the type of operation based on its signature pattern.
 */
public enum OperationType {
	/**
	 * Operation with pattern: getName + operation + arg Example: equalTo(Object
	 * arg)
	 */
	WITH_ARG,

	/**
	 * Operation with pattern: getName + null + arg Example: noOpVals(Object arg)
	 */
	NO_OP_WITH_ARG,

	/**
	 * Operation with pattern: getName + operation + list Example:
	 * inValues(Collection<Object> arg)
	 */
	WITH_LIST,

	/**
	 * /** Operation with pattern: getName + null + list Example:
	 * noOpList(Collection<Object> arg)
	 */
	NO_OP_WITH_LIST,

	/**
	 * Operation with pattern: getName + operation + empty Example: isNotNull()
	 */
	NO_VALUE,

	/**
	 * Operation with pattern: getName + null + empty No operator and no value.
	 * Example: include(), select()
	 */
	NO_OP_NO_VALUE
}
