package my.sample.elasticsearch;

import my.sample.elasticsearch.util.EsUtil;
import my.sample.elasticsearch.util.JsonGenerator;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Client;

public class IndexApiSample {
    public static void main(String[] args) {

        try (Client client = EsUtil.clientBuilder()) {

            // 更新対象となる document の id を指定（対象が存在しなければ追加）
            IndexResponse indexResponse = client.prepareIndex("twitter", "tweet", "1")
                .setSource(JsonGenerator.generateJsonString())
                .get();
            EsUtil.printIndexResponse(indexResponse);

            // document の id を省略すると、id は自動で付与されて追加
            indexResponse = client.prepareIndex("twitter", "tweet")
                .setSource(JsonGenerator.generateJsonStringByHelper())
                .get();
            EsUtil.printIndexResponse(indexResponse);

            // ネストした document
            indexResponse = client.prepareIndex("nested", "parent", "parent-uuid-1")
                .setSource(JsonGenerator.generateNestedJsonArray(1))
                .get();
            EsUtil.printIndexResponse(indexResponse);

            // IndexRequest を利用
            IndexRequest indexRequest = new IndexRequest("twitter", "tweet", "2");
            indexRequest.source(JsonGenerator.generateJsonMap());
            indexResponse = client.index(indexRequest).get();
            EsUtil.printIndexResponse(indexResponse);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
