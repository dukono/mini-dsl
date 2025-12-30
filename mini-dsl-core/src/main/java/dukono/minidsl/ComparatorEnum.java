package dukono.minidsl;

import lombok.AllArgsConstructor;

import java.util.function.Function;
import java.util.function.Predicate;

@AllArgsConstructor
public enum ComparatorEnum {

	FULL(core -> toNew -> toNew.getKey().equalsIgnoreCase(core.getKey()) && toNew.getOp().equalsIgnoreCase(core.getOp())
			&& toNew.getValueAsString().equals(core.getValueAsString())), KEY(
					core -> toNew -> toNew.getKey().equalsIgnoreCase(core.getKey())), KEY_OP(
							core -> toNew -> toNew.getKey().equalsIgnoreCase(core.getKey())
									&& toNew.getOp().equalsIgnoreCase(core.getOp())), KEY_VALUE(
											core -> toNew -> toNew.getKey().equalsIgnoreCase(core.getKey())
													&& toNew.getValueAsString().equals(core.getValueAsString())), OP(
															core -> toNew -> toNew.getOp()
																	.equalsIgnoreCase(core.getOp())), OP_VALUE(
																			core -> toNew -> toNew.getOp()
																					.equalsIgnoreCase(core.getOp())
																					&& toNew.getValueAsString().equals(
																							core.getValueAsString()));

	private final Function<Query, Predicate<Query>> value;

	Function<Query, Predicate<Query>> getValue() {
		return this.value;
	}
}
