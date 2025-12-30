package dukono.minidsl;

import com.google.common.reflect.TypeToken;

public class AnchorOne<T>
		extends
			AnchorHolderOne<Fields, DtoString, AnchorOne<T>, T, AnchorOperationsOneC<AnchorOne<T>, T>> {

	public AnchorOne() {
		super(new TypeToken<DtoString>() {
		}, new Fields(), new TypeToken<AnchorOperationsOneC<AnchorOne<T>, T>>() {
		});
	}

}
