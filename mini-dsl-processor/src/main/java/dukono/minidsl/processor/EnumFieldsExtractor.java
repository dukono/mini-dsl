package dukono.minidsl.processor;

import java.util.LinkedHashMap;
import java.util.Map;

import com.sun.source.tree.ExpressionTree;
import com.sun.source.tree.LiteralTree;
import com.sun.source.tree.NewClassTree;
import com.sun.source.tree.VariableTree;
import com.sun.source.util.TreePath;
import com.sun.source.util.Trees;
import dukono.minidsl.annotation.DslField;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Types;

/**
 * Extracts field definitions from an enum using Trees API.
 * 
 * Reads enum constants and their first String literal constructor argument to
 * create DslField instances.
 * 
 * Example enum:
 * 
 * <pre>
 * public enum OrderFields {
 * 	ORDER_ID("orderId"), CUSTOMER_NAME("customerName");
 * 
 * 	private final String value;
 * 
 * 	OrderFields(String value) {
 * 		this.value = value;
 * 	}
 * }
 * </pre>
 * 
 * Will extract: - ORDER_ID -> "orderId" - CUSTOMER_NAME -> "customerName"
 */
final class EnumFieldsExtractor {

	private final ProcessingEnvironment processingEnv;

	EnumFieldsExtractor(final ProcessingEnvironment processingEnv) {
		this.processingEnv = processingEnv;
	}

	/**
	 * Extracts DslField array from an enum TypeMirror.
	 * 
	 * @param enumTypeMirror
	 *            the TypeMirror of the enum class
	 * @return array of DslField instances
	 */
	DslField[] extractFieldsFromEnum(final TypeMirror enumTypeMirror) {
		// Collect enum values using Trees API
		final Map<String, String> enumValues = this.collectEnumValues(enumTypeMirror);

		final DslField[] fields = new DslField[enumValues.size()];
		int index = 0;

		for (final Map.Entry<String, String> entry : enumValues.entrySet()) {
			final String javaName = entry.getKey();
			final String value = entry.getValue() != null
					? entry.getValue()
					: this.convertEnumNameToFieldValue(javaName);

			// Create a DslField annotation dynamically
			fields[index++] = new DslField() {
				@Override
				public Class<? extends java.lang.annotation.Annotation> annotationType() {
					return DslField.class;
				}

				@Override
				public String value() {
					return value;
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
	 * Collects enum constant names and their first String literal argument values.
	 * Based on EnumValueCollector logic.
	 */
	private Map<String, String> collectEnumValues(final TypeMirror enumTypeMirror) {
		final Map<String, String> result = new LinkedHashMap<>();
		if (enumTypeMirror == null) {
			return result;
		}

		final Types types = this.processingEnv.getTypeUtils();
		final Trees trees = Trees.instance(this.processingEnv);
		final Element enumElement = types.asElement(enumTypeMirror);

		if (enumElement instanceof TypeElement) {
			for (final Element enclosedEl : enumElement.getEnclosedElements()) {
				if (enclosedEl.getKind() == ElementKind.ENUM_CONSTANT) {
					final String enumName = enclosedEl.getSimpleName().toString();
					final String enumValue = this.extractEnumValue(trees, enclosedEl);
					result.put(enumName, enumValue);
				}
			}
		}

		return result;
	}

	/**
	 * Extracts the first String literal argument from an enum constant constructor.
	 */
	private String extractEnumValue(final Trees trees, final Element enclosedEl) {
		String enumValue = null;
		try {
			final TreePath path = trees.getPath(enclosedEl);
			if (path != null && path.getLeaf() instanceof final VariableTree varTree) {
				final ExpressionTree init = varTree.getInitializer();
				if (init instanceof final NewClassTree nct) {
					if (!nct.getArguments().isEmpty()) {
						final ExpressionTree arg0 = nct.getArguments().get(0);
						if (arg0 instanceof final LiteralTree litTree) {
							final Object litVal = litTree.getValue();
							if (litVal != null) {
								enumValue = litVal.toString();
							}
						}
					}
				}
			}
		} catch (final Exception ignored) {
			// If we can't extract the value, return null and fall back to name conversion
		}
		return enumValue;
	}

	/**
	 * Converts UPPER_SNAKE_CASE enum name to camelCase field value. Examples: -
	 * ORDER_ID -> orderId - CUSTOMER_NAME -> customerName - STATUS -> status
	 */
	private String convertEnumNameToFieldValue(final String enumName) {
		final String[] parts = enumName.split("_");
		if (parts.length == 1) {
			return enumName.toLowerCase();
		}

		final StringBuilder result = new StringBuilder(parts[0].toLowerCase());
		for (int i = 1; i < parts.length; i++) {
			final String part = parts[i];
			if (!part.isEmpty()) {
				result.append(Character.toUpperCase(part.charAt(0)));
				if (part.length() > 1) {
					result.append(part.substring(1).toLowerCase());
				}
			}
		}
		return result.toString();
	}
}
