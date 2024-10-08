package com.gentics.graphqlfilter;

import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.gentics.graphqlfilter.util.QueryFile;

public class NumberFilterTest extends AbstractNegatingFilterTest {

	@Test
	public void testEquals() {
		List<Map<String, ?>> result = queryAsList(new QueryFile("number", "equals"), not);

		assertNames(result, not, "images");
	}

	@Test
	public void testNotEquals() {
		List<Map<String, ?>> result = queryAsList(new QueryFile("number", "notEquals"), not);

		assertNames(result, not, "Tree: Pine", "Tree: Oak", "Fruit: Apple", null);
	}

	@Test
	public void testOneOf() {
		List<Map<String, ?>> result = queryAsList(new QueryFile("number", "oneOf"), not);

		assertNames(result, not, "images", "Tree: Pine");
	}

	@Test
	public void testGT() {
		List<Map<String, ?>> result = queryAsList(new QueryFile("number", "gt"), not);

		assertNames(result, not, "Tree: Pine", "Tree: Oak");
	}

	@Test
	public void testGTE() {
		List<Map<String, ?>> result = queryAsList(new QueryFile("number", "gte"), not);

		assertNames(result, not, "Tree: Pine", "Tree: Oak");
	}

	@Test
	public void testLT() {
		List<Map<String, ?>> result = queryAsList(new QueryFile("number", "lt"), not);

		assertNames(result, not, "images", "Tree: Pine", "Fruit: Apple");
	}

	@Test
	public void testLTE() {
		List<Map<String, ?>> result = queryAsList(new QueryFile("number", "lte"), not);

		assertNames(result, not, "images", "Tree: Pine", "Tree: Oak", "Fruit: Apple");
	}

	@Test
	public void testDivisible() {
		List<Map<String, ?>> result = queryAsList(new QueryFile("number", "divisible"), not);

		assertNames(result, not, "images", "Tree: Pine", "Fruit: Apple");
	}

	@Test
	public void testCloseTo() {
		List<Map<String, ?>> result = queryAsList(new QueryFile("number", "closeTo"), not);

		assertNames(result, not, "Tree: Oak");
	}
}
