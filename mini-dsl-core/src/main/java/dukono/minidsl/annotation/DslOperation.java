package dukono.minidsl.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to define an operation in the DSL.
 * 
 * Example:
 * 
 * <pre>
 * {@code
 * &#64;DslOperation(name = "equalTo", operator = "eq")
 * &#64;DslOperation(name = "greaterThan", operator = "gt", hasValue = true)
 * &#64;DslOperation(name = "isNotNull", operator = "is_not_null", hasValue = false)
 * }
 * </pre>
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface DslOperation {

	/**
	 * Java method name for the operation. Example: "equalTo" ->
	 * .field(...).equalTo(value)
	 */
	String name();

	/**
	 * Operator string used in queries. Example: "eq" -> "MARCA eq Toyota" For
	 * JUST_ADD type, this can be empty string.
	 */
	String operator() default "";

	/**
	 * Type of operation defining the method signature pattern. - WITH_ARG:
	 * method(Object arg) - uses getName + operator + arg - WITH_LIST:
	 * method(Collection<Object> arg) - uses getName + operator + list - WITH_EMPTY:
	 * method() - uses getName + operator + empty - JUST_ADD: method() - uses
	 * getName + null + empty
	 */
	OperationType type() default OperationType.WITH_ARG;

	/**
	 * Description of the operation (for documentation).
	 */
	String description() default "";

	/**
	 * Delimiter to use when formatting list values. Only applicable for WITH_LIST
	 * and NO_OP_WITH_LIST types. Example: "$" -> "1$2$3" Default: " " (space)
	 */
	String listDelimiter() default " ";

	/**
	 * Brackets/parentheses to wrap list values. Format: "opening,closing" Only
	 * applicable for WITH_LIST and NO_OP_WITH_LIST types. Examples: - "[]" ->
	 * [1|2|3] - "()" -> (1|2|3) - "{}" -> {1|2|3} - "" -> 1|2|3 (no wrapping)
	 * Default: "" (no wrapping)
	 */
	String listBrackets() default "";
}
