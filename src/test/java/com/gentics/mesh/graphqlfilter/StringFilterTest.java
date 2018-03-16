package com.gentics.mesh.graphqlfilter;

import com.gentics.mesh.graphqlfilter.util.QueryFile;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class StringFilterTest extends AbstractFilterTest {
    @Test
    public void testEquals() {
        List<Map<String, ?>> result = queryNodesAsList(new QueryFile("string", "equals"));

        assertEquals(1, result.size());
        assertEquals("e018fa14-39ed-431c-b09d-b27097b48b85", result.get(0).get("uuid"));
    }

    @Test
    public void testOneOf() {
        List<Map<String, ?>> result = queryNodesAsList(new QueryFile("string", "oneOf"));

        assertEquals(2, result.size());
    }

    @Test
    public void testRegex() {
        List<Map<String, ?>> result = queryNodesAsList(new QueryFile("string", "regex"));

        assertEquals(2, result.size());
    }
}
