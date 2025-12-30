package dukono.minidsl;

import com.google.common.reflect.TypeToken;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.function.UnaryOperator;

@SuppressWarnings("unchecked")
@Getter(AccessLevel.PROTECTED)
@Setter(AccessLevel.PROTECTED)
// @formatter:off
public abstract class AnchorHolderMainLogical<
    F extends Field,
    S extends Dto,
    X extends AnchorHolderMainLogical<F,S, X,W>,
    W extends AnchorOperationsBasic<X>> 
    extends AnchorHolderMain<F,S,X,W>{
  // @formatter:on

	protected AnchorHolderMainLogical() {
		super();
	}

	protected AnchorHolderMainLogical(final TypeToken<S> ob, final TypeToken<?> listClazz, final TypeToken<X> self,
			final TypeToken<W> opClazz, final F fields) {
		super(ob, listClazz, self, opClazz, fields);
	}

	public X and() {
		this.addQuery(Query.AND);
		return (X) this;
	}

	public X or() {
		this.addQuery(Query.OR);
		return (X) this;
	}

	public X open() {
		this.addQuery(Query.OPEN);
		return (X) this;
	}

	public X close() {
		this.addQuery(Query.CLOSE);
		return (X) this;
	}

	// @formatter:off
  public X collapseAnd() {
    // @formatter:on
		return this.collapse(Query.AND);
	}

	// @formatter:off
  
  public X collapseOr() {
    // @formatter:on

		return this.collapse(Query.OR);
	}

	public X collapse() {
		return this.collapse(null);
	}

	// -----------------------------

	// @formatter:off
  protected  <L, N extends AnchorHolderOne<F, S, N, L,?>,
      T extends AnchorHolderList<F, N, X, S, T, L>> X collapse(
      final List<L> list, final UnaryOperator<N> a, final Query query) {
    // @formatter:on
		final T holder = this.newlist();
		holder.setList(list);
		final S dtoBuilt = holder.addForEachCollapsing(a, query).getDto();
		return this.addDto(dtoBuilt);
	}

	// @formatter:off
  protected <L, N extends AnchorHolderOne<F, S, N, L,?>,
      T extends AnchorHolderList<F, N, X, S, T, L>> X notCollapse(
      final List<L> list, final UnaryOperator<N> a) {
    // @formatter:on
		final T holder = this.newlist();
		holder.setList(list);
		final S dtoBuilt = holder.addForEach(a).getDto();
		return this.addDto(dtoBuilt);
	}

	// @formatter:off
   <V extends AnchorHolderOne<F, S, V,Object, ?>, 
      T extends AnchorHolderList<F, V, X, S, T, Object>> X collapse(final Query val) {
    // @formatter:on
		final T newlist = this.newlist();
		newlist.setList(List.of());
		if (val == null) {
			newlist.collapse(this.getDto());
		} else {
			newlist.collapse(this.getDto(), val);
		}

		return (X) this;
	}

}
