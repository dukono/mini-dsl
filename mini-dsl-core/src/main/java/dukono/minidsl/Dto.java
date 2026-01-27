package dukono.minidsl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dukono.minidsl.annotation.OperationDefinition;
import dukono.minidsl.util.ParseConfigFactory;
import lombok.AllArgsConstructor;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@SuppressWarnings("unchecked")
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public abstract class Dto {

	private static final ObjectMapper JSON_MAPPER = new ObjectMapper();

	// ⚡ Comparator reutilizable - evita crear instancias en cada sort
	public static final java.util.Comparator<Queries> QUERIES_COMPARATOR = Queries::compareTo;

	@Default
	private List<Queries> filters = new ArrayList<>();

	// ⚡ Cache para evitar re-ordenar constantemente
	private transient List<Queries> sortedFiltersCache = null;
	private transient boolean filtersDirty = true;

	/**
	 * Marks filters as dirty to invalidate sorted cache. Call this after any
	 * modification to filters list.
	 */
	private void markFiltersDirty() {
		this.filtersDirty = true;
		this.sortedFiltersCache = null;
	}

	public <T extends Dto> T addFilter(final Queries filter) {
		Optional.ofNullable(filter).filter(Queries::notEmpty).ifPresent(s -> {
			this.filters.add(s);
			this.markFiltersDirty();
		});
		return (T) this;
	}
	public <T extends Dto> T resetFilter(final Queries filter) {
		Optional.ofNullable(filter).filter(Queries::notEmpty).ifPresent(s -> {
			this.filters.clear();
			this.filters.add(s);
			this.markFiltersDirty();
		});
		return (T) this;
	}

	public <T extends Dto> T addFilter(final List<Queries> filters) {
		Optional.ofNullable(filters).filter(CollectionUtils::isNotEmpty).ifPresent(s -> {
			this.filters.addAll(s);
			this.markFiltersDirty();
		});
		return (T) this;
	}

	// replaceFilters-----------------------------------------
	public <T extends Dto> T replaceFilters(final Map<Queries, Queries> requestValues) {
		requestValues.forEach((toFind, newValue) -> this.filters.forEach(core -> core.replace(toFind, newValue)));
		this.markFiltersDirty();
		return (T) this;
	}

	public <T extends Dto> T replaceFilters(final Dto requestValues, final ComparatorEnum compareBy) {

		requestValues.filters.forEach(
				queries -> this.filters.forEach(core -> core.replace(queries.getQueries(), compareBy.getValue())));
		this.markFiltersDirty();

		return (T) this;
	}

	// removeFilters-----------------------------------------
	public <T extends Dto> T removeFiltersExactMatch(final List<Queries> requestValues) {

		// ⚡ Acceso directo a filters sin ordenar
		requestValues.forEach(toFind -> {
			this.filters.forEach(core -> core.replace(toFind, null));
			this.filters.removeIf(Queries::empty);
		});
		this.markFiltersDirty();

		return (T) this;
	}

	public <T extends Dto> T removeFiltersQuery(final List<Queries> requestValues, final ComparatorEnum compareBy) {

		requestValues.forEach(
				toFind -> this.filters.forEach(core -> core.remove(toFind.getQueries(), compareBy.getValue())));
		this.markFiltersDirty();

		return (T) this;
	}

	public <T extends Dto> T removeFiltersFull(final List<Queries> requestValues, final ComparatorEnum compareBy) {

		requestValues.forEach(
				toFind -> this.filters.removeIf(core -> core.match(toFind.getQueries(), compareBy.getValue())));
		this.markFiltersDirty();

		return (T) this;
	}

	public <T extends Dto> T removeFilters() {
		Optional.ofNullable(this.filters).ifPresent(values -> {
			this.filters.clear();
			this.markFiltersDirty();
		});
		return (T) this;
	}

	public List<String> filtersAsString() {
		// ⚡ Usa getFilters() que ya está optimizado con cache
		return Optional.ofNullable(this.getFiltersSorted()).stream().flatMap(Collection::stream)
				.map(Queries::filtersAsString).toList();
	}

	/**
	 * Gets sorted filters with lazy sorting and caching. ⚡ Performance: Only sorts
	 * when filters are modified (dirty flag).
	 * 
	 * @return sorted list of filters (cached)
	 */
	public List<Queries> getFiltersSorted() {
		if (this.filtersDirty || this.sortedFiltersCache == null) {
			// ⚡ Sort in-place usando Comparator estático
			this.filters.sort(QUERIES_COMPARATOR);
			this.sortedFiltersCache = this.filters;
			this.filtersDirty = false;
		}
		return this.sortedFiltersCache;
	}

	public void setFilters(final List<Queries> filters) {
		if (CollectionUtils.isNotEmpty(filters)) {
			this.markFiltersDirty();
			this.filters = filters;
		}
	}

	public String filtersAsJson() {
		final List<Map<String, Object>> jsonEntries = new ArrayList<>();
		Optional.ofNullable(this.getFilters()).stream().flatMap(Collection::stream)
				.forEach(qs -> qs.getQueries().forEach(q -> {
					final Map<String, Object> node = new java.util.LinkedHashMap<>();
					if (q.getKey() != null) {
						node.put("key", q.getKey());
					}
					if (q.getOp() != null) {
						node.put("op", q.getOp());
					}
					// value solo si presente
					q.getValue().ifPresent(v -> node.put("value", v));
					jsonEntries.add(node);
				}));
		try {
			return JSON_MAPPER.writeValueAsString(jsonEntries);
		} catch (final Exception e) {
			return "[]"; // fallback
		}
	}

	public <T extends Dto> T parseFiltersJson(final String json, final Query.ParseConfig config) {
		if (json == null || json.isBlank()) {
			return (T) this;
		}
		try {
			final List<Map<String, Object>> nodes = JSON_MAPPER.readValue(json, new TypeReference<>() {
			});
			final List<String> expressions = nodes.stream().map(node -> {
				final String key = Optional.ofNullable(node.get("key")).map(Object::toString).orElse(null);
				final String op = Optional.ofNullable(node.get("op")).map(Object::toString).orElse(null);
				final String value = Optional.ofNullable(node.get("value")).map(Object::toString).orElse(null);
				if (key != null && op != null && value != null) {
					return key + " " + op + " " + value;
				}
				if (key != null && op != null) {
					return key + " " + op;
				}
				if (op != null) {
					return op;
				}
				if (key != null) {
					return key;
				}
				return "";
			}).filter(s -> !s.isBlank()).toList();
			return this.parseFilters(expressions, config);
		} catch (final Exception e) {
			return (T) this;
		}
	}

	public <T extends Dto> T parseFilters(final Collection<String> inputs,
			final Class<? extends Enum<? extends OperationDefinition>> operationEnumClass, final Class<?> fieldsClass) {
		return this.parseFilters(inputs,
				ParseConfigFactory.fromOperationEnumWithFields(operationEnumClass, fieldsClass));
	}

	public <T extends Dto> T parseFilters(final Collection<String> inputs, final Query.ParseConfig config) {
		Optional.ofNullable(inputs).filter(strings -> !strings.isEmpty()).map(HashSet::new).ifPresent(strings -> {
			this.setFilters(strings.stream().map(input -> Query.parseQueries(input, config))
					.filter(requestDynamicQuery -> !requestDynamicQuery.isEmpty()).map(Queries::new)
					.collect(Collectors.toCollection(ArrayList::new)));
			this.markFiltersDirty();
		});
		return (T) this;
	}

	<T extends Dto> void update(final T val) {
		this.addFilter(val.getFilters());
	}

}
