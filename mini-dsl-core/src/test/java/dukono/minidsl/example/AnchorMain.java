package dukono.minidsl.example;

import com.google.common.reflect.TypeToken;
import dukono.minidsl.AnchorHolderMain;
import dukono.minidsl.DtoString;

public class AnchorMain extends AnchorHolderMain<Fields, DtoString, AnchorMain, AnchorOperationsBase<AnchorMain>> {

	public AnchorMain() {
		super(new TypeToken<>() {
		}, new TypeToken<AnchorList<?>>() {
		}, new TypeToken<>() {
		}, new Fields());
	}

}
