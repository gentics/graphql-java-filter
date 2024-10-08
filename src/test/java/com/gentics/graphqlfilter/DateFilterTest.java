package com.gentics.graphqlfilter;

import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.gentics.graphqlfilter.util.QueryFile;

public class DateFilterTest extends AbstractNegatingFilterTest {

	@Test
	public void testEquals() {
		List<Map<String, ?>> result = queryAsList(new QueryFile("date", "equals"), not);

		assertNames(result, not, "images");
	}

	@Test
	public void testNotEquals() {
		List<Map<String, ?>> result = queryAsList(new QueryFile("date", "notEquals"), not);

		assertNames(result, not, "Tree: Pine", "Tree: Oak", "Fruit: Apple", null);
	}

	@Test
	public void testBefore() {
		List<Map<String, ?>> result = queryAsList(new QueryFile("date", "before"), not);

		assertNames(result, not, "images");
	}

	@Test
	public void testAfter() {
		List<Map<String, ?>> result = queryAsList(new QueryFile("date", "after"), not);

		assertNames(result, not, "Tree: Oak", "Fruit: Apple");
	}

	@Test
	public void testBetween() {
		List<Map<String, ?>> result = queryAsList(new QueryFile("date", "between"), not);

		assertNames(result, not, "Tree: Pine");
	}

	@Test
	public void testFuture() {
		List<Map<String, ?>> result = queryAsList(new QueryFile("date", "future"), not);

		assertNames(result, not);
	}

	@Test
	public void testPast() {
		List<Map<String, ?>> result = queryAsList(new QueryFile("date", "past"), not);

		assertNames(result, not, "images", "Tree: Pine", "Tree: Oak", "Fruit: Apple");
	}

	@Test
	public void testFormat() {
		List<Map<String, ?>> result = queryAsList(new QueryFile("date", "format"), not);

		assertNames(result, not, "Tree: Oak", "Fruit: Apple");
	}
}
