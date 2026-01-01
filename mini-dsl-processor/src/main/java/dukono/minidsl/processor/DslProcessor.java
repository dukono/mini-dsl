package dukono.minidsl.processor;

import com.sun.source.util.Trees;
import dukono.minidsl.annotation.DslDomain;
import dukono.minidsl.annotation.DslField;
import dukono.minidsl.annotation.DslOperation;
import dukono.minidsl.processor.generator.ApiGenerator;

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
		final DtoInfo dtoInfo = this.extractDtoClassName(annotation, domainName, packageName);

		// Create context
		final DslContext context = new DslContext(domainName, packageName, fields, operations, dtoInfo.className);

		// Generate only the Api class which contains all nested classes
		this.generateApi(context);

		this.messager.printMessage(Diagnostic.Kind.NOTE, "Successfully generated DSL classes for: " + domainName);
	}

	private void generateApi(final DslContext context) throws IOException {
		final ApiGenerator generator = new ApiGenerator();
		generator.generate(context, this.filer);
	}

	/**
	 * Extracts the DTO class name from annotation or generates it automatically.
	 * 
	 * @param annotation
	 *            the @DslDomain annotation
	 * @param domainName
	 *            the domain name
	 * @param packageName
	 *            the package name
	 * @return DtoInfo with class name and whether it should be generated
	 */
	private DtoInfo extractDtoClassName(final DslDomain annotation, final String domainName, final String packageName) {
		try {
			annotation.dtoClass();
			// If we reach here, it's void.class - generate automatically
			final String generatedDtoName = domainName + "Dto";
			final String fullName = packageName + "." + generatedDtoName;
			this.messager.printMessage(Diagnostic.Kind.NOTE, "Auto-generating DTO class: " + fullName);
			return new DtoInfo(fullName, true);
		} catch (final javax.lang.model.type.MirroredTypeException mte) {
			final javax.lang.model.type.TypeMirror typeMirror = mte.getTypeMirror();
			final String typeName = typeMirror.toString();
			if (!typeName.equals("void") && !typeName.equals("java.lang.Void")) {
				// User specified a class
				this.messager.printMessage(Diagnostic.Kind.NOTE, "Using user-defined DTO class: " + typeName);
				return new DtoInfo(typeName, false);
			} else {
				// void.class - generate automatically
				final String generatedDtoName = domainName + "Dto";
				final String fullName = packageName + "." + generatedDtoName;
				this.messager.printMessage(Diagnostic.Kind.NOTE, "Auto-generating DTO class: " + fullName);
				return new DtoInfo(fullName, true);
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
				return this.extractPackageFromQualifiedName(typeName);
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
				return this.extractPackageFromQualifiedName(typeName);
			}
			return null;
		}
	}

	/**
	 * Extracts package name from a fully qualified class name. Example:
	 * "com.example.MyClass" -> "com.example"
	 */
	private String extractPackageFromQualifiedName(final String qualifiedName) {
		final int lastDot = qualifiedName.lastIndexOf('.');
		if (lastDot > 0) {
			return qualifiedName.substring(0, lastDot);
		}
		return ""; // Default package
	}

}
