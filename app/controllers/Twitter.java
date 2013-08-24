package controllers;

import backend.twitter.TwitterClientFactory;
import com.google.common.base.Strings;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.JsonNodeFactory;
import org.codehaus.jackson.node.ObjectNode;
import play.cache.Cache;
import play.libs.F;
import play.libs.WS;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.Iterator;
import java.util.concurrent.Callable;

/**
 * @author sondre
 */
public class Twitter extends Controller {

    public static final int DEFAULT_SEARCH_MAX_COUNT = 100;
    public static final int CACHE_EXPIRATION_TWITTER_SEARCH = 60 * 10;

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

    public static Result search(final String searchTerm, final String geoCode) {
        try {
            return Cache.getOrElse(String.format("twitterSearch?q=%1$s,geocode=%2$s", searchTerm, geoCode),
                    new TwitterSearchCallable(searchTerm, geoCode), CACHE_EXPIRATION_TWITTER_SEARCH);
        } catch (Exception e) {
            return internalServerError(e.getMessage());
        }
    }

    private static WS.WSRequestHolder getTwitterSearchRequest(String searchTerm) {
        return TwitterClientFactory.create()
                .createRequestFor("/1.1/search/tweets.json")
                .setQueryParameter("q", searchTerm)
                .setQueryParameter("count", Integer.toString(DEFAULT_SEARCH_MAX_COUNT));
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

    private static class TwitterSearchCallable implements Callable<Result> {
        private final String searchTerm;
        private final String geoCode;

        public TwitterSearchCallable(String searchTerm, String geoCode) {
            this.searchTerm = searchTerm;
            this.geoCode = geoCode;
        }

        @Override
        public Result call() throws Exception {
            final WS.WSRequestHolder twitterSearchRequest = getTwitterSearchRequest(searchTerm);
            if (!Strings.isNullOrEmpty(geoCode)) {
                twitterSearchRequest.setQueryParameter("geocode", geoCode);
            }
            final F.Promise<JsonNode> resultJsonPromise = twitterSearchRequest
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
    }
}
