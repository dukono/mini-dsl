package dukono.minidsl;

import com.google.common.reflect.TypeToken;
import lombok.Getter;

@Getter
public class AnchorList<T>
		extends
			AnchorHolderList<Fields, AnchorOne<T>, AnchorLogicalMain, DtoString, AnchorList<T>, T> {

	public AnchorList() {
		super(DtoString.class, new TypeToken<>() {
		}, AnchorLogicalMain.class);
	}

}
