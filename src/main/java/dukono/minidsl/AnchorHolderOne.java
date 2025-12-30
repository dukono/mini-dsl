package dukono.minidsl;

import com.google.common.reflect.TypeToken;
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
public abstract class AnchorHolderOne<
    F extends Field,
    S extends Dto, 
    X extends AnchorHolderOne<F,S, X, O, W>, 
    O,
    W extends AnchorOperationsBasic<X>> 
    extends AnchorHolderMainLogical<F,S, X, W>
{
  // @formatter:on

	@Getter(AccessLevel.PUBLIC)
	private O itemUnit;

	protected AnchorHolderOne(final TypeToken<S> ob, final F fields, final TypeToken<W> opClazz) {
		this.dtoClazz = ob;
		this.fields = fields;
		this.opClazz = opClazz;
	}

	@Override
	public void addQuery(final Query query) {
		this.getQueries().add(query);
	}

	@Override
	public X other() {
		super.other();
		this.queries = null;
		return (X) this;
	}

}
