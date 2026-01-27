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
// @formatter:off
public abstract class AnchorOperationsBasic<
		H extends AnchorHolderMain<? extends Field, ? extends Dto, H, ? extends AnchorOperationsBasic<H>>> {
	// @formatter:off
	protected String name;

	protected H holder;

	 protected String  getDelim(){
		return " ";
	 }
	
	protected Optional<Object> listFormatting(final Collection<?> arg) {
		return this.listFormatting(arg, this.getDelim(), "");
	}

	protected Optional<Object> listFormatting(final Collection<?> arg, final String delimiter, final String brackets) {
		return Optional.ofNullable(arg).map(ar -> {
			final String joined = ar.stream().filter(Objects::nonNull).map(Object::toString).sorted()
					.collect(Collectors.joining(delimiter));

			if (brackets == null || brackets.isEmpty()) {
				return joined;
			}

			// Parse brackets: first char is opening, last char is closing
			if (brackets.length() == 1) {
				return brackets + joined + brackets;
			} else if (brackets.length() >= 2) {
				return brackets.charAt(0) + joined + brackets.charAt(brackets.length() - 1);
			}

			return joined;
		});
	}

	protected H create(final Query addon) {
		this.holder.addQuery(addon);
		return this.holder;
	}
}
