package dev.piggle.filtermap;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Gatherer;

@NullMarked
public final class FilterMap<T, U> implements Gatherer<T, Void, U> {

    private final Function<T, @Nullable U> mapper;

    private FilterMap(Function<T, @Nullable U> mapper) {
        this.mapper = mapper;
    }

    @SuppressWarnings("DataFlowIssue")
    @Override
    public Supplier<Void> initializer() {
        return () -> null;
    }

    @Override
    public Integrator<Void, T, U> integrator() {
        return (_, in, downstream) -> {
            U out = mapper.apply(in);
            if (out != null) {
                downstream.push(out);
            }

            return true;
        };
    }

    public static <T, U> FilterMap<T, U> of(Function<T, @Nullable U> mapper) {
        return new FilterMap<>(mapper);
    }
}
