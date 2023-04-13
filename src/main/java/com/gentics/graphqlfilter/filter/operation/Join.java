package com.gentics.graphqlfilter.filter.operation;

import java.util.Objects;

/**
 * A table join.
 * 
 * @author plyhun
 *
 */
public class Join {

	private final JoinPart left;
	private final JoinPart right;

	public Join(JoinPart left, JoinPart right) {
		this.left = left;
		this.right = right;
	}

	public JoinPart getLeft() {
		return left;
	}

	public JoinPart getRight() {
		return right;
	}

	@Override
	public int hashCode() {
		return Objects.hash(left, right);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Join other = (Join) obj;
		return Objects.equals(left, other.left) && Objects.equals(right, other.right);
	}
}
