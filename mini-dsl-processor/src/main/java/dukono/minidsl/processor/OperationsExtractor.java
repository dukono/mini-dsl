package dukono.minidsl.processor;

import java.util.ArrayList;
import java.util.List;

import com.sun.source.tree.ExpressionTree;
import com.sun.source.tree.LiteralTree;
import com.sun.source.tree.MemberSelectTree;
import com.sun.source.tree.NewClassTree;
import com.sun.source.tree.VariableTree;
import com.sun.source.util.TreePath;
import com.sun.source.util.Trees;
import dukono.minidsl.annotation.DslDomain;
import dukono.minidsl.annotation.DslOperation;
import dukono.minidsl.annotation.OperationType;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

/**
 * Extracts operation definitions from @DslDomain annotation using one of two
 * methods: 1. operationsEnum - from an enum that implements OperationDefinition
 * 2. operations[] - from an array of @DslOperation annotations
 * 
 * Validates that only ONE method is used and provides clear error messages.
 */
final class OperationsExtractor {

	private final ProcessingEnvironment processingEnv;
	private final Messager messager;

	OperationsExtractor(final ProcessingEnvironment processingEnv) {
		this.processingEnv = processingEnv;
		this.messager = processingEnv.getMessager();
	}

	/**
	 * Extracts operations from @DslDomain annotation. Validates that exactly ONE
	 * operation definition method is used, then extracts operations using the
	 * appropriate method.
	 * 
	 * @param annotation
	 *            the @DslDomain annotation
	 * @param element
	 *            the annotated element (for error reporting)
	 * @return array of DslOperation instances
	 */
	DslOperation[] extractOperations(final DslDomain annotation, final Element element) {
		// First, validate that only ONE method is defined
		this.validateSingleOperationDefinition(annotation, element);

		// Then extract using the defined method
		return this.extractOperationsFromDefinedMethod(annotation);
	}

	/**
	 * Validates that only ONE operation definition method is used (operationsEnum
	 * or operations array). Having multiple definitions is an error.
	 */
	private void validateSingleOperationDefinition(final DslDomain annotation, final Element element) {
		int definedCount = 0;
		final StringBuilder definedMethods = new StringBuilder();

		// Check operationsEnum
		if (this.isOperationsEnumDefined(annotation)) {
			definedCount++;
			definedMethods.append("operationsEnum ");
		}

		// Check operations array
		final boolean hasOperationsArray = annotation.operations() != null && annotation.operations().length > 0;
		if (hasOperationsArray) {
			definedCount++;
			definedMethods.append("operations[] ");
		}

		// Validate exactly one is defined
		if (definedCount == 0) {
			this.messager.printMessage(Diagnostic.Kind.ERROR,
					"@DslDomain must specify exactly ONE of: operations[] or operationsEnum", element);
		} else if (definedCount > 1) {
			this.messager.printMessage(
					Diagnostic.Kind.ERROR, "@DslDomain cannot use multiple operation definitions. Found: "
							+ definedMethods.toString().trim() + ". Use only ONE of: operations[] or operationsEnum",
					element);
		}
	}

	/**
	 * Extracts operations from the defined method with priority: 1. operationsEnum
	 * 2. operations[]
	 */
	private DslOperation[] extractOperationsFromDefinedMethod(final DslDomain annotation) {
		// Priority 1: Check if operationsEnum is specified
		final TypeMirror enumTypeMirror = this.getOperationsEnumTypeMirror(annotation);
		if (enumTypeMirror != null) {
			final String typeName = enumTypeMirror.toString();
			if (!typeName.equals("void") && !typeName.equals("java.lang.Void")) {
				this.messager.printMessage(Diagnostic.Kind.NOTE, "Extracting operations from enum: " + typeName);
				return this.extractOperationsFromEnum(enumTypeMirror);
			}
		}

		// Priority 2: Use operations array
		final DslOperation[] operations = annotation.operations();
		if (operations != null && operations.length > 0) {
			this.messager.printMessage(Diagnostic.Kind.NOTE,
					"Extracting operations from @DslOperation array (" + operations.length + " operations)");
		}
		return operations;
	}

