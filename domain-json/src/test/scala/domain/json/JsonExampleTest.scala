package domain.json

import java.util.UUID

import domain.{Address, Business, Employee, TestData}
import org.junit.runner._
import org.scalatest.junit.JUnitRunner
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpec}

@RunWith(classOf[JUnitRunner])
class JsonExampleTest extends WordSpec with TestData with Matchers with BeforeAndAfterAll {


  "Business to/from json" should {
    "be able to jsonify the domain classes with argonaut" in {

      // this is considerably more code which as to be kept in sync, though it does perhaps
      // make it more explicit/clear where each piece is built from.
      // I think it'd be annoying though to maintain for bigger case classes when someone added or removed a field
      // in the middle of all the other params
      import argonaut._
      import Argonaut._

      implicit def uuidCodecJson: CodecJson[UUID] = {
        def fromString(s: Option[String]) = s.map(UUID.fromString).getOrElse(???)
        def unapplyUUID(id: UUID) = Option(id.toString)
        codec1(fromString, unapplyUUID)("uuid")
      }

      implicit def addressCodecJson: CodecJson[Address] = {
        casecodec7(Address.apply, Address.unapply)("id", "name", "street", "line1", "line2", "country", "zip-code")
      }

      implicit def employeeCodecJson: CodecJson[Employee] = {
        def fromValues(id: String, name: String, address: Address): Employee = {
          new Employee(UUID.fromString(id), name, address)
        }
        def unapply(employee: Employee): Option[(String, String, Address)] = {
          Some((employee.id.toString, employee.name, employee.address))
        }
        casecodec3(fromValues, unapply)("id", "name", "address")
      }

      implicit def businessJson: CodecJson[Business] = {
        def fromValues(id: String, name: String, employees: Set[Employee], address: Address): Business = {
          new Business(UUID.fromString(id), name, employees, address)
        }
        def unapply(business: Business): Option[(String, String, Set[Employee], Address)] = {
          Some((business.id.toString, business.name, business.employees, business.address))
        }
        casecodec4(fromValues, unapply)("id", "name", "employees", "address")
      }

      val expectedBusiness = newBusiness
      val json = businessJson.encode(expectedBusiness)

      println("Argonaut JSON:")
      println(json.pretty(PrettyParams.spaces2))

      val backAgainResult: DecodeResult[Business] = businessJson.decodeJson(json)

      val Some(backAgain) = backAgainResult.value
      backAgain should equal(expectedBusiness)

    }

    "be able to jsonify the domain classes with play json inception" in {

      // interestingly Play json inception already knows how to handle UUIDs.
      // I think this is exactly the right balance -- you can easily manually provide your own format if you
      // need to customize the json, and just add one line for each domain class type. What'll that typically be,
      // less than 50, surely
      import play.api.libs.json._
      implicit val addressJsonFmt = Json.format[Address]
      implicit val employeeJsonFmt = Json.format[Employee]
      implicit val businessJsonFmt = Json.format[Business]

      val expectedBusiness = newBusiness
      val json = Json.prettyPrint(businessJsonFmt.writes(expectedBusiness))
      println("Play JSON:")
      println(json)

      val backAgainResult: JsResult[Business] = businessJsonFmt.reads(Json.parse(json))

      val JsSuccess(backAgain, _) = backAgainResult
      backAgain should equal(expectedBusiness)
    }
  }
}
