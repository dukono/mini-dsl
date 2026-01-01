package dukono.minidsl;

import java.util.List;
import java.util.function.UnaryOperator;

import com.google.common.reflect.TypeToken;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

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

	protected AnchorHolderMainLogical(final TypeToken<S> ob, final TypeToken<?> listClazz, final TypeToken<W> opClazz,
			final F fields) {
		super(ob, listClazz, opClazz, fields);
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
	public X any(final String alias) {
		this.addQuery(Query.from(alias));
		return (X) this;
	}
	public X collapseAnd() {
		return this.collapse(Query.AND);
	}
	public X collapseOr() {
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
		final T holder = this.newList();
		holder.setList(list);
		this.getQueries().addAll(holder.addForEachCollapsing(a, query));
		return (X) this;
	}

	// @formatter:off
  protected <L, N extends AnchorHolderOne<F, S, N, L,?>,
      T extends AnchorHolderList<F, N, X, S, T, L>> X notCollapse(
      final List<L> list, final UnaryOperator<N> a) {
    // @formatter:on
		final T holder = this.newList();
		holder.setList(list);
		this.getDto().addFilter(holder.addForEach(a));
		return (X) this;
	}

	// @formatter:off
   <V extends AnchorHolderOne<F, S, V,Object, ?>, 
      T extends AnchorHolderList<F, V, X, S, T, Object>> X collapse(final Query val) {
		 if(this.getDto().getFilters().size()<2){
			 return (X)this;
		 }
    // @formatter:on
		final T newlist = this.newList();
		newlist.setList(List.of());
		final Queries collapse;
		if (val == null) {
			collapse = newlist.collapse(this.getDto().getFilters());
		} else {
			collapse = newlist.collapse(this.getDto().getFilters(), val);
		}

		this.getDto().resetFilter(collapse);
		return (X) this;
	}

}
