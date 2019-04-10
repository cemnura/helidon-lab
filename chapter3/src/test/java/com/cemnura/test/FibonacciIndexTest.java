package com.cemnura.test;

import com.cemnura.util.Fibonacci;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class FibonacciIndexTest {

    private Integer index;
    private Integer expected;

    public FibonacciIndexTest(int index, int expected) {
        this.index = index;
        this.expected = expected;
    }

    @Parameters
    public static Collection<Object[]> loadParameters()
    {
        return Arrays.asList(new Object[][] {
                {1, 1},
                {5, 5},
                {16, 987}
        });
    }

    @Test
    public void given_index_then_expected()
    {
        Assert.assertEquals(expected, Fibonacci.index(index));
    }
}
