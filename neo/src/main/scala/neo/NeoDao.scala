package domain.dao.neo

import org.anormcypher.Neo4jREST

trait NeoDao {

  def save(nodes : Seq[NodeRecord], relationships: Seq[NodeRelationship])
}

object NeoDao {
  class Anorm(neoClient : Neo4jREST) extends NeoDao {
    private implicit val c = neoClient

    override def save(nodes : Seq[NodeRecord], relationships: Seq[NodeRelationship]) = {

    }
  }


  def apply(neoConfig: NeoConfig) : NeoDao = {
    new Anorm(neoConfig.newRestClient)
  }
}
