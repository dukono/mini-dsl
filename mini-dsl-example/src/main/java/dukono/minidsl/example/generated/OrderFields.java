package dukono.minidsl.example.generated;

/**
 * Enum that defines the fields for the Order domain.
 * 
 * This is an alternative to using @DslField[] in @DslDomain annotation.
 * 
 * Benefits: - More concise - Type-safe - Easier to refactor - Less verbose than
 * array of @DslField
 * 
 * The enum constant name (e.g., ORDER_ID) becomes the javaName. The value
 * returned by getValue() becomes the field value (e.g., "orderId").
 */
public enum OrderFields {

	ORDER_ID("orderId"),

	CUSTOMER_NAME("customerName"),

	TOTAL_AMOUNT("totalAmount"),

	STATUS("status"),

	CREATED_DATE("createdDate"),

	ITEMS("items");

	private final String value;

	OrderFields(final String value) {
		this.value = value;
	}

	public String getValue() {
		return this.value;
	}
}
