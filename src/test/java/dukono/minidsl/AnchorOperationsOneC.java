package dukono.minidsl;

import java.util.Collection;
import java.util.function.Function;

import lombok.AllArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder(builderMethodName = "buildOne")
@AllArgsConstructor
public class AnchorOperationsOneC<H extends AnchorHolderOne<?, ?, H, O, ?>, O> extends AnchorOperationsLogicalC<H> {

	public H equalTo(final Function<O, Object> a) {
		return this.newInstance(Query.from(this.getName(), Operations.EQ, a.apply(this.holder.getItemUnit())));
	}

	public H notEqualTo(final Function<O, Object> a) {
		return this.newInstance(Query.from(this.getName(), Operations.NE, a.apply(this.holder.getItemUnit())));
	}

	public H greaterThan(final Function<O, Object> a) {
		return this.newInstance(Query.from(this.getName(), Operations.GT, a.apply(this.holder.getItemUnit())));
	}

	public H greaterThanOrEqual(final Function<O, Object> a) {
		return this.newInstance(Query.from(this.getName(), Operations.GTE, a.apply(this.holder.getItemUnit())));
	}

	public H lessThan(final Function<O, Object> a) {
		return this.newInstance(Query.from(this.getName(), Operations.LT, a.apply(this.holder.getItemUnit())));
	}

	public H lessThanOrEqual(final Function<O, Object> a) {
		return this.newInstance(Query.from(this.getName(), Operations.LTE, a.apply(this.holder.getItemUnit())));
	}

	public H inValues(final Function<O, Collection<?>> a) {
		return this.newInstance(
				Query.from(this.getName(), Operations.IN, this.listFormatting(a.apply(this.holder.getItemUnit()))));
	}

	public H noNotInValues(final Function<O, Collection<?>> a) {
		return this.newInstance(
				Query.from(this.getName(), Operations.NIN, this.listFormatting(a.apply(this.holder.getItemUnit()))));
	}

	public H like(final Function<O, Object> a) {
		return this.newInstance(Query.from(this.getName(), Operations.LIKE, a.apply(this.holder.getItemUnit())));
	}

	public H regexp(final Function<O, Object> a) {
		return this.newInstance(Query.from(this.getName(), Operations.REGEXP, a.apply(this.holder.getItemUnit())));
	}

	public H likeCaseInSensitive(final Function<O, Object> a) {
		return this.newInstance(Query.from(this.getName(), Operations.ILIKE, a.apply(this.holder.getItemUnit())));
	}

	public H notLike(final Function<O, Object> a) {
		return this.newInstance(Query.from(this.getName(), Operations.NOT_LIKE, a.apply(this.holder.getItemUnit())));
	}

}
