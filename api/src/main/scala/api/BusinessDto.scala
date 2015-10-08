package api

case class BusinessDto(name : String, employees : Set[EmployeeDto], address : AddressDto)
