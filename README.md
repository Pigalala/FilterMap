# Filter Map
This Java library adds a stream gatherer operation which maps one value to another and only includes the output if it is not null.

## Install
FilterMap is uploaded to [Maven Central](https://central.sonatype.com/artifact/dev.piggle/FilterMap). Instructions for your specific build tool are there.

```groovy
implementation 'dev.piggle:FilterMap:<release>'
```

## How to use
Below is a comparison between filter map behaviour with and without FilterMap.
The FilterMap class contains static methods with some common mappers for instanceof and optionals.
```java
import dev.piggle.filtermap.FilterMap;

void example() {
    // Without filter map
    List<?> mapFilter = Stream.of(...)
        .map(mapper)
        .filter(Objects::nonNull)
        .toList();

    // With filter map
    List<?> filterMap = Stream.of(...)
        .gather(FilterMap.of(mapper)) // Same mapper as before
        .toList();
    
    List<Integer> integers = Stream.of(1, 2, 3.0) // Cheeky double
            .gather(FilterMap.filterInstanceOf(Integer.class)) // Only keeps the integers
            // This is now a stream of integers due to the filter map above
            .toList();
}
```
There should be negligible difference in performance between these two methods of mapping and filtering (unconfirmed), so whether you use FilterMap or not is mostly down to personal preference.
