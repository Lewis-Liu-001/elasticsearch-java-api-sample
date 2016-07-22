package my.sample.elasticsearch;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.common.xcontent.XContentBuilder;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

public class JsonGenerator {

    private JsonGenerator() {
    }

    public static String generateJsonString() throws JsonProcessingException {

        String jsonString = "{" +
            "\"user\":\"alice\"," +
            "\"postDate\":\"2013-01-30\"," +
            "\"message\":\"trying out Elasticsearch\"" +
            "}";

        return jsonString;
    }

    public static String generateJsonStringByHelper() throws IOException {

        XContentBuilder builder = jsonBuilder()
            .startObject()
            .field("user", "bob")
            .field("postDate", new Date())
            .field("message", "trying out Elasticsearch")
            .endObject();

        return builder.string();
    }

    public static Map<String, Object> generateJsonMap() {

        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("user", "charlie");
        jsonMap.put("postDate", new Date());
        jsonMap.put("message", "trying out Elasticsearch");

        return jsonMap;
    }

    public static byte[] generateJsonArray() throws JsonProcessingException {

        // instance a json mapper
        ObjectMapper mapper = new ObjectMapper(); // create once, reuse

        // generate json
        byte[] jsonArray = mapper.writeValueAsBytes(new Tweet("eve", new Date(), "trying out Elasticsearch"));

        return jsonArray;
    }
}
