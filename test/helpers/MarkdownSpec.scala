package helpers

import org.specs2.mutable.Specification
import play.api.test.WithApplication

/**
 *
 * @author sondre
 */
class MarkdownSpec extends Specification {

  "# header\n\ntest" should {
    "be converted correctly to HTML" in {
      Markdown.toHtml("# header\n\ntest\"") must contain("<h1>header</h1>")
    }
  }

  "reading markdown from resource \"markdown/assignment.md\"" should {
    "be converted correctly to Html" in new WithApplication {
      Markdown.fromResource("/markdown/assignment.md") must not be None
    }
  }

}
