package my.sample.elasticsearch;

import my.sample.elasticsearch.util.EsUtil;
import org.elasticsearch.action.search.MultiSearchResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilders;

public class MultiSearchApiSample {
    public static void main(String[] args) {

        try (Client client = EsUtil.clientBuilder()) {

            // 準備
            EsUtil.prepareIndex(client);

            // Multi Search API
            SearchRequestBuilder srb1 = client
                .prepareSearch().setQuery(QueryBuilders.matchQuery("name", "parent-1")).setSize(1);
            SearchRequestBuilder srb2 = client
                .prepareSearch().setQuery(QueryBuilders.matchQuery("user", "alice")).setSize(1);

            MultiSearchResponse sr = client.prepareMultiSearch()
                .add(srb1)
                .add(srb2)
                .execute().actionGet();

            // You will get all individual responses from MultiSearchResponse#getResponses()
            EsUtil.printMultiSearchResponse(sr);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
