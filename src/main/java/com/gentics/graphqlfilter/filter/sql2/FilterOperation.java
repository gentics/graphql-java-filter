package com.gentics.graphqlfilter.filter.sql2;

import java.util.List;

public interface FilterOperation<T extends Sqlable> extends Sqlable {

	String getOperator();

	List<T> getOperands();

}
