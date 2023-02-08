package com.gentics.graphqlfilter.filter.operation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gentics.graphqlfilter.util.FilterUtil;

public interface FilterOperation<T extends Sqlable> extends Sqlable {

	String getOperator();

	List<T> getOperands();

	@SuppressWarnings("unchecked")
	@Override
	default Map<String, String> getJoins(Map<String, String> parent) {
		return getOperands().stream().map(o -> o.getJoins(parent)).reduce(Map.class.cast(new HashMap<>()), (map, o) -> FilterUtil.addFluent(map, o), (a, b) -> a);
	}
}
