# Recommendation Service

Web application that provides recommendations based on data from market.

Springboot application

## Build
```shell
./gradlew clean
./gradlew build
```

## Start
jar file can be found in build/libs


```shell
java -jar recommendation-0.0.1-SNAPSHOT.jar
```

By default application start on port 8080

## Documentation
Swagger endpoint - http://localhost:8080/swagger-ui.html

## Scaling
There are few options how to scale application.
So far application do most of the processing at the start and later just provide data.
To speed up start of the application we would need to add more CPUs.
To handle more cryptos and range of data at some point it would require more memory.

Right now application keeps calculated data in memory.
If we have a lot of data which we could not keep it in memory 
we can move it to some database and load it on demand.
Please notice that by moving data to database we will introduce slowdown
(usually database is in different server so things like network speed and latency, search in database)
but at the end it would allow us to process more data.

On the other hand some of these calculations could be also moved to database itself so this would speed the application. 

Another option to scale up is spin up another instance of the application and place a load balancer before instances.

Load balancer might be simple like Round Robin or more complex for example where we would analyze request 
and define that instance-1 would handle all requests for BTC while instance-2 would handle all other requests.

We should not forget about deployment process and avoid downtimes. There should be at least 2 instances that are able to process traffic
While one is under deployment of new version the other one can still serve.

## Timeframe
Selecting timeframe is not supported right now.
It can be introduced as new path/query param where user could specify the timeframe.
To introduce an algorithm that will decide which timeframe is more accurate I would need more specification.

## Docker

Build docker image
```shell
docker build -t recommendation-app .
```

Start docker in terminal port 8000
```shell
docker run -p 8000:8080 recommendation-app
```

Start docker in background port 8000
```shell
docker run -d -p 8000:8080 recommendation-app
```

List local containers
```shell
docker container ls
```

## Caveat
I chose 10 decimal spaces for normalization as there was no recommendation for it.
I have created just two tests. One integration and one unit test as an example.
In real application I would try to reach high coverage with unit tests 
and meaningful coverage with integration tests. 