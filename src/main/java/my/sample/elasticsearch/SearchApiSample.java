package my.sample.elasticsearch;

import my.sample.elasticsearch.util.EsUtil;
import my.sample.elasticsearch.util.JsonGenerator;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.get.MultiGetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;

import java.util.Date;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

public class SearchApiSample {
    public static void main(String[] args) {

        try (Client client = EsUtil.clientBuilder()) {

            // 準備
            BulkRequestBuilder bulkRequest = client.prepareBulk();
            bulkRequest.add(new IndexRequest("twitter", "tweet", "1").source(JsonGenerator.generateJsonString()));
            bulkRequest.add(new IndexRequest("twitter", "tweet", "2").source(JsonGenerator.generateJsonStringByHelper()));
            bulkRequest.add(new IndexRequest("twitter", "tweet", "3").source(JsonGenerator.generateJsonMap()));
            bulkRequest.add(new IndexRequest("twitter", "tweet", "4").source(JsonGenerator.generateJsonArray()));
            bulkRequest.add(new IndexRequest("sample", "parent", "parent-uuid-1").source(JsonGenerator.generateNestedJsonArray()));
            BulkResponse bulkResponse = bulkRequest.get();

            // 確認
            if (bulkResponse.hasFailures()) {
                // process failures by iterating through each bulk response item
                for (BulkItemResponse response : bulkResponse.getItems()) {
                    if (response.isFailed()) {
                        System.out.println(response.getFailure().getMessage());
                    }
                }
            }

            // Search API
            SearchResponse searchResponse = client.prepareSearch("twitter", "sample") // index
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
