# How to run

1. start spring boot app
2. run any hystrix dashboard 
  * e.g. `docker run --rm -p 9090:9090 --name hystrx-dashboard  synoa/docker-hystrix-dashboard`
3. Open the hystrix app
  * e.g. http://localhost:9090/
  * add the hystrix stream: http://host.docker.internal:8080/hystrix.stream
4. Call any URL of the sample app

# App URLs

## Return current time
- http://localhost:8080/hello

## Return an error every second request
- http://localhost:8080/withError

## Return a timeout every second request
- http://localhost:8080/withTimeout

# Links
- https://github.com/Netflix/Hystrix/wiki/Configuration