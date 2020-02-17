# Spring Boot ❤️ GraalVM

## Table of content

* [step 1: Getting started](#getting-started)
  * [GraalVM installation](#graalvm-installation)
  * [Hello world compilation](#hello-world-compilation)
  * [Using Docker from scratch](#using-docker-from-scratch-compile-under-linux)
* TODO: [step 2: Static initialization](#static-initialization)
* TODO: [step 3: Spring Boot](#spring-boot)

## Getting started

### GraalVM installation

```bash
sdk ls java | grep grl
## output:
# GraalVM       |     | 19.3.1.r11   | grl     |            | 19.3.1.r11-grl      
#               |     | 19.3.1.r8    | grl     |            | 19.3.1.r8-grl       
#               |     | 19.3.0.r11   | grl     |            | 19.3.0.r11-grl      
#               |     | 19.3.0.r8    | grl     |            | 19.3.0.r8-grl       
#               |     | 19.3.0.2.r11 | grl     |            | 19.3.0.2.r11-grl    
#               |     | 19.3.0.2.r8  | grl     |            | 19.3.0.2.r8-grl     
#               |     | 19.2.1       | grl     |            | 19.2.1-grl          
#               |     | 19.1.1       | grl     |            | 19.1.1-grl          
#               |     | 19.0.2       | grl     |            | 19.0.2-grl          
#               |     | 1.0.0        | grl     |            | 1.0.0-rc-16-grl

source ~/.zshrc # or: source ~/.bashrc
sdk install java 19.3.1.r11-grl
## output:
#Downloading: java 19.3.1.r11-grl
#
#In progress...
#
########################################################################### 100.0%
########################################################################### 100.0%
#Repackaging Java 19.3.1.r11-grl...
#
#Done repackaging...
#Cleaning up residual files...
#
#Installing: java 19.3.1.r11-grl
#Done installing!
#
#
#Setting java 19.3.1.r11-grl as default.

source ~/.zshrc # or: source ~/.bashrc
gu install native-image
## output:
#Downloading: Component catalog from www.graalvm.org
#Processing Component: Native Image
#Downloading: Component native-image: Native Image  from github.com
#Installing new component: Native Image (org.graalvm.native-image, version 19.3.1)

source ~/.zshrc # or: source ~/.bashrc
java -version
#openjdk version "11.0.6" 2020-01-14
#OpenJDK Runtime Environment GraalVM CE 19.3.1 (build 11.0.6+9-jvmci-19.3-b07)
#OpenJDK 64-Bit Server VM GraalVM CE 19.3.1 (build 11.0.6+9-jvmci-19.3-b07, mixed mode, sharing)
#JAVA_HOME=~/.sdkman/candidates/java/current
```

### Hello world compilation

* create `HelloWorld.java` class.
```bash
mkdir target
vi target/HelloWorld.java
# ...
```

* _cat target/HelloWorld.java_

```java
public class HelloWorld {
  public static void main(String[] args){
    System.out.println("Hello, World!");
  }
}
```

* compile

```bash
javac -d out target/HelloWorld.java
```

* create native-image:

_try 1_: with `--static` flag

```bash
native-image --static -cp out/ HelloWorld
## output:
#Build on Server(pid: 29627, port: 64134)*
#[helloworld:29627]    classlist:   1,394.89 ms
#[helloworld:29627]        (cap):   2,796.20 ms
#[helloworld:29627]        setup:   3,902.40 ms
#[helloworld:29627]   (typeflow):   5,079.28 ms
#[helloworld:29627]    (objects):   4,913.89 ms
#[helloworld:29627]   (features):     233.80 ms
#[helloworld:29627]     analysis:  10,437.45 ms
#[helloworld:29627]     (clinit):     177.75 ms
#[helloworld:29627]     universe:     478.28 ms
#[helloworld:29627]      (parse):     876.33 ms
#[helloworld:29627]     (inline):   1,670.98 ms
#[helloworld:29627]    (compile):   7,368.19 ms
#[helloworld:29627]      compile:  10,415.24 ms
#[helloworld:29627]        image:   1,108.42 ms
#[helloworld:29627]        write:     163.91 ms
#Error: DARWIN does not support building static executable images.
#Error: Use -H:+ReportExceptionStackTraces to print stacktrace of underlying exception
#Error: Image build request failed with exit status 1
```

Oops... seems like Mac OS X doesn't not support `--static` option...

_try 2_: without `--static` flag

```bash
native-image -H:+ReportExceptionStackTraces -cp out/ HelloWorld target/hello-world
## output: 
#Build on Server(pid: 29627, port: 64134)
#[helloworld:29627]    classlist:     154.41 ms
#[helloworld:29627]        (cap):   2,037.96 ms
#[helloworld:29627]        setup:   2,318.03 ms
#[helloworld:29627]   (typeflow):   3,372.50 ms
#[helloworld:29627]    (objects):   3,512.64 ms
#[helloworld:29627]   (features):     143.88 ms
#[helloworld:29627]     analysis:   7,186.61 ms
#[helloworld:29627]     (clinit):     254.86 ms
#[helloworld:29627]     universe:     607.40 ms
#[helloworld:29627]      (parse):     425.15 ms
#[helloworld:29627]     (inline):   1,203.33 ms
#[helloworld:29627]    (compile):   3,459.22 ms
#[helloworld:29627]      compile:   5,435.30 ms
#[helloworld:29627]        image:     699.99 ms
#[helloworld:29627]        write:     266.03 ms
#[helloworld:29627]      [total]:  16,763.60 ms

chmod +x target/hello-world
time ./target/hello-world
## output:
#Hello, World!
#./target/hello-world  0.00s user 0.00s system 42% cpu 0.010 total
```

### Using Docker from scratch (compile under Linux)

* prepare Dockerfile

```bash
vi Dockerfile.hello-world
```

```Dockerfile
FROM scratch
COPY ./target/hello-world /tmp/
ENTRYPOINT ["/tmp/hello-world"]
```

* build and run

```bash
docker build -t daggerok/hello-world -f Dockerfile.hello-world .
time docker run -it --rm daggerok/hello-world
```

## Static initialization

TODO: `native-image --initialize-at-build-time=HelloWorld ...`

## Spring boot

TODO...

## Resources

* [YouTube: Running Spring Boot applications as GraalVM native images by Sébastien Deleuze](https://www.youtube.com/watch?v=3eoAxphAUIg)
* [YouTube: Maximizing Applications Performance with GraalVM by Alina Yurenko](https://www.youtube.com/watch?v=5wTB3JFOEbM)
* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/2.2.4.RELEASE/maven-plugin/)
* [Spring Web](https://docs.spring.io/spring-boot/docs/2.2.4.RELEASE/reference/htmlsingle/#boot-features-developing-web-applications)
* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/bookmarks/)
