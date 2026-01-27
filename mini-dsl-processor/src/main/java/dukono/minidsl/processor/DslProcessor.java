package dukono.minidsl.processor;

import com.squareup.javapoet.ClassName;
import com.sun.source.util.Trees;
import dukono.minidsl.annotation.DslDomain;
import dukono.minidsl.annotation.DslField;
import dukono.minidsl.annotation.DslOperation;
import dukono.minidsl.processor.generator.ApiGenerator;
import dukono.minidsl.processor.generator.GeneratedClassNames;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.Diagnostic.Kind;
import java.io.IOException;
import java.util.Set;

/**
 * Annotation processor for @DslDomain annotation.
 * 
 * Generates all necessary classes for a DSL domain: - Fields class -
 * AnchorOperationsBase class - AnchorMain class - Api class
 * 
 * Note: This processor is registered manually via META-INF/services because
 * annotation processing is disabled during compilation (-proc:none).
 */
@SupportedAnnotationTypes("dukono.minidsl.annotation.DslDomain")
@SupportedSourceVersion(SourceVersion.RELEASE_21)
public class DslProcessor extends AbstractProcessor {

	private Filer filer;
	private Messager messager;
	private boolean isEnvironmentUsable = true;

	@Override
	public synchronized void init(final ProcessingEnvironment processingEnv) {
		super.init(processingEnv);
		try {
			final Trees trees = Trees.instance(processingEnv);
			if (trees == null) {
				this.isEnvironmentUsable = false;
			}
		} catch (final Exception e) { // cambiado de Throwable a Exception
			this.isEnvironmentUsable = false;
			processingEnv.getMessager().printMessage(Kind.WARNING,
					"GeretRequestLogicalApiProcessor: ProcessingEnvironment not compatible with your IDE. "
							+ "To complete compile run: 'mvn compile' to generate classes, then rebuild this project. Skipping.");
		}
		this.filer = processingEnv.getFiler();
		this.messager = processingEnv.getMessager();
	}

	@Override
	public boolean process(final Set<? extends TypeElement> annotations, final RoundEnvironment roundEnv) {
		this.messager.printMessage(Diagnostic.Kind.NOTE, "Running.. ");
		for (final Element element : roundEnv.getElementsAnnotatedWith(DslDomain.class)) {
			if (element.getKind() != ElementKind.CLASS || !this.isEnvironmentUsable || roundEnv.errorRaised()) {
				continue;
			}
			try {
				this.processDslDomain(element);
			} catch (final IOException e) {
				this.messager.printMessage(Diagnostic.Kind.ERROR, "Error generating DSL classes: " + e.getMessage(),
						element);
			}
		}
		return true;
	}

	private void processDslDomain(final Element element) throws IOException {
		final DslDomain annotation = element.getAnnotation(DslDomain.class);

		// Extract information
		final String domainName = annotation.name();
		String packageName = annotation.packageName();

		// If packageName is not specified, infer it from fieldsConstants, fieldsEnum,
		// or the annotated class
		if (packageName.isEmpty()) {
			packageName = this.inferPackageName(annotation, element);
		}

		this.messager.printMessage(Diagnostic.Kind.NOTE,
				"Generating DSL classes for domain: " + domainName + " in package: " + packageName);

		// Extract fields using FieldsExtractor (validates and extracts)
		final DslField[] fields = new FieldsExtractor(this.processingEnv).extractFields(annotation, element);

		// Extract operations using OperationsExtractor (validates and extracts)
		final DslOperation[] operations = new OperationsExtractor(this.processingEnv).extractOperations(annotation,
				element);

		// Extract or generate DTO class name
		final DtoInfo dtoInfo = this.extractDtoClassName(annotation, domainName);

		// Extract operations enum ClassName
		final ClassName operationsEnumClassName = this.extractOperationsEnumClassName(annotation);

		// Extract field name/value transformations
		final String fieldNamePrefix = annotation.fieldNamePrefix();
		final String fieldNameSuffix = annotation.fieldNameSuffix();
		final String fieldValuePrefix = annotation.fieldValuePrefix();
		final String fieldValueSuffix = annotation.fieldValueSuffix();

		// Create context
		final DslContext context = new DslContext(domainName, packageName, fields, operations, dtoInfo.className,
				dtoInfo.shouldGenerate, operationsEnumClassName, fieldNamePrefix, fieldNameSuffix, fieldValuePrefix,
				fieldValueSuffix);

		// Generate only the Api class which contains all nested classes
		new ApiGenerator().generate(context, this.filer);

		this.messager.printMessage(Diagnostic.Kind.NOTE, "Successfully generated DSL classes for: " + domainName);
	}

