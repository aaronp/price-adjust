package api.service

trait CrudService[Key, Value] {
  def get(key : Key) : Option[Value]
  def delete(key : Key) : Option[Value]
  def save(key : Key, value : Value) : Value
}
