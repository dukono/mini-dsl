package dukono.minidsl.util;

import dukono.minidsl.Query;
import dukono.minidsl.annotation.DslOperation;
import dukono.minidsl.annotation.OperationDefinition;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Utility class for creating Query.ParseConfig from DSL operation definitions.
 */
public class ParseConfigFactory {

	/**
	 * Cache para ParseConfig creados a partir de enum + fields class. Key:
	 * Combinación de operationsEnum.getName() + fieldsClass.getName() Value:
	 * ParseConfig configurado
	 */
	private static final java.util.Map<String, Query.ParseConfig> PARSE_CONFIG_CACHE = new java.util.concurrent.ConcurrentHashMap<>();

	/**
	 * Creates a ParseConfig from an array of DslOperation annotations.
	 * 
	 * @param operations
	 *            Array of DslOperation annotations
	 * @return ParseConfig configured with the operations
	 */
	public static Query.ParseConfig fromDslOperations(final DslOperation[] operations) {
		final Set<String> valueOps = new HashSet<>();
		final Set<String> noValueOps = new HashSet<>();

		for (final DslOperation op : operations) {
			if (op.operator() != null && !op.operator().isEmpty()) {
				switch (op.type()) {
					case WITH_ARG, WITH_LIST -> valueOps.add(op.operator());
					case NO_VALUE -> noValueOps.add(op.operator());
					case NO_OP_WITH_ARG, NO_OP_WITH_LIST, NO_OP_NO_VALUE -> {
						// No se agregan a ninguna lista
						// NO_OP_WITH_ARG: se detecta automáticamente cuando no hay operador
						// NO_OP_NO_VALUE: se detecta cuando no hay operador ni valor
					}
				}
			}
		}

		return Query.ParseConfig.builder().valueOperators(valueOps).noValueOperators(noValueOps)
				.logicalOperators(new HashSet<>(Arrays.asList("and", "or"))).allowUnknownOperators(false).build();

	}

	/**
	 * Creates a ParseConfig from an enum that implements OperationDefinition and
	 * Fields class.
	 * 
	 * Uses caching to avoid expensive reflection operations on repeated calls.
	 * 
	 * @param operationEnumClassEnum
	 *            class that implements OperationDefinition
	 * @param fieldsClass
	 *            Fields class containing valid field definitions
	 * @return ParseConfig configured with the operations from the enum and valid
	 *         fields
	 */

	public static Query.ParseConfig fromOperationEnumWithFields(
			final Class<? extends Enum<? extends OperationDefinition>> operationEnumClass, final Class<?> fieldsClass) {

		// Crear clave única para el caché
		final String cacheKey = operationEnumClass.getName() + "|"
				+ (fieldsClass != null ? fieldsClass.getName() : "null");

		// Intentar obtener del caché
		final Query.ParseConfig cached = PARSE_CONFIG_CACHE.get(cacheKey);
		if (cached != null) {
			return cached;
		}

		// Si no está en caché, crear nuevo ParseConfig
		final Set<String> valueOps = new HashSet<>();
		final Set<String> noValueOps = new HashSet<>();

		final Enum<? extends OperationDefinition>[] enumConstants = operationEnumClass.getEnumConstants();
		for (final Enum<? extends OperationDefinition> enumConstant : enumConstants) {
			final OperationDefinition op = (OperationDefinition) enumConstant;
			if (op.getOperator() != null && !op.getOperator().isEmpty()) {
				switch (op.getType()) {
					case WITH_ARG, WITH_LIST -> valueOps.add(op.getOperator());
					case NO_VALUE -> noValueOps.add(op.getOperator());
					case NO_OP_WITH_ARG, NO_OP_WITH_LIST, NO_OP_NO_VALUE -> {
						// No se agregan a ninguna lista
					}
				}
			}
		}

		// Extraer campos válidos de la clase Fields si se proporciona
		Set<String> validFields = null;
		if (fieldsClass != null) {
			validFields = extractValidFieldsFromClass(fieldsClass);
		}

		// Crear ParseConfig usando el builder de Lombok
		final Query.ParseConfig config = Query.ParseConfig.builder().valueOperators(valueOps)
				.noValueOperators(noValueOps).validFields(validFields)
				.logicalOperators(new HashSet<>(Arrays.asList("and", "or"))).allowUnknownOperators(false).build();

		// Guardar en caché
		PARSE_CONFIG_CACHE.put(cacheKey, config);

		return config;
	}

