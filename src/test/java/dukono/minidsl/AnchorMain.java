package dukono.minidsl;

import com.google.common.reflect.TypeToken;

public class AnchorMain extends AnchorHolderMain<Fields, DtoString, AnchorMain, AnchorOperationsBasicC<AnchorMain>> {

	public AnchorMain() {
		super(new TypeToken<DtoString>() {
		}, new TypeToken<AnchorList<?>>() {
		}, new TypeToken<AnchorMain>() {
		}, new TypeToken<AnchorOperationsBasicC<AnchorMain>>() {
		}, new Fields());
	}

}
