package com.gentics.graphqlfilter.filter.operation;

import java.util.Optional;

/**
 * A filter operation that has an owner.
 * 
 * @author plyhun
 *
 */
public abstract class OwnedOperation implements Sqlable {

	protected final Optional<String> maybeOwner;

	public OwnedOperation(Optional<String> maybeOwner) {
		super();
		this.maybeOwner = maybeOwner;
	}

	//@Override
	public Optional<String> maybeGetOwner() {
		return maybeOwner;
	}

}
