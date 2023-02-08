package com.gentics.graphqlfilter.filter.sql2;

public interface FilterOperand<T> extends Sqlable {

	T getValue();
}
