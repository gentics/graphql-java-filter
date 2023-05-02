package com.gentics.graphqlfilter.filter.operation;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

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

	@SuppressWarnings("unchecked")
	@Override
	public String toSql() {
		if (value != null && value instanceof Iterable) {
			return (String) StreamSupport.stream(Iterable.class.cast(value).spliterator(), false).map(this::format).collect(Collectors.joining(",", "[", "]"));
		} else {
			return format(value);
		}
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
	public Set<Join> getJoins(Set<Join> parent) {
		return parent;
	}

	@Override
	public boolean isFieldName() {
		return false;
	}

	@Override
	public Optional<String> maybeGetOwner() {
		return Optional.empty();
	}

	private String format(Object value) {
		return escape ? String.format("'%s'", value) : String.valueOf(value);
	}
}
