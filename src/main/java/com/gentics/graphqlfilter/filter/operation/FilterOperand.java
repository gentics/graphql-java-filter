package com.gentics.graphqlfilter.filter.operation;

public interface FilterOperand<T> extends Sqlable {

	T getValue();
}
