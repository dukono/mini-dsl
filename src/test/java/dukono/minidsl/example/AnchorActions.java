package dukono.minidsl.example;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.function.UnaryOperator;

import com.google.common.reflect.TypeToken;
import dukono.minidsl.AnchorHolderMainActions;
import dukono.minidsl.Comparator;
import dukono.minidsl.ComparatorEnum;
import dukono.minidsl.DtoString;
import dukono.minidsl.Query;
import dukono.minidsl.RemoveBy;
import dukono.minidsl.RemoveBy.Remover;

public class AnchorActions
		extends
			AnchorHolderMainActions<Fields, DtoString, AnchorActions, AnchorOperationsLogical<AnchorActions>> {

	public AnchorActions() {
		super(new TypeToken<DtoString>() {
		}, new TypeToken<AnchorList<?>>() {
		}, new TypeToken<AnchorActions>() {
		}, new TypeToken<AnchorOperationsLogical<AnchorActions>>() {
		}, new Fields());
	}

	public AnchorActions(final DtoString dto) {
		this();
		this.dto = dto;
	}

	public AnchorActions(final Collection<String> dtoData) {
		this();
		this.getDto().parseFilters(dtoData);
	}

	public <L> AnchorActions listCollapseAnd(final List<L> list, final UnaryOperator<AnchorOne<L>> a) {
		return super.collapse(list, a, Query.AND);
	}

	public <L> AnchorActions listCollapseOr(final List<L> list, final UnaryOperator<AnchorOne<L>> a) {
		return super.collapse(list, a, Query.OR);
	}

	public <L> AnchorActions listAddForEach(final List<L> list, final UnaryOperator<AnchorOne<L>> a) {
		return super.notCollapse(list, a);
	}

	public AnchorActions replace(final UnaryOperator<AnchorLogicalMain> toFind,
			final UnaryOperator<AnchorLogicalMain> newValue) {
		return super.replace(toFind, newValue, new TypeToken<AnchorLogicalMain>() {
		});
	}

	public AnchorActions modify(final UnaryOperator<AnchorLogicalMain> change,
			final Function<Comparator, ComparatorEnum> b) {
		return super.modify(change, b, new TypeToken<AnchorLogicalMain>() {
		});
	}

	public AnchorActions remove(final Function<RemoveBy<AnchorMain, AnchorLogicalMain>, Remover> b) {
		return super.remove(b, new TypeToken<AnchorMain>() {
		}, new TypeToken<AnchorLogicalMain>() {
		});

	}

}
