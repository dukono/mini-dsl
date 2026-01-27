package dukono.minidsl.processor;

import com.squareup.javapoet.ClassName;
import dukono.minidsl.annotation.DslField;
import dukono.minidsl.annotation.DslOperation;
import dukono.minidsl.processor.generator.GeneratedClassNames;

/**
 * Context object holding all information needed to generate DSL classes.
 */
public class DslContext {

	private final String domainName;
	private final String packageName;
	private final DslField[] fields;
	private final DslOperation[] operations;
	private final String dtoClassFullName;
	private final boolean shouldGenerateDto;
	private final ClassName operationsEnumClassName;
	private final String fieldNamePrefix;
	private final String fieldNameSuffix;
	private final String fieldValuePrefix;
	private final String fieldValueSuffix;

	public DslContext(final String domainName, final String packageName, final DslField[] fields,
			final DslOperation[] operations, final String dtoClassFullName, final boolean shouldGenerateDto,
			final ClassName operationsEnumClassName, final String fieldNamePrefix, final String fieldNameSuffix,
			final String fieldValuePrefix, final String fieldValueSuffix) {
		this.domainName = domainName;
		this.packageName = packageName;
		this.fields = fields;
		this.operations = operations;
		this.dtoClassFullName = dtoClassFullName;
		this.shouldGenerateDto = shouldGenerateDto;
		this.operationsEnumClassName = operationsEnumClassName;
		this.fieldNamePrefix = fieldNamePrefix;
		this.fieldNameSuffix = fieldNameSuffix;
		this.fieldValuePrefix = fieldValuePrefix;
		this.fieldValueSuffix = fieldValueSuffix;
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

	public boolean shouldGenerateDto() {
		return this.shouldGenerateDto;
	}

	public ClassName getOperationsEnumClassName() {
		return this.operationsEnumClassName;
	}

	public String getFieldNamePrefix() {
		return this.fieldNamePrefix;
	}

	public String getFieldNameSuffix() {
		return this.fieldNameSuffix;
	}

	public String getFieldValuePrefix() {
		return this.fieldValuePrefix;
	}

	public String getFieldValueSuffix() {
		return this.fieldValueSuffix;
	}

	// Helper methods for generators

	public String getAnchorClassName() {
		return this.domainName + "Anchor";
	}

	public String getFieldsClassName() {
		return this.domainName + GeneratedClassNames.FIELDS.getClassName();
	}

	public String getAnchorOperationsClassName() {
		return this.domainName + GeneratedClassNames.ANCHOR_OPERATIONS.getClassName();
	}

	public String getOperationsClassName() {
		return this.domainName + "Operations";
	}

	public String getApiClassName() {
		return this.domainName + GeneratedClassNames.API.getClassName();
	}

	public String getDtoPackageName() {
		// If DTO is auto-generated (nested class), it doesn't have a separate package
		if (this.shouldGenerateDto) {
			return this.packageName; // Use the API's package
		}
		// For user-provided DTOs, extract package from full name
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

	/**
	 * Returns the appropriate ClassName for the DTO. For auto-generated DTOs
	 * (nested classes), uses bestGuess with simple name. For user-provided DTOs,
	 * uses full package + class name.
	 * 
	 * @return the ClassName for the DTO
	 */
	public ClassName getDtoClassName() {
		if (this.shouldGenerateDto) {
			// Auto-generated DTO is a nested class, use simple name
			return ClassName.bestGuess(this.getDtoSimpleName());
		} else {
			// User-provided DTO, use full package + class name
			return ClassName.get(this.getDtoPackageName(), this.getDtoSimpleName());
		}
	}
}
