package dukono.minidsl.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Represents a field in the DSL domain. This is used to define fields within
 * the @DslDomain annotation.
 * 
 * Example:
 * 
 * <pre>
 * {@code
 * &#64;DslField(value = "name", javaName = "NAME")
 * &#64;DslField(value = "age", javaName = "AGE")
 * &#64;DslField(value = "email", javaName = "EMAIL")
 * }
 * </pre>
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface DslField {

	/**
	 * The field value/identifier used in queries. Example: "name" -> "name eq
	 * 'John'"
	 */
	String value();

	/**
	 * The Java constant name for the field. Required. Example: "NAME" ->
	 * Fields.NAME
	 */
	String javaName();

	/**
	 * Description of the field (for documentation).
	 */
	String description() default "";
}
