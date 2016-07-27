package my.sample.elasticsearch;

import my.sample.elasticsearch.util.EsUtil;
import my.sample.elasticsearch.util.JsonGenerator;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.MultiSearchResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.collect.HppcMaps;
import org.elasticsearch.index.query.QueryBuilders;

public class MultiSearchApiSample {
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
