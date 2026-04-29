# Filter Map
This Java library adds a stream gatherer operation which maps one value to another and only includes the output if it is not null.

## Install
### Prerequisites
- Java 25

FilterMap is uploaded to [Maven Central](https://central.sonatype.com/artifact/dev.piggle/FilterMap). Instructions for your specific build tool are there.
```groovy
// Gradle
implementation 'dev.piggle:FilterMap:<release>'
```

## How to use
Below is a comparison between filter map behaviour with and without FilterMap.
```java
import dev.piggle.filtermap.FilterMap;

// Example mapper
Function<Integer, Integer> mapper = i -> i % 2 == 0 ? i : null; // Even or else null

// Without filter map
List<Integer> mapFilter = Stream.of(...)
    .map(mapper)
    .filter(Objects::nonNull)
    .toList();

// With filter map
List<Integer> filterMap = Stream.of(...)
    .gather(FilterMap.of(mapper))
    .toList();

assertEquals(mapFilter, filterMap);
```
There should be negligible difference in performance between these two methods of mapping and filtering (unconfirmed), so whether you use FilterMap or not is mostly down to personal preference.

### Built-in FilterMap instances
```java
List<Integer> _ = Stream.of(1, 2, 3.0) // Cheeky double
            .gather(FilterMap.filterInstanceOf(Integer.class))
            .toList();

List<String> _ = Stream.of(Optional.of("SILLY"), Optional.empty())
        .gather(FilterMap.filterOptional(Function.identity())) // You'd probably find a better use than this :P
        .toList();

assertThrows(Exception.class, () -> Math.toIntExact(Long.MAX_VALUE));
List<Integer> _ = Stream.of(2L, Long.MAX_VALUE)
        .gather(
                FilterMap.filterNoException(Math::toIntExact, ex -> log.error("uh oh", ex))
        )
        .toList();
```