	/**
	 * Extracts valid field names from a Fields class by reading the values of
	 * Field.FieldHolder fields or public static final String constants.
	 * 
	 * @param fieldsClass
	 *            The Fields class
	 * @return Set of valid field names
	 */
	private static Set<String> extractValidFieldsFromClass(final Class<?> fieldsClass) {
		final Set<String> validFields = new HashSet<>();

		// Try to extract from Field.FieldHolder fields (generated Fields class)
		try {
			final Object fieldsInstance = fieldsClass.getDeclaredConstructor().newInstance();
			Arrays.stream(fieldsClass.getDeclaredFields())
					.filter(field -> field.getType().getName().equals("dukono.minidsl.Field$FieldHolder"))
					.forEach(field -> {
						try {
							field.setAccessible(true);
							final Object fieldHolder = field.get(fieldsInstance);
							if (fieldHolder != null) {
								// Obtener el nombre del campo desde FieldHolder
								final java.lang.reflect.Method getNameMethod = fieldHolder.getClass()
										.getMethod("getName");
								final String fieldName = (String) getNameMethod.invoke(fieldHolder);
								if (fieldName != null && !fieldName.isEmpty()) {
									validFields.add(fieldName);
								}
							}
						} catch (final Exception e) {
							// Ignorar campos que no se pueden leer
						}
					});

			// If we found FieldHolder fields, return
			if (!validFields.isEmpty()) {
				return validFields;
			}
		} catch (final Exception e) {
			// Continue to try extracting from String constants
		}

		// Try to extract from public static final String constants
		try {
			Arrays.stream(fieldsClass.getDeclaredFields())
					.filter(field -> java.lang.reflect.Modifier.isStatic(field.getModifiers())
							&& java.lang.reflect.Modifier.isFinal(field.getModifiers())
							&& java.lang.reflect.Modifier.isPublic(field.getModifiers())
							&& field.getType().equals(String.class))
					.forEach(field -> {
						try {
							final String fieldValue = (String) field.get(null);
							if (fieldValue != null && !fieldValue.isEmpty()) {
								validFields.add(fieldValue);
							}
						} catch (final Exception e) {
							// Ignorar campos que no se pueden leer
						}
					});
		} catch (final Exception e) {
			// Si no se puede leer, retornar set vacío (no validar)
		}

		return validFields;
	}

	/**
	 * Creates a ParseConfig with custom logical operators.
	 * 
	 * @param operations
	 *            Array of DslOperation annotations
	 * @param logicalOperators
	 *            Custom logical operators (e.g., "AND", "OR", "&&", "||")
	 * @return ParseConfig configured with the operations and custom logical
	 *         operators
	 */
	public static Query.ParseConfig fromDslOperationsWithCustomLogical(final DslOperation[] operations,
			final Set<String> logicalOperators) {
		final Set<String> valueOps = new HashSet<>();
		final Set<String> noValueOps = new HashSet<>();

		for (final DslOperation op : operations) {
			if (op.operator() != null && !op.operator().isEmpty()) {
				switch (op.type()) {
					case WITH_ARG, WITH_LIST -> valueOps.add(op.operator());
					case NO_VALUE -> noValueOps.add(op.operator());
					case NO_OP_WITH_ARG, NO_OP_WITH_LIST, NO_OP_NO_VALUE -> {
						// No se agregan a ninguna lista
					}
				}
			}
		}

		return Query.ParseConfig.builder().valueOperators(valueOps).noValueOperators(noValueOps)
				.logicalOperators(logicalOperators).allowUnknownOperators(false).build();
	}

	/**
	 * Limpia el caché de ParseConfig. Útil para testing o para liberar memoria.
	 */
	public static void clearCache() {
		PARSE_CONFIG_CACHE.clear();
	}

	/**
	 * Obtiene el tamaño actual del caché.
	 * 
	 * @return número de ParseConfig cacheados
	 */
	public static int getCacheSize() {
		return PARSE_CONFIG_CACHE.size();
	}
}
