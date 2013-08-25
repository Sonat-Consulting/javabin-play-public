package controllers

import play.api.libs.ws.WS
import play.api.mvc._
import play.api.mvc.Results._
import play.api.libs.json.Json
import scala.concurrent.ExecutionContext
import ExecutionContext.Implicits.global
import scala.xml.Node
import play.api.Logger
import play.api.cache.Cache
import play.api.Play.current

/**
 *
 * @author sondre
 */
object YrOgGlad {

  private val cacheTimeout: Int = 10 * 60

  def observations(country: String, county: String, municipality: String, place: String) = Action {
    Cache.getOrElse(s"observations.$country.$county.$municipality.$place", cacheTimeout) {
      Async {
        WS.url(s"http://www.yr.no/sted/$country/$county/$municipality/$place/varsel.xml").get().map {
          response =>
            Ok(mapToJson(response.xml))
        }
      }

    }
  }

  def mapToJson(xmlResponse: Node) = {

    Logger.debug(s"XML data from YR $xmlResponse")
    val weatherStationNode: Node = (xmlResponse \\ "observations" \ "weatherstation").head
    Json.obj(
      "id" -> (weatherStationNode \ "@stno").text,
      "name" -> (weatherStationNode \ "@name").text,
      "position" -> Json.obj(
        "longitude" -> (weatherStationNode \ "@lon").text.toFloat,
        "latitude" -> (weatherStationNode \ "@lat").text.toFloat),
      "observation" -> Json.obj(
        "description" -> (weatherStationNode \ "symbol" \ "@name").text,
        "timestamp" -> (weatherStationNode \ "symbol" \ "@time").text
      ),
      "temperature" -> Json.obj(
        "value" -> (weatherStationNode \ "temperature" \ "@value").text.toDouble,
        "unit" -> (weatherStationNode \ "temperature" \ "@unit").text,
        "timestamp" -> (weatherStationNode \ "temperature" \ "@time").text
      )
    )
  }


}
