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
	private final String dtoClassFullName;

	public DslContext(final String domainName, final String packageName, final DslField[] fields,
			final DslOperation[] operations, final String dtoClassFullName) {
		this.domainName = domainName;
		this.packageName = packageName;
		this.fields = fields;
		this.operations = operations;
		this.dtoClassFullName = dtoClassFullName;
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
		return this.dtoClassFullName;
	}

	// Helper methods for generators

	public String getAnchorClassName() {
		return this.domainName + "Anchor";
	}

	public String getFieldsClassName() {
		return this.domainName + "Fields";
	}

	public String getAnchorOperationsClassName() {
		return this.domainName + "AnchorOperations";
	}

	public String getOperationsClassName() {
		return this.domainName + "Operations";
	}

	public String getApiClassName() {
		return this.domainName + "Api";
	}

	public String getDtoPackageName() {
		final int lastDot = this.dtoClassFullName.lastIndexOf('.');
		if (lastDot > 0) {
			return this.dtoClassFullName.substring(0, lastDot);
		}
		return "";
	}

	public String getDtoSimpleName() {
		final int lastDot = this.dtoClassFullName.lastIndexOf('.');
		if (lastDot > 0) {
			return this.dtoClassFullName.substring(lastDot + 1);
		}
		return this.dtoClassFullName;
	}
}
