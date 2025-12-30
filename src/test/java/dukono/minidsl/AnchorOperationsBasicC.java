package dukono.minidsl;

import java.util.Collection;
import java.util.Optional;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@SuperBuilder(builderMethodName = "builderBasic")
@Getter(AccessLevel.PROTECTED)
@AllArgsConstructor
public class AnchorOperationsBasicC<H extends AnchorHolderMain<?, ?, H, ?>> extends AnchorOperationsBasic<H> {

	public H equalTo(final Object arg) {
		return this.newInstance(Query.from(this.getName(), Operations.EQ, arg));
	}

	public H notEqualTo(final Object arg) {
		return this.newInstance(Query.from(this.getName(), Operations.NE, arg));
	}

	public H greaterThan(final Object arg) {
		return this.newInstance(Query.from(this.getName(), Operations.GT, arg));
	}

	public H greaterThanOrEqual(final Object arg) {
		return this.newInstance(Query.from(this.getName(), Operations.GTE, arg));
	}

	public H lessThan(final Object arg) {
		return this.newInstance(Query.from(this.getName(), Operations.LT, arg));
	}

	public H lessThanOrEqual(final Object arg) {
		return this.newInstance(Query.from(this.getName(), Operations.LTE, arg));
	}

	public H inValues(final Collection<Object> arg) {
		return this.newInstance(Query.from(this.getName(), Operations.IN, this.listFormatting(arg)));
	}

	public H noNotInValues(final Collection<?> arg) {
		return this.newInstance(Query.from(this.getName(), Operations.NIN, this.listFormatting(arg)));
	}

	public H like(final Object arg) {
		return this.newInstance(Query.from(this.getName(), Operations.LIKE, arg));
	}

	public H likeCaseInSensitive(final Object arg) {
		return this.newInstance(Query.from(this.getName(), Operations.ILIKE, arg));
	}

	public H notLike(final Object arg) {
		return this.newInstance(Query.from(this.getName(), Operations.NOT_LIKE, arg));
	}

	public H regexp(final Object arg) {
		return this.newInstance(Query.from(this.getName(), Operations.REGEXP, arg));
	}

	public H isNotNull() {
		return this.newInstance(Query.from(this.getName(), Operations.IS_NOT_NULL, Optional.empty()));
	}

	public H justAdd() {
		return this.newInstance(Query.from(this.getName(), null, Optional.empty()));
	}

}
