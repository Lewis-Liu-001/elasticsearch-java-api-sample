package my.sample.elasticsearch;

import my.sample.elasticsearch.util.EsUtil;
import my.sample.elasticsearch.util.JsonGenerator;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.Client;

public class DeleteApiSample {
    public static void main(String[] args) {

        try (Client client = EsUtil.clientBuilder()) {

            // 準備
            IndexRequest indexRequest1 = new IndexRequest("nested", "parent", "parent-uuid-1");
            indexRequest1.source(JsonGenerator.generateNestedJsonArray(1));
            client.index(indexRequest1);

            IndexRequest indexRequest2 = new IndexRequest("twitter", "tweet", "1");
            indexRequest2.source(JsonGenerator.generateJsonArray());
            client.index(indexRequest2);

            // index の作成が完了するのを待つ
            Thread.sleep(1000);

            // id を指定して削除
            DeleteResponse deleteResponse = client.prepareDelete("twitter", "tweet", "1").get();
            EsUtil.printDeleteResponse(deleteResponse);

            // 存在しない document を削除
            deleteResponse = client
                .prepareDelete("twitter", "tweet", Long.toString(System.nanoTime()))
                .get();
            EsUtil.printDeleteResponse(deleteResponse);

            // DeleteRequest を利用
            DeleteRequest deleteRequest = new DeleteRequest("nested", "parent", "parent-uuid-1");
            deleteResponse = client.delete(deleteRequest).get();
            EsUtil.printDeleteResponse(deleteResponse);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
