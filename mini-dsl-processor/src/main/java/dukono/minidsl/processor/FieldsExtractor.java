package dukono.minidsl.processor;

import dukono.minidsl.annotation.DslDomain;
import dukono.minidsl.annotation.DslField;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic;

/**
 * Extracts field definitions from @DslDomain annotation using one of three
 * methods: 1. fieldsConstants - from a class with static final String constants
 * 2. fieldsEnum - from an enum with String values 3. fields[] - from an array
 * of
 * 
 * @DslField annotations
 * 
 *           Validates that only ONE method is used and provides clear error
 *           messages.
 */
final class FieldsExtractor {

	private final ProcessingEnvironment processingEnv;
	private final Messager messager;

	FieldsExtractor(final ProcessingEnvironment processingEnv) {
		this.processingEnv = processingEnv;
		this.messager = processingEnv.getMessager();
	}

	/**
	 * Extracts fields from @DslDomain annotation. Validates that exactly ONE field
	 * definition method is used, then extracts fields using the appropriate
	 * extractor.
	 * 
	 * @param annotation
	 *            the @DslDomain annotation
	 * @param element
	 *            the annotated element (for error reporting)
	 * @return array of DslField instances
	 */
	DslField[] extractFields(final DslDomain annotation, final Element element) {
		// First, validate that only ONE method is defined
		this.validateSingleFieldDefinition(annotation, element);

		// Then extract using the defined method
		return this.extractFieldsFromDefinedMethod(annotation);
	}

	/**
	 * Validates that only ONE field definition method is used (fieldsConstants,
	 * fieldsEnum, or fields array). Having multiple definitions is an error.
	 */
	private void validateSingleFieldDefinition(final DslDomain annotation, final Element element) {
		int definedCount = 0;
		final StringBuilder definedMethods = new StringBuilder();

		// Check fieldsConstants
		if (this.isFieldsConstantsDefined(annotation)) {
			definedCount++;
			definedMethods.append("fieldsConstants ");
		}

		// Check fieldsEnum
		if (this.isFieldsEnumDefined(annotation)) {
			definedCount++;
			definedMethods.append("fieldsEnum ");
		}

		// Check fields array
		final boolean hasFieldsArray = annotation.fields() != null && annotation.fields().length > 0;
		if (hasFieldsArray) {
			definedCount++;
			definedMethods.append("fields[] ");
		}

		// Validate exactly one is defined
		if (definedCount == 0) {
			this.messager.printMessage(Diagnostic.Kind.ERROR,
					"@DslDomain must specify exactly ONE of: fields[], fieldsEnum, or fieldsConstants", element);
		} else if (definedCount > 1) {
			this.messager.printMessage(Diagnostic.Kind.ERROR,
					"@DslDomain cannot use multiple field definitions. Found: " + definedMethods.toString().trim()
							+ ". Use only ONE of: fields[], fieldsEnum, or fieldsConstants",
					element);
		}
	}

	/**
	 * Extracts fields from the defined method with priority: 1. fieldsConstants 2.
	 * fieldsEnum 3. fields[]
	 */
	private DslField[] extractFieldsFromDefinedMethod(final DslDomain annotation) {
		// Priority 1: Check if fieldsConstants is specified
		final TypeMirror constantsTypeMirror = this.getFieldsConstantsTypeMirror(annotation);
		if (constantsTypeMirror != null) {
			final String typeName = constantsTypeMirror.toString();
			if (!typeName.equals("void") && !typeName.equals("java.lang.Void")) {
				this.messager.printMessage(Diagnostic.Kind.NOTE, "Extracting fields from constants class: " + typeName);
				final ConstantFieldsExtractor extractor = new ConstantFieldsExtractor(this.processingEnv);
				return extractor.extractFieldsFromConstants(constantsTypeMirror);
			}
		}

		// Priority 2: Check if fieldsEnum is specified
		final TypeMirror enumTypeMirror = this.getFieldsEnumTypeMirror(annotation);
		if (enumTypeMirror != null) {
			final String typeName = enumTypeMirror.toString();
			if (!typeName.equals("void") && !typeName.equals("java.lang.Void")) {
				this.messager.printMessage(Diagnostic.Kind.NOTE, "Extracting fields from enum: " + typeName);
				final EnumFieldsExtractor extractor = new EnumFieldsExtractor(this.processingEnv);
				return extractor.extractFieldsFromEnum(enumTypeMirror);
			}
		}

		// Priority 3: Use fields array
		final DslField[] fields = annotation.fields();
		if (fields != null && fields.length > 0) {
			this.messager.printMessage(Diagnostic.Kind.NOTE,
					"Extracting fields from @DslField array (" + fields.length + " fields)");
		}
		return fields;
	}

	/**
	 * Checks if fieldsConstants is defined (not void).
	 */
	private boolean isFieldsConstantsDefined(final DslDomain annotation) {
		try {
			annotation.fieldsConstants();
			return false; // void.class
		} catch (final MirroredTypeException mte) {
			final String typeName = mte.getTypeMirror().toString();
			return !typeName.equals("void") && !typeName.equals("java.lang.Void");
		}
	}

	/**
	 * Checks if fieldsEnum is defined (not void).
	 */
	private boolean isFieldsEnumDefined(final DslDomain annotation) {
		try {
			annotation.fieldsEnum();
			return false; // void.class
		} catch (final MirroredTypeException mte) {
			final String typeName = mte.getTypeMirror().toString();
			return !typeName.equals("void") && !typeName.equals("java.lang.Void");
		}
	}

	/**
	 * Gets the TypeMirror for fieldsConstants if specified.
	 */
	private TypeMirror getFieldsConstantsTypeMirror(final DslDomain annotation) {
		try {
			annotation.fieldsConstants();
			return null; // void.class
		} catch (final MirroredTypeException mte) {
			return mte.getTypeMirror();
		}
	}

	/**
	 * Gets the TypeMirror for fieldsEnum if specified.
	 */
	private TypeMirror getFieldsEnumTypeMirror(final DslDomain annotation) {
		try {
			annotation.fieldsEnum();
			return null; // void.class
		} catch (final MirroredTypeException mte) {
			return mte.getTypeMirror();
		}
	}
}
