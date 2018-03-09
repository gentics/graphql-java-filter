package com.gentics.mesh.graphqlfilter;

import com.gentics.mesh.graphqlfilter.util.QueryFile;
import graphql.ExecutionResult;
import graphql.GraphQLError;
import graphql.validation.ValidationError;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class NestedFilterTest extends AbstractFilterTest {
    @Test
    public void testEnum() {
        List<Map<String, ?>> nodes = queryNodesAsList(new QueryFile("nested", "enum"));
        assertEquals(3, nodes.size());
    }

    @Test
    public void testInvalidEnum() {
        ExecutionResult result = queryNodes(new QueryFile("nested", "invalidEnum"), false);
        assertEquals(1, result.getErrors().size());
        GraphQLError error = result.getErrors().get(0);
        ValidationError validationError = (ValidationError) error;
        assertEquals("WrongType", validationError.getValidationErrorType().name());
    }

    @Test
    public void testSchemaUuid() {
        List<Map<String, ?>> nodes = queryNodesAsList(new QueryFile("nested", "schemaUuid"));
        assertEquals(3, nodes.size());
    }
}
