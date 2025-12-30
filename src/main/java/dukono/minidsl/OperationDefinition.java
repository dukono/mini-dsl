package dukono.minidsl;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum OperationDefinition {

	NONE("none", "NONE", OperationArgMode.NONE), LIST("list", "LIST", OperationArgMode.LIST), SINGLE("single", "SINGLE",
			OperationArgMode.SINGLE);

	String name;

	String opToken;

	OperationArgMode argMode;
}
