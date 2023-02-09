package com.gentics.graphqlfilter.filter.operation;

import java.util.Collections;
import java.util.Map;

/**
 * A literal filter operand.
 * 
 * @author plyhun
 *
 * @param <T>
 */
public class LiteralOperand<T> implements FilterOperand<T> {

	private final boolean escape;
	private final T value;

	/**
	 * Constructor
	 * 
	 * @param value the literal value
	 * @param escape should the value be escaped (with the single quotes by default).
	 */
	public LiteralOperand(T value, boolean escape) {
		this.escape = escape;
		this.value = value;
	}

	@Override
	public String toSql() {
		return escape ? String.format("'%s'", value) : String.valueOf(value);
	}

	@Override
	public T getValue() {
		return value;
	}

	@Override
	public String toString() {
		return "LiteralOperand [escape=" + escape + ", value=" + value + "]";
	}

	@Override
	public Map<String, String> getJoins(Map<String, String> parent) {
		return Collections.emptyMap();
	}
}
