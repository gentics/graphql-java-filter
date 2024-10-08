package com.gentics.graphqlfilter;

import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.gentics.graphqlfilter.util.QueryFile;

public class BooleanFilterTest extends AbstractNegatingFilterTest {

	@Test
	public void testTrue() {
		List<Map<String, ?>> result = queryAsList(new QueryFile("boolean", "published"), not);

		assertNames(result, not, "images", "Tree: Pine", "Fruit: Apple");
	}

	@Test
	public void testFalse() {
		List<Map<String, ?>> result = queryAsList(new QueryFile("boolean", "unpublished"), not);

		assertNames(result, not, "Tree: Oak");
	}
}
