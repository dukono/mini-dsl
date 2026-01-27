package dukono.minidsl.example.complete;

import dukono.minidsl.annotation.DslDomain;
import dukono.minidsl.annotation.OperationDefinition;
import dukono.minidsl.annotation.OperationType;

/**
 * Ejemplo completo: Sistema de búsqueda de productos de e-commerce
 * 
 * Este ejemplo combina: - Fields definidos con Enum - Múltiples tipos de
 * operaciones - Prefijo en valores para eliminar namespace - Operaciones con
 * listas personalizadas (delimitadores y brackets)
 */
// @formatter:off
@DslDomain(
	name = "EcommerceProductSearch",
	fieldsEnum = EcommerceCompleteExample.ProductFields.class,
	fieldValuePrefix = "product.",
	operationsEnum = EcommerceCompleteExample.ProductOperations.class
)
// @formatter:on
public class EcommerceCompleteExample {

	/**
	 * Campos de producto con namespace
	 */
	public enum ProductFields {
		ID("product.id"), 
		NAME("product.name"), 
		CATEGORY("product.category"), 
		BRAND("product.brand"), 
		PRICE("product.price"), 
		STOCK("product.stock"), 
		RATING("product.rating"), 
		TAGS("product.tags"),
		DESCRIPTION("product.description"), 
		CREATED_DATE("product.createdDate"), 
		ACTIVE("product.active");
		// @formatter:on
		private final String value;

		ProductFields(final String value) {
			this.value = value;
		}

		public String getValue() {
			return this.value;
		}
	}

	/**
	 * Operaciones completas para búsqueda de productos
	 */
	public enum ProductOperations implements OperationDefinition {
		// Comparaciones básicas
		EQUALS("equalTo", "eq", OperationType.WITH_ARG, "Equal to value"), NOT_EQUALS("notEquals", "ne",
				OperationType.WITH_ARG, "Not equal to value"),

		// Comparaciones numéricas
		GREATER_THAN("greaterThan", "gt", OperationType.WITH_ARG, "Greater than value"), LESS_THAN("lessThan", "lt",
				OperationType.WITH_ARG,
				"Less than value"), BETWEEN("between", "between", OperationType.WITH_LIST, "-", "()", "Between range"),

		// Búsqueda de texto
		CONTAINS("contains", "contains", OperationType.WITH_ARG, "Contains text"), STARTS_WITH("startsWith",
				"starts_with", OperationType.WITH_ARG,
				"Starts with text"), ENDS_WITH("endsWith", "ends_with", OperationType.WITH_ARG, "Ends with text"),

		// Listas
		IN_CATEGORIES("inCategories", "in", OperationType.WITH_LIST, ",", "[]", "In category list"), HAS_ANY_TAG(
				"hasAnyTag", "has_tag", OperationType.WITH_LIST, "|", "{}", "Has any of these tags"),

		// Nullability
		EXISTS("exists", "is_not_null", OperationType.NO_VALUE, "Field is not null"), NOT_EXISTS("notExists", "is_null",
				OperationType.NO_VALUE, "Field is null"),

		// Especiales
		ADD_FIELD("addField", null, OperationType.NO_OP_NO_VALUE, "Just add field name");

		private final String name;
		private final String operator;
		private final OperationType type;
		private final String listDelimiter;
		private final String listBrackets;
		private final String description;

		ProductOperations(final String name, final String operator, final OperationType type,
				final String description) {
			this(name, operator, type, " ", "[]", description);
		}

		ProductOperations(final String name, final String operator, final OperationType type,
				final String listDelimiter, final String listBrackets, final String description) {
			this.name = name;
			this.operator = operator;
			this.type = type;
			this.listDelimiter = listDelimiter;
			this.listBrackets = listBrackets;
			this.description = description;
		}

		@Override
		public String getName() {
			return this.name;
		}

		@Override
		public String getOperator() {
			return this.operator;
		}

		@Override
		public OperationType getType() {
			return this.type;
		}

		@Override
		public String getListDelimiter() {
			return this.listDelimiter;
		}

		@Override
		public String getListBrackets() {
			return this.listBrackets;
		}

		@Override
		public String getDescription() {
			return this.description;
		}
	}
}
