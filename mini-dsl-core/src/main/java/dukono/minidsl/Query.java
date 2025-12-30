package dukono.minidsl;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Query implements Comparable<Query> {

	public static final Query OR = Query.from("or");

	public static final Query AND = Query.from("and");

	public static final Query OPEN = Query.from("(");

	public static final Query CLOSE = Query.from(")");

	public static final Function<Query, Predicate<Query>> COMPARATOR_FULL = core -> toNew -> toNew.getKey()
			.equalsIgnoreCase(core.getKey()) && toNew.getOp().equalsIgnoreCase(core.getOp())
			&& toNew.getValueAsString().equals(core.getValueAsString());

	public static final Function<Query, Predicate<Query>> COMPARATOR_KEY = core -> toNew -> toNew.getKey()
			.equalsIgnoreCase(core.getKey());

	public static final Function<Query, Predicate<Query>> COMPARATOR_KEY_OP = core -> toNew -> toNew.getKey()
			.equalsIgnoreCase(core.getKey()) && toNew.getOp().equalsIgnoreCase(core.getOp());

	public static final Function<Query, Predicate<Query>> COMPARATOR_KEY_VALUE = core -> toNew -> toNew.getKey()
			.equalsIgnoreCase(core.getKey()) && toNew.getValueAsString().equals(core.getValueAsString());

	public static final Function<Query, Predicate<Query>> COMPARATOR_OP = core -> toNew -> toNew.getOp()
			.equalsIgnoreCase(core.getOp());

	public static final Function<Query, Predicate<Query>> COMPARATOR_OP_VALUE = core -> toNew -> toNew.getOp()
			.equalsIgnoreCase(core.getOp()) && toNew.getValueAsString().equals(core.getValueAsString());

	protected String key;

	protected String op;

	protected Optional<Object> value;

	@Override
	public String toString() {
		return "{" + this.asString() + '}';
	}

	public String asString() {
		if (ObjectUtils.allNotNull(this.key, this.op, this.value.orElse(null))) {
			return this.key + " " + this.op + " " + this.value.get();
		}
		if (ObjectUtils.allNotNull(this.key, this.op)) {
			return this.key + " " + this.op;
		}
		if (ObjectUtils.allNotNull(this.key)) {
			return this.key;
		}
		if (ObjectUtils.allNotNull(this.op)) {
			return this.op;
		}
		return "";
	}

	public void set(final Query newVal) {
		Optional.ofNullable(newVal).ifPresent(nev -> {
			this.key = nev.getKey();
			this.op = nev.getOp();
			this.value = nev.getValue();
		});

	}

	public static Query from(final String key, final String op, final Object value) {
		return new Query(key, op, Optional.ofNullable(value));
	}

	public static Query from(final String key, final String op, final Optional<Object> value) {
		return new Query(key, op, Objects.isNull(value) ? Optional.empty() : value);
	}

	public static Query from(final String op) {
		return new Query(null, op, Optional.empty());
	}

	@Override
	public int compareTo(final Query o) {
		return this.toCompare().compareTo(o.toCompare());
	}

	public String toCompare() {
		return ObjectUtils.defaultIfNull(this.getKey(), "") + this.getValue().orElse("");
	}

	public static List<Query> parseQueries(final String input) {
		final List<Query> result = new ArrayList<>();
		if (input == null || input.isBlank()) {
			return result;
		}
		// Normalizar espacios alrededor de paréntesis para tokenización limpia
		final String normalized = input.trim().replace("(", " ( ").replace(")", " ) ").replaceAll("\\s+", " ").trim();
		final String[] tokens = normalized.split(" ");
		int i = 0;
		while (i < tokens.length) {
			final String token = tokens[i];
			// Operadores lógicos y paréntesis como tokens independientes
			if (equalsAnyIgnoreCase(token, "and", "or")) {
				result.add(Query.from(token));
				i++;
				continue;
			}
			if (token.equals("(")) {
				result.add(OPEN);
				i++;
				continue;
			}
			if (token.equals(")")) {
				result.add(CLOSE);
				i++;
				continue;
			}
			// token se interpreta como clave potencial
			final String key = token;
			// Mirar siguiente token como posible operador
			if (i + 1 < tokens.length) {
				final String maybeOp = tokens[i + 1];
				// Parada si el siguiente es lógico o paréntesis: solo clave (justAdd)
				if (equalsAnyIgnoreCase(maybeOp, "and", "or") || maybeOp.equals("(") || maybeOp.equals(")")) {
					result.add(new Query(key, null, Optional.empty()));
					i++;
					continue;
				}
				// Operadores que NO requieren valor
				if (equalsAnyIgnoreCase(maybeOp, "is-not-null")) {
					result.add(Query.from(key, maybeOp, Optional.empty()));
					i += 2;
					continue;
				}
				// Operadores que requieren valor
				if (isValueOperator(maybeOp)) {
					if (i + 2 < tokens.length) {
						final String valueToken = tokens[i + 2];
						result.add(Query.from(key, maybeOp, valueToken));
						i += 3;
						continue;
					} else {
						// No hay valor suficiente, se agrega la clave sola para no perder contexto
						result.add(new Query(key, null, Optional.empty()));
						i++;
						continue;
					}
				}
				// Si no es operador reconocido, tratarlo como clave sola
				result.add(new Query(key, null, Optional.empty()));
				i++;
			} else {
				// Último token sin pareja
				result.add(new Query(key, null, Optional.empty()));
				i++;
			}
		}
		return result;
	}

	private static boolean equalsAnyIgnoreCase(final String token, final String... values) {
		for (final String v : values) {
			if (token.equalsIgnoreCase(v)) {
				return true;
			}
		}
		return false;
	}

	private static boolean isValueOperator(final String op) {
		return equalsAnyIgnoreCase(op, "eq", "ne", "gt", "gte", "lt", "lte", "like", "ilike", "not-like", "in", "nin");
	}

	protected String getValueAsString() {
		return this.value.map(Object::toString).map(String::trim).orElse("");
	}
}
