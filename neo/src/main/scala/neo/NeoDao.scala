package neo

import java.util.UUID

import com.typesafe.config.Config
import neo.CypherOps._
import org.anormcypher._

import scala.compat.Platform.EOL

trait NeoDao {

  def getNodeById(id: UUID): Option[NodeRecord] = getNodesById(Set(id)).get(id)

  def getNodesById(ids: Set[UUID]): Map[UUID, NodeRecord]

  def save(nodes: Seq[NodeRecord], relationships: Seq[NodeRelationship]): Unit

  def save(first: NodeRecord, theRest: NodeRecord*): Unit = save(first +: theRest.toSeq, Nil)

  def save(first: NodeRelationship, theRest: NodeRelationship*): Unit = save(Nil, first +: theRest.toSeq)
}

object NeoDao {
  def apply(config: Config): NeoDao = apply(NeoConfig(config))

  def apply(config: NeoConfig): NeoDao = new Anorm(config.newRestClient)


  class Anorm(neoClient: Neo4jREST) extends NeoDao {
    private implicit val c = neoClient

    private val SingleLineParser = CypherRowParser[NodeRecord] { row =>



      val keys = row.asMap.keySet

      println(keys)

        val props = row.propertiesFor("n")
        val StringField(idStr) = props(NodeRecord.IdColumnName)
        val labels:Set[String] = row.labelsFor("n")

        val propsSansId = props - NodeRecord.IdColumnName

        val record = NodeRecord(UUID.fromString(idStr), labels, propsSansId)
        Success(record)
     }

    override def save(nodes: Seq[NodeRecord], relationships: Seq[NodeRelationship]) = {

      val allIds = nodes.map(_.id).toSet
      val byId = getNodesById(allIds)

      val oldRecords = allIds.flatMap(byId.get)
      val newIds = allIds -- byId.keySet
      val newNodes = nodes.filter(n ⇒ newIds.contains(n.id))
      create(newNodes)


    }

    def update(nodes: Seq[NodeRecord]) = {

      val nodeStatements: Seq[Boolean] = nodes.map {
        case NodeRecord(id, labels, properties) =>
          val formattedProperties: Map[String, String] = properties.mapValues(CypherFormatter.formatField)
          val params: Map[String, Any] = Map("props" -> formattedProperties)
          val statement = CypherStatement(
            s"""
               |MATCH (n {_id : "${id}"})
               |SET n = { props }
               |RETURN n
            """.stripMargin, params)
            statement.execute()
      }
      val r: Seq[Boolean] = nodeStatements
      require(r.forall(_.booleanValue()))
    }

    def create(nodes: Seq[NodeRecord]) = {
      val nodeStatements = nodes.map(CypherFormatter.createCypher).mkString(EOL)
      val r = CypherStatement(nodeStatements).execute()
      require(r)
    }

    override def getNodesById(ids: Set[UUID]): Map[UUID, NodeRecord] = {
      val indexByUUID: Map[UUID, Int] = ids.zipWithIndex.toMap

      val names = indexByUUID.values.map("n" + _).mkString(",")

      val query: String = indexByUUID.map {
        case (id, idx) ⇒ s"""(n${idx} {_id : "${id}"}) """
      }.mkString("MATCH ", ", ", s" RETURN ${names}")

      println(query)
      val results: Stream[CypherResultRow] = CypherStatement(query)()

      println(results.size)
      results.map { row =>
        val data = row.data

        val md = row.metaData

        val map = row.asMap

        println(map)
        println(data)
        val id = map("_id")

        ???
      }
      ???
    }


  }

}
