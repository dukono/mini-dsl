package dukono.minidsl.example;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderItem {
	private String orderId;
	private String customerName;
	private Integer quantity;
	private Double price;
	private Integer status;
}
