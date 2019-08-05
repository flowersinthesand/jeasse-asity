# Jeasse Asity

Jeasse Asity is a jEaSSE connector for Asity, which allows to add the Server Sent Events feature to your web application no matter what Java web framework you use. Jeasse Asity is based on two libraries:

- [jEaSSE](https://github.com/mariomac/jeasse): A Server Sent Events (SSE) implementation for Java.
- [Asity](https://github.com/cettia/asity): An HTTP/WebSocket abstraction layer for Java.

## Getting Started

### Adding Dependencies

Add the following dependencies to your application. These modules allow to create a SSE application which is framework-agnostic.

```xml
<dependency>
  <groupId>info.macias</groupId>
  <artifactId>jeasse-common</artifactId>
  <version>0.11.3</version>
</dependency>
<dependency>
  <groupId>kim.donghwan</groupId>
  <artifactId>jeasse-asity-core</artifactId>
  <version>0.1.0</version>
</dependency>
```

Add an Asity bridge according to your web framework. This module allows to plug your SSE application into the framework of your choice. Asity supports the following web frameworks as of 3Q 2019.

- `asity-bridge-vertx3`: Vert.x 3
- `asity-bridge-vertx2`: Vert.x 2
- `asity-bridge-spring-webmvc4`: Spring MVC 4
- `asity-bridge-spring-webflux5`: Spring WebFlux 5
- `asity-bridge-servlet3`: Java Servlet 3
- `asity-bridge-play2`: Play Framework 2
- `asity-bridge-netty4`: Netty 4
- `asity-bridge-grizzly2`: Grizzly 2
- `asity-bridge-atmosphere2`: Atmosphere 2

```xml
<dependency>
  <groupId>io.cettia.asity</groupId>
  <artifactId>asity-bridge-${your.web.framework}</artifactId>
  <version>3.0.0</version>
</dependency>
```

### Writing a Web Fragment

A web fragment is a labmda that takes a [`ServerHttpExchange`](https://static.javadoc.io/io.cettia.asity/asity-http/3.0.0/io/cettia/asity/http/ServerHttpExchange.html) instance which an abstraction of framework-specific HTTP request-response exchanges.

```java
public class MyJeasseAsityAction implements Action<ServerHttpExchange> {
  @Override
  public void on(ServerHttpExchange http) {
    switch (http.method()) {
      case GET:
        // Handle GET requests
        break;
      case POST:
        // Handle POST requests
        break;
      default:
        http.setStatus(HttpStatus.METHOD_NOT_ALLOWED).end();
        break;
    }
  }
}
```

The above web fragment handles only GET and POST requests and responds with 405, Method Not Allowed, for other requests. You'll learn how to plug it into your framework later.

### Working with a SSE Connection

To open a SSE connection, when handling GET requests, create an `AsityEventTarget` with a given `ServerHttpExchange` which implements jEaSSE's [`EventTarget`](https://static.javadoc.io/info.macias/jeasse-common/0.11.3/info/macias/sse/EventTarget.html) interface and call its `ok()` and `open()` methods.

```java
EventTarget eventTarget = new AsityEventTarget((ServerHttpExchange) http);
eventTarget.ok().open();
```

To send an event through a SSE connection, use `send(MessageEvent messageEvent)` or `send(String event, String data)` of EventTarget interface.

```java
MessageEvent event = new MessageEvent.Builder().setData("Hello World").build();
eventTarget.send(event);
```

### Working with SSE Connections

jEaSSE provides the [`EventBroadcast`](https://static.javadoc.io/info.macias/jeasse-common/0.11.3/info/macias/sse/EventBroadcast.html) class that streamlines handling of multiple SSE connections. Assume that it's created as a field of the web fragment and shared by each different requests.

```java
EventBroadcast broadcaster = new EventBroadcast();
```

To open a SSE connection through `EventBroadcast` and add it as a subscriber, call its `addSubscriber(EventTarget)` method. You don't need to call `EventTarget`'s `ok()` and `open()` methods in this case.

```java
EventTarget eventTarget = new AsityEventTarget(http);
broadcaster.addSubscriber(eventTarget);
```

To send some events to all the subscribers, call its `broadcast(MessageEvent messageEvent)` or `broadcast(String event, String data)` method.

```java
broadcaster.broadcast("message", "Hello World");
```

### Plugging the Web Fragment to Your Web Application

The last step is to plug the web fragment, `MyJeasseAsityAction`, into your web application using an Asity bridge for the web framework you are using. Here's an example of Servlet 3.

```java
@WebListener
public class ChatServerInitializer implements ServletContextListener {
  @Override
  public void contextInitialized(ServletContextEvent event) {
    MyJeasseAsityAction httpAction = new MyJeasseAsityAction();

    ServletContext context = event.getServletContext();
    Servlet servlet = new AsityServlet().onhttp(httpAction);
    ServletRegistration.Dynamic reg = context.addServlet("chat", servlet);
    reg.setAsyncSupported(true);
    reg.addMapping("/send");
  }
}
```

`asity-bridge-servlet3` module provides the `AsityServlet` class which extends Servlet's `HttpServlet` and abstracts Servlet's `HttpServletRequest` and `HttpServletResponse` into Asity's `ServerHttpExchange`. With the `AsityServlet`, you can feed the web fragment with HTTP request-response exchanges in a Servlet application as above.

Likewise, other Asity bridges provide such adapters per framework so that you can set up web fragments in your webapp with ease. For the details, see the Asity website's [Run Anywhere](https://asity.cettia.io/#run-anywhere) section.

## Example

Play around with the following examples to better understand jEaSSE and Asity. Examples require Java 8+ and Maven 3+. Clone or download the repository.

```
git clone https://github.com/flowersinthesand/jeasse-asity.git
cd jeasse-asity
```

To run a Servlet 3 example,

```
cd example-servlet3
mvn jetty:run
```

To run a Grizzly 2 example,

```
cd example-girzzly2
mvn package exec:java
```

To run a Play Framework 2 example,

```
cd example-play2
sbt run
```

All examples behave the same. Run one of examples and open a browser to `http://localhost:8080/index.html`. You'll see a screen similar to the following.

![Screenshot](https://raw.githubusercontent.com/mariomac/jeasse/master/examples/sshot.png)

The web fragment of the example, `MyJeasseAsityAction`, is located at [here](https://github.com/flowersinthesand/jeasse-asity/blob/master/example/src/main/java/kim/donghwan/jeasse/asity/example/MyJeasseAsityAction.java).

For your information, as you may know, examples borrowed most code from [the original example](https://github.com/mariomac/jeasse/blob/master/examples/) in jEaSSE.

## Support

If you have any questions or issues with Jeasse Asity, create an issue in the issue tracker in Github or DM me on Twitter ([@flowersits](https://twitter.com/flowersits)).
