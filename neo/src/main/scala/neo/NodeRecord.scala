package domain.dao.neo

import java.util.UUID


case class NodeRecord(id : UUID, labels : Set[String], properties: Map[String, NodeField])
case class NodeRelationship(fromNodeId : UUID, toNodeId : UUID, label : Option[String], properties: Map[String, NodeField])

sealed trait NodeField
case class IntField(value: Int) extends NodeField
case class StringField(value: String) extends NodeField
case class DateField(epochMillis: String) extends NodeField
