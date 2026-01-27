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
public class RemoveBy<A extends AnchorHolderMain<?, ?, ?, ?>> {
	private TypeToken<A> classA;

	public ComparatorFullByPattern<A> fullLine() {
		return new ComparatorFullByPattern<>(this.classA);
	}

	public RemoverExactQueryMatch<A> queryMatch(final UnaryOperator<A> operator) {
		return new RemoverExactQueryMatch<>(this.classA, operator);
	}
	public ComparatorByPattern<A> comparatorMatch() {
		return new ComparatorByPattern<>(this.classA);
	}

	public abstract static class Remover {
		abstract <T extends Dto> void accept(final T dto);
	}

	@AllArgsConstructor
	public static class ComparatorFullByPattern<A extends AnchorHolderMain<?, ?, ?, ?>> {
		TypeToken<A> classA;

		public RemoverFullLineByComparator<A> byKey(final UnaryOperator<A> operator) {
			return new RemoverFullLineByComparator<>(ComparatorEnum.KEY, operator, this.classA);
		}

		public RemoverFullLineByComparator<A> byKeyValue(final UnaryOperator<A> operator) {
			return new RemoverFullLineByComparator<>(ComparatorEnum.KEY_VALUE, operator, this.classA);
		}

		public RemoverFullLineByComparator<A> byKeyOperation(final UnaryOperator<A> operator) {
			return new RemoverFullLineByComparator<>(ComparatorEnum.KEY_OP, operator, this.classA);
		}

		public RemoverFullLineByComparator<A> byOperation(final UnaryOperator<A> operator) {
			return new RemoverFullLineByComparator<>(ComparatorEnum.OP, operator, this.classA);
		}

		public RemoverFullLineByComparator<A> byFullMatch(final UnaryOperator<A> operator) {
			return new RemoverFullLineByComparator<>(ComparatorEnum.FULL, operator, this.classA);
		}
	}
	@AllArgsConstructor
	public static class ComparatorByPattern<A extends AnchorHolderMain<?, ?, ?, ?>> {
		TypeToken<A> classA;

		public RemoverComparator<A> byKey(final UnaryOperator<A> operator) {
			return new RemoverComparator<>(ComparatorEnum.KEY, operator, this.classA);
		}

		public RemoverComparator<A> byKeyValue(final UnaryOperator<A> operator) {
			return new RemoverComparator<>(ComparatorEnum.KEY_VALUE, operator, this.classA);
		}

		public RemoverComparator<A> byKeyOperation(final UnaryOperator<A> operator) {
			return new RemoverComparator<>(ComparatorEnum.KEY_OP, operator, this.classA);
		}

		public RemoverComparator<A> byOperation(final UnaryOperator<A> operator) {
			return new RemoverComparator<>(ComparatorEnum.OP, operator, this.classA);
		}

		public RemoverComparator<A> byFullMatch(final UnaryOperator<A> operator) {
			return new RemoverComparator<>(ComparatorEnum.FULL, operator, this.classA);
		}
	}

	@Builder
	@AllArgsConstructor
	public static class RemoverFullLineByComparator<B extends AnchorHolderMain<?, ?, ?, ?>> extends Remover {
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
	public static class RemoverExactQueryMatch<B extends AnchorHolderMain<?, ?, ?, ?>> extends Remover {
		private TypeToken<B> classB;

		private UnaryOperator<B> operator;

		@Override
		protected <T extends Dto> void accept(final T dto) {
			final List<Queries> filters = this.operator.apply(newType((Class<? extends B>) this.classB.getRawType()))
					.getDto().getFilters();
			dto.removeFiltersExactMatch(filters);
		}

	}
	@Builder
	@AllArgsConstructor
	public static class RemoverComparator<B extends AnchorHolderMain<?, ?, ?, ?>> extends Remover {
		private ComparatorEnum comparatorEnum;

		private UnaryOperator<B> operator;

		private TypeToken<B> classA;

		@Override
		protected <T extends Dto> void accept(final T dto) {
			final List<Queries> filters = this.operator.apply(newType((Class<? extends B>) this.classA.getRawType()))
					.getDto().getFilters();
			dto.removeFiltersQuery(filters, this.comparatorEnum);
		}

	}

}
