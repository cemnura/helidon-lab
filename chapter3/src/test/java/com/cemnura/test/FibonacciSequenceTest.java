package com.cemnura.test;

import com.cemnura.util.Fibonacci;
import org.hamcrest.MatcherAssert;
import static org.hamcrest.CoreMatchers.*;

import org.junit.Assert;
import org.junit.Test;
import org.junit.matchers.JUnitMatchers;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;


import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@RunWith(Parameterized.class)
public class FibonacciSequenceTest {

    private int index;
    private List<Integer> expected_sequence;

    public FibonacciSequenceTest(int index, List<Integer> expected_sequence) {
        this.index = index;
        this.expected_sequence = expected_sequence;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> loadParameters()
    {
        return Arrays.asList(new Object[][] {
                {1, Arrays.asList(0, 1, 1)},
                {5, Arrays.asList(0, 1, 1, 2, 3, 5)},
                {16, Arrays.asList(0, 1, 1, 2, 3, 5, 8, 13, 21, 34, 55, 89, 144, 233, 377, 610, 987)}

        });
    }

    @Test
    public void given_index_then_sequence()
    {
        Assert.assertThat(Fibonacci.createSequence(index), is(expected_sequence));
    }
}
