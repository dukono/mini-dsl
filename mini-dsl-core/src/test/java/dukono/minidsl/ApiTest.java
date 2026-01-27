package dukono.minidsl;

import dukono.minidsl.example.Api;
import dukono.minidsl.example.Item;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * Este test está deshabilitado porque depende de clases generadas del módulo
 * mini-dsl-example. Para ejecutar tests que usen el DSL generado, ve a
 * mini-dsl-example/src/test/java/dukono/minidsl/example/Tests.java
 */
@Disabled("Este test requiere clases generadas del módulo mini-dsl-example")
class ApiTest {

	@Test
	void when_simple_operators_built_then_list_sorted() {

		// Ejemplo de cómo debería verse el código con las clases generadas:
		final List<Item> build = List.of(Item.builder().model("modelFuture").year(2030).build(),
				Item.builder().model("modelPresent").year(2026).build(),
				Item.builder().model("modelPast").year(2010).build());
		//@formatter:off
		Api.from().field(a -> a.MARCA).equalTo(22).other().field(a -> a.MARCA).equalTo(22);

		Api.from().listAddForEach(build, api -> api.field(f -> f.YEAR).equalTo(Item::getYear)).other()
				.field(fields -> fields.MARCA).inValues(build.stream().map(Item::getYear).toList())
				.getDto()
				.filtersAsString();
		//@formatter:on
	}

}
