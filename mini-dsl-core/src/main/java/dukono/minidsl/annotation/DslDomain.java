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
	DslField[] fields();

	/**
	 * The operations available for the domain.
	 * 
	 * @return array of operation definitions
	 */
	DslOperation[] operations();

	/**
	 * The fully qualified name of the DTO class that this DSL will work with.
	 * 
	 * @return the DTO class name
	 */
	String dtoClass();

	/**
	 * Whether to generate logical operations (AND, OR, NOT).
	 * 
	 * @return true to include logical operations
	 */
	boolean withLogical() default true;

	/**
	 * Whether to generate action operations (add, remove, etc).
	 * 
	 * @return true to include action operations
	 */
	boolean withActions() default true;

	/**
	 * Whether to generate list operations.
	 * 
	 * @return true to include list operations
	 */
	boolean withList() default true;

	/**
	 * Whether to generate the high-level Api class.
	 * 
	 * @return true to generate Api class
	 */
	boolean generateApi() default true;
}
