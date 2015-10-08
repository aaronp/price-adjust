package domain.dao

import java.util.UUID

import domain.Employee

trait EmployeeDao {
  def save(employee: Employee): Employee
  def get(employeeId: UUID): Option[Employee]
  def delete(employeeId: UUID): Option[Employee]
  def list(): Stream[Employee]
}
