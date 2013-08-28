package controllers

import org.specs2.mutable.Specification
import play.api.test.WithApplication

/**
 *
 * @author sondre
 */
class DolcherWordsTest extends Specification {

  "When reading in all dolcher words" should {
    "there be at list one dolcher word to choose from" in new WithApplication {
        DolcherWords.getAllDolcherWords must not be None
    }
  }

}
