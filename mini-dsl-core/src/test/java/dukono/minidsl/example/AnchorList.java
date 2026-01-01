package dukono.minidsl.example;

import com.google.common.reflect.TypeToken;
import dukono.minidsl.AnchorHolderList;
import dukono.minidsl.DtoString;
import lombok.Getter;

@Getter
// @formatter:off
public class AnchorList<T> extends AnchorHolderList<Fields, AnchorOne<T>, AnchorLogicalMain, DtoString, AnchorList<T>, T> {

	public AnchorList() {
		super( new TypeToken<AnchorOne<T>>() {}, AnchorLogicalMain.class);
	}
	// @formatter:on
}
