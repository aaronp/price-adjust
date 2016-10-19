package mongo

import org.mongodb.scala._

import scala.collection.mutable.ListBuffer
import scala.concurrent.{Future, Promise}
import scala.util.Try

object Observables {

  def futureObserver[T] = new FutureObs[T]

  class FutureObs[T] extends Observer[T] {

    private val promise = Promise[List[T]]()
    private val buffer = ListBuffer[T]()

    def future: Future[List[T]] = promise.future

    override def onNext(result: T): Unit = {
      buffer += result
    }

    override def onError(e: Throwable): Unit = {
      promise.tryFailure(e)
    }

    override def onComplete(): Unit = {
      promise.complete(Try(buffer.reverse.toList))
    }
  }

}
