package dukono.minidsl;

import lombok.Builder;
import lombok.Getter;

public class Field {

	public static FieldHolder from(final String name) {
		return FieldHolder.builder().name(name).build();
	}

	@Builder
	@Getter
	public static class FieldHolder {
		String name;
	}

}
