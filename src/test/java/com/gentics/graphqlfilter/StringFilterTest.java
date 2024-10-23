package com.gentics.graphqlfilter;

import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.gentics.graphqlfilter.util.QueryFile;

public class StringFilterTest extends AbstractNegatingFilterTest {

	@Test
	public void testEquals() {
		List<Map<String, ?>> result = queryAsList(new QueryFile("string", "equals"), not);

		assertNames(result, not, "images");
	}

	@Test
	public void testNotEquals() {
		List<Map<String, ?>> result = queryAsList(new QueryFile("string", "notEquals"), not);

		assertNames(result, not, "Tree: Pine", "Tree: Oak", "Fruit: Apple", null);
	}

	@Test
	public void testOneOf() {
		List<Map<String, ?>> result = queryAsList(new QueryFile("string", "oneOf"), not);

		assertNames(result, not, "images", "Tree: Pine");
	}

	@Test
	public void testRegex() {
		List<Map<String, ?>> result = queryAsList(new QueryFile("string", "regex"), not);

		assertNames(result, not, "Tree: Oak", "Tree: Pine");
	}
}
