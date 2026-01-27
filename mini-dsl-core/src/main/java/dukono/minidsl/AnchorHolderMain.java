package dukono.minidsl;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

import com.google.common.reflect.TypeToken;
import dukono.minidsl.Field.FieldHolder;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

	private static final Map<Class<?>, Constructor<?>> CONSTRUCTOR_CACHE = new ConcurrentHashMap<>();

	protected Queries queries;

	protected TypeToken<S> dtoClazz;

	protected TypeToken<?> listClazz;

	protected TypeToken<W> opClazz;

	protected F fields;

	protected Optional<S> dtoClazzInstance = Optional.empty();
	protected Optional<W> opClazzInstance = Optional.empty();

	protected AnchorHolderMain(final TypeToken<S> ob, final TypeToken<?> listClazz, final TypeToken<W> opClazz,
			final F fields) {
		this.dtoClazz = ob;
		this.listClazz = listClazz;
		this.opClazz = opClazz;
		this.fields = fields;
	}

	public W field(final Function<F, FieldHolder> wwFunction) {
		Objects.requireNonNull(wwFunction, "Field selector function cannot be null");

		final FieldHolder apply = wwFunction.apply(this.fields);
		Objects.requireNonNull(apply, "Field selector must return a valid FieldHolder, got null");

		final String fieldName = apply.getName();
		if (fieldName == null || fieldName.isBlank()) {
			throw new IllegalArgumentException("Field name cannot be null or blank");
		}

		final W w = this.newOpClass();
		w.setHolder((X) this);
		w.setName(fieldName);
		return w;
	}

	public X other() {
		this.getDto().addFilter(this.queries);
		this.queries = null;
		return (X) this;
	}

	// -----Getters and Setters
	protected void addQuery(final Query query) {
		this.getQueries().add(query);
	}

	Queries getQueries() {
		return Optional.ofNullable(this.queries).orElseGet(() -> {
			this.queries = Queries.builder().build();
			return this.queries;
		});
	}

	public S getDto() {
		final S dto = this.newDto();
		dto.addFilter(this.queries);
		this.queries = null;
		return dto;
	}

	// ----Instancietors
	<L, V extends AnchorHolderList<F, ?, X, S, V, L>> V newList() {
		return newType((Class<? extends V>) this.listClazz.getRawType());
	}

	S newDto() {
		return this.dtoClazzInstance.orElseGet(() -> {
			final S s = newType((Class<? extends S>) this.dtoClazz.getRawType());
			this.dtoClazzInstance = Optional.of(s);
			return s;
		});
	}
	W newOpClass() {
		return this.opClazzInstance.orElseGet(() -> {
			final W s = newType((Class<? extends W>) this.opClazz.getRawType());
			this.opClazzInstance = Optional.of(s);
			return s;
		});
	}

	<Y> Y newType(final TypeToken<Y> clazz) {
		return newType((Class<? extends Y>) clazz.getRawType());
	}

	static <Y> Y newType(final Class<? extends Y> rawType) {
		Objects.requireNonNull(rawType, "Target class cannot be null");
		try {
			@SuppressWarnings("unchecked")
			final Constructor<Y> constructor = (Constructor<Y>) CONSTRUCTOR_CACHE.computeIfAbsent(rawType, clazz -> {
				try {
					return clazz.getDeclaredConstructor();
				} catch (final NoSuchMethodException e) {
					throw new DslInstantiationException(clazz, "No default (no-args) constructor found", e);
				}
			});
			return constructor.newInstance();
		} catch (final InstantiationException e) {
			throw new DslInstantiationException(rawType, "Class is abstract or interface", e);
		} catch (final IllegalAccessException e) {
			throw new DslInstantiationException(rawType, "Constructor is not accessible", e);
		} catch (final InvocationTargetException e) {
			final Throwable cause = e.getCause();
			throw new DslInstantiationException(rawType,
					"Constructor threw an exception: " + (cause != null ? cause.getMessage() : "unknown"), cause);
		} catch (final DslInstantiationException e) {
			throw e;
		} catch (final Exception e) {
			throw new DslInstantiationException(rawType, "Unexpected error during instantiation", e);
		}
	}

}
