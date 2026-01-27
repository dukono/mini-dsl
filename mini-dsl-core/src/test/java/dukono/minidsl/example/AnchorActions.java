package dukono.minidsl.example;

import com.google.common.reflect.TypeToken;
import dukono.minidsl.AnchorHolderMainActions;
import dukono.minidsl.Comparator;
import dukono.minidsl.ComparatorEnum;
import dukono.minidsl.DtoString;
import dukono.minidsl.OrderFieldConstants;
import dukono.minidsl.OrderOperationsEnum;
import dukono.minidsl.Query;
import dukono.minidsl.RemoveBy;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.function.UnaryOperator;

// @formatter:off
public class AnchorActions extends
			AnchorHolderMainActions<Fields, DtoString, AnchorActions, AnchorOperationsLogical<AnchorActions>> {
	public AnchorActions() {
		super(new TypeToken<DtoString>() {}, new TypeToken<AnchorList<?>>() {}, new TypeToken<AnchorOperationsLogical<AnchorActions>>() {}, new Fields());
	}

	public AnchorActions(final Collection<String> dtoData) {
		this();
		this.getDto().parseFilters(dtoData, OrderOperationsEnum.class,OrderFieldConstants.class);
	}
	public AnchorActions(final DtoString dto) {
		this();
		this.dtoClazzInstance = java.util.Optional.ofNullable(dto);
	}



	public <L> AnchorActions listCollapseAnd(final List<L> list, final UnaryOperator<AnchorOne<L>> operator) {
		return super.collapse(list, operator, Query.AND);
	}

	public <L> AnchorActions listCollapseOr(final List<L> list, final UnaryOperator<AnchorOne<L>> operator) {
		return super.collapse(list, operator, Query.OR);
	}

	public <L> AnchorActions listAddForEach(final List<L> list, final UnaryOperator<AnchorOne<L>> operator) {
		return super.notCollapse(list, operator);
	}

	public AnchorActions replace(final UnaryOperator<AnchorLogicalMain> toFind,
								 final UnaryOperator<AnchorLogicalMain> newValue) {
		return super.replace(toFind, newValue, new TypeToken<AnchorLogicalMain>() {});
	}

	public AnchorActions modify(final UnaryOperator<AnchorLogicalMain> change,
								final Function<Comparator, ComparatorEnum> comparatorFunction) {
		return super.modify(change, comparatorFunction, new TypeToken<AnchorLogicalMain>() {});
	}

	public AnchorActions remove(
			final Function<RemoveBy<AnchorLogicalMain>, RemoveBy.Remover> removeFunction) {
		return super.remove(removeFunction, new TypeToken<AnchorLogicalMain>() {});
	}


}
