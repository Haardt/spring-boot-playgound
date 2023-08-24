package com.sft.util;

import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class StreamUtils {
    public static <T> Stream<T> toStream(Iterable<T> iterator) {
        return StreamSupport.stream(iterator.spliterator(), false);
    }
}
