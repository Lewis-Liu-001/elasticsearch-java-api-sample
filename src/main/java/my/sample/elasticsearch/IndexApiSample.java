package my.sample.elasticsearch;

import org.elasticsearch.client.Client;

public class IndexApiSample {
    public static void main(String[] args) {

        try (Client client = EsUtil.clientBuilder()) {
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
