package dukono.minidsl.util;

import dukono.minidsl.OrderFieldConstants;
import dukono.minidsl.Query;
import dukono.minidsl.annotation.DslOperation;
import dukono.minidsl.annotation.OperationDefinition;
import dukono.minidsl.annotation.OperationType;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for ParseConfigFactory utility.
 */
class ParseConfigFactoryTest {

	@Test
	void testFromOperationEnumParsing() {
		final Query.ParseConfig config = ParseConfigFactory.fromOperationEnumWithFields(TestOperationsEnum.class,
				OrderFieldConstants.class);

		// Usar campos válidos de OrderFieldConstants: orderId, customerName, status
		final String input = "orderId eq John and customerName gt 25 and status is_not_null";
		final List<Query> queries = Query.parseQueries(input, config);

		assertThat(queries).hasSize(7);
		assertThat(queries.get(0).getKey()).isEqualTo("orderId");
		assertThat(queries.get(0).getOp()).isEqualTo("eq");
		assertThat(queries.get(0).getValue().get()).isEqualTo("John");

		assertThat(queries.get(2).getKey()).isEqualTo("customerName");
		assertThat(queries.get(2).getOp()).isEqualTo("gt");

		assertThat(queries.get(4).getKey()).isEqualTo("status");
		assertThat(queries.get(4).getOp()).isEqualTo("is_not_null");
		assertThat(queries.get(4).getValue()).isEmpty();
	}
	@Test
	void testFromOperationEnumParsingWithList() {

		final Query.ParseConfig config = ParseConfigFactory.fromOperationEnumWithFields(TestOperationsEnum.class,
				OrderFieldConstants.class);

		// Test 1: Operador inválido "aa" -> No debe generar Query
		final String input1 = "orderId aa John";
		final List<Query> queries1 = Query.parseQueries(input1, config);
		assertThat(queries1).isEmpty(); // "aa" no es un operador válido, se ignora

		// Test 2: Operador válido "eq" -> Debe generar Query
		final String input2 = "orderId eq John";
		final List<Query> queries2 = Query.parseQueries(input2, config);
		assertThat(queries2).hasSize(1);
		assertThat(queries2.get(0).getKey()).isEqualTo("orderId");
		assertThat(queries2.get(0).getOp()).isEqualTo("eq");
		assertThat(queries2.get(0).getValue().get()).isEqualTo("John");

		// Test 3: Campo inválido -> No debe generar Query
		final String input3 = "invalidField eq John";
		final List<Query> queries3 = Query.parseQueries(input3, config);
		assertThat(queries3).isEmpty(); // "invalidField" no existe en OrderFieldConstants
	}

	@Test
	void testFromOperationEnumParsingWithValidFields() {
		final Query.ParseConfig config = ParseConfigFactory.fromOperationEnumWithFields(TestOperationsEnum.class,
				OrderFieldConstants.class);

		// Usar campos válidos de OrderFieldConstants
		final String input = "orderId eq 123 and customerName eq John and status is_not_null";
		final List<Query> queries = Query.parseQueries(input, config);

		assertThat(queries).hasSize(7);
		assertThat(queries.get(0).getKey()).isEqualTo("orderId");
		assertThat(queries.get(0).getOp()).isEqualTo("eq");
		assertThat(queries.get(0).getValue().get()).isEqualTo("123");

		assertThat(queries.get(2).getKey()).isEqualTo("customerName");
		assertThat(queries.get(2).getOp()).isEqualTo("eq");
		assertThat(queries.get(2).getValue().get()).isEqualTo("John");

		assertThat(queries.get(4).getKey()).isEqualTo("status");
		assertThat(queries.get(4).getOp()).isEqualTo("is_not_null");
		assertThat(queries.get(4).getValue()).isEmpty();
	}

	@Test
	void testFromOperationEnumParsingMixedValidInvalid() {
		final Query.ParseConfig config = ParseConfigFactory.fromOperationEnumWithFields(TestOperationsEnum.class,
				OrderFieldConstants.class);

		// Mezcla de campos válidos e inválidos
		final String input = "orderId eq 123 and invalidField ne 456 and status is_not_null";
		final List<Query> queries = Query.parseQueries(input, config);

		// Debería parsear solo orderId y status (campos válidos)
		// invalidField debe ser ignorado
		assertThat(queries).hasSize(5); // orderId eq 123, and, and, status is_not_null
		assertThat(queries.get(0).getKey()).isEqualTo("orderId");
		assertThat(queries.get(3).getKey()).isEqualTo("status");
	}

	@Test
	void testFromDslOperationsWithCustomLogical() {
		final DslOperation[] operations = {this.createDslOperation("equalTo", "eq", OperationType.WITH_ARG),
				this.createDslOperation("greaterThan", "gt", OperationType.WITH_ARG)};

		final Query.ParseConfig config = ParseConfigFactory.fromDslOperationsWithCustomLogical(operations,
				new HashSet<>(Arrays.asList("AND", "OR", "&&", "||")));

		assertThat(config.getLogicalOperators()).containsExactlyInAnyOrder("AND", "OR", "&&", "||");

		final String input = "name eq John AND age gt 25";
		final List<Query> queries = Query.parseQueries(input, config);

		assertThat(queries).hasSize(5);
		assertThat(queries.get(1).getOp()).isEqualTo("AND");
	}

