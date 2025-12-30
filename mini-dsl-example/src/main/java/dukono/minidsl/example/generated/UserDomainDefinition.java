package dukono.minidsl.example.generated;

import dukono.minidsl.annotation.DslDomain;
import dukono.minidsl.annotation.DslField;
import dukono.minidsl.annotation.DslOperation;
import dukono.minidsl.annotation.OperationType;

/**
 * Example of using @DslDomain annotation with custom operations.
 * 
 * This example demonstrates the new operation system where you can define
 * custom operations with different types:
 * 
 * - WITH_ARG: method(Object arg) - uses getName + operation + arg - WITH_LIST:
 * method(Collection<Object> arg) - uses getName + operation + list -
 * WITH_EMPTY: method() - uses getName + operation + empty - JUST_ADD: method()
 * - uses getName + null + empty
 * 
 * This single annotation will generate: - UserFields - UserOperations -
 * UserAnchorOperations (with custom operations) - UserAnchor - UserApi
 * 
 * Usage example after generation:
 * 
 * <pre>
 * UserApi.from().field(f -> f.NAME).equalTo("John").and().field(f -> f.AGE).greaterThan(18).and().field(f -> f.ROLE)
 * 		.inValues(List.of("ADMIN", "USER")).and().field(f -> f.EMAIL).isNotNull().other().getDto();
 * </pre>
 */
@DslDomain(name = "User", packageName = "dukono.minidsl.example.generated.user", dtoClass = "dukono.minidsl.example.dto.UserDto", fields = {
		@DslField(value = "name", type = "String"), @DslField(value = "age", type = "int"),
		@DslField(value = "email", type = "String"), @DslField(value = "role", type = "String")}, operations = {
				// WITH_ARG operations - method(Object arg)
				@DslOperation(name = "equalTo", operator = "eq", type = OperationType.WITH_ARG, description = "Checks if field equals the given value"),
				@DslOperation(name = "notEqualTo", operator = "ne", type = OperationType.WITH_ARG, description = "Checks if field is not equal to the given value"),
				@DslOperation(name = "greaterThan", operator = "gt", type = OperationType.WITH_ARG, description = "Checks if field is greater than the given value"),
				@DslOperation(name = "lessThan", operator = "lt", type = OperationType.WITH_ARG, description = "Checks if field is less than the given value"),
				@DslOperation(name = "like", operator = "like", type = OperationType.WITH_ARG, description = "Checks if field matches the given pattern"),

				// WITH_LIST operations - method(Collection<Object> arg)
				@DslOperation(name = "inValues", operator = "in", type = OperationType.WITH_LIST, description = "Checks if field is in the given list of values"),
				@DslOperation(name = "notInValues", operator = "nin", type = OperationType.WITH_LIST, description = "Checks if field is not in the given list of values"),

				// WITH_EMPTY operations - method()
				@DslOperation(name = "isNotNull", operator = "is_not_null", type = OperationType.WITH_EMPTY, description = "Checks if field is not null"),
				@DslOperation(name = "isNull", operator = "is_null", type = OperationType.WITH_EMPTY, description = "Checks if field is null"),

				// JUST_ADD operations - method() with no operator
				@DslOperation(name = "justAdd", type = OperationType.JUST_ADD, description = "Adds the field without any operation")})
public class UserDomainDefinition {
	// This class only holds the annotation
	// All implementation is generated at compile-time
}
