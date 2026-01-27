package dukono.minidsl;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Comparator {
	public static final Comparator INSTANCE = new Comparator();

	public ComparatorEnum byOperation = ComparatorEnum.OP;

	public ComparatorEnum byKey = ComparatorEnum.KEY;

	public ComparatorEnum byKeyOperation = ComparatorEnum.KEY_OP;

	public ComparatorEnum byKeyValue = ComparatorEnum.KEY_VALUE;

	public ComparatorEnum byOperationValue = ComparatorEnum.OP_VALUE;

}
