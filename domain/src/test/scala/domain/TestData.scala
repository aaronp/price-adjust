package domain

import java.util.UUID

trait TestData {

  def newAddress = Address(UUID.randomUUID(), "Some address", "Somewhere", Some("Main St"), None, "UK", "AB1 2CD")
  def newEmployee = Employee(UUID.randomUUID(), "Dave", newAddress)
  def newBusiness = Business(UUID.randomUUID, "some business", Set(newEmployee), newAddress)

}
