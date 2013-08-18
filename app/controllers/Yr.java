package controllers;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.JsonNodeFactory;
import org.codehaus.jackson.node.ObjectNode;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import play.libs.F;
import play.libs.WS;
import play.libs.XPath;
import play.mvc.Controller;
import play.mvc.Result;

/**
 * @author sondre
 */
public class Yr extends Controller {

    public static Result observationsBergen() {
        final F.Promise<JsonNode> weatherObservationPromise = WS.url("http://www.yr.no/sted/Norge/Hordaland/Bergen/Bergen/varsel.xml").get().map(new F.Function<WS.Response, JsonNode>() {
            @Override
            public JsonNode apply(WS.Response response) throws Throwable {
                final NodeList weatherStations = XPath.selectNodes("//weatherstation", response.asXml());
                final Node stationXmlNode = weatherStations.item(0);
                final ObjectNode stationJsonNode = JsonNodeFactory.instance.objectNode();
                stationJsonNode.put("id", stationXmlNode.getAttributes().getNamedItem("stno").getNodeValue());
                stationJsonNode.put("name", stationXmlNode.getAttributes().getNamedItem("name").getNodeValue());
                final ObjectNode observationNode = JsonNodeFactory.instance.objectNode();
                observationNode.put("description", XPath.selectText("//symbol/@name", stationXmlNode));
                observationNode.put("timestamp", XPath.selectText("//symbol/@time", stationXmlNode));
                stationJsonNode.put("observation", observationNode);
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
}
