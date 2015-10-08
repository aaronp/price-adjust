package domain

import java.util.UUID

import api.BusinessDto

case class Business(id : UUID, name : String, employees : Set[Employee], address : Address)

