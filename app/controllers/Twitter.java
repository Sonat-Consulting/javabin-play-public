package controllers;

import backend.twitter.TwitterClientFactory;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.JsonNodeFactory;
import org.codehaus.jackson.node.ObjectNode;
import play.libs.F;
import play.libs.WS;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.Iterator;

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

    public static Result search(final String searchTerm) {
        final F.Promise<JsonNode> resultJsonPromise = TwitterClientFactory.create()
                .createRequestFor("/1.1/search/tweets.json")
                .setQueryParameter("q", searchTerm)
                .setQueryParameter("count", "100")
                .get().map(new F.Function<WS.Response, JsonNode>() {
                    @Override
                    public JsonNode apply(WS.Response response) throws Throwable {
                        return mapSearchResult(response.asJson());
                    }
                });
        return async(resultJsonPromise.map(new F.Function<JsonNode, Result>() {
            @Override
            public Result apply(final JsonNode jsonNode) throws Throwable {
                return ok(jsonNode);
            }
        }));
    }

    private static JsonNode mapSearchResult(final JsonNode jsonNode) {
        final JsonNode statuses = jsonNode.findPath("statuses");
        final Iterator<JsonNode> elementsIterator = statuses.getElements();
        ArrayNode resultArray = JsonNodeFactory.instance.arrayNode();
        while (elementsIterator.hasNext()) {
            final JsonNode status = elementsIterator.next();
            final ObjectNode resultNode = mapTweet(status);
            resultArray.add(resultNode);
        }
        return resultArray;
    }

    private static ObjectNode mapTweet(final JsonNode status) {
        final ObjectNode resultNode = JsonNodeFactory.instance.objectNode();
        resultNode.put("id", status.findPath("id").getLongValue());
        resultNode.put("text", status.findPath("text").getTextValue());
        resultNode.put("user", status.findPath("user").findPath("screen_name").getTextValue());
        resultNode.put("time", status.findPath("created_at").getTextValue());
        final JsonNode inReplyToStatusNode = status.findPath("in_reply_to_status_id");
        if(! inReplyToStatusNode.isNull()) {
            resultNode.put("replyTo", inReplyToStatusNode.getLongValue());
        }
        return resultNode;
    }
}
