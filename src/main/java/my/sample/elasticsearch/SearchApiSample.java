package my.sample.elasticsearch;

import my.sample.elasticsearch.util.EsUtil;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilders;

public class SearchApiSample {
    public static void main(String[] args) {

        try (Client client = EsUtil.clientBuilder()) {

            // 準備
            EsUtil.prepareIndex(client);

            // Search API
            SearchResponse searchResponse = client.prepareSearch("twitter", "nested") // index
                .setTypes("tweet", "parent") // type
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(QueryBuilders.termQuery("message", "elasticsearch"))                 // Query
//                .setPostFilter(QueryBuilders.rangeQuery("age").from(12).to(18))     // Filter
//                .setFrom(0).setSize(60).setExplain(true)
                .execute()
                .actionGet();

            EsUtil.printSearchResponse(searchResponse);

            // MatchAll on the whole cluster with all default options
            EsUtil.printSearchResponse(client.prepareSearch().execute().actionGet());


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
