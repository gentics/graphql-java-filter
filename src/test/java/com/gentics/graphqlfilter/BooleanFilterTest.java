package com.gentics.graphqlfilter;

import org.junit.Assert;
import org.junit.Test;

import com.gentics.graphqlfilter.util.QueryFile;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class BooleanFilterTest extends AbstractFilterTest {
	@Test
	public void testBoolean() {
		List<Map<String, ?>> result = queryNodesAsList(new QueryFile("boolean", "published"));

		assertEquals(3, result.size());

		result.stream()
			.map(node -> (Boolean) node.get("published"))
			.forEach(Assert::assertTrue);
	}
}
