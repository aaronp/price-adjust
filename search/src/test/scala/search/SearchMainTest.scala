package search

import com.sksamuel.elastic4s.{IndexResult, ElasticDsl}
import com.sksamuel.elastic4s.ElasticDsl._
import com.sksamuel.elastic4s.testkit.ElasticSugar
import org.elasticsearch.action.index.IndexResponse
import org.junit.runner.RunWith
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.junit.JUnitRunner
import org.scalatest.time.{Millis, Seconds, Span}
import org.scalatest.{Matchers, WordSpec}

import scala.concurrent.Future

@RunWith(classOf[JUnitRunner])
class SearchMainTest extends WordSpec with ElasticSugar with ScalaFutures with Matchers {

  "Search.index" should {
    "index and get stuff" in {
      val query = ElasticDsl.index into "foo" / "bar" fields ("hello" -> "world")
      val indexResultF: Future[IndexResult] = client.execute(query)
      val indexResult = indexResultF.futureValue
      indexResult.isCreated should be(true)

      val readBack = client.execute(ElasticDsl.get(indexResult.getId) from "foo" / "bar").futureValue
      readBack.source.get("hello") should be("world")

    }
  }

  implicit override val patienceConfig = PatienceConfig(timeout = scaled(Span(10, Seconds)), interval = scaled(Span(150, Millis)))
}