	/**
	 * Extracts the DTO class name from annotation or generates it automatically.
	 * 
	 * @param annotation
	 *            the @DslDomain annotation
	 * @param domainName
	 *            the domain name
	 * @return DtoInfo with class name and whether it should be generated
	 */
	private DtoInfo extractDtoClassName(final DslDomain annotation, final String domainName) {
		try {
			annotation.dtoClass();
			// If we reach here, it's void.class - generate automatically
			// Auto-generated DTOs are nested classes, so we only store the simple name
			final String generatedDtoName = domainName + GeneratedClassNames.DTO.getClassName();
			this.messager.printMessage(Diagnostic.Kind.NOTE, "Auto-generating nested DTO class: " + generatedDtoName);
			return new DtoInfo(generatedDtoName, true);
		} catch (final javax.lang.model.type.MirroredTypeException mte) {
			final javax.lang.model.type.TypeMirror typeMirror = mte.getTypeMirror();
			final String typeName = typeMirror.toString();
			if (!typeName.equals("void") && !typeName.equals("java.lang.Void")) {
				// User specified a class
				this.messager.printMessage(Diagnostic.Kind.NOTE, "Using user-defined DTO class: " + typeName);
				return new DtoInfo(typeName, false);
			} else {
				// void.class - generate automatically
				// Auto-generated DTOs are nested classes, so we only store the simple name
				final String generatedDtoName = domainName + GeneratedClassNames.DTO.getClassName();
				this.messager.printMessage(Diagnostic.Kind.NOTE,
						"Auto-generating nested DTO class: " + generatedDtoName);
				return new DtoInfo(generatedDtoName, true);
			}
		}
	}

	/**
	 * Simple data class to hold DTO information.
	 */
	private static class DtoInfo {
		final String className;
		final boolean shouldGenerate;

		DtoInfo(final String className, final boolean shouldGenerate) {
			this.className = className;
			this.shouldGenerate = shouldGenerate;
		}
	}

	/**
	 * Extracts the operations enum ClassName from the annotation.
	 * 
	 * @param annotation
	 *            the @DslDomain annotation
	 * @return ClassName of the operations enum
	 */
	private com.squareup.javapoet.ClassName extractOperationsEnumClassName(final DslDomain annotation) {
		try {
			annotation.operationsEnum();
			return null; // void.class - shouldn't happen if validation passed
		} catch (final javax.lang.model.type.MirroredTypeException mte) {
			final javax.lang.model.type.TypeMirror typeMirror = mte.getTypeMirror();
			final String typeName = typeMirror.toString();
			if (!typeName.equals("void") && !typeName.equals("java.lang.Void")) {
				// Parse the fully qualified name to handle nested classes
				// Example: "com.example.Outer.Inner" or "com.example.MyClass"
				return this.parseClassName(typeName);
			}
			return null;
		}
	}

