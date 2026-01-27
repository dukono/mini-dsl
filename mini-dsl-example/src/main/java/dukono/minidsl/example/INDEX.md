# Mini-DSL Examples - Índice General

## 📚 Resumen

Este directorio contiene ejemplos completos y organizados del framework Mini-DSL. Los ejemplos están categorizados por funcionalidad para facilitar el aprendizaje y la referencia.

## 📂 Estructura de Carpetas

```
example/
├── prefixsuffix/        → Transformación de nombres y valores
├── fields/              → Definición de campos (annotations, enum, constants)
├── operations/          → Tipos de operaciones y formatos
└── complete/            → Ejemplos completos de casos de uso reales
```

## 🎯 Guía Rápida por Caso de Uso

### Quiero definir campos (fields)...

| Necesidad | Ejemplo | Ubicación |
|-----------|---------|-----------|
| Usar anotaciones explícitas | `ExampleFieldsWithAnnotations` | `fields/` |
| Usar un enum existente | `ExampleFieldsWithEnum` | `fields/` |
| Usar constantes existentes | `ExampleFieldsWithConstants` | `fields/` |

### Quiero eliminar prefijos/sufijos...

| Necesidad | Ejemplo | Ubicación |
|-----------|---------|-----------|
| Eliminar prefijo de nombres | `ExampleFieldNamePrefix` | `prefixsuffix/` |
| Eliminar sufijo de nombres | `ExampleFieldNameSuffix` | `prefixsuffix/` |
| Eliminar prefijo de valores | `ExampleFieldValuePrefix` | `prefixsuffix/` |
| Combinación compleja | `ExampleComplexPrefixSuffix` | `prefixsuffix/` |

### Quiero usar operaciones específicas...

| Tipo de Operación | Ejemplo | Ubicación |
|-------------------|---------|-----------|
| Con argumento (eq, like, gt) | `ExampleOperationsWithArg` | `operations/` |
| Con lista ([1,2,3]) | `ExampleOperationsWithList` | `operations/` |
| Sin valor (is_not_null) | `ExampleOperationsNoValue` | `operations/` |
| Sin operador | `ExampleOperationsNoOp` | `operations/` |
| Formatos de lista personalizados | `ExampleListFormatting` | `operations/` |
| Todos los tipos mezclados | `ExampleOperationsMixed` | `operations/` |

### Quiero ver ejemplos completos...

| Caso de Uso | Ejemplo | Ubicación |
|-------------|---------|-----------|
| E-commerce (búsqueda productos) | `EcommerceCompleteExample` | `complete/` |
| Filtrado de usuarios | `UserFilterCompleteExample` | `complete/` |

## 🚀 Cómo Empezar

### 1. Elegir un ejemplo base

```bash
# Para principiantes, comenzar con:
fields/ExampleFieldsWithEnum.java

# Para casos avanzados:
complete/EcommerceCompleteExample.java
```

### 2. Compilar

```bash
mvn clean compile -pl mini-dsl-example -am
```

### 3. Ver las clases generadas

```
mini-dsl-example/target/generated-sources/annotations/dukono/minidsl/example/
```

### 4. Usar la API generada

```java
// Ejemplo con ExampleFieldsWithEnum
ExampleFieldsWithEnumApi api = ExampleFieldsWithEnumApi.main();

api.PRODUCT_ID().equalTo("12345")
   .and()
   .CATEGORY().inValues(List.of("Electronics", "Gadgets"));
```

## 📖 Guías Detalladas

- **[README.md](README.md)** - Documentación detallada de todas las funcionalidades
- **Cada paquete** contiene ejemplos auto-documentados con comentarios explicativos

## 🔑 Conceptos Clave

### Tipos de Operaciones

```java
OperationType.WITH_ARG          // field eq "value"
OperationType.WITH_LIST         // field in [1, 2, 3]
OperationType.NO_VALUE          // field is_not_null
OperationType.NO_OP_NO_VALUE    // field
OperationType.NO_OP_WITH_ARG    // field "value"
OperationType.NO_OP_WITH_LIST   // field [1, 2, 3]
```

### Formas de Definir Fields

```java
// 1. Anotaciones
@DslField(javaName = "USER_ID", value = "userId")

// 2. Enum
enum Fields {
    USER_ID("userId")
}

// 3. Constantes
class Fields {
    public static final String USER_ID = "userId";
}
```

### Transformaciones

```java
@DslDomain(
    fieldNamePrefix = "FIELD_",      // FIELD_ID → ID
    fieldNameSuffix = "_COL",        // ID_COL → ID
    fieldValuePrefix = "user.",      // user.id → id
    fieldValueSuffix = ".value"      // id.value → id
)
```

## 💡 Tips

1. **Comenzar simple**: Usa `ExampleFieldsWithEnum` para entender lo básico
2. **Iteración rápida**: Compila solo el módulo example: `mvn compile -pl mini-dsl-example`
3. **Inspeccionar generados**: Revisa las clases generadas para entender la API
4. **Reutilizar constantes**: Si ya tienes enums/constantes, úsalos con `fieldsEnum`/`fieldsConstants`
5. **Formatos personalizados**: Usa `listDelimiter` y `listBrackets` para controlar el formato de salida

## 🐛 Troubleshooting

### Las clases no se generan
```bash
# Limpiar y recompilar
mvn clean compile -pl mini-dsl-example -am
```

### No encuentro las clases generadas
```bash
# Buscar en:
mini-dsl-example/target/generated-sources/annotations/
```

### Errores de compilación con IntelliJ
```bash
# Compilar con Maven primero
mvn clean compile

# Luego: File → Invalidate Caches → Invalidate and Restart
```

## 📝 Convenciones

- **Clases de configuración**: `Example[Feature][Type].java`
- **APIs generadas**: `Example[Feature][Type]Api`
- **Todas las clases internas**: Se generan dentro de la API principal
- **Paquete de generados**: `dukono.minidsl.example.generated.[nombre]`

## 🎓 Aprendizaje Progresivo

### Nivel 1: Básico
1. `ExampleFieldsWithEnum` - Entender fields y operaciones básicas
2. `ExampleOperationsWithArg` - Operaciones con argumentos

### Nivel 2: Intermedio
3. `ExampleFieldsWithConstants` - Reutilizar constantes existentes
4. `ExampleFieldNamePrefix` - Transformaciones simples
5. `ExampleOperationsWithList` - Operaciones con listas

### Nivel 3: Avanzado
6. `ExampleComplexPrefixSuffix` - Múltiples transformaciones
7. `ExampleListFormatting` - Formatos personalizados
8. `ExampleOperationsMixed` - Múltiples tipos de operaciones

### Nivel 4: Producción
9. `EcommerceCompleteExample` - Caso completo de e-commerce
10. `UserFilterCompleteExample` - Caso completo de filtrado

## 📬 Contribuir

Para añadir nuevos ejemplos:
1. Crear la clase en el paquete apropiado
2. Documentar con JavaDoc explicativo
3. Seguir las convenciones de nomenclatura
4. Actualizar este índice

---

**Versión**: 1.0  
**Última actualización**: Enero 2026

