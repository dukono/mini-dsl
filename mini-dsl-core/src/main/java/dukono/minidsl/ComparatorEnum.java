package dukono.minidsl;

import lombok.AllArgsConstructor;

import java.util.function.Function;
import java.util.function.Predicate;

@AllArgsConstructor
public enum ComparatorEnum {

	// @formatter:off
	FULL(core -> toNew -> compareNotNull(toNew.getKey(), core.getKey()) 
			&& compareNotNull(toNew.getOp(), core.getOp())
			&& compareNotNull(toNew.getValueAsString(), core.getValueAsString())),
	ANY(core -> toNew -> compareNotNull(toNew.getKey(), core.getKey()) 
			|| compareNotNull(toNew.getOp(), core.getOp())
			|| compareNotNull(toNew.getValueAsString(), core.getValueAsString())),
	KEY(core -> toNew -> compareNotNull(toNew.getKey(), core.getKey())),
	KEY_OP(core -> toNew -> compareNotNull(toNew.getKey(), core.getKey())
			&& compareNotNull(toNew.getOp(), core.getOp())),
	KEY_VALUE(core -> toNew -> compareNotNull(toNew.getKey(), core.getKey())
			&& compareNotNull(toNew.getValueAsString(), core.getValueAsString())), 
	OP(core -> toNew -> compareNotNull(toNew.getOp(), core.getOp())), 
	OP_VALUE(core -> toNew -> compareNotNull(toNew.getOp(), core.getOp())
			&& compareNotNull(toNew.getValueAsString(), core.getValueAsString()));
	// @formatter:on
	private final Function<Query, Predicate<Query>> value;

	Function<Query, Predicate<Query>> getValue() {
		return this.value;
	}
	private static boolean compare(final String toNewOp, final String coreOp) {
		if (toNewOp == null && coreOp == null) {
			return true;
		}
		if (toNewOp == null || coreOp == null) {
			return false;
		}
		return toNewOp.equalsIgnoreCase(coreOp);

	}
	private static boolean compareNotNull(final String toNewOp, final String coreOp) {

		if (toNewOp == null || coreOp == null) {
			return false;
		}
		return toNewOp.equalsIgnoreCase(coreOp);

	}
}
