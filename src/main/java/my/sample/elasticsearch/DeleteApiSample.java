package my.sample.elasticsearch;

import my.sample.elasticsearch.util.EsUtil;
import my.sample.elasticsearch.util.JsonGenerator;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.collect.HppcMaps;

public class DeleteApiSample {
    public static void main(String[] args) {

        try (Client client = EsUtil.clientBuilder()) {

            // 準備
            IndexResponse indexResponse = client.prepareIndex("twitter", "tweet", "1")
                .setSource(JsonGenerator.generateJsonString())
                .get();

            // id を指定して削除
            DeleteResponse deleteResponse = client.prepareDelete("twitter", "tweet", "1").get();
            EsUtil.printDeleteResponse(deleteResponse);

            // 存在しない document を削除
            deleteResponse = client
                .prepareDelete("twitter", "tweet", Long.toString(System.nanoTime()))
                .get();
            EsUtil.printDeleteResponse(deleteResponse);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
