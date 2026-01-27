package dukono.minidsl;

import static dukono.minidsl.Dto.QUERIES_COMPARATOR;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

import com.google.common.collect.Lists;
import com.google.common.reflect.TypeToken;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;

@SuppressWarnings("unchecked")
@Getter(AccessLevel.PACKAGE)
@Setter(AccessLevel.PACKAGE)
@NoArgsConstructor
@AllArgsConstructor
// @formatter:off
public abstract class AnchorHolderList<
    F extends Field,
    N extends AnchorHolderOne<F,S, N, O,?>,
    A extends AnchorHolderMain<F,S, A,?>,
    S extends Dto, 
    X extends AnchorHolderList<F,N,A,S, X, O>, 
    O>
{
  // @formatter:on
	private static final Predicate<Queries> CHECK_IF_ENDS_WITH_OP = s -> s.endWithSomeOf(Query.AND, Query.OR);

	protected List<O> list;

	protected S dto;

	protected Class<S> dtoClazz;

	protected Class<A> objectClazz;

	protected TypeToken<N> oneClazz;

	protected Optional<N> oneClazzInstance = Optional.empty();

	protected Optional<A> objectClazzInstance = Optional.empty();

	private Queries queries;

	protected AnchorHolderList(final TypeToken<N> oneClazz, final Class<A> objectClazz) {
		this.oneClazz = oneClazz;
		this.objectClazz = objectClazz;
	}

	protected AnchorHolderList(final List<O> list, final TypeToken<N> oneClazz, final Class<A> objectClazz) {
		this(oneClazz, objectClazz);
		this.list = list;
	}

	public List<Queries> addForEach(final UnaryOperator<N> a) {

		final List<Queries> queries = Lists.newArrayList();
		for (final O t : this.list) {
			final N requestApi = this.newOne(t);
			requestApi.setItemUnit(t);
			final S dto1 = a.apply(requestApi).other().getDto();
			queries.add(dto1.getFilters().getFirst());
		}

		return queries;
	}

	protected Queries addForEachCollapsing(final UnaryOperator<N> a, final Query val) {

		final List<Queries> queries = Lists.newArrayList();
		for (final O t : this.list) {
			queries.add(a.apply(this.newOne(t)).getQueries());
		}

		final Queries collapse;
		if (val == null) {
			collapse = this.collapse(queries);
		} else {
			collapse = this.collapse(queries, val);
		}
		return collapse;
	}

	protected Queries collapse(final List<Queries> obj, final Query andOr) {
		obj.sort(QUERIES_COMPARATOR);
		final Queries neww = Queries.builder().build();
		if (CollectionUtils.isNotEmpty(obj)) {
			for (final Queries q : obj) {
				if (!CHECK_IF_ENDS_WITH_OP.test(q) && !obj.getLast().equals(q)) {
					q.addLast(andOr);
				}
				neww.addAll(q);
			}
		}
		return neww;

	}

	protected Queries collapse(final List<Queries> obj) {
		obj.sort(QUERIES_COMPARATOR);
		final Queries neww = Queries.builder().build();

		if (CollectionUtils.isNotEmpty(obj)) {
			for (final Queries q : obj) {
				neww.addAll(q);
			}
		}
		return neww;
	}

	protected N newOne(final O t) {
		return this.oneClazzInstance.map(n -> {
			n.setQueries(Queries.builder().build());
			n.setItemUnit(t);
			n.getDto().removeFilters();
			return n;
		}).orElseGet(() -> {
			final Class<? extends N> rawType = (Class<? extends N>) this.oneClazz.getRawType();
			final N n = AnchorHolderMain.newType(rawType);
			this.oneClazzInstance = Optional.of(n);
			n.setQueries(Queries.builder().build());
			n.setItemUnit(t);
			n.getDto().removeFilters();
			return n;
		});
	}
}
