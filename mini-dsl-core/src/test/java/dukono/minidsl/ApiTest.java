package dukono.minidsl;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * Este test está deshabilitado porque depende de clases generadas del módulo
 * mini-dsl-example. Para ejecutar tests que usen el DSL generado, ve a
 * mini-dsl-example/src/test/java/dukono/minidsl/example/Tests.java
 */
@Disabled("Este test requiere clases generadas del módulo mini-dsl-example")
class ApiTest {

	@Test
	void when_simple_operators_built_then_list_sorted() {
		// Este test fue movido a
		// mini-dsl-example/src/test/java/dukono/minidsl/example/Tests.java
		// Allí puedes usar OrderApi.from() con las clases generadas automáticamente

		// Ejemplo de cómo debería verse el código con las clases generadas:
		// final List<Item> build = List.of(
		// Item.builder().model("modelFuture").year(2030).build(),
		// Item.builder().model("modelPresent").year(2026).build(),
		// Item.builder().model("modelPast").year(2010).build()
		// );
		//
		// OrderApi.AnchorActions result = OrderApi.from()
		// .field(a -> a.ORDER_ID).equalTo(22).other()
		// .field(a -> a.ORDER_ID).equalTo(22);
		//
		// OrderApi.from()
		// .listCollapseOr(build, anchorOne -> anchorOne.field(f ->
		// f.YEAR).equalTo(Item::getYear))
		// .other()
		// .open().field(fields -> fields.ORDER_ID).equalTo("BMW").close().other()
		// .collapseOr()
		// .getDto().filtersAsString();
	}

}
