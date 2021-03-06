# URLs

## cache read trough

- GET http://localhost:8080/api/person/1

## Redis Template

- GET http://localhost:8080/redis

## Spring Data Redis

- GET http://localhost:8080/api/data/1

## REDIS CLI commands

`docker exec -it 41ab352c53a9 redis-cli KEYS "*"`
`docker exec -it 41ab352c53a9 redis-cli HGETALL "entity:Muster_id"docker exec -it 41ab352c53a9 redis-cli HGETALL "entity:Muster_id"


## Sync
java -jar target/load-runner.jar -c 250 -u http://localhost:8080/redis/sync -m post -p "fooooo"

-- Counters --------------------------------------------------------------------
201.POST.http://localhost:8080/redis/sync
             count = 10000
failed.POST.http://localhost:8080/redis/sync
             count = 0

-- Timers ----------------------------------------------------------------------
POST.http://localhost:8080/redis/sync
             count = 10000
         mean rate = 863,84 calls/second
     1-minute rate = 803,59 calls/second
     5-minute rate = 795,19 calls/second
    15-minute rate = 793,73 calls/second
               min = 218,65 milliseconds
               max = 863,28 milliseconds
              mean = 285,44 milliseconds
            stddev = 79,53 milliseconds
            median = 264,17 milliseconds
              75% <= 298,76 milliseconds
              95% <= 375,86 milliseconds
              98% <= 661,74 milliseconds
              99% <= 728,41 milliseconds
            99.9% <= 849,35 milliseconds

## Async
java -jar target/load-runner.jar -c 250 -u http://localhost:8080/redis/async -m post -p "fooooo"

-- Counters --------------------------------------------------------------------
201.POST.http://localhost:8080/redis/async
             count = 10000
failed.POST.http://localhost:8080/redis/async
             count = 0

-- Timers ----------------------------------------------------------------------
POST.http://localhost:8080/redis/async
             count = 10000
         mean rate = 855,24 calls/second
     1-minute rate = 781,16 calls/second
     5-minute rate = 771,36 calls/second
    15-minute rate = 769,66 calls/second
               min = 211,77 milliseconds
               max = 974,83 milliseconds
              mean = 283,49 milliseconds
            stddev = 75,00 milliseconds
            median = 261,57 milliseconds
              75% <= 299,08 milliseconds
              95% <= 361,01 milliseconds
              98% <= 396,92 milliseconds
              99% <= 726,36 milliseconds
            99.9% <= 962,44 milliseconds
            
            
-- Counters --------------------------------------------------------------------
201.POST.http://localhost:8080/redis/reactive
             count = 10000
failed.POST.http://localhost:8080/redis/reactive
             count = 0


## Reactive
java -jar target/load-runner.jar -c 250 -u http://localhost:8080/redis/reactive -m post -p "fooooo"

-- Timers ----------------------------------------------------------------------
POST.http://localhost:8080/redis/reactive
             count = 10000
         mean rate = 927,59 calls/second
     1-minute rate = 875,02 calls/second
     5-minute rate = 868,18 calls/second
    15-minute rate = 867,00 calls/second
               min = 204,39 milliseconds
               max = 787,71 milliseconds
              mean = 263,14 milliseconds
            stddev = 65,05 milliseconds
            median = 243,30 milliseconds
              75% <= 292,99 milliseconds
              95% <= 320,40 milliseconds
              98% <= 365,45 milliseconds
              99% <= 659,78 milliseconds
            99.9% <= 729,45 milliseconds
            