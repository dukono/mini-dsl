package dukono.minidsl.example;

import dukono.minidsl.AnchorHolderMain;
import dukono.minidsl.AnchorOperationsBasic;
import dukono.minidsl.Operations;
import dukono.minidsl.Query;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.Collection;
import java.util.Optional;

@SuperBuilder(builderMethodName = "builderBasic")
@Getter(AccessLevel.PROTECTED)
@AllArgsConstructor
// @formatter:off
public class AnchorOperationsBase<H extends AnchorHolderMain<?, ?, H, ?>> 
		extends AnchorOperationsBasic<H> {
	// @formatter:on
	@Override
	protected String getDelim() {
		return "|";
	}

	/**
	 * Example: WITH_ARG - Operation with operator and argument Pattern: getName() +
	 * operator + arg Result: "FIELD eq value"
	 */
	public H equalTo(final Object arg) {
		return this.create(Query.from(this.getName(), Operations.EQ, arg));
	}

	/**
	 * Example: NO_OP_WITH_ARG - Operation without operator but with argument
	 * Pattern: getName() + null + arg Result: "FIELD value"
	 */
	public H noOpVals(final Object arg) {
		return this.create(Query.from(this.getName(), null, arg));
	}

	/**
	 * Example: WITH_LIST - Operation with operator and list argument Pattern:
	 * getName() + operator + list Result: "FIELD in [value1,value2]"
	 */
	public H inValues(final Collection<?> arg) {
		return this.create(Query.from(this.getName(), Operations.IN, this.listFormatting(arg)));
	}

	/**
	 * Example: WITH_EMPTY - Operation with operator but no argument Pattern:
	 * getName() + operator + Optional.empty() Result: "FIELD is_not_null"
	 */
	public H isNotNull() {
		return this.create(Query.from(this.getName(), Operations.IS_NOT_NULL, Optional.empty()));
	}

	/**
	 * Example: JUST_ADD - Operation without operator and without argument Pattern:
	 * getName() + null + Optional.empty() Result: "FIELD"
	 */
	public H justAdd() {
		return this.create(Query.from(this.getName(), null, Optional.empty()));
	}

}
