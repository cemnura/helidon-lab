package com.cemnura.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.ObjIntConsumer;
import java.util.function.Supplier;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Fibonacci {

    public static List<Integer> createSequence(int sequenceCount)
    {
        List<Integer> sequence = new ArrayList<>();

        sequence.add(0);
        sequence.add(1);
        sequence.add(1);

        for (int i = 3; i < sequenceCount + 1; i++) {
            sequence.add(sequence.get(i - 1) + sequence.get(i - 2));
        }

        return sequence;
    }

    public static Integer index(int index)
    {
        return createSequence(index).get(index);
    }

}
