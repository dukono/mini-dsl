package dukono.minidsl;

import dukono.minidsl.example.AnchorLogicalMain;
import dukono.minidsl.example.AnchorMain;
import org.junit.jupiter.api.Test;

class ApiTest {

	@Test
	void when_simple_operators_built_then_list_sorted() {
		// @formatter:off
		final DtoString result = new AnchorMain()
				.field(a -> a.MARCA).equalTo(22).other()
				.field(a -> a.MARCA).equalTo(22)
				.getDto();

		final DtoString result = new AnchorLogicalMain()
				.o
				.field(a -> a.MARCA).equalTo(22).other()
				.field(a -> a.MARCA).equalTo(22)
				.getDto();
		// @formatter:on

	}

}
