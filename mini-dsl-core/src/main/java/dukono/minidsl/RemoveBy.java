package dukono.minidsl;

import com.google.common.reflect.TypeToken;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.function.UnaryOperator;

import static dukono.minidsl.AnchorHolderMain.newType;

@SuppressWarnings("ALL")
@Builder
@AllArgsConstructor
public class RemoveBy<A extends AnchorHolderMain<?, ?, ?, ?>, B extends AnchorHolderMain<?, ?, ?, ?>> {
	private TypeToken<A> classA;

	private TypeToken<B> classB;

	public ComparatorFullByPattern<A> fullLine() {
		return new ComparatorFullByPattern<>(this.classA);
	}

	public RemoverExactMatch<B> exactMatch(final UnaryOperator<B> operator) {
		return new RemoverExactMatch<>(this.classB, operator);
	}

	public abstract static class Remover {

		abstract <T extends Dto> void accept(final T dto);
	}

	@AllArgsConstructor
	public static class ComparatorFullByPattern<A extends AnchorHolderMain<?, ?, ?, ?>> {
		TypeToken<A> classA;

		public RemoverFullLine<A> byKey(final UnaryOperator<A> operator) {
			return new RemoverFullLine<>(ComparatorEnum.KEY, operator, this.classA);
		}

		public RemoverFullLine<A> byKeyValue(final UnaryOperator<A> operator) {
			return new RemoverFullLine<>(ComparatorEnum.KEY_VALUE, operator, this.classA);
		}

		public RemoverFullLine<A> byKeyOperation(final UnaryOperator<A> operator) {
			return new RemoverFullLine<>(ComparatorEnum.KEY_OP, operator, this.classA);
		}

		public RemoverFullLine<A> byOperation(final UnaryOperator<A> operator) {
			return new RemoverFullLine<>(ComparatorEnum.OP, operator, this.classA);
		}

		public RemoverFullLine<A> byFullMatch(final UnaryOperator<A> operator) {
			return new RemoverFullLine<>(ComparatorEnum.FULL, operator, this.classA);
		}
	}

	@Builder
	@AllArgsConstructor
	public static class RemoverFullLine<B extends AnchorHolderMain<?, ?, ?, ?>> extends Remover {
		private ComparatorEnum comparatorEnum;

		private UnaryOperator<B> operator;

		private TypeToken<B> classA;

		@Override
		protected <T extends Dto> void accept(final T dto) {
			final List<Queries> filters = this.operator.apply(newType((Class<? extends B>) this.classA.getRawType()))
					.getDto().getFilters();
			dto.removeFiltersFull(filters, this.comparatorEnum);
		}

	}

	@Getter
	@Builder
	@AllArgsConstructor
	public static class RemoverExactMatch<B extends AnchorHolderMain<?, ?, ?, ?>> extends Remover {
		private TypeToken<B> classB;

		private UnaryOperator<B> operator;

		@Override
		protected <T extends Dto> void accept(final T dto) {
			final List<Queries> filters = this.operator.apply(newType((Class<? extends B>) this.classB.getRawType()))
					.getDto().getFilters();
			dto.removeFiltersExactMatch(filters);
		}

	}

}
