package dukono.minidsl;

import lombok.Data;

@Data
public class OperationSpec {
	String name;

	String opToken;

	OperationArgMode argMode;
}
