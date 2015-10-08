package domain

import java.util.UUID

case class Address(id : UUID, name : String, street : String, line1 : Option[String], line2 : Option[String], country : String, zip : String)
