package dukono.minidsl.processor;

import java.io.IOException;
import java.util.Set;

import com.google.auto.service.AutoService;
import dukono.minidsl.annotation.DslDomain;
import dukono.minidsl.processor.generator.AnchorActionsGenerator;
import dukono.minidsl.processor.generator.AnchorListGenerator;
import dukono.minidsl.processor.generator.AnchorLogicalMainGenerator;
import dukono.minidsl.processor.generator.AnchorMainGenerator;
import dukono.minidsl.processor.generator.AnchorOneGenerator;
import dukono.minidsl.processor.generator.AnchorOperationsGenerator;
import dukono.minidsl.processor.generator.AnchorOperationsLogicalGenerator;
import dukono.minidsl.processor.generator.AnchorOperationsOneGenerator;
import dukono.minidsl.processor.generator.ApiGenerator;
import dukono.minidsl.processor.generator.FieldsGenerator;
import dukono.minidsl.processor.generator.OperationsGenerator;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;

/**
 * Annotation processor for @DslDomain annotation.
 * 
 * Generates all necessary classes for a DSL domain: - Fields class - Operations
 * class - AnchorOperationsBase class - AnchorMain class - Api class
 */
@AutoService(Processor.class)
@SupportedAnnotationTypes("dukono.minidsl.annotation.DslDomain")
@SupportedSourceVersion(SourceVersion.RELEASE_21)
public class DslProcessor extends AbstractProcessor {

	private Filer filer;
	private Messager messager;

	@Override
	public synchronized void init(final ProcessingEnvironment processingEnv) {
		super.init(processingEnv);
		this.filer = processingEnv.getFiler();
		this.messager = processingEnv.getMessager();
	}

	@Override
	public boolean process(final Set<? extends TypeElement> annotations, final RoundEnvironment roundEnv) {
		for (final Element element : roundEnv.getElementsAnnotatedWith(DslDomain.class)) {
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
		if (packageName.isEmpty()) {
			packageName = this.processingEnv.getElementUtils().getPackageOf(element).getQualifiedName().toString();
		}

		this.messager.printMessage(Diagnostic.Kind.NOTE,
				"Generating DSL classes for domain: " + domainName + " in package: " + packageName);

		// Create context
		final DslContext context = new DslContext(domainName, packageName, annotation.fields(), annotation.operations(),
				annotation.dtoClass(), annotation.withLogical(), annotation.withActions(), annotation.withList(),
				annotation.generateApi());

		// Generate classes
		this.generateFields(context);
		this.generateOperations(context);
		this.generateAnchorOperationsBase(context);

		// Generate logical operations if enabled
		if (annotation.withLogical()) {
			this.generateAnchorOperationsLogical(context);
		}

		// Generate list-related classes if enabled
		if (annotation.withList()) {
			this.generateAnchorOperationsOne(context);
			this.generateAnchorOne(context);
			this.generateAnchorList(context);
			this.generateAnchorLogicalMain(context);
		}

		// Generate main anchor
		this.generateAnchorMain(context);

		// Generate actions if enabled
		if (annotation.withActions()) {
			this.generateAnchorActions(context);
		}

		if (annotation.generateApi()) {
			this.generateApi(context);
		}

		this.messager.printMessage(Diagnostic.Kind.NOTE, "Successfully generated DSL classes for: " + domainName);
	}

	private void generateFields(final DslContext context) throws IOException {
		final FieldsGenerator generator = new FieldsGenerator();
		generator.generate(context, this.filer);
	}

	private void generateOperations(final DslContext context) throws IOException {
		final OperationsGenerator generator = new OperationsGenerator();
		generator.generate(context, this.filer);
	}

	private void generateAnchorOperationsBase(final DslContext context) throws IOException {
		final AnchorOperationsGenerator generator = new AnchorOperationsGenerator();
		generator.generate(context, this.filer);
	}

	private void generateAnchorMain(final DslContext context) throws IOException {
		final AnchorMainGenerator generator = new AnchorMainGenerator();
		generator.generate(context, this.filer);
	}

	private void generateApi(final DslContext context) throws IOException {
		final ApiGenerator generator = new ApiGenerator();
		generator.generate(context, this.filer);
	}

	private void generateAnchorOperationsLogical(final DslContext context) throws IOException {
		final AnchorOperationsLogicalGenerator generator = new AnchorOperationsLogicalGenerator();
		generator.generate(context, this.filer);
	}

	private void generateAnchorOperationsOne(final DslContext context) throws IOException {
		final AnchorOperationsOneGenerator generator = new AnchorOperationsOneGenerator();
		generator.generate(context, this.filer);
	}

	private void generateAnchorOne(final DslContext context) throws IOException {
		final AnchorOneGenerator generator = new AnchorOneGenerator();
		generator.generate(context, this.filer);
	}

	private void generateAnchorList(final DslContext context) throws IOException {
		final AnchorListGenerator generator = new AnchorListGenerator();
		generator.generate(context, this.filer);
	}

	private void generateAnchorLogicalMain(final DslContext context) throws IOException {
		final AnchorLogicalMainGenerator generator = new AnchorLogicalMainGenerator();
		generator.generate(context, this.filer);
	}

	private void generateAnchorActions(final DslContext context) throws IOException {
		final AnchorActionsGenerator generator = new AnchorActionsGenerator();
		generator.generate(context, this.filer);
	}
}
