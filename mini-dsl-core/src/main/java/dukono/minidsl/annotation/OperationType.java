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
	 * Operation with pattern: getName + operation + list Example:
	 * inValues(Collection<Object> arg)
	 */
	WITH_LIST,

	/**
	 * Operation with pattern: getName + operation + empty Example: isNotNull()
	 */
	WITH_EMPTY,

	/**
	 * Operation with pattern: getName + null + empty Example: justAdd()
	 */
	JUST_ADD
}
