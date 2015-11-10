package neo

import java.util.UUID

trait NeoImplicits {

  class NeoString(s: String) {
    def asNode: NodeRecord = {
      NodeRecord(id = UUID.randomUUID(), labels = Set(s), properties = Map.empty)
    }
  }

  implicit def neoString(s: String) = new NeoString(s)

}

object NeoImplicits extends NeoImplicits
