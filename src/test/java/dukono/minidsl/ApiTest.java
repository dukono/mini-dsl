package dukono.minidsl;

import dukono.minidsl.example.AnchorMain;
import org.junit.jupiter.api.Test;

class ApiTest {

	@Test
	void when_simple_operators_built_then_list_sorted() {
		// Api.from()
		// .listAddForEach(List.of("a", "b"), anchorOne -> anchorOne.field(fields ->
		// fields.YEAR).equalTo(s -> s))
		// .getDto().filtersAsString();

		new AnchorMain().field(a -> a.MARCA).equalTo(22).other().field(a -> a.MARCA).greaterThan(22).other();

		// new AnchorLogicalMain().open().field(am ->
		// am.MARCA).equalTo(22).and().field(am -> am.MARCA).greaterThan(22)
		// .other().field(fields -> fields.MARCA).greaterThan(22).other().close();
		//
		// Api.from().field(fields -> fields.MARCA).equalTo(22).other().field(fields ->
		// fields.YEAR).greaterThan(22)
		// .other()
		//
		// .listAddForEach(List.of("a"), anchorOne -> anchorOne.field(fields ->
		// fields.MARCA).equalTo(s -> s))
		// .modify(an -> an.field(fields -> fields.MARCA).equalTo(1), comparator ->
		// comparator.byKeyOperation);

	}

}
