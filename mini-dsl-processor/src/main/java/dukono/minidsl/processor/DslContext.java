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
	private final boolean withLogical;
	private final boolean withActions;
	private final boolean withList;
	private final boolean generateApi;

	public DslContext(final String domainName, final String packageName, final DslField[] fields, final DslOperation[] operations,
			final String dtoClass, final boolean withLogical, final boolean withActions, final boolean withList, final boolean generateApi) {
		this.domainName = domainName;
		this.packageName = packageName;
		this.fields = fields;
		this.operations = operations;
		this.dtoClass = dtoClass;
		this.withLogical = withLogical;
		this.withActions = withActions;
		this.withList = withList;
		this.generateApi = generateApi;
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

	public boolean isWithLogical() {
		return this.withLogical;
	}

	public boolean isWithActions() {
		return this.withActions;
	}

	public boolean isWithList() {
		return this.withList;
	}

	public boolean isGenerateApi() {
		return this.generateApi;
	}

	// Helper methods

	public String getFieldsClassName() {
		return this.domainName + "Fields";
	}

	public String getOperationsClassName() {
		return this.domainName + "Operations";
	}

	public String getAnchorOperationsClassName() {
		return this.domainName + "AnchorOperations";
	}

	public String getAnchorClassName() {
		return this.domainName + "Anchor";
	}

	public String getApiClassName() {
		return this.domainName + "Api";
	}

	public String getDtoSimpleName() {
		final int lastDot = this.dtoClass.lastIndexOf('.');
		return lastDot > 0 ? this.dtoClass.substring(lastDot + 1) : this.dtoClass;
	}
}
