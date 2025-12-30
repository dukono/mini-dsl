package dukono.minidsl.example;

import dukono.minidsl.AnchorHolderMainLogical;
import dukono.minidsl.Query;
import lombok.AllArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder(builderMethodName = "buildLogical")
@AllArgsConstructor
public class AnchorOperationsLogical<H extends AnchorHolderMainLogical<?, ?, H, ?>> extends AnchorOperationsBase<H> {

	public H open() {
		return this.create(Query.OPEN);
	}

	public H close() {
		return this.create(Query.CLOSE);
	}

}
