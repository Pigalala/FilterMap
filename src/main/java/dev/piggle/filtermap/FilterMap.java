package dev.piggle.filtermap;

import org.jspecify.annotations.Nullable;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Gatherer;

/// Filter map implementation.
///
/// Also contains static factory methods.
///
/// @param <T> Input type of mapping function
/// @param <U> Output type of mapping function
public final class FilterMap<T, U> implements Gatherer<T, Void, U> {

    private final Function<T, @Nullable U> mapper;

    /// @param mapper The mapper function from T to U. An output (U) is discarded if null
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

    /// Creates a FilterMap instance from a mapping function.
    /// @see FilterMap#FilterMap(Function)
    public static <T, U> FilterMap<T, U> of(Function<T, @Nullable U> mapper) {
        return new FilterMap<>(mapper);
    }

    /// Creates a FilterMap instance where empty optionals are discarded
    public static <T, U> FilterMap<T, U> filterOptional(Function<T, Optional<U>> mapper) {
        return new FilterMap<>(in -> mapper.apply(in).orElse(null));
    }

    /// Creates a FilterMap instance where instances of a given class are mapped, the rest are discarded.
    /// @param target The class that each object will be after the map
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
