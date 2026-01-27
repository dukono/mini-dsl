package dukono.minidsl.example.operations;

import dukono.minidsl.annotation.DslDomain;
import dukono.minidsl.annotation.DslField;
import dukono.minidsl.annotation.OperationDefinition;
import dukono.minidsl.annotation.OperationType;

/**
 * Ejemplo: Personalización de delimitadores y brackets en operaciones de lista
 * 
 * Muestra diferentes combinaciones de delimitadores y brackets para formatear
 * listas: - Delimiter: el separador entre elementos (ej: ",", "|", ";", " ") -
 * Brackets: los caracteres que envuelven la lista (ej: "[]", "()", "{}", "<>")
 */
// @formatter:off
@DslDomain(
	name = "ExampleListFormatting",
	fields = {
		@DslField(javaName = "TAGS", value = "tags"),
		@DslField(javaName = "CATEGORIES", value = "categories"),
		@DslField(javaName = "IDS", value = "ids"),
		@DslField(javaName = "VALUES", value = "values")
	},
	operationsEnum = ExampleListFormatting.ListFormattingOperations.class
)
// @formatter:on
public class ExampleListFormatting {

	/**
	 * Operaciones con diferentes formatos de lista
	 */
	public enum ListFormattingOperations implements OperationDefinition {
		// Formato estándar: [val1, val2, val3]
		STANDARD_LIST("standardList", "in", OperationType.WITH_LIST, " ", "[]", "Standard format: [val1, val2, val3]"),

		// Formato compacto: [val1,val2,val3]
		COMPACT_LIST("compactList", "in", OperationType.WITH_LIST, ",", "[]", "Compact format: [val1,val2,val3]"),

		// Formato con pipe: {val1|val2|val3}
		PIPE_LIST("pipeList", "contains_any", OperationType.WITH_LIST, "|", "{}", "Pipe format: {val1|val2|val3}"),

		// Formato con semicolon: (val1;val2;val3)
		SEMICOLON_LIST("semicolonList", "in_set", OperationType.WITH_LIST, ";", "()",
				"Semicolon format: (val1;val2;val3)"),

		// Formato SQL-like: (val1, val2, val3)
		SQL_LIST("sqlList", "in", OperationType.WITH_LIST, ", ", "()", "SQL format: (val1, val2, val3)"),

		// Formato con guiones: <val1-val2-val3>
		DASH_LIST("dashList", "range", OperationType.WITH_LIST, "-", "<>", "Dash format: <val1-val2-val3>"),

		// Sin brackets: val1,val2,val3
		NO_BRACKETS("noBrackets", "csv", OperationType.WITH_LIST, ",", "", "No brackets: val1,val2,val3"),

		// Formato personalizado: |val1$val2$val3|
		CUSTOM_FORMAT("customFormat", "custom", OperationType.WITH_LIST, "$", "||", "Custom format: |val1$val2$val3|");

		private final String name;
		private final String operator;
		private final OperationType type;
		private final String listDelimiter;
		private final String listBrackets;
		private final String description;

		ListFormattingOperations(final String name, final String operator, final OperationType type,
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
