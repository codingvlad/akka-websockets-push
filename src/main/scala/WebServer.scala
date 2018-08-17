import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.ws.{Message, TextMessage}
import akka.http.scaladsl.server.Directives._
import akka.stream.scaladsl.GraphDSL.Implicits._
import akka.stream.scaladsl.{Flow, GraphDSL, Sink, Source}
import akka.stream.{ActorMaterializer, FlowShape}

import scala.concurrent.ExecutionContextExecutor
import scala.concurrent.duration._
import scala.io.StdIn

object WebServer extends App {

  implicit val system: ActorSystem = ActorSystem("demo-system")
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  // needed for the future flatMap/onComplete in the end
  implicit val executionContext: ExecutionContextExecutor = system.dispatcher

  // An infinite stream from the fibonacci number sequence
  val fibStream: Stream[BigInt] = BigInt(0) #:: BigInt(1) #:: fibStream.zip(fibStream.tail).map { n => n._1 + n._2 }

  // This flow does not consider any input, instead sends text messages mapped from elements of the fibonacci stream
  def fibStreamToWS: Flow[Message, Message, Any] =
    Flow.fromGraph(GraphDSL.create() { implicit b =>
      // Ignore the any input Message
      val textMsgFlow = b.add(Flow[Message])
      // Map and throttle the stream of fibonacci sequence numbers
      val fibonacciSource = b.add(Source(fibStream)
        .throttle(1, 3.seconds)
        .map(n => TextMessage(n.toString)))

      textMsgFlow ~> Sink.ignore
      FlowShape(textMsgFlow.in, fibonacciSource.out)
    })

  val route =
    path("fibonacci") {
      get {
        handleWebSocketMessages(fibStreamToWS)
      }
    }

  val bindingFuture = Http().bindAndHandle(route, "localhost", 8080)

  println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
  StdIn.readLine()
  bindingFuture
    .flatMap(_.unbind())
    .onComplete(_ => system.terminate())
}
