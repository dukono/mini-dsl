package dukono.minidsl.processor.generator;

/**
 * Enum containing all the names of the generated DSL classes. This centralizes
 * the naming convention and prevents typos and repetition.
 */
public enum GeneratedClassNames {

	/** The main API facade class suffix */
	API("Api"),

	/** The Fields class that extends Field and contains all field definitions */
	FIELDS("Fields"),

	/** The DTO class that extends Dto */
	DTO("Dto"),

	/** The operations class for the main anchor */
	ANCHOR_OPERATIONS("AnchorOperations"),

	/** The logical operations class extending AnchorOperations */
	ANCHOR_OPERATIONS_LOGICAL("AnchorOperationsLogical"),

	/** The operations class for single items in lists */
	ANCHOR_OPERATIONS_ONE("AnchorOperationsOne"),

	/** The anchor for single list items */
	ANCHOR_ONE("AnchorOne"),

	/** The anchor for list operations */
	ANCHOR_LIST("AnchorList"),

	/** The main logical anchor */
	ANCHOR_LOGICAL_MAIN("AnchorLogicalMain"),

	/** The main anchor class */
	ANCHOR_MAIN("AnchorMain"),

	/** The actions anchor with replace, modify, remove operations */
	ANCHOR_ACTIONS("AnchorActions");

	private final String className;

	GeneratedClassNames(final String className) {
		this.className = className;
	}

	/**
	 * Returns the class name.
	 * 
	 * @return the class name as string
	 */
	public String getClassName() {
		return this.className;
	}

	/**
	 * Returns the class name (alias for getClassName for convenience).
	 * 
	 * @return the class name as string
	 */
	@Override
	public String toString() {
		return this.className;
	}
}
