package dukono.minidsl.example.generated;

/**
 * Example of using @DslDomain annotation to generate DSL classes.
 * 
 * This single annotation will generate: - VehicleFields - VehicleOperations -
 * VehicleAnchorOperations - VehicleAnchor - VehicleApi
 * 
 * Usage example after generation:
 * 
 * <pre>
 * VehicleApi.from().field(f -> f.MARCA).equalTo("Toyota").and().field(f -> f.YEAR).greaterThan(2020).other().getDto();
 * </pre>
 */

public class VehicleDomainDefinition {
	// This class only holds the annotation
	// All implementation is generated at compile-time
}
