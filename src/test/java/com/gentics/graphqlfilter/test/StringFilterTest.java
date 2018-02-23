package com.gentics.graphqlfilter.test;

import com.gentics.graphqlfilter.test.util.QueryFile;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class StringFilterTest extends AbstractFilterTest {
    @Test
    public void testEquals() {
        List<Map<String, ?>> result = queryNodesAsList(new QueryFile("string", "eq"));

        assertEquals(1, result.size());
        assertEquals("1", result.get(0).get("id"));
    }

    @Test
    public void testOneOf() {
        List<Map<String, ?>> result = queryNodesAsList(new QueryFile("string", "oneOf"));

        assertEquals(2, result.size());
    }
}
