package backend.twitter;

import play.libs.WS;

/**
 * @author sondre
 */
public final class TwitterClientFactory {

    public static final String TWITTER_API_BASE = "https://api.twitter.com";
    public static final String BEARER_TOKEN = "AAAAAAAAAAAAAAAAAAAAALuLSwAAAAAAj5p7%2BOqToO2QQfJ804N%2BavNgj1E%3DNpTRXpzWev0RMZhwcCE08FlL19mE5ehYbPQVlTI";

    private TwitterClientFactory() {

    }

    public static TwitterClientFactory create() {
        return new TwitterClientFactory();
    }

    public WS.WSRequestHolder createRequestFor(final String resource) {

        return WS.url(TWITTER_API_BASE + addLeadingSlashIfApplicable(resource)).setHeader("Authorization", "Bearer " + BEARER_TOKEN);
    }

    private String addLeadingSlashIfApplicable(final String resource) {
        return resource.startsWith("/") ? resource : "/" + resource;
    }
}
