# Mini-DSL Examples

Esta carpeta contiene ejemplos organizados de cómo usar el Mini-DSL framework.

## Estructura de Ejemplos

### 📁 `prefixsuffix/` - Transformación de Nombres y Valores

Ejemplos de cómo usar prefijos y sufijos para transformar nombres de campos y sus valores:

- **ExampleFieldNamePrefix** - Eliminar prefijo de nombres de constantes (`FIELD_ORDER_ID` → `ORDER_ID`)
- **ExampleFieldNameSuffix** - Eliminar sufijo de nombres de constantes (`PRODUCT_ID_FIELD` → `PRODUCT_ID`)
- **ExampleFieldValuePrefix** - Eliminar prefijo de valores (`order.customerId` → `customerId`)
- **ExampleComplexPrefixSuffix** - Combinar múltiples transformaciones

**Propiedades disponibles:**
```java
@DslDomain(
    fieldNamePrefix = "FIELD_",     // Elimina prefijo del nombre Java
    fieldNameSuffix = "_FIELD",      // Elimina sufijo del nombre Java
    fieldValuePrefix = "order.",     // Elimina prefijo del valor
    fieldValueSuffix = ".value"      // Elimina sufijo del valor
)
```

### 📁 `fields/` - Definición de Campos

Ejemplos de las tres formas de definir campos (fields):

- **ExampleFieldsWithAnnotations** - Usando `@DslField` annotations
  ```java
  fields = {
      @DslField(javaName = "ORDER_ID", value = "orderId"),
      @DslField(javaName = "CUSTOMER_NAME", value = "customerName")
  }
  ```

- **ExampleFieldsWithEnum** - Usando un enum
  ```java
  fieldsEnum = ProductFields.class
  
  enum ProductFields {
      PRODUCT_ID("productId"),
      PRODUCT_NAME("productName")
  }
  ```

- **ExampleFieldsWithConstants** - Usando una clase de constantes
  ```java
  fieldsConstants = CustomerFields.class
  
  class CustomerFields {
      public static final String CUSTOMER_ID = "customerId";
      public static final String FIRST_NAME = "firstName";
  }
  ```

### 📁 `operations/` - Tipos de Operaciones

Ejemplos de los diferentes tipos de operaciones disponibles:

- **ExampleOperationsWithArg** - `OperationType.WITH_ARG`
  - Operaciones con un argumento: `userId eq "john123"`
  - Ejemplos: `equalTo`, `greaterThan`, `like`, `startsWith`

- **ExampleOperationsWithList** - `OperationType.WITH_LIST`
  - Operaciones con lista de valores: `status in ["ACTIVE", "PENDING"]`
  - Configuración de delimitadores y brackets personalizados
  - Ejemplos: `inValues`, `notInValues`, `between`

- **ExampleOperationsNoValue** - `OperationType.NO_VALUE`
  - Operaciones sin valor: `email is_not_null`
  - Ejemplos: `isNotNull`, `isNull`, `isEmpty`

- **ExampleOperationsNoOp** - `OperationType.NO_OP_NO_VALUE`
  - Sin operador ni valor: `userId` (solo el campo)
  - Útil para seleccionar campos sin filtros

- **ExampleOperationsMixed** - Combinación de todos los tipos
  - Ejemplo completo con múltiples tipos de operaciones

### 📁 `complete/` - Ejemplos Completos

Ejemplos de casos de uso reales que combinan múltiples features:

- **EcommerceCompleteExample** - Sistema de búsqueda de productos
  - Fields con enum
  - Múltiples tipos de operaciones
  - Prefijo en valores
  - Operaciones con listas personalizadas

- **UserFilterCompleteExample** - Sistema de filtrado de usuarios
  - Fields con anotaciones
  - Operaciones completas
  - Delimitadores personalizados para diferentes listas

## Configuración de Operaciones

### Tipos de Operaciones

```java
public enum OperationType {
    WITH_ARG,           // fieldName + operator + value
    WITH_LIST,          // fieldName + operator + [val1, val2, ...]
    NO_VALUE,           // fieldName + operator
    NO_OP_NO_VALUE,     // fieldName (sin operador ni valor)
    NO_OP_WITH_ARG,     // fieldName + value (sin operator)
    NO_OP_WITH_LIST     // fieldName + [val1, val2, ...] (sin operator)
}
```

### Definición de Operación

```java
public enum MyOperations implements OperationDefinition {
    EQUAL_TO("equalTo", "eq", OperationType.WITH_ARG, "Equal to"),
    IN_VALUES("inValues", "in", OperationType.WITH_LIST, ",", "[]", "In values")

    // Implementar métodos requeridos
}
```

### Listas Personalizadas

Para operaciones `WITH_LIST` y `NO_OP_WITH_LIST`:

```java
IN_VALUES("inValues", "in", OperationType.WITH_LIST, ",", "[]", "In list");
//                                                      ↑    ↑
//                                             delimiter  brackets

// Resultado: [value1,value2,value3]
```

Opciones de configuración:
- **listDelimiter**: `,` (coma), `|` (pipe), `;` (semicolon), ` ` (espacio)
- **listBrackets**: `[]`, `()`, `{}`, `<>`

## Uso de los Ejemplos

1. **Compila el proyecto:**
   ```bash
   mvn clean compile
   ```

2. **Las clases DSL generadas estarán en:**
   ```
   mini-dsl-example/target/generated-sources/annotations/
   ```

3. **Usa la API generada:**
   ```java
   // Ejemplo: ExampleFieldsWithEnum genera → ExampleFieldsWithEnumApi
   ExampleFieldsWithEnumApi api = ExampleFieldsWithEnumApi.main();
   
   api.PRODUCT_ID().equalTo("12345")
      .and()
      .CATEGORY().inValues(List.of("Electronics", "Gadgets"))
      .and()
      .PRICE().greaterThan("100");
   ```

## Convenciones de Nomenclatura

Cada ejemplo sigue el patrón:
- **Clase de configuración**: `Example[Feature][Type]`
- **API generada**: `Example[Feature][Type]Api`
- **Clases internas generadas**: Todas dentro de la API principal

Ejemplo:
```
ExampleFieldsWithEnum.java → ExampleFieldsWithEnumApi (contiene todas las clases DSL)
```

## Migración desde Versiones Antiguas

Si tienes ejemplos en la raíz del paquete `example/`:
- Los nuevos ejemplos están organizados en subpaquetes
- Los ejemplos antiguos pueden coexistir o moverse
- No hay cambios en la funcionalidad, solo en la organización