	@Test
	void testCachingPerformance() {
		// Limpiar caché antes del test
		ParseConfigFactory.clearCache();
		assertThat(ParseConfigFactory.getCacheSize()).isZero();

		// Primera llamada - debe crear y cachear
		final long start1 = System.nanoTime();
		final Query.ParseConfig config1 = ParseConfigFactory.fromOperationEnumWithFields(TestOperationsEnum.class,
				OrderFieldConstants.class);
		final long time1 = System.nanoTime() - start1;

		assertThat(ParseConfigFactory.getCacheSize()).isEqualTo(1);

		// Segunda llamada - debe obtener del caché (mucho más rápido)
		final long start2 = System.nanoTime();
		final Query.ParseConfig config2 = ParseConfigFactory.fromOperationEnumWithFields(TestOperationsEnum.class,
				OrderFieldConstants.class);
		final long time2 = System.nanoTime() - start2;

		// Verificar que es la misma instancia (del caché)
		assertThat(config1).isSameAs(config2);

		// El tiempo del caché debería ser significativamente menor
		// (al menos 5x más rápido, pero normalmente es 100x+)
		assertThat(time2).isLessThan(time1 / 5);

		System.out.println("Primera llamada (con reflexión): " + time1 + " ns");
		System.out.println("Segunda llamada (desde caché): " + time2 + " ns");
		System.out.println("Speedup: " + (time1 / (double) time2) + "x");
	}

	@Test
	void testCacheWithDifferentClasses() {
		ParseConfigFactory.clearCache();

		// Cachear con OrderFieldConstants
		final Query.ParseConfig config1 = ParseConfigFactory.fromOperationEnumWithFields(TestOperationsEnum.class,
				OrderFieldConstants.class);

		assertThat(ParseConfigFactory.getCacheSize()).isEqualTo(1);

		// Cachear con null (sin fields)
		final Query.ParseConfig config2 = ParseConfigFactory.fromOperationEnumWithFields(TestOperationsEnum.class,
				null);

		assertThat(ParseConfigFactory.getCacheSize()).isEqualTo(2);

		// Verificar que son instancias diferentes
		assertThat(config1).isNotSameAs(config2);

		// Verificar que config1 tiene validFields y config2 no
		assertThat(config1.getValidFields()).isNotEmpty();
		assertThat(config2.getValidFields()).isNullOrEmpty();
	}

	// Helper method para crear DslOperation mock
	private DslOperation createDslOperation(final String name, final String operator, final OperationType type) {
		return new DslOperation() {
			@Override
			public String name() {
				return name;
			}

			@Override
			public String operator() {
				return operator;
			}

			@Override
			public OperationType type() {
				return type;
			}

			@Override
			public String description() {
				return "";
			}

			@Override
			public String listDelimiter() {
				return " ";
			}

			@Override
			public String listBrackets() {
				return "";
			}

			@Override
			public Class<? extends java.lang.annotation.Annotation> annotationType() {
				return DslOperation.class;
			}
		};
	}

	// Enum de prueba
	// @formatter:off
	enum TestOperationsEnum implements OperationDefinition {
		EQUAL_TO("equalTo", "eq", OperationType.WITH_ARG, "Equal", " ", ""),
		GREATER_THAN("greaterThan", "gt", OperationType.WITH_ARG, "Greater than", " ", ""),
		IN_VALUES("inValues", "in", OperationType.WITH_LIST, "In values", ",", "[]"),
		IS_NOT_NULL("isNotNull", "is_not_null", OperationType.NO_VALUE, "Is not null", " ", ""),
		NO_OP_VALS("noOpVals", null, OperationType.NO_OP_WITH_ARG, "No op with value", " ", ""),
		JUST_ADD("justAdd", null, OperationType.NO_OP_NO_VALUE, "Just add", " ", "");
		// @formatter:on
		private final String name;
		private final String operator;
		private final OperationType type;
		private final String description;
		private final String listDelimiter;
		private final String listBrackets;

		TestOperationsEnum(final String name, final String operator, final OperationType type, final String description,
				final String listDelimiter, final String listBrackets) {
			this.name = name;
			this.operator = operator;
			this.type = type;
			this.description = description;
			this.listDelimiter = listDelimiter;
			this.listBrackets = listBrackets;
		}

		@Override
		public String getName() {
			return this.name;
		}

		@Override
		public String getOperator() {
			return this.operator;
		}

		@Override
		public OperationType getType() {
			return this.type;
		}

		@Override
		public String getDescription() {
			return this.description;
		}

		@Override
		public String getListDelimiter() {
			return this.listDelimiter;
		}

		@Override
		public String getListBrackets() {
			return this.listBrackets;
		}
	}

}
