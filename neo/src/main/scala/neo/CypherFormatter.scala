package neo

object CypherFormatter {

  def createCypher(t: NodeRecord): String = {
    val labelStr = if (t.labels.isEmpty) "" else t.labels.mkString(":", ":", "")
    val propStr = (t.properties + (NodeRecord.IdColumnName -> StringField(t.id.toString))).map {
      case (n, p) => s"${n} : ${formatField(p)}"
    }.mkString("{ ", ",", "}")
    s"""MERGE (n${labelStr} $propStr) RETURN n"""
  }

  def formatField(t: NodeField): String = {
    t match {
      case StringField(s) =>
        // TODO - proper string escaping
        val escaped = s.replaceAllLiterally("\"", "'")
        s""""${escaped}""""
      case DateField(epoch) => epoch.toString
      //        case IntField(x) => x.toString
      case LongField(x) => x.toString
    }
  }
}
