package backend.twitter;

import com.ning.http.client.Realm;
import org.codehaus.jackson.JsonNode;
import play.libs.F;
import play.libs.WS;

/**
 * @author sondre
 */
public final class TwitterApplicationAuthenticator {

    private TwitterApplicationAuthenticator() {}

    public static TwitterApplicationAuthenticator create() {
        return new TwitterApplicationAuthenticator();
    }

    public F.Promise<String> obtainBearerToken(final String consumerKey, final String consumerToken) {
        final F.Promise<WS.Response> responsePromise = WS.url(TwitterClientFactory.TWITTER_API_BASE + "/oauth2/token")
                .setAuth(consumerKey, consumerToken, Realm.AuthScheme.BASIC)
                .setContentType("application/x-www-form-urlencoded;charset=UTF-8")
                .post("grant_type=client_credentials");
        return responsePromise.map(new F.Function<WS.Response, String>() {
            @Override
            public String apply(WS.Response response) throws Throwable {
                final JsonNode jsonNode = response.asJson();
                if("bearer".equals(jsonNode.findPath("token_type").getTextValue())) {
                    return jsonNode.findPath("access_token").getTextValue();
                } else {
                    throw new RuntimeException(String.format("Illegal response from the Twitter API. Data returned: %1$s", response.getBody()));
                }
            }
        });
    }


}
