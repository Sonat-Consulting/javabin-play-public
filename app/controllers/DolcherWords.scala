package controllers

import play.api.mvc.WebSocket
import play.api.libs.iteratee.{Enumerator, Iteratee}
import play.api.Play
import play.api.Play.current
import play.api.libs.concurrent.Promise
import scala.util.Random
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.io.Source

/**
 *
 * @author sondre
 */
object DolcherWords {

  def getAllDolcherWords = {
    Play.resourceAsStream("/dolcher_words.txt").map {
      inputStream => Source.fromInputStream(inputStream).getLines.toArray
    }
  }

  def index = WebSocket.using[String] {
    request =>

    // Log events to the console
      val in = Iteratee.foreach[String](println).mapDone {
        _ =>
          println("Disconnected")
      }
      val dolcherWords = getAllDolcherWords


      val enumerator = dolcherWords.map {
        array => Enumerator.generateM {

          Promise.timeout(Some(array(Random.nextInt(array.length))), 2 seconds)
        }
      }.getOrElse {
        Enumerator("error")
      }


      (in, enumerator)
  }
}
