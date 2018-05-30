Run
---

```$ ./gradlew bootRun```

Have a look at the last logged lines.
```
2018-05-30 13:24:21.207  INFO [-,f8db5378f741ead9,323d848591e98e58,true] 990 --- [nio-8080-exec-1] com.example.demo.FooController           : entering class com.example.demo.FooController
2018-05-30 13:24:21.252  INFO [-,f8db5378f741ead9,f8db5378f741ead9,true] 990 --- [client-epoll-12] com.example.demo.Start                   : received foo
2018-05-30 13:24:21.265  INFO [-,f8db5378f741ead9,1618f2fe90eae945,true] 990 --- [nio-8080-exec-2] com.example.demo.BarController           : entering class com.example.demo.BarController
2018-05-30 13:24:21.269  INFO [-,f8db5378f741ead9,f8db5378f741ead9,true] 990 --- [client-epoll-13] com.example.demo.Start                   : concatenating foo bar
2018-05-30 13:24:21.270  INFO [-,f8db5378f741ead9,f8db5378f741ead9,true] 990 --- [client-epoll-13] com.example.demo.Start                   : received foo bar
2018-05-30 13:24:21.277  INFO [-,f8db5378f741ead9,06b7ec13104932af,true] 990 --- [nio-8080-exec-3] com.example.demo.BazController           : entering class com.example.demo.BazController
2018-05-30 13:24:21.281  INFO [-,f8db5378f741ead9,f8db5378f741ead9,true] 990 --- [client-epoll-12] com.example.demo.Start                   : concatenating foo bar baz
2018-05-30 13:24:21.281  INFO [-,f8db5378f741ead9,f8db5378f741ead9,true] 990 --- [           main] com.example.demo.Start                   : Finished
```

Compare the output with Finchley.RC2 by changing the version.
```
2018-05-30 13:25:35.266  INFO [-,ba781f94d10e868f,1d856e921de84189,true] 2445 --- [nio-8080-exec-1] com.example.demo.FooController           : entering class com.example.demo.FooController
2018-05-30 13:25:35.308  INFO [-,,,] 2445 --- [client-epoll-12] com.example.demo.Start                   : received foo
2018-05-30 13:25:35.320  INFO [-,ba781f94d10e868f,c87a12dcc154bcb0,true] 2445 --- [nio-8080-exec-2] com.example.demo.BarController           : entering class com.example.demo.BarController
2018-05-30 13:25:35.323  INFO [-,,,] 2445 --- [client-epoll-13] com.example.demo.Start                   : concatenating foo bar
2018-05-30 13:25:35.323  INFO [-,,,] 2445 --- [client-epoll-13] com.example.demo.Start                   : received foo bar
2018-05-30 13:25:35.330  INFO [-,ba781f94d10e868f,2fe3c357a00a678a,true] 2445 --- [nio-8080-exec-3] com.example.demo.BazController           : entering class com.example.demo.BazController
2018-05-30 13:25:35.332  INFO [-,,,] 2445 --- [client-epoll-12] com.example.demo.Start                   : concatenating foo bar baz
2018-05-30 13:25:35.332  INFO [-,ba781f94d10e868f,ba781f94d10e868f,true] 2445 --- [           main] com.example.demo.Start                   : Finished
```
