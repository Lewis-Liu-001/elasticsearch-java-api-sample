package my.sample.elasticsearch;

import my.sample.elasticsearch.util.EsUtil;
import my.sample.elasticsearch.util.JsonGenerator;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.get.MultiGetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.Client;

import java.util.Date;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

public class BulkApiSample {
    public static void main(String[] args) {

        try (Client client = EsUtil.clientBuilder()) {

            // Bulk API
            BulkRequestBuilder bulkRequest = client.prepareBulk();

            // 通常の API と同様に Request を作成し、BulkRequest に追加する
            bulkRequest.add(new IndexRequest("twitter", "tweet", "1").source(JsonGenerator.generateJsonString()));
            bulkRequest.add(new IndexRequest("twitter", "tweet", "2").source(JsonGenerator.generateJsonStringByHelper()));
            bulkRequest.add(new IndexRequest("twitter", "tweet", "3").source(JsonGenerator.generateJsonMap()));
            bulkRequest.add(new IndexRequest("twitter", "tweet", "4").source(JsonGenerator.generateJsonArray()));
            bulkRequest.add(new IndexRequest("nested", "parent", "parent-uuid-1").source(JsonGenerator.generateNestedJsonArray(1)));

            // Client#index を用いても可能
            bulkRequest.add(client.prepareIndex("twitter", "tweet", "5")
                .setSource(jsonBuilder()
                    .startObject()
                    .field("user", "kimchy")
                    .field("postDate", new Date())
                    .field("message", "another post")
                    .endObject()
                )
            );

            // Bulk API の呼び出し
            BulkResponse bulkResponse = bulkRequest.get();

            // 結果確認
            if (bulkResponse.hasFailures()) {
                // process failures by iterating through each bulk response item
                for (BulkItemResponse response : bulkResponse.getItems()) {
                    if (response.isFailed()) {
                        System.out.println(response.getFailure().getMessage());
                    }
                }
            }

            // Multi Get API
            MultiGetResponse multiGetItemResponses = client.prepareMultiGet()
                .add("twitter", "tweet", "1", "2", "3", "4", "5")
                .add("nested", "parent", "parent-uuid-1")
                .get();

            EsUtil.printMultiGetResponse(multiGetItemResponses);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
