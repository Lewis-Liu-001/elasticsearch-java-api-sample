package my.sample.elasticsearch;

import my.sample.elasticsearch.util.EsUtil;
import my.sample.elasticsearch.util.JsonGenerator;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.Client;

public class GetApiSample {
    public static void main(String[] args) {

        try (Client client = EsUtil.clientBuilder()) {

            // 準備
            IndexRequest indexRequest1 = new IndexRequest("sample", "parent", "parent-uuid-1");
            indexRequest1.source(JsonGenerator.generateNestedJsonArray());
            client.index(indexRequest1);

            IndexRequest indexRequest2 = new IndexRequest("twitter", "tweet", "1");
            indexRequest2.source(JsonGenerator.generateJsonArray());
            client.index(indexRequest2);


            // index の作成が完了するのを待つ
            Thread.sleep(1000);

            // index, type, document の id を指定
            GetResponse getResponse = client.prepareGet("sample", "parent", "parent-uuid-1").get();
            EsUtil.printGetResponse(getResponse);

            // GetRequest を利用
            GetRequest getRequest = new GetRequest("twitter", "tweet", "1");
            getResponse = client.get(getRequest).get();
            EsUtil.printGetResponse(getResponse);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
