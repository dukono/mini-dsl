package dukono.minidsl.example;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Item {
	Integer year;
	String model;
}
