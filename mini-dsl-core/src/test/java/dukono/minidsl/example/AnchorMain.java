package dukono.minidsl.example;

import com.google.common.reflect.TypeToken;
import dukono.minidsl.AnchorHolderMain;
import dukono.minidsl.DtoString;

public class AnchorMain extends AnchorHolderMain<Fields, DtoString, AnchorMain, AnchorOperationsBase<AnchorMain>> {

	public AnchorMain() {
		super(new TypeToken<DtoString>() {
		}, new TypeToken<AnchorList<?>>() {
		}, new TypeToken<AnchorMain>() {
		}, new TypeToken<AnchorOperationsBase<AnchorMain>>() {
		}, new Fields());
	}

}
