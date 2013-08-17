package backend.twitter;

import org.codehaus.jackson.JsonNode;
import org.junit.Test;
import play.libs.F;
import play.libs.WS;

import static org.junit.Assert.assertNotNull;

/**
 * @author sondre
 */
public class TwitterClientFactoryTest {

    @Test
    public void testCreateRequestFor() throws Exception {
        final JsonNode jsonNode = TwitterClientFactory.create()
                .createRequestFor("1.1/application/rate_limit_status.json")
                .setQueryParameter("resources", "search")
                .get().map(new F.Function<WS.Response, JsonNode>() {
                    @Override
                    public JsonNode apply(WS.Response response) throws Throwable {
                        return response.asJson().findPath("resources");
                    }
                }).get();
        assertNotNull(jsonNode);
        System.out.println(jsonNode.toString());
    }
}
