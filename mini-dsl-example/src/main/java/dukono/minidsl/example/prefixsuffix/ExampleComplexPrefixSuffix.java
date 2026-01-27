package dukono.minidsl.example.prefixsuffix;

import dukono.minidsl.annotation.DslDomain;
import dukono.minidsl.annotation.OperationDefinition;
import dukono.minidsl.annotation.OperationType;

/**
 * Ejemplo: Combinar múltiples transformaciones
 * 
 * Se pueden combinar múltiples prefijos/sufijos: - fieldNamePrefix: elimina
 * prefijo del nombre Java - fieldNameSuffix: elimina sufijo del nombre Java -
 * fieldValuePrefix: elimina prefijo del valor - fieldValueSuffix: elimina
 * sufijo del valor
 * 
 * Ejemplo: - Nombre: FIELD_INVOICE_ID_COLUMN -> INVOICE_ID - Valor:
 * invoice.invoiceId.value -> invoiceId
 */
// @formatter:off
@DslDomain(
	name = "ExampleComplexPrefixSuffix", 
	fieldsConstants = ExampleComplexPrefixSuffix.InvoiceFieldConstantsComplex.class,
	fieldNamePrefix = "FIELD_",
	fieldNameSuffix = "_COLUMN",
	fieldValuePrefix = "invoice.",
	fieldValueSuffix = ".value",
	operationsEnum = ExampleComplexPrefixSuffix.SimpleOperations.class
)
// @formatter:on
public class ExampleComplexPrefixSuffix {

	public static class InvoiceFieldConstantsComplex {
		// Nombre: FIELD_INVOICE_ID_COLUMN -> INVOICE_ID
		// Valor: invoice.invoiceId.value -> invoiceId
		public static final String FIELD_INVOICE_ID_COLUMN = "invoice.invoiceId.value";
		public static final String FIELD_AMOUNT_COLUMN = "invoice.amount.value";
		public static final String FIELD_DATE_COLUMN = "invoice.date.value";
	}

	/**
	 * Enum simple de operaciones
	 */
	public enum SimpleOperations implements OperationDefinition {
		EQUAL_TO("equalTo", "eq", OperationType.WITH_ARG, "");

		private final String name;
		private final String operator;
		private final OperationType type;
		private final String description;

		SimpleOperations(final String name, final String operator, final OperationType type, final String description) {
			this.name = name;
			this.operator = operator;
			this.type = type;
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
		public String getDescription() {
			return this.description;
		}
	}
}
