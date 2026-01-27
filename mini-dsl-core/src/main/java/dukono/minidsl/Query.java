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
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Query implements Comparable<Query> {

	/**
	 * Configuration for parsing queries with custom operators.
	 */
	@Builder
	@Data
	@lombok.AllArgsConstructor(access = lombok.AccessLevel.PUBLIC)
	@lombok.NoArgsConstructor
	public static class ParseConfig {
		/**
		 * Operators that require a value (e.g., "eq", "like", "gt").
		 */
		private Set<String> valueOperators;

		/**
		 * Operators that don't require a value (e.g., "is-not-null", "is-empty").
		 */
		private Set<String> noValueOperators;

		/**
		 * Valid field names that can be used in queries. If null or empty, field
		 * validation is disabled.
		 */
		private Set<String> validFields;

		/**
		 * Logical operators (default: "and", "or").
		 */
		private Set<String> logicalOperators;

		/**
		 * If true, treats any unrecognized token after key as a potential operator. If
		 * false, only recognizes operators from valueOperators and noValueOperators.
		 */
		private boolean allowUnknownOperators;

		public boolean isValueOperator(final String op) {
			return this.valueOperators != null && this.valueOperators.stream().anyMatch(v -> v.equalsIgnoreCase(op));
		}

		public boolean isNoValueOperator(final String op) {
			return this.noValueOperators != null
					&& this.noValueOperators.stream().anyMatch(v -> v.equalsIgnoreCase(op));
		}

		public boolean isLogicalOperator(final String op) {
			return this.logicalOperators != null
					&& this.logicalOperators.stream().anyMatch(v -> v.equalsIgnoreCase(op));
		}

		public boolean isValidField(final String fieldName) {
			// Si validFields es null o vacío, no validar (modo permisivo)
			if (this.validFields == null || this.validFields.isEmpty()) {
				return true;
			}
			return this.validFields.stream().anyMatch(v -> v.equalsIgnoreCase(fieldName));
		}
	}

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

	/**
	 * Parses a query string using custom configuration. This allows parsing queries
	 * with user-defined operators.
	 * 
	 * @param input
	 *            Query string to parse
	 * @param config
	 *            Custom parse configuration with user-defined operators
	 * @return List of parsed Query objects
	 */
	public static List<Query> parseQueries(final String input, final ParseConfig config) {
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
			if (config.isLogicalOperator(token)) {
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

			// Validar si el campo es válido según la configuración
			if (!config.isValidField(key)) {
				// Campo no válido, saltar este token
				i++;
				continue;
			}

			// Mirar siguiente token como posible operador
			if (i + 1 < tokens.length) {
				final String maybeOp = tokens[i + 1];
				// Parada si el siguiente es lógico o paréntesis: solo clave (justAdd)
				if (config.isLogicalOperator(maybeOp) || maybeOp.equals("(") || maybeOp.equals(")")) {
					result.add(new Query(key, null, Optional.empty()));
					i++;
					continue;
				}
				// Operadores que NO requieren valor
				if (config.isNoValueOperator(maybeOp)) {
					result.add(Query.from(key, maybeOp, Optional.empty()));
					i += 2;
					continue;
				}
				// Operadores que requieren valor
				if (config.isValueOperator(maybeOp)) {
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
				// Si allowUnknownOperators está activado, tratar el siguiente token como
				// operador
				// y buscar un valor
				if (config.isAllowUnknownOperators()) {
					// Asumir que maybeOp es un operador personalizado
					if (i + 2 < tokens.length) {
						final String valueToken = tokens[i + 2];
						// Verificar que el valueToken no sea un operador lógico o paréntesis
						if (!config.isLogicalOperator(valueToken) && !valueToken.equals("(")
								&& !valueToken.equals(")")) {
							result.add(Query.from(key, maybeOp, valueToken));
							i += 3;
							continue;
						}
					}
					// Si no hay valor válido, tratar como operador sin valor (tipo NO_OP_WITH_ARG)
					result.add(Query.from(key, null, maybeOp));
					i += 2;
					continue;
				}
				// Si no es operador reconocido y allowUnknownOperators es false,
				// ignorar este campo y el operador inválido
				i += 2;
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

	protected String getValueAsString() {
		return this.value.map(Object::toString).map(String::trim).orElse("");
	}

	@Override
	public String toString() {
		return "{" + this.formatString() + '}';
	}

	public String formatString() {
		if (ObjectUtils.allNotNull(this.key, this.op, this.value.orElse(null))) {
			return this.formatString(" ", this.key, this.op, this.value.get());
		}
		if (ObjectUtils.allNotNull(this.key, this.op)) {
			return this.formatString(" ", this.key, this.op);
		}
		if (ObjectUtils.allNotNull(this.key, this.getValue().orElse(null))) {
			return this.formatString(" ", this.key, this.value.get());
		}
		if (ObjectUtils.allNotNull(this.key)) {
			return this.formatString(" ", this.key);
		}
		if (ObjectUtils.allNotNull(this.op)) {
			return this.formatString(" ", this.op);
		}
		return "";
	}
	private String formatString(final String delim, final Object... v) {
		return Stream.of(v).map(Object::toString).map(String::trim).collect(Collectors.joining(delim));
	}
}
