package neo

import java.util.UUID


case class NodeRecord(id: UUID, labels: Set[String], properties: Map[String, NodeField]) {
  def withLabel(label : String) = copy(labels = labels + label)
  def +(key: String, value: String) = withProperty(key -> StringField(value))
  def +(key: String, value: Long) = withProperty(key -> LongField(value))
  def withProperty(keyValue: (String, NodeField)) = copy(properties = properties.updated(keyValue._1, keyValue._2))
}

object NodeRecord {
  val IdColumnName = "_id"
}


case class NodeRelationship(fromNodeId: UUID, toNodeId: UUID, label: Option[String], properties: Map[String, NodeField])

sealed trait NodeField

object NodeField {
  def apply(s : String) = StringField(s)
  def apply(i : Long) = LongField(i)
}

case class DecimalField(value: BigDecimal) extends NodeField
case class LongField(value: Long) extends NodeField

//case class IntField(value: Int) extends NodeField

case class StringField(value: String) extends NodeField

case class DateField(epochMillis: String) extends NodeField
