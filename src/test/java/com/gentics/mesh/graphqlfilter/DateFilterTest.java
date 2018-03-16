package com.gentics.mesh.graphqlfilter;

import com.gentics.mesh.graphqlfilter.util.QueryFile;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class DateFilterTest extends AbstractFilterTest {
    @Test
    public void testEquals() {
        List<Map<String, ?>> result = queryNodesAsList(new QueryFile("date", "equals"));

        assertEquals(1, result.size());
        assertEquals("images", result.get(0).get("name"));
    }

    @Test
    public void testBefore() {
        List<Map<String, ?>> result = queryNodesAsList(new QueryFile("date", "before"));

        assertEquals(1, result.size());
        assertEquals("images", result.get(0).get("name"));
    }

    @Test
    public void testAfter() {
        List<Map<String, ?>> result = queryNodesAsList(new QueryFile("date", "after"));

        assertEquals(2, result.size());
        assertEquals("Tree: Oak", result.get(0).get("name"));
        assertEquals("Fruit: Apple", result.get(1).get("name"));
    }

    @Test
    public void testBetween() {
        List<Map<String, ?>> result = queryNodesAsList(new QueryFile("date", "between"));

        assertEquals(1, result.size());
        assertEquals("Tree: Pine", result.get(0).get("name"));
    }

    @Test
    public void testFuture() {
        List<Map<String, ?>> result = queryNodesAsList(new QueryFile("date", "future"));

        assertEquals(0, result.size());
    }

    @Test
    public void testPast() {
        List<Map<String, ?>> result = queryNodesAsList(new QueryFile("date", "past"));

        assertEquals(4, result.size());
    }

    @Test
    public void testFormat() {
        List<Map<String, ?>> result = queryNodesAsList(new QueryFile("date", "format"));

        assertEquals(2, result.size());
        assertEquals("Tree: Oak", result.get(0).get("name"));
        assertEquals("Fruit: Apple", result.get(1).get("name"));
    }
}
