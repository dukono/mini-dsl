package dukono.minidsl.processor;

import java.util.LinkedHashMap;
import java.util.Map;

import com.sun.source.tree.ExpressionTree;
import com.sun.source.tree.LiteralTree;
import com.sun.source.tree.VariableTree;
import com.sun.source.util.TreePath;
import com.sun.source.util.Trees;
import dukono.minidsl.annotation.DslField;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Types;

/**
 * Extracts field definitions from a class with static final String constants.
 * 
 * Reads public static final String fields from a class to create DslField
 * instances.
 * 
 * Example class:
 * 
 * <pre>
 * public class OrderFieldConstants {
 * 	public static final String ORDER_ID = "orderId";
 * 	public static final String CUSTOMER_NAME = "customerName";
 * 	public static final String TOTAL_AMOUNT = "totalAmount";
 * }
 * </pre>
 * 
 * Will extract: - ORDER_ID -> "orderId" - CUSTOMER_NAME -> "customerName" -
 * TOTAL_AMOUNT -> "totalAmount"
 */
final class ConstantFieldsExtractor {

	private final ProcessingEnvironment processingEnv;

	ConstantFieldsExtractor(final ProcessingEnvironment processingEnv) {
		this.processingEnv = processingEnv;
	}

	/**
	 * Extracts DslField array from a class containing static final String
	 * constants.
	 * 
	 * @param classTypeMirror
	 *            the TypeMirror of the constants class
	 * @return array of DslField instances
	 */
	DslField[] extractFieldsFromConstants(final TypeMirror classTypeMirror) {
		// Collect constant values using Trees API
		final Map<String, String> constantValues = this.collectConstantValues(classTypeMirror);

		final DslField[] fields = new DslField[constantValues.size()];
		int index = 0;

		for (final Map.Entry<String, String> entry : constantValues.entrySet()) {
			final String javaName = entry.getKey();
			final String value = entry.getValue();

			// Create a DslField annotation dynamically
			fields[index++] = new DslField() {
				@Override
				public Class<? extends java.lang.annotation.Annotation> annotationType() {
					return DslField.class;
				}

				@Override
				public String value() {
					return value != null ? value : javaName.toLowerCase();
				}

				@Override
				public String javaName() {
					return javaName;
				}

				@Override
				public String description() {
					return "";
				}
			};
		}

		return fields;
	}

	/**
	 * Collects static final String field names and their values. Based on
	 * ConstantFieldCollector logic.
	 */
	private Map<String, String> collectConstantValues(final TypeMirror classTypeMirror) {
		final Map<String, String> result = new LinkedHashMap<>();
		if (classTypeMirror == null) {
			return result;
		}

		final Types types = this.processingEnv.getTypeUtils();
		final Trees trees = Trees.instance(this.processingEnv);
		final Element classElement = types.asElement(classTypeMirror);

		if (classElement instanceof TypeElement) {
			for (final Element enclosedEl : classElement.getEnclosedElements()) {
				if (this.isStaticFinalStringField(enclosedEl)) {
					final String fieldName = enclosedEl.getSimpleName().toString();
					final String fieldValue = this.extractFieldValue(trees, enclosedEl);
					result.put(fieldName, fieldValue);
				}
			}
		}

		return result;
	}

	/**
	 * Checks if an element is a static final String field.
	 */
	private boolean isStaticFinalStringField(final Element element) {
		return element.getKind() == ElementKind.FIELD && element.getModifiers().contains(Modifier.STATIC)
				&& element.getModifiers().contains(Modifier.FINAL)
				&& "java.lang.String".equals(element.asType().toString());
	}

	/**
	 * Extracts the String value from a static final field using AST and fallback to
	 * constant value.
	 */
	private String extractFieldValue(final Trees trees, final Element enclosedEl) {
		String fieldValue = null;

		try {
			// Try to get value from AST
			final TreePath path = trees.getPath(enclosedEl);
			if (path != null && path.getLeaf() instanceof final VariableTree varTree) {
				final ExpressionTree init = varTree.getInitializer();
				if (init instanceof final LiteralTree litTree) {
					final Object litVal = litTree.getValue();
					if (litVal != null) {
						fieldValue = litVal.toString();
					}
				}
			}

			// Fallback: try to get constant value
			if (fieldValue == null && enclosedEl instanceof final VariableElement varEl) {
				final Object constVal = varEl.getConstantValue();
				if (constVal instanceof final String s && !s.isEmpty()) {
					fieldValue = s;
				}
			}
		} catch (final Exception ignored) {
			// If we can't extract the value, return null
		}

		return fieldValue;
	}
}
