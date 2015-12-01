package neo

import java.util.UUID

import com.typesafe.config.Config
import neo.CypherOps._
import org.anormcypher._

import scala.compat.Platform.EOL

trait NeoDao {

  def getNodeById(id: UUID): Option[NodeRecord] = getNodesById(Set(id)).get(id)

  def getNodesById(ids: Set[UUID]): Map[UUID, NodeRecord]

  def deleteNodes(ids: Set[UUID]): Unit

  def save(nodes: Seq[NodeRecord], relationships: Seq[NodeRelationship]): Unit

  final def save(first: NodeRecord, theRest: NodeRecord*): Unit = save(first +: theRest.toSeq, Nil)

  final def save(first: NodeRelationship, theRest: NodeRelationship*): Unit = save(Nil, first +: theRest.toSeq)
}

object NeoDao {
  def apply(config: Config): NeoDao = apply(NeoConfig(config))

  def apply(config: NeoConfig): NeoDao = new Anorm(config.newRestClient)


  class Anorm(neoClient: Neo4jREST) extends NeoDao {
    private implicit val c = neoClient

    private val SingleLineParser = CypherRowParser[NodeRecord] { row =>
      val props = row.properties
      val StringField(idStr) = props(NodeRecord.IdColumnName)
      val labels = row.labels
      val propsSansId = props - NodeRecord.IdColumnName
      val record = NodeRecord(UUID.fromString(idStr), labels, propsSansId)
      Success(record)
    }

    override def deleteNodes(ids: Set[UUID]): Unit = {
      val queries = ids.map(id => s"""
                     |MATCH (n {${NodeRecord.IdColumnName} : "${id}"})
                     |DELETE n
            """.stripMargin)
      val success = queries.map(q => CypherStatement(q).execute).forall(_.booleanValue())
      require(success)
    }
    override def save(nodes: Seq[NodeRecord], relationships: Seq[NodeRelationship]) = {

      val allIds = nodes.map(_.id).toSet
      val byId = getNodesById(allIds)

      println(s"Found ${byId.mkString(EOL, EOL, EOL)}")

      val oldRecords = allIds.flatMap(byId.get)
      val newIds = allIds -- byId.keySet
      val newNodes = nodes.filter(n ⇒ newIds.contains(n.id))

      val nodesToUpdate = nodes.filter(n => oldRecords.exists(_.id == n.id))

      println(s"Saving ${nodes.size} nodes will create ${newNodes.size} new nodes and update ${nodesToUpdate.size} nodes")
      require(relationships.isEmpty, "TODO - save relationships")

      create(newNodes)
      update(nodesToUpdate)
    }

    def update(nodes: Seq[NodeRecord]) = {

      val nodeStatements: Seq[Boolean] = nodes.map {
        case NodeRecord(id, labels, properties) =>
          val formattedProperties: Map[String, String] = {
            val allProps = properties + (NodeRecord.IdColumnName -> StringField(id.toString))
            allProps.mapValues(CypherFormatter.formatField)
          }
          val props = formattedProperties.map{
            case (k,v) => s"$k : $v"
          }.mkString("," + EOL)

          val query = s"""
                         |MATCH (n {${NodeRecord.IdColumnName} : "${id}"})
                         |SET n = { $props }
                         |RETURN n
            """.stripMargin

          println(query)
          val statement = CypherStatement(query)
          statement.execute()
      }
      val r: Seq[Boolean] = nodeStatements
      require(r.forall(_.booleanValue()))
    }

    def create(nodes: Seq[NodeRecord]) = {
      if (nodes.nonEmpty) {
        val nodeStatements = nodes.map(CypherFormatter.createCypher).mkString(EOL)
        val r = CypherStatement(nodeStatements).execute()
        require(r, s"Create failed for $nodes")
      }
    }

    override def getNodesById(ids: Set[UUID]): Map[UUID, NodeRecord] = {
      val idByNodeName: Map[String, UUID] = ids.zipWithIndex.toMap.map {
        case (id, i) => s"n$i" -> id
      }


      val results: Seq[NodeRecord] = {
        val query: String = {
          val names = idByNodeName.keySet.mkString(",")
          idByNodeName.map {
            case (name, id) ⇒ s"""($name {${NodeRecord.IdColumnName} : "${id}"}) """
          }.mkString("MATCH ", ", ", s" RETURN ${names}")
        }
        println(query)

        CypherStatement(query).list(SingleLineParser)
      }

      results.map(r => r.id -> r).toMap

    }
  }
}
