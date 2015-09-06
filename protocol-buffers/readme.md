# Windows 7 i5 3570K @3700 GHz 16 GB Memory Java 1.8.0_60

## Jackson Statistics
| What             | Total Time in MS             | Average Time in NS     | Slowest in NS             | Slowest in %               |Fastest in NS             | Fastest in % |
|:-----------------|-----------------------------:|-----------------------:| -------------------------:| --------------------------:|-------------------------:|-------------:|
| Build Message:   | 37      |  37      |  1064342      |  0,00      |  0      |  88,77|
| Serialization:   | 572      |  572      |  36418028      |  0,00      |  301      |  40,49|
| Deserialization: | 961      |  961      |  23803135      |  0,00      |  602      |  28,11|
* Message Size: 95 bytes
* Total time: 1570 ms

## Squareup Wire
| What             | Total Time in MS             | Average Time in NS     | Slowest in NS             | Slowest in %               |Fastest in NS             | Fastest in % |
|:-----------------|-----------------------------:|-----------------------:| -------------------------:| --------------------------:|-------------------------:|-------------:|
| Build Message:   | 74      |  74      |  3220734      |  0,00      |  0      |  83,73|
| Serialization:   | 384      |  384      |  29586241      |  0,00      |  0      |  0,22|
| Deserialization: | 467      |  467      |  4993432      |  0,00      |  301      |  61,36|
* Message Size: 14 bytes
* Total time: 925 ms

## Protocol Buffers Statistics
| What             | Total Time in MS             | Average Time in NS     | Slowest in NS             | Slowest in %               |Fastest in NS             | Fastest in % |
|:-----------------|-----------------------------:|-----------------------:| -------------------------:| --------------------------:|-------------------------:|-------------:|
| Build Message:   | 74      |  74      |  729439      |  0,00      |  0      |  81,50|
| Serialization:   | 127      |  127      |  1634461      |  0,00      |  0      |  65,34|
| Deserialization: | 182      |  182      |  3749291      |  0,00      |  0      |  49,22|
* Message Size: 14 bytes
* Total time: 383 ms


------------------------------------------------------------

# Mac Book Pro i7 16 GB Memory Java 1.8.0_60
## Jackson Statistics
* Build Message:    64 ms avg for 1:  64ns
* Serialization:   626 ms avg for 1: 626ns
* Deserialization: 999 ms avg for 1: 999ns
* Message Size:     95 bytes

## Protocol Buffer Statistics
* Build Message:    83 ms avg for 1:  83ns
* Serialization:   134 ms avg for 1: 134ns
* Deserialization: 178 ms avg for 1: 178ns
* Message Size:     14 bytes

## Squareup Wire Statistics
* Build Message:    86 ms avg for 1:  86ns
* Serialization:   438 ms avg for 1: 438ns
* Deserialization: 427 ms avg for 1: 427ns
* Message Size:     14 bytes