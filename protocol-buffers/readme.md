# Links

[Blog & Discussion](http://sterl.org/2015/09/serialization-jackson-vs-google-protocol-buffers/)

# Mac Book Pro i7 2,5 GHz 16 GB Memory 1.8.0_51-b16
## Jackson Statistics
| What             | Total Time in MS             | Average Time in NS     | Slowest in NS             | Slowest in %               |Fastest in NS             | Fastest in % |
|:-----------------|-----------------------------:|-----------------------:| -------------------------:| --------------------------:|-------------------------:|-------------:|
| Build Message:   | 96          |      96          |      999223          |      0,0001        |      58          |      0,0006|
| Serialization:   | 1264        |      1264        |      57233600        |      0,0001        |      930         |      0,0001|
| Deserialization: | 2037        |      2037        |      26793114        |      0,0001        |      1585        |      0,0001|
* Message Size: 396 bytes
* Total time: 3397 ms

## Protocol Buffers Statistics
| What             | Total Time in MS             | Average Time in NS     | Slowest in NS             | Slowest in %               |Fastest in NS             | Fastest in % |
|:-----------------|-----------------------------:|-----------------------:| -------------------------:| --------------------------:|-------------------------:|-------------:|
| Build Message:   | 186        |      186        |      500976         |      0,0001         |      128        |      0,0001|
| Serialization:   | 676        |      676        |      4489810        |      0,0001         |      529        |      0,0001|
| Deserialization: | 622        |      622        |      2541217        |      0,0001         |      439        |      0,0001|
* Message Size: 214 bytes
* Total time: 1484 ms

## Squareup Wire Statistics
| What             | Total Time in MS             | Average Time in NS     | Slowest in NS             | Slowest in %               |Fastest in NS             | Fastest in % |
|:-----------------|-----------------------------:|-----------------------:| -------------------------:| --------------------------:|-------------------------:|-------------:|
| Build Message:   | 274         |      274         |      7213193        |      0,0001        |      185         |      0,0001|
| Serialization:   | 1329        |      1329        |      4584036        |      0,0001        |      1105        |      0,0001|
| Deserialization: | 1237        |      1237        |      4338794        |      0,0001        |      972         |      0,0001|
* Message Size: 214 bytes
* Total time: 2840 ms

# AWS c3.large 2 CPUs 3.75 GB Memory, Java 1.8.0_60
## Jackson Statistics
| What             | Total Time in MS             | Average Time in NS     | Slowest in NS             | Slowest in %               |Fastest in NS             | Fastest in % |
|:-----------------|-----------------------------:|-----------------------:| -------------------------:| --------------------------:|-------------------------:|-------------:|
| Build Message:   | 793         |      793         |      2356681         |      0.0001        |      700         |      0.0001|
| Serialization:   | 2725        |      2725        |      76098740        |      0.0001        |      2252        |      0.0001|
| Deserialization: | 4537        |      4537        |      43503766        |      0.0001        |      3717        |      0.0001|
* Message Size: 396 bytes
* Total time: 8055 ms

## Protocol Buffers Statistics
| What             | Total Time in MS             | Average Time in NS     | Slowest in NS             | Slowest in %               |Fastest in NS             | Fastest in % |
|:-----------------|-----------------------------:|-----------------------:| -------------------------:| --------------------------:|-------------------------:|-------------:|
| Build Message:   | 1034        |      1034        |      8056119        |      0.0001        |      885         |      0.0006|
| Serialization:   | 1682        |      1682        |      4171363        |      0.0001        |      1443        |      0.0001|
| Deserialization: | 1795        |      1795        |      2961406        |      0.0001        |      1456        |      0.0002|
* Message Size: 214 bytes
* Total time: 4511 ms

## Squareup Wire Statistics
| What             | Total Time in MS             | Average Time in NS     | Slowest in NS             | Slowest in %               |Fastest in NS             | Fastest in % |
|:-----------------|-----------------------------:|-----------------------:| -------------------------:| --------------------------:|-------------------------:|-------------:|
| Build Message:   | 1259        |      1259        |      11466607        |      0.0001        |      1018        |      0.0001|
| Serialization:   | 2909        |      2909        |      10472528        |      0.0001        |      2429        |      0.0001|
| Deserialization: | 3199        |      3199        |      7917387         |      0.0001        |      2459        |      0.0001|
* Message Size: 214 bytes
* Total time: 7367 ms

# Android 5.0.2 HTC One -- 10.000 Cycles
## Jackson Statistics
| What             | Total Time in MS             | Average Time in NS     | Slowest in NS             | Slowest in %               |Fastest in NS             | Fastest in % |
|:-----------------|-----------------------------:|-----------------------:| -------------------------:| --------------------------:|-------------------------:|-------------:|
| Build Message:   | 109         |      10944         |      8820387        |      0,0100        |      0        |      69,0400|
| Serialization:   | 1630        |      163088        |      65710362        |      0,0100        |      122081        |      15,9600|
| Deserialization: | 2635        |      263534        |      42850602        |      0,0100        |      213642        |      12,7700|
* Message Size: 396 bytes
* Total time: 4374 ms

## Protocol Buffers Statistics
| What             | Total Time in MS             | Average Time in NS     | Slowest in NS             | Slowest in %               |Fastest in NS             | Fastest in % |
|:-----------------|-----------------------------:|-----------------------:| -------------------------:| --------------------------:|-------------------------:|-------------:|
| Build Message:   | 283        |      28368         |      3204639        |      0,0100        |      0        |      14,5800|
| Serialization:   | 1349       |      134967        |      8026857        |      0,0100        |      91561        |      2,0500|
| Deserialization: | 511        |      51139         |      2289028        |      0,0100        |      30520        |      26,0600|
* Message Size: 214 bytes
* Total time: 2143 ms
 
## Squareup Wire Statistics
| What             | Total Time in MS             | Average Time in NS     | Slowest in NS             | Slowest in %               |Fastest in NS             | Fastest in % |
|:-----------------|-----------------------------:|-----------------------:| -------------------------:| --------------------------:|-------------------------:|-------------:|
| Build Message:   | 576        |      57649        |      9217152        |      0,0100        |      30520        |      17,4700|
| Serialization:   | 2229       |      222911        |      13062719        |      0,0100        |      183122        |      28,4200|
| Deserialization: | 2336       |      233609        |      14771860        |      0,0100        |      183122        |      12,9200|
* Message Size: 214 bytes
* Total time: 5141 ms

# Android 5.0.2 HTC One -- 100.000 Cycles
## Jackson Statistics
| What             | Total Time in MS             | Average Time in NS     | Slowest in NS             | Slowest in %               |Fastest in NS             | Fastest in % |
|:-----------------|-----------------------------:|-----------------------:| -------------------------:| --------------------------:|-------------------------:|-------------:|
| Build Message:   | 1011        |      10117        |      15351747        |      0,0010        |      0        |      69,7920|
| Serialization:   | 15500        |      155009        |      78803602        |      0,0010        |      122081        |      16,4510|
| Deserialization: | 25741        |      257412        |      34671143        |      0,0010        |      213642        |      11,4890|
* Message Size: 396 bytes
* Total time: 42252 ms

## Protocol Buffers Statistics
| What             | Total Time in MS             | Average Time in NS     | Slowest in NS             | Slowest in %               |Fastest in NS             | Fastest in % |
|:-----------------|-----------------------------:|-----------------------:| -------------------------:| --------------------------:|-------------------------:|-------------:|
| Build Message:   | 2905        |      29054        |      32626279         |      0,0010        |      0            |      14,6030|
| Serialization:   | 13630       |      136306       |      15077064         |      0,0010        |      91561        |      1,0850|
| Deserialization: | 5108        |      51080        |      13337402         |      0,0010        |      30520        |      27,1340|
* Message Size: 214 bytes
* Total time: 21643 ms

## Squareup Wire Statistics
| What             | Total Time in MS             | Average Time in NS     | Slowest in NS             | Slowest in %               |Fastest in NS             | Fastest in % |
|:-----------------|-----------------------------:|-----------------------:| -------------------------:| --------------------------:|-------------------------:|-------------:|
| Build Message:   | 5486         |      54861         |      30978181        |      0,0010        |      30520         |      19,2860|
| Serialization:   | 21731        |      217317        |      29665802        |      0,0010        |      183122        |      29,0670|
| Deserialization: | 22375        |      223750        |      14558218        |      0,0010        |      183122        |      14,9980|
* Message Size: 214 bytes
* Total time: 49592 ms
