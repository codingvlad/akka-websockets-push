# akka-websockets-push

A small playground project for implementing a light http server that pushes numbers from a fibonacci stream to a websocket client using akka http.

## Motivation

A lot of the example projects that one can find online of akka http and websockets are simple and almost always use the same use case: a chat application.
I recently started a project that is a full stack for monitoring a docker infrastructure. One of the features
that I wanted to implement is to have an almost real-time plot in the front end that reflects the state of the internal logic of a particular dockerized application.
By using Akka persistence query I can get a stream of all persisted events. A big question mark was how to stream a simplified version of that
information to the web console but without continuously pulling for the information. Altough this project streams `BigInt`, it serves as start point for the final
version.

## Getting Started

```
start firefox and start the Simple WebSocket Client (by WangFenjin) or any websocket client
URL: ws://localhost:8080/fibonacci  

(inside akka-websockets-push folder) 
sbt run
```

### Prerequisites

One can use any websocket client. I use the [Simple WebSocket Client](https://addons.mozilla.org/en-US/firefox/addon/simple-websocket-client/)

### Installing

Clone the repository and point a console to the `akka-websockets-push` folder

```
git clone the project
cd akka-websockets-push
```

## Built With

* [akka](https://akka.io/) - Http server
* [sbt](https://www.scala-sbt.org/) - Dependency Management

## Versioning

* Version 0.1 Working http server streaming bigints to websocket

## Authors

* **Vlad Filip** - *Initial work* - [HeavensAbove](https://www.heavens-above.com/)

## Acknowledgments

* Thanks to **Fabio Tiriticco** for his post [Akka Http, handle Websockets with Actors](http://ticofab.io/akka-http-websocket-example/)
