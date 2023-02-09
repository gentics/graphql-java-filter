package com.gentics.graphqlfilter.filter.operation;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

/**
 * A table field operand.
 * 
 * @author plyhun
 *
 * @param <T> table type
 */
public class FieldOperand<T> implements FilterOperand<String> {

	private final T owner;
	private final String field;
	private final Optional<String> alias;

	/**
	 * Constructor.
	 * 
	 * @param etype
	 * @param field
	 */
	public FieldOperand(T etype, String field) {
		this(etype, field, Optional.empty());
	}

	/**
	 * Use this constructor to override a default generated `_{owner_name}` field owner alies.
	 * 
	 * @param owner
	 * @param field
	 * @param alias
	 */
	public FieldOperand(T owner, String field, Optional<String> alias) {
		this.owner = owner;
		this.field = field;
		this.alias = alias;
	}

	@Override
	public String getValue() {
		return field;
	}

	@Override
	public String toSql() {
		return alias.orElse("_" + String.valueOf(owner).toLowerCase()) + "." + field;
	}

	@Override
	public String toString() {
		return "FieldOperand [owner=" + owner + ", field=" + field + ", alias=" + alias + "]";
	}

	/**
	 * Get an owner type.
	 * 
	 * @return
	 */
	public T getOwner() {
		return owner;
	}

	@Override
	public Map<String, String> getJoins(Map<String, String> parent) {
		return Collections.singletonMap(String.valueOf(owner), field);
	}
}
