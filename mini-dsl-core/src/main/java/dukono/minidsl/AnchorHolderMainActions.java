package dukono.minidsl;

import java.util.AbstractMap.SimpleEntry;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.google.common.reflect.TypeToken;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@SuppressWarnings("unchecked")
@Getter(AccessLevel.PROTECTED)
@Setter(AccessLevel.PROTECTED)
// @formatter:off
public abstract class AnchorHolderMainActions<
    F extends Field,
    S extends Dto,
    X extends AnchorHolderMainActions<F,S, X,W>,
    W extends AnchorOperationsBasic<X>> 
    extends AnchorHolderMainLogical<F,S,X,W > {
  // @formatter:on
	protected AnchorHolderMainActions() {
		super();
	}

	protected AnchorHolderMainActions(final TypeToken<S> ob, final TypeToken<?> listClazz, final TypeToken<W> opClazz,
			final F fields) {
		super(ob, listClazz, opClazz, fields);
	}

	protected <Y extends AnchorHolderMain<F, S, Y, ?>> X replace(final UnaryOperator<Y> find,
			final UnaryOperator<Y> change, final TypeToken<Y> clazz) {
		final List<Queries> filters = find.apply(this.newType(clazz)).getDto().getFilters();
		final List<Queries> filtersChange = change.apply(this.newType(clazz)).getDto().getFilters();

		final Map<Queries, Queries> collect = IntStream.range(0, filters.size()).filter(i -> i < filtersChange.size())
				.mapToObj(i -> new SimpleEntry<>(filters.get(i), filtersChange.get(i)))
				.collect(Collectors.toMap(SimpleEntry::getKey, SimpleEntry::getValue));

		this.getDto().replaceFilters(collect);
		return (X) this;
	}

	protected <Y extends AnchorHolderMain<F, S, Y, ?>> X modify(final UnaryOperator<Y> change,
			final Function<Comparator, ComparatorEnum> b, final TypeToken<Y> clazz) {
		this.getDto().replaceFilters(change.apply(this.newType(clazz)).getDto(), b.apply(Comparator.INSTANCE));
		return (X) this;
	}

	protected <A extends AnchorHolderMain<F, S, A, ?>, B extends AnchorHolderMainLogical<F, S, B, ?>> X remove(
			final Function<RemoveBy<A, B>, RemoveBy.Remover> toDo, final TypeToken<A> clazz,
			final TypeToken<B> clazzB) {

		toDo.apply(new RemoveBy<>(clazz, clazzB)).accept(this.getDto());
		return (X) this;
	}

}
