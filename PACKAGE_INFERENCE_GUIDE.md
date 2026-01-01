# Automatic Package Inference

## Overview

When `packageName` is not specified in `@DslDomain`, the annotation processor automatically infers it from your field or operation definitions, making your annotations even more concise.

## Inference Priority

The processor uses the following priority to infer the package:

1. **fieldsConstants** - Uses the package of the constants class
2. **fieldsEnum** - Uses the package of the fields enum
3. **Annotated class** - Uses the package of the class with @DslDomain (fallback)

## Example: Before

```java
@DslDomain(
    name = "Order",
    packageName = "com.example.order",  // <-- Must specify explicitly
    fieldsConstants = OrderFieldConstants.class,
    operationsEnum = OrderOperationsEnum.class
)
public class OrderDomainDefinition {}
```

## Example: After (Auto-inferred)

```java
@DslDomain(
    name = "Order",
    // packageName NOT specified - automatically inferred from OrderFieldConstants!
    fieldsConstants = OrderFieldConstants.class,  // Package: com.example.order
    operationsEnum = OrderOperationsEnum.class
)
public class OrderDomainDefinition {}
```

**Result:** The generated classes will be placed in `com.example.order` package (same as `OrderFieldConstants`).

## How It Works

### Scenario 1: Using fieldsConstants

```java
// OrderFieldConstants.java is in package: com.example.constants
package com.example.constants;

public class OrderFieldConstants {
    public static final String ORDER_ID = "orderId";
    // ...
}

// OrderDomainDefinition.java
@DslDomain(
    name = "Order",
    fieldsConstants = OrderFieldConstants.class  // Infers: com.example.constants
)
```

**Generated classes will be in:** `com.example.constants`

### Scenario 2: Using fieldsEnum

```java
// OrderFields.java is in package: com.example.fields
package com.example.fields;

public enum OrderFields {
    ORDER_ID("orderId"),
    // ...
}

// OrderDomainDefinition.java
@DslDomain(
    name = "Order",
    fieldsEnum = OrderFields.class  // Infers: com.example.fields
)
```

**Generated classes will be in:** `com.example.fields`

### Scenario 3: Using fields[] array (fallback)

```java
// OrderDomainDefinition.java is in package: com.example.dsl
package com.example.dsl;

@DslDomain(
    name = "Order",
    fields = {
        @DslField(value = "orderId", javaName = "ORDER_ID")
    }
)
public class OrderDomainDefinition {}
```

**Generated classes will be in:** `com.example.dsl` (same as annotated class)

## Benefits

1. **Less Verbose** - No need to repeat package names
2. **DRY Principle** - Package is defined once in the constants/enum class
3. **Consistency** - Generated classes are in the same package as your field definitions
4. **Easier Refactoring** - Moving constants/enum automatically updates the package

## When to Specify packageName Explicitly

You may want to specify `packageName` explicitly when:

- Generated classes should be in a different package than your constants/enum
- You're using `fields[]` array but want a specific package
- You have complex package structure and need explicit control

## Example: Override Auto-inference

```java
@DslDomain(
    name = "Order",
    packageName = "com.example.generated",  // <-- Explicit override
    fieldsConstants = OrderFieldConstants.class  // Package: com.example.constants
)
```

**Result:** Generated classes will be in `com.example.generated` (not `com.example.constants`)

## Processor Messages

The processor logs which package it's using:

```
NOTE: Inferred package from fieldsConstants: com.example.constants
```

Or:

```
NOTE: Inferred package from fieldsEnum: com.example.fields
```

Or:

```
NOTE: Using annotated class package: com.example.dsl
```

## Complete Example

```java
// File: src/main/java/com/example/order/OrderFieldConstants.java
package com.example.order;

public class OrderFieldConstants {
    public static final String ORDER_ID = "orderId";
    public static final String CUSTOMER_NAME = "customerName";
}

// File: src/main/java/com/example/order/OrderOperationsEnum.java
package com.example.order;

public enum OrderOperationsEnum implements OperationDefinition {
    EQUAL_TO("equalTo", "eq", OperationType.WITH_ARG),
    // ...
}

// File: src/main/java/com/example/config/OrderDomainDefinition.java
package com.example.config;

@DslDomain(
    name = "Order",
    // NO packageName specified!
    dtoClass = "com.example.dto.OrderDto",
    fieldsConstants = OrderFieldConstants.class,  // Infers: com.example.order
    operationsEnum = OrderOperationsEnum.class
)
public class OrderDomainDefinition {}
```

**Generated files:**
- `com/example/order/OrderFields.java`
- `com/example/order/OrderAnchor.java`
- `com/example/order/OrderApi.java`
- ... (all in `com.example.order`)

## Summary

✅ **Automatic** - No need to specify `packageName` in most cases  
✅ **Smart** - Infers from fieldsConstants or fieldsEnum  
✅ **Flexible** - Can still override with explicit `packageName`  
✅ **Clean** - Makes @DslDomain annotations even more concise

