package dev.piggle.filtermap;

import org.jspecify.annotations.Nullable;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Gatherer;

public final class FilterMap<T, U> implements Gatherer<T, Void, U> {

    private final Function<T, @Nullable U> mapper;

    public FilterMap(Function<T, @Nullable U> mapper) {
        this.mapper = mapper;
    }

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

    public static <T, U> FilterMap<T, U> filterOptional(Function<T, Optional<U>> mapper) {
        return new FilterMap<>(in -> mapper.apply(in).orElse(null));
    }

    @SuppressWarnings("unchecked")
    public static <T, U> FilterMap<T, U> filterInstanceOf(Class<U> target) {
        return new FilterMap<>(in -> {
            if (target.isInstance(in)) {
                return (U) in;
            }

            return null;
        });
    }
}
