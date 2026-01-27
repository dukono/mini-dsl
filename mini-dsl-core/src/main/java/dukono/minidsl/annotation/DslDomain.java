package dukono.minidsl.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to mark a domain class for DSL generation.
 * 
 * When applied to a class, this annotation triggers the annotation processor to
 * generate the following classes: - Fields class: Contains field definitions
 * for the domain - Operations class: Contains comparison and logical operations
 * - AnchorOperationsBase class: Base operations for the DSL - AnchorMain class:
 * Main entry point for the DSL - Api class: High-level API for using the DSL
 * 
 * Example usage:
 * 
 * <pre>
 * {
 * 	&#64;code
 * 	&#64;DslDomain(name = "User", packageName = "com.example.dsl", fields = {
 * 			&#64;DslField(value = "name", javaName = "NAME"), &#64;DslField(value = "age", javaName = "AGE"),
 * 			&#64;DslField(value = "email", javaName = "EMAIL")}, operations = {
 * 					&#64;DslOperation(name = "equalTo", operator = "eq"),
 * 					&#64;DslOperation(name = "greaterThan", operator = "gt")}, dtoClass = "com.example.UserDto")
 * 	public class UserDslConfig {
 * 	}
 * }
 * </pre>
 * 
 * This will generate UserFields, UserOperations, UserAnchorOperationsBase,
 * UserAnchorMain, and UserApi classes.
 */
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
public @interface DslDomain {

	/**
	 * The name of the domain. This will be used as a prefix for generated classes.
	 * Required.
	 * 
	 * @return the domain name
	 */
	String name();

	/**
	 * The package where generated classes will be placed. If not specified, the
	 * same package as the annotated class will be used.
	 * 
	 * @return the target package for generated classes
	 */
	String packageName() default "";

	/**
	 * The fields of the domain.
	 * 
	 * @return array of field definitions
	 */
	DslField[] fields() default {};

	/**
	 * The Enum class that defines the fields of the domain. Alternative to using
	 * fields() array. The enum constant names will be used as javaName, and their
	 * values (via implementing an interface with getValue()) will be used as field
	 * values.
	 * 
	 * Example:
	 * 
	 * <pre>
	 * public enum OrderFields {
	 * 	ORDER_ID("orderId"), CUSTOMER_NAME("customerName"), TOTAL_AMOUNT("totalAmount");
	 * 
	 * 	private final String value;
	 * 
	 * 	OrderFields(String value) {
	 * 		this.value = value;
	 * 	}
	 * 
	 * 	public String getValue() {
	 * 		return value;
	 * 	}
	 * }
	 * </pre>
	 * 
	 * @return the Enum class defining fields, or void.class if not used
	 */
	Class<?> fieldsEnum() default void.class;

	/**
	 * A class with public static final String constants that define the fields of
	 * the domain. Alternative to using fields() array or fieldsEnum().
	 * 
	 * Example:
	 * 
	 * <pre>
	 * public class OrderFieldConstants {
	 * 	public static final String ORDER_ID = "orderId";
	 * 	public static final String CUSTOMER_NAME = "customerName";
	 * 	public static final String TOTAL_AMOUNT = "totalAmount";
	 * }
	 * </pre>
	 * 
	 * @return the class containing field constants, or void.class if not used
	 */
	Class<?> fieldsConstants() default void.class;

	/**
	 * Prefix to remove from field names when extracting from fieldsEnum or
	 * fieldsConstants.
	 * 
	 * Example: If fields are named "FIELD_ORDER_ID", "FIELD_CUSTOMER_NAME" and
	 * fieldNamePrefix = "FIELD_", the generated field names will be "ORDER_ID",
	 * "CUSTOMER_NAME"
	 * 
	 * @return the prefix to remove from field names
	 */
	String fieldNamePrefix() default "";

	/**
	 * Suffix to remove from field names when extracting from fieldsEnum or
	 * fieldsConstants.
	 * 
	 * Example: If fields are named "ORDER_ID_FIELD", "CUSTOMER_NAME_FIELD" and
	 * fieldNameSuffix = "_FIELD", the generated field names will be "ORDER_ID",
	 * "CUSTOMER_NAME"
	 * 
	 * @return the suffix to remove from field names
	 */
	String fieldNameSuffix() default "";

	/**
	 * Prefix to remove from field values when extracting from fieldsEnum or
	 * fieldsConstants.
	 * 
	 * Example: If field values are "order.orderId", "order.customerName" and
	 * fieldValuePrefix = "order.", the generated field values will be "orderId",
	 * "customerName"
	 * 
	 * @return the prefix to remove from field values
	 */
	String fieldValuePrefix() default "";

	/**
	 * Suffix to remove from field values when extracting from fieldsEnum or
	 * fieldsConstants.
	 * 
	 * Example: If field values are "orderId.value", "customerName.value" and
	 * fieldValueSuffix = ".value", the generated field values will be "orderId",
	 * "customerName"
	 * 
	 * @return the suffix to remove from field values
	 */
	String fieldValueSuffix() default "";

	/**
	 * The operations available for the domain.
	 * 
	 * @return array of operation definitions
	 */
	DslOperation[] operations() default {};

	/**
	 * An Enum class that implements OperationDefinition interface to define
	 * operations. Alternative to using operations() array.
	 * 
	 * The enum must implement OperationDefinition interface and provide getName(),
	 * getOperator(), and getType() methods.
	 * 
	 * Example:
	 * 
	 * <pre>
	 * public enum OrderOperations implements OperationDefinition {
	 * 	EQUAL_TO("equalTo", "eq", OperationType.WITH_ARG), NOT_EQUAL_TO("notEqualTo", "ne", OperationType.WITH_ARG);
	 * 
	 * 	private final String name;
	 * 	private final String operator;
	 * 	private final OperationType type;
	 * 
	 * 	OrderOperations(String name, String operator, OperationType type) {
	 * 		this.name = name;
	 * 		this.operator = operator;
	 * 		this.type = type;
	 * 	}
	 * 
	 * 	public String getName() {
	 * 		return name;
	 * 	}
	 * 
	 * 	public String getOperator() {
	 * 		return operator;
	 * 	}
	 * 
	 * 	public OperationType getType() {
	 * 		return type;
	 * 	}
	 * }
	 * </pre>
	 * 
	 * @return the Enum class implementing OperationDefinition, or void.class if not
	 *         used
	 */
	Class<? extends OperationDefinition> operationsEnum();

	/**
	 * The DTO class that extends dukono.minidsl.Dto.
	 * 
	 * If not specified (void.class), a DTO class will be generated automatically
	 * following the naming pattern: {DomainName}Dto
	 * 
	 * Example: For domain "Order", generates "OrderDto"
	 * 
	 * @return the DTO class, or void.class to auto-generate
	 */
	Class<?> dtoClass() default void.class;

}
