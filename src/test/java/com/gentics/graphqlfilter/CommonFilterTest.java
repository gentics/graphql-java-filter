package com.gentics.graphqlfilter;

import org.junit.Assert;
import org.junit.Test;

import com.gentics.graphqlfilter.util.QueryFile;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class CommonFilterTest extends AbstractFilterTest {

	@Test
	public void testSingleAnd() {
		List<Map<String, ?>> nodes = queryNodesAsList(new QueryFile("common", "and", "single"));
		assertEquals(1, nodes.size());
		assertEquals("1f9c42ed-506d-481d-b31e-1a9466e31a81", nodes.get(0).get("uuid"));
	}

	@Test
	public void testEmptyAnd() {
		List<Map<String, ?>> nodes = queryNodesAsList(new QueryFile("common", "and", "empty"));
		assertEquals(testData.size(), nodes.size());
	}

	@Test
	public void testBogusAnd() {
		List<Map<String, ?>> nodes = queryNodesAsList(new QueryFile("common", "and", "bogus"));
		assertEquals(0, nodes.size());
	}

	@Test
	public void testSimpleAnd() {
		List<Map<String, ?>> nodes = queryNodesAsList(new QueryFile("common", "and", "simple"));
		assertEquals(2, nodes.size());
		nodes.stream()
			.map(node -> ((String) node.get("name")).startsWith("Tree"))
			.forEach(Assert::assertTrue);
	}

	@Test
	public void testSingleOr() {
		List<Map<String, ?>> nodes = queryNodesAsList(new QueryFile("common", "or", "single"));
		assertEquals(1, nodes.size());
		assertEquals("1f9c42ed-506d-481d-b31e-1a9466e31a81", nodes.get(0).get("uuid"));
	}

	@Test
	public void testEmptyOr() {
		List<Map<String, ?>> nodes = queryNodesAsList(new QueryFile("common", "or", "empty"));
		assertEquals(testData.size(), nodes.size());
	}

	@Test
	public void testMultipleOr() {
		List<Map<String, ?>> nodes = queryNodesAsList(new QueryFile("common", "or", "multiple"));
		assertEquals(1, nodes.size());
	}

	@Test
	public void testSimpleOr() {
		List<Map<String, ?>> nodes = queryNodesAsList(new QueryFile("common", "or", "simple"));
		assertEquals(2, nodes.size());
	}

	@Test
	public void testIsNull() {
		List<Map<String, ?>> nodes = queryNodesAsList(new QueryFile("common", "isNull"));
		assertEquals(1, nodes.size());
	}
}
