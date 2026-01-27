package dukono.minidsl.example;

import dukono.minidsl.example.complete.EcommerceProductSearchApi;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class Tests {
	final List<OrderItem> items = List.of(
			OrderItem.builder().orderId("ORD-002").customerName("Jane Smith").quantity(3).price(149.99).status(2).build(),
			OrderItem.builder().orderId("ORD-001").customerName("John Doe").quantity(5).price(99.99).status(1).build(),
			OrderItem.builder().orderId("ORD-003").customerName("Bob Johnson").quantity(10).price(79.99).status(1)
					.build());

	@Test
	void testBasicDslOperations() {
		// @formatter:off
		final List<String> strings = EcommerceProductSearchApi.from()
				.field(fields -> fields.CREATED_DATE).contains("aa")
				.listCollapseAnd(List.of("a"), orderAnchorOne -> 
					orderAnchorOne.field(orderFields -> orderFields.CATEGORY).equalTo(String::toUpperCase))
				.getDto().filtersAsString();
		
		assertThat(strings).hasSize(1);
		// @formatter:on
	}

	@Test
	void testDslWithLombokGeneratedClasses() {
		// Demuestra que Lombok funciona correctamente con el DSL
		// Lombok genera el método builder() automáticamente
		final List<OrderItem> items = List.of(
				OrderItem.builder().orderId("ORD-001").customerName("John Doe").quantity(5).price(99.99).build(),
				OrderItem.builder().orderId("ORD-002").customerName("Jane Smith").quantity(3).price(149.99).build(),
				OrderItem.builder().orderId("ORD-003").customerName("Bob Johnson").quantity(10).price(79.99).build());

		// @formatter:off
		final List<String> filters = EcommerceProductSearchApi.from()
				.listCollapseOr(items, anchorOne -> 
					anchorOne.field(f -> f.ACTIVE).equalTo(OrderItem::getOrderId)).other()
				.open()
					.field(f -> f.CREATED_DATE).equalTo("John Doe")
				.close()
				.getDto().filtersAsString();
		// @formatter:on

		assertThat(filters).isNotEmpty();
		assertThat(items).allMatch(item -> item.getOrderId() != null).extracting(OrderItem::getCustomerName)
				.contains("John Doe", "Jane Smith", "Bob Johnson");
	}

	@Test
	void testComplexDslWithMultipleOperations() {

		// @formatter:off
		final EcommerceProductSearchApi.EcommerceProductSearchDto dto = EcommerceProductSearchApi.from()
				.listCollapseOr(this.items,
						api -> api.open().field(f -> f.CREATED_DATE).equalTo(OrderItem::getOrderId).close()).other()
				.listAddForEach(this.items, api -> api.field(f -> f.CREATED_DATE).contains(OrderItem::getPrice)).other()
				.field(f -> f.CREATED_DATE).between(this.items.stream().map(OrderItem::getStatus).toList()).other()
				.field(f -> f.CREATED_DATE).greaterThan(this.items.size())
				.getDto();
		// ........
		EcommerceProductSearchApi.from(dto)
				.replace(api -> api.field(f -> f.ACTIVE).greaterThan(3),
				api -> api.field(f -> f.CREATED_DATE).equalTo(LocalDate.now().toString()));

		EcommerceProductSearchApi.from(dto)
				.modify(api -> api
						.field(f -> f.BRAND).equalTo("WWW").other()
						.field(fields -> fields.DESCRIPTION).contains("as")
						.field(fields -> fields.PRICE).hasAnyTag(List.of(3,1)), comparator -> comparator.byKey);

		EcommerceProductSearchApi.from(dto)
				.remove(remove-> remove.fullLine().byKeyValue(api -> api.field(f -> f.DESCRIPTION).equalTo("WWW")));

		// @formatter:on
		final List<String> strings = dto.filtersAsString();
	}

	@Test
	void testParsers() {

		final List<String> strings = List.of("(orderId in John|Anna)  ", "(orderId eq John)");

		// @formatter:off

		final EcommerceProductSearchApi.EcommerceProductSearchDto dto = EcommerceProductSearchApi.from(strings)
				.remove(removeBy -> removeBy.comparatorMatch().byKeyOperation(api -> api.field(fields -> fields.BRAND).equalTo("aa"))).getDto();

		// @formatter:on
		dto.filtersAsString();

	}
}