	/**
	 * Extracts DslOperation array from an enum using Trees API to read constructor
	 * arguments.
	 */
	private DslOperation[] extractOperationsFromEnum(final TypeMirror enumTypeMirror) {
		final Types types = this.processingEnv.getTypeUtils();
		final Trees trees = Trees.instance(this.processingEnv);
		final Element enumElement = types.asElement(enumTypeMirror);

		if (!(enumElement instanceof final TypeElement enumTypeElement)) {
			this.messager.printMessage(Diagnostic.Kind.ERROR, "operationsEnum must be a valid type: " + enumTypeMirror);
			return new DslOperation[0];
		}

		// Verify it's an enum
		if (enumTypeElement.getKind() != ElementKind.ENUM) {
			this.messager.printMessage(Diagnostic.Kind.ERROR, "operationsEnum must be an enum type: " + enumTypeMirror);
			return new DslOperation[0];
		}

		// Extract enum constants and their constructor arguments
		final List<DslOperation> operations = new ArrayList<>();

		for (final Element enclosedEl : enumTypeElement.getEnclosedElements()) {
			if (enclosedEl.getKind() == ElementKind.ENUM_CONSTANT) {
				final String enumName = enclosedEl.getSimpleName().toString();
				final OperationData opData = this.extractOperationData(trees, enclosedEl);

				if (opData != null) {
					operations.add(this.createDslOperationFromData(opData));
				} else {
					this.messager.printMessage(Diagnostic.Kind.WARNING,
							"Could not extract operation data from enum constant: " + enumName);
				}
			}
		}

		return operations.toArray(new DslOperation[0]);
	}

	/**
	 * Extracts operation data from an enum constant's constructor arguments using
	 * AST.
	 */
	private OperationData extractOperationData(final Trees trees, final Element enumConstant) {
		try {
			final TreePath path = trees.getPath(enumConstant);
			if (path == null || !(path.getLeaf() instanceof final VariableTree varTree)) {
				return null;
			}

			final ExpressionTree init = varTree.getInitializer();

			if (!(init instanceof final NewClassTree nct)) {
				return null;
			}

			final List<? extends ExpressionTree> args = nct.getArguments();

			// Expected: (String name, String operator, OperationType type, String
			// description)
			if (args.size() < 3) {
				return null; // Need at least name, operator, type
			}

			final String name = this.extractStringLiteral(args.get(0));
			final String operator = this.extractStringLiteral(args.get(1));
			final OperationType type = this.extractOperationType(args.get(2));
			final String description = args.size() > 3 ? this.extractStringLiteral(args.get(3)) : "";

			if (name == null || type == null) {
				return null; // operator can be null for JUST_ADD
			}

			return new OperationData(name, operator, type, description);

		} catch (final Exception e) {
			return null;
		}
	}

	/**
	 * Extracts a String literal value from an expression tree.
	 */
	private String extractStringLiteral(final ExpressionTree expr) {
		if (expr instanceof LiteralTree) {
			final Object value = ((LiteralTree) expr).getValue();
			return value != null ? value.toString() : null;
		}
		return null;
	}

	/**
	 * Extracts OperationType from a MemberSelectTree (e.g.,
	 * OperationType.WITH_ARG).
	 */
	private OperationType extractOperationType(final ExpressionTree expr) {
		if (expr instanceof final MemberSelectTree mst) {
			final String memberName = mst.getIdentifier().toString();

			try {
				return OperationType.valueOf(memberName);
			} catch (final IllegalArgumentException e) {
				return null;
			}
		}
		return null;
	}

	/**
	 * Creates a DslOperation from extracted operation data.
	 */
	private DslOperation createDslOperationFromData(final OperationData opData) {
		return new DslOperation() {
			@Override
			public Class<? extends java.lang.annotation.Annotation> annotationType() {
				return DslOperation.class;
			}

			@Override
			public String name() {
				return opData.name;
			}

			@Override
			public String operator() {
				return opData.operator != null ? opData.operator : "";
			}

			@Override
			public OperationType type() {
				return opData.type;
			}

			@Override
			public String description() {
				return opData.description != null ? opData.description : "";
			}
		};
	}

	/**
	 * Simple data class to hold operation information extracted from enum.
	 */
	private static class OperationData {
		final String name;
		final String operator;
		final OperationType type;
		final String description;

		OperationData(final String name, final String operator, final OperationType type, final String description) {
			this.name = name;
			this.operator = operator;
			this.type = type;
			this.description = description;
		}
	}

	/**
	 * Checks if operationsEnum is defined (not void).
	 */
	private boolean isOperationsEnumDefined(final DslDomain annotation) {
		try {
			annotation.operationsEnum();
			return false; // void.class
		} catch (final MirroredTypeException mte) {
			final String typeName = mte.getTypeMirror().toString();
			return !typeName.equals("void") && !typeName.equals("java.lang.Void");
		}
	}

	/**
	 * Gets the TypeMirror for operationsEnum if specified.
	 */
	private TypeMirror getOperationsEnumTypeMirror(final DslDomain annotation) {
		try {
			annotation.operationsEnum();
			return null; // void.class
		} catch (final MirroredTypeException mte) {
			return mte.getTypeMirror();
		}
	}
}
