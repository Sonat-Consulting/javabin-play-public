package controllers;

import com.google.common.base.Strings;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.JsonNodeFactory;
import org.codehaus.jackson.node.ObjectNode;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import play.cache.Cached;
import play.libs.F;
import play.libs.WS;
import play.libs.XPath;
import play.mvc.Controller;
import play.mvc.Result;

/**
 * @author sondre
 */
public class Yr extends Controller {

    public static final int CACHE_EXPIRATION_OBSERVATIONS = 10 * 60;
    public static final String YR_FORECAST_URL = "http://www.yr.no/sted/%1$s/%2$s/%3$s/%4$s/varsel.xml";

    public static Result observations(final String country,
                                      final String county,
                                      final String municipality,
                                      final String place) {
        if(Strings.isNullOrEmpty(country)
                || Strings.isNullOrEmpty(county)
                || Strings.isNullOrEmpty(municipality)
                || Strings.isNullOrEmpty(place)) {
            return badRequest("Empty or illegal values provided");
        }

        final F.Promise<JsonNode> weatherObservationPromise = WS.url(String.format(YR_FORECAST_URL, country, county, municipality, place))
                .get()
                .map(new F.Function<WS.Response, JsonNode>() {
                    @Override
                    public JsonNode apply(WS.Response response) throws Throwable {
                        final NodeList weatherStations = XPath.selectNodes("//weatherstation", response.asXml());
                        final Node stationXmlNode = weatherStations.item(0);
                        final JsonNodeFactory jsonNodeFactory = JsonNodeFactory.instance;
                        final ObjectNode stationJsonNode = jsonNodeFactory.objectNode();
                        stationJsonNode.put("id", stationXmlNode.getAttributes().getNamedItem("stno").getNodeValue());
                        stationJsonNode.put("name", stationXmlNode.getAttributes().getNamedItem("name").getNodeValue());

                        final ObjectNode positionNode = jsonNodeFactory.objectNode();
                        positionNode.put("longitude", Float.valueOf(XPath.selectText("//weatherstation/@lon", stationXmlNode)).floatValue());
                        positionNode.put("latitude", Float.valueOf(XPath.selectText("//weatherstation/@lat", stationXmlNode)).floatValue());
                        stationJsonNode.put("position", positionNode);

                        final ObjectNode observationNode = jsonNodeFactory.objectNode();
                        observationNode.put("description", XPath.selectText("//symbol/@name", stationXmlNode));
                        observationNode.put("timestamp", XPath.selectText("//symbol/@time", stationXmlNode));
                        stationJsonNode.put("observation", observationNode);
                        final ObjectNode temperatureNode = jsonNodeFactory.objectNode();
                        final NamedNodeMap temperatureElementAttributes = XPath.selectNode("//temperature", stationXmlNode).getAttributes();

                        temperatureNode.put("value", temperatureElementAttributes.getNamedItem("value").getNodeValue());
                        temperatureNode.put("unit", temperatureElementAttributes.getNamedItem("unit").getNodeValue());
                        temperatureNode.put("timestamp", XPath.selectText("//temperature/@time", stationXmlNode));
                        stationJsonNode.put("temperature", temperatureNode);

                        return stationJsonNode;

                    }
                });
        return async(weatherObservationPromise.map(new F.Function<JsonNode, Result>() {
            @Override
            public Result apply(JsonNode jsonNode) throws Throwable {
                return ok(jsonNode);
            }
        }));
    }

    @Cached(key = "observationsBergen", duration = CACHE_EXPIRATION_OBSERVATIONS)
    public static Result observationsBergen() {
         return observations("Norge", "Hordaland", "Bergen", "Bergen");
    }
}