	/**
	 * Parses a fully qualified class name into a ClassName, handling nested classes
	 * correctly.
	 * 
	 * @param fullyQualifiedName
	 *            the fully qualified class name (e.g., "com.example.Outer.Inner")
	 * @return ClassName that correctly represents the class
	 */
	private com.squareup.javapoet.ClassName parseClassName(final String fullyQualifiedName) {
		// Split by dots
		final String[] parts = fullyQualifiedName.split("\\.");

		// Find where the package ends and the class names begin
		// Convention: package names are lowercase, class names start with uppercase
		int classNameStartIndex = 0;
		for (int i = 0; i < parts.length; i++) {
			if (Character.isUpperCase(parts[i].charAt(0))) {
				classNameStartIndex = i;
				break;
			}
		}

		// Extract package name (everything before the first class)
		final StringBuilder packageBuilder = new StringBuilder();
		for (int i = 0; i < classNameStartIndex; i++) {
			if (i > 0) {
				packageBuilder.append(".");
			}
			packageBuilder.append(parts[i]);
		}
		final String packageName = packageBuilder.toString();

		// Extract class names (outer class and nested classes)
		final String[] classNames = new String[parts.length - classNameStartIndex];
		System.arraycopy(parts, classNameStartIndex, classNames, 0, classNames.length);

		// Create ClassName using appropriate JavaPoet method
		if (classNames.length == 1) {
			// Simple class (not nested)
			return com.squareup.javapoet.ClassName.get(packageName, classNames[0]);
		} else {
			// Nested class
			final String outerClass = classNames[0];
			final String[] nestedClasses = new String[classNames.length - 1];
			System.arraycopy(classNames, 1, nestedClasses, 0, nestedClasses.length);
			return com.squareup.javapoet.ClassName.get(packageName, outerClass, nestedClasses);
		}
	}

	/**
	 * Infers the package name when not specified in @DslDomain. Priority: 1.
	 * fieldsConstants package 2. fieldsEnum package 3. Annotated class package
	 */
	private String inferPackageName(final DslDomain annotation, final Element element) {
		// Priority 1: Try to get package from fieldsConstants
		final String constantsPackage = this.getPackageFromFieldsConstants(annotation);
		if (constantsPackage != null) {
			this.messager.printMessage(Diagnostic.Kind.NOTE,
					"Inferred package from fieldsConstants: " + constantsPackage);
			return constantsPackage;
		}

		// Priority 2: Try to get package from fieldsEnum
		final String enumPackage = this.getPackageFromFieldsEnum(annotation);
		if (enumPackage != null) {
			this.messager.printMessage(Diagnostic.Kind.NOTE, "Inferred package from fieldsEnum: " + enumPackage);
			return enumPackage;
		}

		// Priority 3: Use annotated class package (default behavior)
		final String classPackage = this.processingEnv.getElementUtils().getPackageOf(element).getQualifiedName()
				.toString();
		this.messager.printMessage(Diagnostic.Kind.NOTE, "Using annotated class package: " + classPackage);
		return classPackage;
	}

	/**
	 * Gets the package name from fieldsConstants if specified.
	 */
	private String getPackageFromFieldsConstants(final DslDomain annotation) {
		try {
			annotation.fieldsConstants();
			return null; // void.class
		} catch (final javax.lang.model.type.MirroredTypeException mte) {
			final javax.lang.model.type.TypeMirror typeMirror = mte.getTypeMirror();
			final String typeName = typeMirror.toString();
			if (!typeName.equals("void") && !typeName.equals("java.lang.Void")) {
				// Use parseClassName to correctly handle nested classes
				final ClassName className = this.parseClassName(typeName);
				return className.packageName();
			}
			return null;
		}
	}

	/**
	 * Gets the package name from fieldsEnum if specified.
	 */
	private String getPackageFromFieldsEnum(final DslDomain annotation) {
		try {
			annotation.fieldsEnum();
			return null; // void.class
		} catch (final javax.lang.model.type.MirroredTypeException mte) {
			final javax.lang.model.type.TypeMirror typeMirror = mte.getTypeMirror();
			final String typeName = typeMirror.toString();
			if (!typeName.equals("void") && !typeName.equals("java.lang.Void")) {
				// Use parseClassName to correctly handle nested classes
				final com.squareup.javapoet.ClassName className = this.parseClassName(typeName);
				return className.packageName();
			}
			return null;
		}
	}

}
