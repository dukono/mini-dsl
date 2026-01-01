package dukono.minidsl.processor;

import dukono.minidsl.annotation.DslField;
import dukono.minidsl.annotation.DslOperation;

/**
 * Context object holding all information needed to generate DSL classes.
 */
public class DslContext {

	private final String domainName;
	private final String packageName;
	private final DslField[] fields;
	private final DslOperation[] operations;
	private final String dtoClass;

	public DslContext(final String domainName, final String packageName, final DslField[] fields,
			final DslOperation[] operations, final String dtoClass) {
		this.domainName = domainName;
		this.packageName = packageName;
		this.fields = fields;
		this.operations = operations;
		this.dtoClass = dtoClass;
	}

	// Getters

	public String getDomainName() {
		return this.domainName;
	}

	public String getPackageName() {
		return this.packageName;
	}

	public DslField[] getFields() {
		return this.fields;
	}

	public DslOperation[] getOperations() {
		return this.operations;
	}

	public String getDtoClass() {
		return this.dtoClass;
	}
}
