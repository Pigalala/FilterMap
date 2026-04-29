package dev.piggle;

import dev.piggle.filtermap.FilterMap;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FilterMapTest {

    @Test
    void testMap() {
        List<Integer> lengths = Stream.of("a", "bb", "ccc")
                .gather(FilterMap.of(String::length))
                .toList();

        assertEquals(lengths, List.of(1, 2, 3));
    }

    @Test
    void testEmpty() {
        List<Integer> lengths = Stream.<String>of()
                .gather(FilterMap.of(String::length))
                .toList();

        assertEquals(lengths, List.of());
    }

    @Test
    void testFilter() {
        List<String> strings = Stream.of("a", null, "ccc", null)
                .gather(FilterMap.of(Function.identity()))
                .toList();

        assertEquals(strings, List.of("a", "ccc"));
    }

    @Test
    void testFilterAndMap() {
        List<Integer> lengths = Stream.of("a", "bb", "ccc", "dddd")
                .gather(FilterMap.of(s -> {
                    int length = s.length();
                    if (length > 2) {
                        return null;
                    }
                    return length;
                }))
                .toList();

        assertEquals(lengths, List.of(1, 2));
    }

    @Test
    void testOptional() {
        List<String> things = Stream.of("lala", null, "hmmm", null, null)
                .gather(FilterMap.filterOptional(Optional::ofNullable))
                .toList();

        assertEquals(things, List.of("lala", "hmmm"));
    }

    @Test
    void testInstanceOf() {
        List<Integer> integers = Stream.of(1, 2, 0.0, 2f, 3, 4L)
                .gather(FilterMap.filterInstanceOf(Integer.class))
                .toList();

        assertEquals(integers, List.of(1, 2, 3));
    }

    @Test
    void testExceptions() {
        assertThrows(Exception.class, () -> Math.toIntExact(Long.MAX_VALUE));

        List<Integer> what = Stream.of(2L, Long.MAX_VALUE /* This should cause an exception */)
                .gather(FilterMap.filterNoException(Math::toIntExact, _ -> {}))
                .toList();

        assertEquals(what, List.of(2));
    }
}
