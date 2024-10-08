package com.gentics.graphqlfilter;

import java.util.Arrays;

import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public abstract class AbstractNegatingFilterTest extends AbstractFilterTest {
	@Parameters(name = "{index}: not={0}")
	public static Iterable<Object[]> data() {
		return Arrays.asList(new Object[][] { { false }, { true } });
	}

	@Parameter(0)
	public boolean not;

}
