package dukono.minidsl.example;

import java.util.Collection;
import java.util.function.Function;

import dukono.minidsl.AnchorHolderOne;
import dukono.minidsl.Operations;
import dukono.minidsl.Query;
import lombok.AllArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder(builderMethodName = "buildOne")
@AllArgsConstructor
public class AnchorOperationsOne<H extends AnchorHolderOne<?, ?, H, O, ?>, O> extends AnchorOperationsLogical<H> {

	@Override
	protected String getDelim() {
		return "|";
	}

	public H equalTo(final Function<O, Object> a) {
		return this.create(Query.from(this.getName(), Operations.EQ, a.apply(this.holder.getItemUnit())));
	}

	public H noNotInValues(final Function<O, Collection<?>> a) {
		return this.create(
				Query.from(this.getName(), Operations.NIN, this.listFormatting(a.apply(this.holder.getItemUnit()))));
	}

}
