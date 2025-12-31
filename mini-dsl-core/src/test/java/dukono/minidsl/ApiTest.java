package dukono.minidsl;

import java.util.List;

import dukono.minidsl.example.AnchorActions;
import dukono.minidsl.example.AnchorMain;
import dukono.minidsl.example.Item;
import org.junit.jupiter.api.Test;

class ApiTest {

	@Test
	void when_simple_operators_built_then_list_sorted() {
		final List<Item> build = List.of(Item.builder().model("modelFuture").year(2030).build(),
				Item.builder().model("modelPresent").year(2026).build(),
				Item.builder().model("modelPast").year(2010).build()

		);

		// @formatter:off
		final DtoString result = new AnchorMain()
				.field(a -> a.MARCA).equalTo(22).other()
				.field(a -> a.MARCA).equalTo(22)
				.getDto();
		
		result.getFilters();


		new AnchorActions()
				.listCollapseOr(build, anchorOne -> anchorOne.field(f -> f.YEAR).equalTo(Item::getYear)).other()
				.open().field(fields -> fields.MARCA).equalTo("BMW").close().other()
				.collapseOr()
				.getDto().filtersAsString();
		// @formatter:on

		result.getFilters();
	}

}
