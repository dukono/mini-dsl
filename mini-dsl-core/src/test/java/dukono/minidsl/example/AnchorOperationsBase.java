package dukono.minidsl.example;

import java.util.Collection;
import java.util.Optional;

import dukono.minidsl.AnchorHolderMain;
import dukono.minidsl.AnchorOperationsBasic;
import dukono.minidsl.Operations;
import dukono.minidsl.Query;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@SuperBuilder(builderMethodName = "builderBasic")
@Getter(AccessLevel.PROTECTED)
@AllArgsConstructor
// @formatter:off
public class AnchorOperationsBase<H extends AnchorHolderMain<?, ?, H, ?>> 
		extends AnchorOperationsBasic<H> {
	// @formatter:on
	public H equalTo(final Object arg) {
		return this.create(Query.from(this.getName(), Operations.EQ, arg));
	}

	public H inValues(final Collection<Object> arg) {
		return this.create(Query.from(this.getName(), Operations.IN, this.listFormatting(arg)));
	}

	public H isNotNull() {
		return this.create(Query.from(this.getName(), Operations.IS_NOT_NULL, Optional.empty()));
	}

	public H justAdd() {
		return this.create(Query.from(this.getName(), null, Optional.empty()));
	}

}
