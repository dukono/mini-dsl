package dukono.minidsl.example;

import dukono.minidsl.DtoString;
import lombok.Getter;

import java.util.Set;

@Getter
public class Api {

	public static AnchorActions from(final Set<String> queries) {
		return new AnchorActions(queries);
	}

	public static AnchorActions from() {
		return new AnchorActions();
	}

	public static AnchorActions from(final DtoString dto) {
		return new AnchorActions(dto);
	}

}
