package com.gentics.graphqlfilter.test;

import com.gentics.graphqlfilter.test.util.QueryFile;
import graphql.ExecutionResult;
import org.junit.Test;

public class StringFilterTest extends AbstractFilterTest {
    @Test
    public void testEquals() {
        ExecutionResult result = queryNodes(new QueryFile("string", "eq"));
        Object data = result.getData();
    }
}
