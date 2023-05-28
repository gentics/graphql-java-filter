package com.gentics.graphqlfilter.filter.operation;

import java.util.Optional;

public abstract class AbstractOperation<T extends Sqlable> implements FilterOperation<T> {

	private Optional<String> maybeId;
	private final String initiatingFilterName;

	public AbstractOperation(Optional<String> maybeId, String initiatingFilterName) {
		super();
		this.maybeId = maybeId;
		this.initiatingFilterName = initiatingFilterName;
	}

	@Override
	public String getInitiatingFilterName() {
		return initiatingFilterName;
	}

	@Override
	public Optional<String> maybeGetFilterId() {
		return maybeId;
	}

	@Override
	public AbstractOperation<T> maybeSetFilterId(Optional<String> maybeId) {
		this.maybeId = maybeId;
		return this;
	}
}
