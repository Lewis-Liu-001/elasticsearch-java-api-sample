package my.sample.elasticsearch;

import my.sample.elasticsearch.util.EsUtil;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.client.Client;

public class GetApiSample {
    public static void main(String[] args) {

        try (Client client = EsUtil.clientBuilder()) {

            // index, type, document の id を指定
            GetResponse getResponse = client.prepareGet("sample", "parent", "parent-uuid-1").get();
            EsUtil.printGetResponse(getResponse);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
