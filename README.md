# Filter Map
This Java library adds a stream gatherer operation which maps one value to another and only includes the output if it is not null.

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
}
```
There is a negligible difference in performance between these two methods of mapping and filtering, so whether you use FilterMap or not is mostly down to personal preference.
