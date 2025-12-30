package dukono.minidsl;

import lombok.AllArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder(builderMethodName = "buildLogical")
@AllArgsConstructor
public class AnchorOperationsLogicalC<H extends AnchorHolderMainLogical<?, ?, H, ?>> extends AnchorOperationsBasicC<H> {

	public H open() {
		return this.newInstance(Query.OPEN);
	}

	public H close() {
		return this.newInstance(Query.CLOSE);
	}

}
