package dukono.minidsl;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@SuperBuilder
@Getter(AccessLevel.PROTECTED)
@Setter(AccessLevel.PROTECTED)
@NoArgsConstructor
@AllArgsConstructor
public class AnchorOperationsBasic<H extends AnchorHolderMain<? extends Field, ? extends Dto, H, ? extends AnchorOperationsBasic<H>>> {

	protected String name;

	protected H holder;

	protected Optional<Object> listFormatting(final Collection<?> arg) {
		return Optional.ofNullable(arg).map(ar -> ar.stream().filter(Objects::nonNull).map(Object::toString).sorted()
				.collect(Collectors.joining("|")));
	}

	protected H newInstance(final Query addon) {
		this.holder.addQuery(addon);
		return this.holder;
	}
}
