package dukono.minidsl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

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

	@Default
	private List<Queries> filters = new ArrayList<>();

	public <T extends Dto> T addFilter(final Queries filter) {
		Optional.ofNullable(filter).ifPresent(s -> this.getFilters().add(s));
		return (T) this;
	}

	public <T extends Dto> T addFilter(final List<Queries> filters) {
		Optional.ofNullable(filters).ifPresent(s -> this.getFilters().addAll(s));
		return (T) this;
	}

	// replaceFilters-----------------------------------------
	public <T extends Dto> T replaceFilters(final Map<Queries, Queries> request) {
		Optional.ofNullable(request).ifPresent(requestValues -> requestValues
				.forEach((toFind, newValue) -> this.getFilters().forEach(core -> core.replace(toFind, newValue))));
		return (T) this;
	}

	public <T extends Dto> T replaceFilters(final Dto dtoWithNewValues, final ComparatorEnum compareBy) {
		Optional.ofNullable(dtoWithNewValues).ifPresent(requestValues -> {
			requestValues.getFilters().forEach(queries -> this.getFilters()
					.forEach(core -> core.replace(queries.getQueries(), compareBy.getValue())));
		});
		return (T) this;
	}

	// removeFilters-----------------------------------------
	public <T extends Dto> T removeFiltersExactMatch(final List<Queries> toRemove) {
		Optional.ofNullable(toRemove).ifPresent(requestValues -> requestValues.forEach(toFind -> {
			this.getFilters().forEach(core -> core.replace(toFind, null));
			this.getFilters().removeIf(Queries::empty);
		}));
		return (T) this;
	}

	public <T extends Dto> T removeFiltersQuery(final List<Queries> toRemove, final ComparatorEnum compareBy) {
		Optional.ofNullable(toRemove).ifPresent(requestValues -> requestValues.forEach(
				toFind -> this.getFilters().forEach(core -> core.remove(toFind.getQueries(), compareBy.getValue()))));
		return (T) this;
	}

	public <T extends Dto> T removeFiltersFull(final List<Queries> toRemove, final ComparatorEnum compareBy) {
		Optional.ofNullable(toRemove).ifPresent(requestValues -> requestValues.forEach(
				toFind -> this.getFilters().removeIf(core -> core.match(toFind.getQueries(), compareBy.getValue()))));
		return (T) this;
	}

	public <T extends Dto> T removeFilters() {
		Optional.ofNullable(this.getFilters()).ifPresent(values -> this.getFilters().clear());
		return (T) this;
	}

	public List<String> filtersAsString() {
		return Optional.ofNullable(this.getFilters()).stream().flatMap(Collection::stream).map(Queries::filtersAsString)
				.sorted().toList();
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

	public <T extends Dto> T parseFiltersJson(final String json) {
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
			return this.parseFilters(expressions);
		} catch (final Exception e) {
			return (T) this;
		}
	}

	public <T extends Dto> T parseFilters(final Collection<String> inputs) {
		Optional.ofNullable(inputs).filter(strings -> !strings.isEmpty()).map(HashSet::new)
				.ifPresent(strings -> this.setFilters(strings.stream().map(Query::parseQueries)
						.filter(requestDynamicQuery -> !requestDynamicQuery.isEmpty()).map(Queries::new)
						.collect(Collectors.toCollection(ArrayList::new))));
		return (T) this;
	}

	<T extends Dto> void update(final T val) {
		this.addFilter(val.getFilters());

	}

}
