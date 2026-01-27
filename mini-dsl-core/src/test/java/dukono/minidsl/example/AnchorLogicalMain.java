package dukono.minidsl.example;

import java.util.List;
import java.util.function.UnaryOperator;

import com.google.common.reflect.TypeToken;
import dukono.minidsl.AnchorHolderMainLogical;
import dukono.minidsl.DtoString;
import dukono.minidsl.Query;
// @formatter:off
public class AnchorLogicalMain extends
			AnchorHolderMainLogical<Fields, DtoString, AnchorLogicalMain, AnchorOperationsLogical<AnchorLogicalMain>> {
	// @formatter:on
	public AnchorLogicalMain() {
		super(new TypeToken<>() {
		}, new TypeToken<AnchorList<?>>() {
		}, new TypeToken<>() {
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
