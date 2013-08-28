import models.Resource;
import org.junit.Test;
import play.data.Form;
import play.i18n.Messages;
import play.libs.F;
import play.mvc.Content;
import play.test.TestBrowser;

import java.util.Collections;

import static org.fest.assertions.Assertions.assertThat;
import static play.test.Helpers.*;


/**
 * Simple (JUnit) tests that can call all parts of a play app.
 * If you are interested in mocking a whole application, see the wiki for more details.
 */
public class ApplicationTest {

    public static final int TEST_SERVER_PORT = 3101;

    @Test
    public void simpleCheck() {
        int a = 1 + 1;
        assertThat(a).isEqualTo(2);
    }

    @Test
    public void renderTemplate() {
        Content html = views.html.index.render(Collections.<Resource>emptyList(),
                Form.form(Resource.class));
        assertThat(contentType(html)).isEqualTo("text/html");
        assertThat(contentAsString(html)).contains(Messages.get("header"));
    }

    @Test
    public void testInBrowser() throws Exception {
        running(testServer(TEST_SERVER_PORT), FIREFOX, new F.Callback<TestBrowser>() {
            @Override
            public void invoke(TestBrowser browser) throws Throwable {
                browser.goTo("http://localhost:" + TEST_SERVER_PORT);
                assertThat(browser.$("h1").getText()).isEqualTo(Messages.get("header"));
            }
        });
    }
}
