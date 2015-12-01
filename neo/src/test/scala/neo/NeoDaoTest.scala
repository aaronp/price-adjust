package neo

import com.typesafe.config.ConfigFactory
import org.scalatest.{Matchers, WordSpec}

class NeoDaoTest extends WordSpec with Matchers with NeoImplicits {

  "NeoDao" should {
    val dao = NeoDao(ConfigFactory.parseResourcesAnySyntax("neo-test").getConfig("neo4j"))
    "be able to create a new node" in {
      val node = "hi".asNode.withLabel("create") + ("foo", "bar") + ("small", Long.MinValue) + ("big", Long.MaxValue)
      dao.save(node)

      val Some(backAgain) = dao.getNodeById(node.id)

      backAgain shouldBe node
    }
    "be able to update a node" in {
      val originalNode = "updateMe".asNode.withLabels("original", "test") + ("foo", "bar") + ("small", Long.MinValue) + ("big", Long.MaxValue)
      dao.save(originalNode)
      val updated = originalNode + ("x", "y")
      dao.save(updated)

      val Some(backAgain) = dao.getNodeById(updated.id)

      backAgain shouldBe updated
    }
    "be able to update a node with a new label" in {
      val originalNode = "updateMe".asNode.withLabel("original") + ("foo", "bar") + ("small", Long.MinValue) + ("big", Long.MaxValue)
      dao.save(originalNode)
      val updated = originalNode.withLabel("updated") + ("x", "y")
      dao.save(updated.ensuring(_.labels == Set("original", "updated")))

      val Some(backAgain) = dao.getNodeById(updated.id)

      backAgain shouldBe updated
    }
    "be remove node properties in an update" in {
      val originalNode = "deleteProps".asNode + ("original", "property")
      dao.save(originalNode)
      val updated = originalNode.copy(properties = Map("new" -> NodeField("prop")))
      dao.save(updated)

      val Some(backAgain) = dao.getNodeById(updated.id)

      backAgain shouldBe updated
    }
  }
}
