package com.gentics.graphqlfilter.filter.operation;

import java.util.Map;
import java.util.Optional;

import com.gentics.graphqlfilter.util.FilterUtil;

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
	private final Optional<Map<JoinPart, JoinPart>> joins;

	/**
	 * Constructor.
	 * 
	 * @param etype
	 * @param field
	 */
	public FieldOperand(T etype, String field) {
		this(etype, field, Optional.empty(), Optional.empty());
	}

	/**
	 * Use this constructor to override a default generated `_{owner_name}` field owner alies.
	 * 
	 * @param owner
	 * @param field
	 * @param alias
	 */
	public FieldOperand(T owner, String field, Optional<Map<JoinPart, JoinPart>> joins, Optional<String> alias) {
		this.owner = owner;
		this.field = field;
		this.alias = alias;
		this.joins = joins;
	}

	@Override
	public String getValue() {
		return field;
	}

	@Override
	public String toSql() {
		return alias.orElse("_" + String.valueOf(owner).toLowerCase()) + "." + field;
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
	public Map<JoinPart, JoinPart> getJoins(Map<JoinPart, JoinPart> parent) {
		return joins.map(j -> FilterUtil.addFluent(j, parent)).orElse(parent);
	}

	@Override
	public String toString() {
		return "FieldOperand [owner=" + owner + ", field=" + field + ", alias=" + alias + ", joins=" + joins + "]";
	}

	@Override
	public boolean isFieldName() {
		return true;
	}

	@Override
	public Optional<String> maybeGetOwner() {
		return Optional.ofNullable(getOwner()).map(String::valueOf);
	}
}
