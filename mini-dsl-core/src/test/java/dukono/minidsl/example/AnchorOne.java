package dukono.minidsl.example;

import com.google.common.reflect.TypeToken;
import dukono.minidsl.AnchorHolderOne;
import dukono.minidsl.DtoString;

public class AnchorOne<T>
		extends
			AnchorHolderOne<Fields, DtoString, AnchorOne<T>, T, AnchorOperationsOne<AnchorOne<T>, T>> {

	public AnchorOne() {
		super(new TypeToken<>() {
		}, new Fields(), new TypeToken<>() {
		});
	}

}
