package dukono.minidsl;

import com.google.common.reflect.TypeToken;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

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

	protected AnchorHolderList(final Class<S> ob, final TypeToken<N> oneClazz, final Class<A> objectClazz) {
		this.dtoClazz = ob;
		this.oneClazz = oneClazz;
		this.objectClazz = objectClazz;
	}

	protected AnchorHolderList(final Class<S> ob, final List<O> list, final TypeToken<N> oneClazz,
			final Class<A> objectClazz) {
		this(ob, oneClazz, objectClazz);
		this.list = list;
	}

	public A collapseOr(final UnaryOperator<N> a) {
		this.addForEachCollapsing(a, Query.OR);// add
		final A requestApiObject = this.newObject();
		requestApiObject.addDto(this.getDto());
		return requestApiObject;
	}

	public A collapseAnd(final UnaryOperator<N> a) {
		this.addForEachCollapsing(a, Query.AND);// add
		final A requestApiObject = this.newObject();
		requestApiObject.addDto(this.getDto());
		return requestApiObject;
	}

	public A addForEach(final UnaryOperator<N> a) {
		this.addForEachNoCollapse(a);
		final A requestApiObject = this.newObject();
		requestApiObject.addDto(this.getDto());
		return requestApiObject;
	}

	protected X addForEachCollapsing(final UnaryOperator<N> a, final Query val) {

		final S dto = this.newDto();
		for (final O t : this.list) {
			final N singleUnitApi = this.newOne();
			singleUnitApi.setItemUnit(t);
			final S unitDto = a.apply(singleUnitApi).other().getDto();
			dto.addFilter(unitDto.getFilters());
		}
		if (val == null) {
			this.collapse(dto);
		} else {
			this.collapse(dto, val);
		}

		this.changeDto(dto);

		return (X) this;
	}

	protected void addForEachNoCollapse(final UnaryOperator<N> a) {

		final S dto = this.newDto();
		for (final O t : this.list) {
			final N requestApi = this.newOne();
			requestApi.setItemUnit(t);
			final S dto1 = a.apply(requestApi).other().getDto();
			dto.addFilter(dto1.getFilters());
		}

		this.changeDto(dto);

	}

	protected void collapse(final S obj, final Query andOr) {
		final List<Queries> filters = obj.getFilters();
		final Queries neww = Queries.builder().build();
		if (CollectionUtils.isNotEmpty(filters)) {
			filters.getFirst().addFirst(Query.OPEN);
			filters.getLast().addLast(Query.CLOSE);
		}
		if (CollectionUtils.isNotEmpty(filters) && filters.size() > 1) {

			for (final Queries queries : filters) {
				if (!CHECK_IF_ENDS_WITH_OP.test(queries) && !filters.getLast().equals(queries)) {
					queries.addLast(andOr);
				}
				neww.addAll(queries);
			}
			obj.setFilters(neww.toList());
		}

	}

	protected void collapse(final S obj) {
		final List<Queries> filters = obj.getFilters();
		final Queries neww = Queries.builder().build();

		if (CollectionUtils.isNotEmpty(filters)) {
			for (final Queries queries : filters) {
				neww.addAll(queries);
			}
			obj.setFilters(neww.toList());
		}
	}

	protected void changeDto(final S dto) {
		Optional.ofNullable(this.getDto()).ifPresentOrElse(s -> s.update(dto), () -> this.setDto(dto));
	}

	protected S newDto() {
		return AnchorHolderMain.newType(this.dtoClazz);
	}

	protected A newObject() {
		return AnchorHolderMain.newType(this.objectClazz);
	}

	protected N newOne() {
		return this.oneClazzInstance.map(n -> {
			n.setQueries(Queries.builder().build());
			n.setItemUnit(null);
			return n;
		}).orElseGet(() -> {
			final Class<? extends N> rawType = (Class<? extends N>) this.oneClazz.getRawType();
			return AnchorHolderMain.newType(rawType);
		});

	}

	public S getDto() {
		Optional.ofNullable(this.dto).ifPresentOrElse(s -> {
		}, () -> this.setDto(this.newDto()));

		return this.dto;
	}

}
