package domain.dao

import java.util.UUID

import domain.Business

trait BusinessDao {
  def save(business: Business): Business
  def get(businessId: UUID): Option[Business]
  def delete(businessId: UUID): Option[Business]
  def list(): Stream[Business]
}
