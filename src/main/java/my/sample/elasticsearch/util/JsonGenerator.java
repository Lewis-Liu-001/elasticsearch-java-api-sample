package my.sample.elasticsearch.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import my.sample.elasticsearch.type.Child;
import my.sample.elasticsearch.type.Parent;
import my.sample.elasticsearch.type.Tweet;
import org.elasticsearch.common.xcontent.XContentBuilder;

import java.io.IOException;
import java.util.*;

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

    public static byte[] generateNestedJsonArray() throws JsonProcessingException {

        ObjectMapper mapper = new ObjectMapper(); // create once, reuse

        List<Child> children = new ArrayList<>();
        children.add(new Child("child-uuid-1", "child-1", "チャイルド1の説明"));
        children.add(new Child("child-uuid-2", "child-2", "チャイルド2の説明"));
        children.add(new Child("child-uuid-3", "child-3", "チャイルド3の説明"));

        byte[] jsonArray = mapper.writeValueAsBytes(new Parent("parent-uuid-1", "parent-1", "ペアレント1の説明", children));

        return jsonArray;
    }
}
