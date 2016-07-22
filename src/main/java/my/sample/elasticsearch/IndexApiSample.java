package my.sample.elasticsearch;

import org.elasticsearch.action.delete.DeleteResponse;
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

            // id を指定して削除
            DeleteResponse deleteResponse = client.prepareDelete("twitter", "tweet", "1").get();
            EsUtil.printDeleteResponse(deleteResponse);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
