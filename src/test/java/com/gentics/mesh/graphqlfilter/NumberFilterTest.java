package com.gentics.mesh.graphqlfilter;

import com.gentics.mesh.graphqlfilter.util.QueryFile;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class NumberFilterTest extends AbstractFilterTest {
    @Test
    public void testEquals() {
        List<Map<String, ?>> result = queryNodesAsList(new QueryFile("number", "equals"));

        assertEquals(1, result.size());
        assertEquals("images", result.get(0).get("name"));
    }

    @Test
    public void testOneOf() {
        List<Map<String, ?>> result = queryNodesAsList(new QueryFile("number", "oneOf"));

        assertEquals(2, result.size());
        assertEquals("images", result.get(0).get("name"));
        assertEquals("Tree: Pine", result.get(1).get("name"));
    }

    @Test
    public void testGT() {
        List<Map<String, ?>> result = queryNodesAsList(new QueryFile("number", "gt"));

        assertEquals(2, result.size());
        assertEquals("Tree: Pine", result.get(0).get("name"));
        assertEquals("Tree: Oak", result.get(1).get("name"));
    }

    @Test
    public void testGTE() {
        List<Map<String, ?>> result = queryNodesAsList(new QueryFile("number", "gte"));

        assertEquals(2, result.size());
        assertEquals("Tree: Pine", result.get(0).get("name"));
        assertEquals("Tree: Oak", result.get(1).get("name"));
    }

    @Test
    public void testLT() {
        List<Map<String, ?>> result = queryNodesAsList(new QueryFile("number", "lt"));

        assertEquals(3, result.size());
        assertEquals("images", result.get(0).get("name"));
        assertEquals("Tree: Pine", result.get(1).get("name"));
        assertEquals("Fruit: Apple", result.get(2).get("name"));
    }

    @Test
    public void testLTE() {
        List<Map<String, ?>> result = queryNodesAsList(new QueryFile("number", "lte"));

        assertEquals(4, result.size());
        assertEquals("images", result.get(0).get("name"));
        assertEquals("Tree: Pine", result.get(1).get("name"));
        assertEquals("Tree: Oak", result.get(2).get("name"));
        assertEquals("Fruit: Apple", result.get(3).get("name"));
    }

    @Test
    public void testDivisble() {
        List<Map<String, ?>> result = queryNodesAsList(new QueryFile("number", "divisible"));

        assertEquals(3, result.size());
        assertEquals("images", result.get(0).get("name"));
        assertEquals("Tree: Pine", result.get(1).get("name"));
        assertEquals("Fruit: Apple", result.get(2).get("name"));
    }

    @Test
    public void testCloseTo() {
        List<Map<String, ?>> result = queryNodesAsList(new QueryFile("number", "closeTo"));

        assertEquals(1, result.size());
        assertEquals("Tree: Oak", result.get(0).get("name"));
    }
}
