package dukono.minidsl;

import com.google.common.reflect.TypeToken;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Optional;
import java.util.function.Function;

@SuppressWarnings("unchecked")
@Getter(AccessLevel.PROTECTED)
@Setter(AccessLevel.PROTECTED)
@NoArgsConstructor
@AllArgsConstructor
// @formatter:off
public abstract class AnchorHolderMain<
    F extends Field,
    S extends Dto,
    X extends AnchorHolderMain<F,S,X,W>,
    W extends AnchorOperationsBasic<X>> {
  // @formatter:on

	protected Queries queries;

	protected S dto;

	protected TypeToken<S> dtoClazz;

	protected TypeToken<X> self;

	protected TypeToken<?> listClazz;

	protected TypeToken<W> opClazz;

	protected F fields;

	protected AnchorHolderMain(final TypeToken<S> ob, final TypeToken<?> listClazz, final TypeToken<X> self,
			final TypeToken<W> opClazz, final F fields) {
		this.dtoClazz = ob;
		this.listClazz = listClazz;
		this.self = self;
		this.opClazz = opClazz;
		this.fields = fields;
	}

	public W field(final Function<F, Field.FieldHolder> wwFunction) {
		final Field.FieldHolder apply = wwFunction.apply(this.fields);
		final W w = this.newType(this.opClazz);
		w.setHolder((X) this);
		w.setName(apply.getName());
		return w;
	}

	public X other() {
		this.getDto().addFilter(this.queries);
		this.queries = null;
		return (X) this;
	}

	// -----Getters and Setters
	void addQuery(final Query query) {
		this.getQueries().add(query);
	}

	Queries getQueries() {
		return Optional.ofNullable(this.queries).orElseGet(() -> {
			this.queries = Queries.builder().build();
			return this.queries;
		});
	}

	X addDto(final S dto) {
		Optional.ofNullable(this.getDto()).ifPresentOrElse(s -> s.update(dto), () -> this.dto = dto);
		return (X) this;
	}

	public S getDto() {
		Optional.ofNullable(this.dto).ifPresentOrElse(s -> {
			this.dto.addFilter(this.queries);
			this.queries = null;
		}, () -> {
			this.dto = this.newObj().addFilter(this.queries);
			this.queries = null;
		});
		return this.dto;
	}

	// ----Instancietors
	<L, V extends AnchorHolderList<F, ?, X, S, V, L>> V newlist() {
		return newType((Class<? extends V>) this.listClazz.getRawType());
	}

	X newSelf() {
		return newType((Class<? extends X>) this.self.getRawType());
	}

	S newObj() {
		return newType((Class<? extends S>) this.dtoClazz.getRawType());
	}

	<Y> Y newType(final TypeToken<Y> clazz) {
		return newType((Class<? extends Y>) clazz.getRawType());
	}

	static <Y> Y newType(final Class<? extends Y> rawType) {
		try {
			return rawType.getConstructor().newInstance();
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}
	}

}
