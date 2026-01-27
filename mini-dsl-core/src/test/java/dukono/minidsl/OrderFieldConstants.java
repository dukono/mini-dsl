package dukono.minidsl;

/**
 * Class that defines fields as public static final String constants.
 * 
 * This is an alternative to using @DslField[] or enum in @DslDomain annotation.
 * 
 * Benefits: - Simple and straightforward - No need for enum constructor or
 * getValue() method - Can be used in other places (e.g., constant references) -
 * Familiar pattern for many developers
 * 
 * The constant name (e.g., ORDER_ID) becomes the javaName. The constant value
 * (e.g., "orderId") becomes the field value.
 */
public class OrderFieldConstants {

	public static final String ORDER_ID = "orderId";

	public static final String CUSTOMER_NAME = "customerName";

	public static final String TOTAL_AMOUNT = "totalAmount";

	public static final String STATUS = "status";

	public static final String CREATED_DATE = "createdDate";

	public static final String ITEMS = "items";

	// Private constructor to prevent instantiation
	private OrderFieldConstants() {
		throw new UnsupportedOperationException("This is a constants class and cannot be instantiated");
	}
}
