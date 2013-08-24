package controllers;

import org.junit.Test;
import play.mvc.Result;

import static org.junit.Assert.assertEquals;
import static play.mvc.Http.Status.OK;
import static play.test.Helpers.contentType;
import static play.test.Helpers.status;

/**
 * @author sondre
 */
public class YrTest {
    @Test
    public void testObservationsBergen() throws Exception {
        final Result result = Yr.observationsBergen();
        assertEquals(OK, status(result));
        assertEquals("application/json", contentType(result));
    }
}
