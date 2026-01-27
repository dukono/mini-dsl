package dukono.minidsl.example;

import dukono.minidsl.example.generated.OrderApi;
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
		final List<String> strings = OrderApi.from()
				.field(fields -> fields.CREATED_DATE).noOpVals("aa")
				.listCollapseAnd(List.of("a"), orderAnchorOne -> 
					orderAnchorOne.field(orderFields -> orderFields.ORDER_ID).equalTo(String::toUpperCase))
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
		final List<String> filters = OrderApi.from()
				.listCollapseOr(items, anchorOne -> 
					anchorOne.field(f -> f.ORDER_ID).equalTo(OrderItem::getOrderId)).other()
				.open()
					.field(f -> f.CUSTOMER_NAME).equalTo("John Doe")
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
		final OrderApi.OrderDto dto = OrderApi.from()
				.listCollapseOr(this.items,
						api -> api.open().field(f -> f.ORDER_ID).equalTo(OrderItem::getOrderId).close()).other()
				.listAddForEach(this.items, api -> api.field(f -> f.TOTAL_AMOUNT).contains(OrderItem::getPrice)).other()
				.field(f -> f.STATUS).inValues(this.items.stream().map(OrderItem::getStatus).toList()).other()
				.field(f -> f.ITEMS).greaterThanOrEqual(this.items.size())
				.getDto();
		// ........
		OrderApi.from(dto)
				.replace(api -> api.field(f -> f.ITEMS).greaterThanOrEqual(3),
				api -> api.field(f -> f.CREATED_DATE).equalTo(LocalDate.now().toString()));

		OrderApi.from(dto)
				.modify(api -> api
						.field(f -> f.ORDER_ID).equalTo("WWW").other()
						.field(fields -> fields.TOTAL_AMOUNT).contains("as")
						.field(fields -> fields.STATUS).inValues(List.of(3,1)), comparator -> comparator.byKey);

		OrderApi.from(dto)
				.remove(remove-> remove.fullLine().byKeyValue(api -> api.field(f -> f.ORDER_ID).equalTo("WWW")));

		// @formatter:on
		final List<String> strings = dto.filtersAsString();
	}

	@Test
	void testParsers() {

		final List<String> strings = List.of("(orderId in John|Anna)  ", "(orderId eq John)");

		// @formatter:off

		final OrderApi.OrderDto dto = OrderApi.from(strings)
				.remove(removeBy -> removeBy.comparatorMatch().byKeyOperation(api -> api.field(fields -> fields.ORDER_ID).equalTo("aa"))).getDto();

		// @formatter:on
		dto.filtersAsString();

	}
}
