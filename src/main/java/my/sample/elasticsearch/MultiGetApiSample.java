package my.sample.elasticsearch;

import my.sample.elasticsearch.util.EsUtil;
import my.sample.elasticsearch.util.JsonGenerator;
import org.elasticsearch.action.get.MultiGetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.Client;

public class MultiGetApiSample {
    public static void main(String[] args) {

        try (Client client = EsUtil.clientBuilder()) {

            // 準備
            // TODO:複数の index を 作成/削除する場合は Bulk API を使用すること
            client.index(new IndexRequest("twitter", "tweet", "1").source(JsonGenerator.generateJsonString()));
            client.index(new IndexRequest("twitter", "tweet", "2").source(JsonGenerator.generateJsonStringByHelper()));
            client.index(new IndexRequest("twitter", "tweet", "3").source(JsonGenerator.generateJsonMap()));
            client.index(new IndexRequest("twitter", "tweet", "4").source(JsonGenerator.generateJsonArray()));
            client.index(new IndexRequest("sample", "parent", "parent-uuid-1").source(JsonGenerator.generateNestedJsonArray()));

            MultiGetResponse multiGetItemResponses = client.prepareMultiGet()
                .add("twitter", "tweet", "1")
                .add("twitter", "tweet", "2", "3", "4")
                .add("sample", "parent", "parent-uuid-1")
                .get();

            // 対象の index が存在しない場合、
            // MultiGetItemResponse#getResponse は null を返す
            EsUtil.printMultiGetResponse(multiGetItemResponses);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
