package dukono.minidsl;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for parsing queries with custom operators.
 */
class QueryParseCustomOperatorsTest {

	@Test
	void testParseWithCustomOperators() {

		final Query.ParseConfig config = Query.ParseConfig.builder()
				.valueOperators(new HashSet<>(Arrays.asList("equalTo", "greaterThan", "lessThan", "like")))
				.noValueOperators(new HashSet<>(Arrays.asList("isNotNull", "isEmpty")))
				.logicalOperators(new HashSet<>(Arrays.asList("and", "or"))).allowUnknownOperators(false).build();

		final String input = "name equalTo John and age greaterThan 25";
		final List<Query> queries = Query.parseQueries(input, config);

		assertThat(queries).hasSize(5);
		assertThat(queries.get(0).getKey()).isEqualTo("name");
		assertThat(queries.get(0).getOp()).isEqualTo("equalTo");
		assertThat(queries.get(0).getValue().get()).isEqualTo("John");

		assertThat(queries.get(1).getOp()).isEqualTo("and");

		assertThat(queries.get(2).getKey()).isEqualTo("age");
		assertThat(queries.get(2).getOp()).isEqualTo("greaterThan");
		assertThat(queries.get(2).getValue().get()).isEqualTo("25");
	}

	@Test
	void testParseWithCustomNoValueOperators() {
		final Query.ParseConfig config = Query.ParseConfig.builder().valueOperators(new HashSet<>(List.of("eq")))
				.noValueOperators(new HashSet<>(Arrays.asList("isNotNull", "isEmpty", "isActive")))
				.logicalOperators(new HashSet<>(Arrays.asList("and", "or"))).allowUnknownOperators(false).build();

		final String input = "email isNotNull and status isActive";
		final List<Query> queries = Query.parseQueries(input, config);

		assertThat(queries).hasSize(5);
		assertThat(queries.get(0).getKey()).isEqualTo("email");
		assertThat(queries.get(0).getOp()).isEqualTo("isNotNull");
		assertThat(queries.get(0).getValue()).isEmpty();

		assertThat(queries.get(1).getOp()).isEqualTo("and");

		assertThat(queries.get(2).getKey()).isEqualTo("status");
		assertThat(queries.get(2).getOp()).isEqualTo("isActive");
		assertThat(queries.get(2).getValue()).isEmpty();
	}

	@Test
	void testParseWithAllowUnknownOperators() {
		final Query.ParseConfig config = Query.ParseConfig.builder().valueOperators(new HashSet<>(List.of("eq")))
				.noValueOperators(new HashSet<>()).logicalOperators(new HashSet<>(Arrays.asList("and", "or")))
				.allowUnknownOperators(true) // Permitir cualquier operador
				.build();

		final String input = "name customOp value1 and age unknownOp 25";
		final List<Query> queries = Query.parseQueries(input, config);

		assertThat(queries).hasSize(5);
		assertThat(queries.get(0).getKey()).isEqualTo("name");
		assertThat(queries.get(0).getOp()).isEqualTo("customOp");
		assertThat(queries.get(0).getValue().get()).isEqualTo("value1");

		assertThat(queries.get(1).getOp()).isEqualTo("and");

		assertThat(queries.get(2).getKey()).isEqualTo("age");
		assertThat(queries.get(2).getOp()).isEqualTo("unknownOp");
		assertThat(queries.get(2).getValue().get()).isEqualTo("25");
	}

	@Test
	void testParseNoOpWithArg() {
		final Query.ParseConfig config = Query.ParseConfig.builder().valueOperators(new HashSet<>(List.of("eq")))
				.noValueOperators(new HashSet<>()).logicalOperators(new HashSet<>(Arrays.asList("and", "or")))
				.allowUnknownOperators(false) // No reconocer operadores desconocidos
				.build();

		// Cuando no se reconoce el operador, se trata como NO_OP_WITH_ARG
		final String input = "name John and age 25";
		final List<Query> queries = Query.parseQueries(input, config);

		assertThat(queries).hasSize(5);
		// NO_OP_WITH_ARG: key="name", op=null, value="John"
		assertThat(queries.get(0).getKey()).isEqualTo("name");
		assertThat(queries.get(0).getOp()).isNull();
		assertThat(queries.get(0).getValue().get()).isEqualTo("John");

		assertThat(queries.get(1).getOp()).isEqualTo("and");

		// NO_OP_WITH_ARG: key="age", op=null, value="25"
		assertThat(queries.get(2).getKey()).isEqualTo("age");
		assertThat(queries.get(2).getOp()).isNull();
		assertThat(queries.get(2).getValue().get()).isEqualTo("25");
	}

