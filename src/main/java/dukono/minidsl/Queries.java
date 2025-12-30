package dukono.minidsl;

import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Queries {
	private static final Random RANDOM = new Random();

	private static final Comparator<String> comparing = Comparator.comparing(s1 -> s1, String::compareTo);

	@Default
	List<Query> queries = new ArrayList<>();

	public void add(final Query filter) {
		Optional.ofNullable(filter).ifPresent(s -> this.getQueries().add(s));
	}

	public void addAll(final Queries filter) {
		Optional.ofNullable(filter).ifPresent(s -> this.getQueries().addAll(s.getQueries()));
	}

	public void addFirst(final Query filter) {
		Optional.ofNullable(filter).ifPresent(s -> {
			final List<Query> newQueries = new ArrayList<>();
			newQueries.add(s);
			newQueries.addAll(this.getQueries());
			this.queries.clear();
			this.queries.addAll(newQueries);
		});
	}

	public void addLast(final Query filter) {
		Optional.ofNullable(filter).ifPresent(s -> this.queries.addLast(s));
	}

	public boolean endWithSomeOf(final Query... filter) {
		return Optional.ofNullable(filter).stream().map(Stream::of).flatMap(Stream::distinct)
				.anyMatch(s -> this.queries.getLast().getOp().equals(s.getOp()));
	}

	public void replace(final Query toFind, final Query newValue, final Function<Query, Predicate<Query>> compareBy) {
		Optional.ofNullable(toFind).ifPresent(find -> this.getQueries().stream()
				.filter(q -> compareBy.apply(q).test(find)).forEach(val -> val.set(newValue)));
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
							final List<Query> replaced = new ArrayList<>();
							replaced.addAll(source.subList(0, i));
							Optional.ofNullable(newValue).map(Queries::getQueries).ifPresent(replaced::addAll);
							replaced.addAll(source.subList(i + patternSize, source.size()));
							this.setQueries(replaced);
							break;
						}
					}
				});

	}

	public void replace(final List<Query> request, final Function<Query, Predicate<Query>> compareBy) {

		Optional.ofNullable(request).ifPresent(requestValues -> requestValues.forEach(newValues -> this.getQueries()
				.stream().filter(q -> compareBy.apply(q).test(newValues)).forEach(val -> val.set(newValues))));
	}

	public boolean match(final List<Query> request, final Function<Query, Predicate<Query>> compareBy) {
		return Optional.ofNullable(request)
				.map(requestValues -> requestValues.stream().anyMatch(
						find -> this.getQueries().stream().anyMatch(core -> compareBy.apply(core).test(find))))
				.orElse(false);
	}

	public void remove(final List<Query> request, final Function<Query, Predicate<Query>> compareBy) {
		Optional.ofNullable(request).ifPresent(requestValues -> this.getQueries()
				.removeIf(find -> requestValues.stream().anyMatch(core -> compareBy.apply(core).test(find))));

	}

	protected String filtersAsString(final Collection<Query> v) {
		return v.stream().map(Query::asString).collect(Collectors.joining(" "));
	}

	public String filtersAsString() {
		return this.getQueries().stream().map(Query::asString).collect(Collectors.joining(" "));
	}

	public List<Queries> toList() {
		return Lists.newArrayList(this);
	}

	public boolean empty() {
		return this.getQueries().isEmpty();
	}

	private static int randomNonZeroInRange() {
		final int num = RANDOM.nextInt(20);
		return num < 10 ? num - 10 : num - 9;
	}

}
