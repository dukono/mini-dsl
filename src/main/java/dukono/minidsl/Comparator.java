package dukono.minidsl;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Comparator {
	public static final Comparator INSTANCE = Comparator.builder().build();

	@Default
	public ComparatorEnum byOperation = ComparatorEnum.OP;

	@Default
	public ComparatorEnum byKey = ComparatorEnum.KEY;

	@Default
	public ComparatorEnum byKeyOperation = ComparatorEnum.KEY_OP;

	@Default
	public ComparatorEnum byKeyValue = ComparatorEnum.KEY_VALUE;

	@Default
	public ComparatorEnum byOperationValue = ComparatorEnum.OP_VALUE;

}
