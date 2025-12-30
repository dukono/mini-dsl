package dukono.minidsl;

import com.google.common.reflect.TypeToken;

import java.util.List;
import java.util.function.UnaryOperator;

public class AnchorLogicalMain
		extends
			AnchorHolderMainLogical<Fields, DtoString, AnchorLogicalMain, AnchorOperationsLogicalC<AnchorLogicalMain>> {

	public AnchorLogicalMain() {
		super(new TypeToken<DtoString>() {
		}, new TypeToken<AnchorList<?>>() {
		}, new TypeToken<AnchorLogicalMain>() {
		}, new TypeToken<AnchorOperationsLogicalC<AnchorLogicalMain>>() {
		}, new Fields());
	}

	public <L> AnchorLogicalMain listCollapseAnd(final List<L> list, final UnaryOperator<AnchorOne<L>> a) {
		return super.collapse(list, a, Query.AND);
	}

	public <L> AnchorLogicalMain listCollapseOr(final List<L> list, final UnaryOperator<AnchorOne<L>> a) {
		return super.collapse(list, a, Query.OR);
	}

	public <L> AnchorLogicalMain listAddForEach(final List<L> list, final UnaryOperator<AnchorOne<L>> a) {
		return super.notCollapse(list, a);
	}

}
