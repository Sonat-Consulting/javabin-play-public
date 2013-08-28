package helpers

import play.api.Play.current
import play.api.Play
import org.pegdown.{Extensions, PegDownProcessor}
import scala.reflect.io.Streamable

/**
 *
 * @author sondre
 */
object Markdown {

  private val markdownProcessor = new PegDownProcessor(Extensions.ALL)

  def toHtml(markdown: String) = {
    markdownProcessor.markdownToHtml(markdown)
  }

  def fromResource(fileName: String): Option[String] = {
    Play.resourceAsStream(fileName).map(inputStream => toHtml(Streamable.slurp(inputStream)))
  }

  def asHtmlStringFromResource(fileName: String): String = {
    fromResource(fileName) getOrElse ""
  }
}

