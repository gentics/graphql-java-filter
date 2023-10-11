package com.gentics.graphqlfilter;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.junit.Test;

import com.gentics.graphqlfilter.util.QueryFile;

import graphql.ExecutionResult;
import graphql.GraphQL;
import graphql.GraphQLError;

/**
 * Test for multithreaded building and executing GraphQL
 * This test must be run in a new JVM, because threading issues might be caused by initial generation of some shared objects
 */
public class MultithreadingTest {
	/**
	 * Number of threads
	 */
	protected final static int THREADS = 10;

	/**
	 * Test that building and executing graphQl is thread-safe
	 * @throws InterruptedException
	 * @throws ExecutionException
	 * @throws TimeoutException
	 */
	@Test
	public void testParallelBuildingAndExecuting() throws InterruptedException, ExecutionException, TimeoutException {
		ExecutorService service = Executors.newFixedThreadPool(THREADS);
		final String query = new QueryFile("string", "equals").getQuery();

		List<Future<?>> futures = new ArrayList<>();

		for (int i = 0; i < THREADS; i++) {
			futures.add(service.submit(() -> {
				GraphQL graphQL = AbstractFilterTest.buildGraphQL();
				ExecutionResult result = graphQL.execute(query);
				List<GraphQLError> errors = result.getErrors();
				assertThat(errors).as("Errors").isEmpty();
			}));
		}

		for (Future<?> future : futures) {
			future.get(10, TimeUnit.SECONDS);
		}

		service.shutdown();
		service.awaitTermination(1, TimeUnit.SECONDS);
	}
}
