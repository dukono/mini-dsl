package dukono.minidsl;

import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Queries implements Comparable<Queries> {

	private static final Comparator<String> comparing = Comparator.comparing(s1 -> s1, String::compareTo);

	@Default
	List<Query> queries = new ArrayList<>();

	private transient String cachedFilterString;
	private transient int cachedHashCode = 0;
	private transient boolean hashCodeCached = false;

	Queries(final List<Query> queries) {
		this.queries = queries;
	}
	public void add(final Query filter) {
		Optional.ofNullable(filter).ifPresent(s -> {
			this.getQueries().add(s);
			this.invalidateCache();
		});
	}

	public void addAll(final Queries filter) {
		Optional.ofNullable(filter).filter(Queries::notEmpty).ifPresent(s -> {
			this.getQueries().addAll(s.getQueries());
			this.invalidateCache();
		});
	}

	public void addFirst(final Query filter) {
		Optional.ofNullable(filter).ifPresent(s -> {
			final List<Query> newQueries = new ArrayList<>();
			newQueries.add(s);
			newQueries.addAll(this.getQueries());
			this.queries.clear();
			this.queries.addAll(newQueries);
			this.invalidateCache();
		});
	}

	public void addLast(final Query filter) {
		Optional.ofNullable(filter).ifPresent(s -> {
			this.queries.addLast(s);
			this.invalidateCache();
		});
	}

	public boolean endWithSomeOf(final Query... filter) {
		return Optional.ofNullable(filter).stream().map(Stream::of).flatMap(Stream::distinct)
				.anyMatch(s -> this.queries.getLast().getOp().equals(s.getOp()));
	}

	public void replace(final Query toFind, final Query newValue, final Function<Query, Predicate<Query>> compareBy) {
		Optional.ofNullable(toFind).ifPresent(find -> {
			this.getQueries().stream().filter(q -> compareBy.apply(q).test(find)).forEach(val -> val.set(newValue));
			this.invalidateCache();
		});
	}

	public void replace(final Queries toFind, final Queries newValue) {
		Optional.ofNullable(toFind).filter(tf -> !Optional.ofNullable(tf.getQueries()).map(List::isEmpty).orElse(false))
				.ifPresent(patternQueries -> {
					final List<Query> pattern = patternQueries.getQueries();
					final int patternSize = pattern.size();
					final List<Query> source = this.getQueries();
					if (patternSize == 0 || source.size() < patternSize) {
						return;
					}
					final String patternString = patternQueries.filtersAsString();
					for (int i = 0; i <= source.size() - patternSize; i++) {
						final List<Query> window = source.subList(i, i + patternSize);
						final String windowString = this.filtersAsString(window);
						if (windowString.equals(patternString)) {
							final List<Query> replaced = new ArrayList<>(source.subList(0, i));
							Optional.ofNullable(newValue).map(Queries::getQueries).ifPresent(replaced::addAll);
							replaced.addAll(source.subList(i + patternSize, source.size()));
							this.setQueries(replaced);
							this.invalidateCache();
							break;
						}
					}
				});

	}

	public void replace(final List<Query> request, final Function<Query, Predicate<Query>> compareBy) {

		Optional.ofNullable(request).ifPresent(requestValues -> {
			requestValues.forEach(newValues -> this.getQueries().stream()
					.filter(q -> compareBy.apply(q).test(newValues)).forEach(val -> val.set(newValues)));
			this.invalidateCache();
		});
	}

	public boolean match(final List<Query> request, final Function<Query, Predicate<Query>> compareBy) {
		return Optional.ofNullable(request)
				.map(requestValues -> requestValues.stream().anyMatch(
						find -> this.getQueries().stream().anyMatch(core -> compareBy.apply(core).test(find))))
				.orElse(false);
	}

	public void remove(final List<Query> request, final Function<Query, Predicate<Query>> compareBy) {
		Optional.ofNullable(request).filter(CollectionUtils::isNotEmpty).ifPresent(requestValues -> {
			this.getQueries()
					.removeIf(core -> requestValues.stream().anyMatch(find -> compareBy.apply(core).test(find)));
			this.invalidateCache();
		});

	}

	protected String filtersAsString(final Collection<Query> v) {
		return v.stream().map(Query::formatString).collect(Collectors.joining(" "));
	}

	public String filtersAsString() {
		return this.getQueries().stream().map(Query::formatString).collect(Collectors.joining(" "));
	}

	public List<Queries> toList() {
		return Lists.newArrayList(this);
	}

	public boolean empty() {
		return CollectionUtils.isEmpty(this.getQueries());
	}
	public boolean notEmpty() {
		return !this.empty();
	}

	/**
	 * Invalidates cached values. Call this method when queries list is modified.
	 */
	private void invalidateCache() {
		this.cachedFilterString = null;
		this.hashCodeCached = false;
		this.cachedHashCode = 0;
	}

	/**
	 * Gets the filter string with lazy caching for performance optimization. This
	 * is particularly important for sorting operations where compareTo is called
	 * many times.
	 * 
	 * @return cached filter string
	 */
	private String getCachedFilterString() {
		if (this.cachedFilterString == null) {
			this.cachedFilterString = this.filtersAsString();
		}
		return this.cachedFilterString;
	}

	@Override
	public int compareTo(final Queries o) {

		return this.getCachedFilterString().compareTo(o.getCachedFilterString());
	}

	@Override
	public int hashCode() {
		if (!this.hashCodeCached) {
			this.cachedHashCode = this.getCachedFilterString().hashCode();
			this.hashCodeCached = true;
		}
		return this.cachedHashCode;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || this.getClass() != obj.getClass()) {
			return false;
		}
		final Queries other = (Queries) obj;
		return this.getCachedFilterString().equals(other.getCachedFilterString());
	}

}
