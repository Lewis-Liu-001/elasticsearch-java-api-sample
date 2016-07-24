package my.sample.elasticsearch;

import my.sample.elasticsearch.util.EsUtil;
import my.sample.elasticsearch.util.JsonGenerator;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.Client;

import java.util.Date;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

public class UpdateApiSample {
    public static void main(String[] args) {

        try (Client client = EsUtil.clientBuilder()) {

            // 準備
            IndexRequest indexRequest1 = new IndexRequest("twitter", "tweet", "1");
            indexRequest1.source(JsonGenerator.generateJsonString());
            client.index(indexRequest1);

            // index の作成が完了するのを待つ
            Thread.sleep(1000);

            // UpdateRequest を利用
            UpdateRequest updateRequest = new UpdateRequest("twitter", "tweet", "1");
            updateRequest.doc(jsonBuilder()
                .startObject()
                .field("user", "bob")
                .field("postDate", new Date())
                .field("message", "trying out Elasticsearch")
                .endObject());
            UpdateResponse updateResponse = client.update(updateRequest).get();
            EsUtil.printUpdateResponse(updateResponse);

            // prepareUpdate を利用して、フィールドを追加
            updateResponse = client.prepareUpdate("twitter", "tweet", "1")
                .setDoc(jsonBuilder()
                    .startObject()
                    .field("gender", "male")
                    .endObject()).get();
            EsUtil.printUpdateResponse(updateResponse);


            // Upsert
            // 更新対象がなければ、新規追加（updateRequestでセットした更新はされない）
            // 更新対象があれば、updateRequestでセットしたフィールドのみ更新
            IndexRequest indexRequest2 = new IndexRequest("twitter", "tweet", "2");
            indexRequest2.source(JsonGenerator.generateJsonMap());

            updateRequest = new UpdateRequest("twitter", "tweet", "2")
                .doc(jsonBuilder()
                    .startObject()
                    .field("gender", "male")
                    .field("message", "updated")
                    .endObject())
                .upsert(indexRequest2);
            client.update(updateRequest).get();
            EsUtil.printUpdateResponse(updateResponse);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