	@Test
	void testParseJustAdd() {
		final Query.ParseConfig config = Query.ParseConfig.builder().valueOperators(new HashSet<>(List.of("eq")))
				.noValueOperators(new HashSet<>()).logicalOperators(new HashSet<>(Arrays.asList("and", "or")))
				.allowUnknownOperators(false).build();

		// JUST_ADD: solo el campo sin operador ni valor
		final String input = "name and email or phone";
		final List<Query> queries = Query.parseQueries(input, config);

		assertThat(queries).hasSize(5);
		// JUST_ADD: key="name", op=null, value=empty
		assertThat(queries.get(0).getKey()).isEqualTo("name");
		assertThat(queries.get(0).getOp()).isNull();
		assertThat(queries.get(0).getValue()).isEmpty();

		assertThat(queries.get(1).getOp()).isEqualTo("and");

		assertThat(queries.get(2).getKey()).isEqualTo("email");
		assertThat(queries.get(2).getOp()).isNull();
		assertThat(queries.get(2).getValue()).isEmpty();

		assertThat(queries.get(3).getOp()).isEqualTo("or");

		assertThat(queries.get(4).getKey()).isEqualTo("phone");
		assertThat(queries.get(4).getOp()).isNull();
		assertThat(queries.get(4).getValue()).isEmpty();
	}

	@Test
	void testParseWithParentheses() {
		final Query.ParseConfig config = Query.ParseConfig.builder()
				.valueOperators(new HashSet<>(Arrays.asList("eq", "gt"))).noValueOperators(new HashSet<>())
				.logicalOperators(new HashSet<>(Arrays.asList("and", "or"))).allowUnknownOperators(false).build();

		final String input = "( name eq John or age gt 25 ) and status eq active";
		final List<Query> queries = Query.parseQueries(input, config);

		assertThat(queries).hasSize(11);
		assertThat(queries.get(0).getOp()).isEqualTo("(");
		assertThat(queries.get(1).getKey()).isEqualTo("name");
		assertThat(queries.get(1).getOp()).isEqualTo("eq");
		assertThat(queries.get(2).getOp()).isEqualTo("or");
		assertThat(queries.get(3).getKey()).isEqualTo("age");
		assertThat(queries.get(3).getOp()).isEqualTo("gt");
		assertThat(queries.get(4).getOp()).isEqualTo(")");
		assertThat(queries.get(5).getOp()).isEqualTo("and");
		assertThat(queries.get(6).getKey()).isEqualTo("status");
		assertThat(queries.get(6).getOp()).isEqualTo("eq");
	}

	@Test
	void testParseComplexQuery() {
		final Query.ParseConfig config = Query.ParseConfig.builder()
				.valueOperators(new HashSet<>(Arrays.asList("eq", "ne", "gt", "lt", "like", "in")))
				.noValueOperators(new HashSet<>(Arrays.asList("isNotNull", "isEmpty")))
				.logicalOperators(new HashSet<>(Arrays.asList("and", "or"))).allowUnknownOperators(false).build();

		final String input = "name eq John and ( age gt 18 or status like active ) and email isNotNull";
		final List<Query> queries = Query.parseQueries(input, config);

		assertThat(queries).hasSize(13);
		assertThat(queries.get(0).getKey()).isEqualTo("name");
		assertThat(queries.get(0).getOp()).isEqualTo("eq");
		assertThat(queries.get(0).getValue().get()).isEqualTo("John");
	}
}
