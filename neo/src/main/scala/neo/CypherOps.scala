package neo

import org.anormcypher.CypherRow

object CypherOps {

  implicit class RowOps(val row: CypherRow) extends AnyVal {
    def nodeMap(name: String): Map[String, Any] = {
      row.asMap(name) match {
        case map: Map[String, Any] => map
        case other => sys.error(s"Expected '$name' row to be a map, but got $other")
      }
    }

    def labels : Set[String] = labelsFor(onlyNodeName)

    def labelsFor(name: String): Set[String] = {
      metadataFor(name).get("labels").fold(Set[String]()) { values =>
        values match {
          case i: Iterable[_] => i.map {
            case s: String => s
            case other => sys.error(s"non-string Label was $other")
          }.toSet
          case other => sys.error(s"Expected labels to be iterable, but was $other")
        }
      }
    }

    def onlyNodeName = row.asMap.keySet.toList match {
      case List(only) => only
      case many => sys.error(s"${many.size} map entries found for what should be a single row: ${many}")
    }

    def properties : Map[String, NodeField] = propertiesFor(onlyNodeName)

    def propertiesFor(name: String): Map[String, NodeField] = {
      dataFor(name).map {
        case (key, value: String) => (key, StringField(value))
        case (key, value: BigDecimal) if value.isValidLong => (key, LongField(value.toLong))
        case (key, value: BigDecimal) => (key, LongField(value.toLong))
        case (key, value: Long) => (key, LongField(value))
        case (key, value: java.lang.Long) => (key, LongField(value.toLong))
        case (key, value) => sys.error(s"Unhandled property type ${value.getClass} for $key : $value")
      }
    }

    def metadataFor(name: String) = nestedMap(name, "metadata")

    def dataFor(name: String): Map[String, Any] = nestedMap(name, "data")

    private def nestedMap(name: String, subKey: String): Map[String, Any] = nodeMap(name)(subKey).asInstanceOf[Map[String, Any]]
  }

}
