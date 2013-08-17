package controllers;

import backend.twitter.TwitterClientFactory;
import org.codehaus.jackson.JsonNode;
import play.libs.F;
import play.libs.WS;
import play.mvc.Controller;
import play.mvc.Result;

/**
 * @author sondre
 */
public class Twitter extends Controller {

    public static Result showRateLimitForResource(final String resource) {
        final F.Promise<JsonNode> jsonNodePromise = TwitterClientFactory.create()
                .createRequestFor("1.1/application/rate_limit_status.json")
                .setQueryParameter("resources", resource)
                .get().map(new F.Function<WS.Response, JsonNode>() {
                    @Override
                    public JsonNode apply(WS.Response response) throws Throwable {
                        return response.asJson().findPath("resources");
                    }
                });

        return async(jsonNodePromise.map(new F.Function<JsonNode, Result>() {
            @Override
            public Result apply(JsonNode jsonNode) throws Throwable {
                return ok(jsonNode);
            }
        }));
    }
}
