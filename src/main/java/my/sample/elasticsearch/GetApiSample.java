package my.sample.elasticsearch;

import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.client.Client;

public class GetApiSample {
    public static void main(String[] args) {

        try (Client client = EsUtil.clientBuilder()) {
            GetResponse getResponse = client.prepareGet("twitter", "tweet", "2").get();
            EsUtil.printGetResponse(getResponse);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
