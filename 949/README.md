# Spring Cloud Sleuth loosing ParentSpanId

This repository contains a minor Spring Boot 2 application with the purpose of replicating a bug in Spring Cloud Sleuth
where the ParentSpanId is lost on subsequent calls. See [gh-949](https://github.com/spring-cloud/spring-cloud-sleuth/issues/949)

## Run
Execute the following command to start the service on port 8000

```bash
$ ./gradlew bootRun | grep -B 3 '^X-B3'
```

## Reproduce

```bash
$ curl http://localhost:8000/users
```
Watch the log outout from the bootRun command

### Expected Behaviour
The header `X-B3-ParentSpanId` should exist on all requests and have the same value.

### Actual Behaviour
The header `X-B3-ParentSpanId` only exists on the first call. 

## Troubleshooting

* The expected behaviour works before commit [f6a0e38c7195b9bd9c418195d0c5e22db4e46de6](https://github.com/spring-cloud/spring-cloud-sleuth/commit/f6a0e38c7195b9bd9c418195d0c5e22db4e46de6)
* The expected behaviour works when only `spring-boot-starter-webflux` is added as a dependency (Running in Netty).
* The expected behaviour works when `spring-boot-starter-webflux` and `spring-boot-starter-tomcat` are added as dependencies
* The actual behaviour is reproduced when the project contains both a dependency to `spring-boot-starter-webflux` and `spring-boot-starter-web`

So my investigation concludes that something happens when `spring-boot-starter-web` is on the classpath, resulting in a lost context when the second call is performed.

The reason for using both spring-boot-starter-webflux and spring-boot-starter-web is due to the lack of webflux-support in  springfox [gh-1773](https://github.com/springfox/springfox/issues/1773)

