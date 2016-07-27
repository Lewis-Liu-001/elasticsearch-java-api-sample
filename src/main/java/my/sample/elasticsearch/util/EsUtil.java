package my.sample.elasticsearch.util;

import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.get.MultiGetItemResponse;
import org.elasticsearch.action.get.MultiGetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.MultiSearchResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.SearchHit;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;

public class EsUtil {

    private EsUtil() {
    }

    public static Client clientBuilder() throws UnknownHostException {
        return clientBuilder("127.0.0.1", 9300);
    }

    public static Client clientBuilder(String address, int port) throws UnknownHostException {

        Client client = TransportClient.builder().build()
            .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(address), port));

        return client;
    }

    public static void prepareIndex(Client client) {

        // index が既に存在していると IndexAlreadyExistsException がスローされるため、削除
        client.admin().indices().prepareDelete("nested", "twitter").get();
        client.admin().indices().prepareCreate("nested")
            .addMapping("parent", "{\n" +
                "    \"parent\": {\n" +
                "      \"properties\": {\n" +
                "        \"children\": {\n" +
                "          \"type\": \"nested\"\n" +
                "        }\n" +
                "      }\n" +
                "    }\n" +
                "  }")
            .get();

        BulkRequestBuilder bulkRequest = client.prepareBulk();
        try {
            bulkRequest.add(new IndexRequest("twitter", "tweet", "1").source(JsonGenerator.generateJsonString()));
            bulkRequest.add(new IndexRequest("twitter", "tweet", "2").source(JsonGenerator.generateJsonStringByHelper()));
            bulkRequest.add(new IndexRequest("twitter", "tweet", "3").source(JsonGenerator.generateJsonMap()));
            bulkRequest.add(new IndexRequest("twitter", "tweet", "4").source(JsonGenerator.generateJsonArray()));
            bulkRequest.add(new IndexRequest("nested", "parent", "parent-uuid-1").source(JsonGenerator.generateNestedJsonArray(1)));
            bulkRequest.add(new IndexRequest("nested", "parent", "parent-uuid-2").source(JsonGenerator.generateNestedJsonArray(2)));
            bulkRequest.add(new IndexRequest("nested", "parent", "parent-uuid-3").source(JsonGenerator.generateNestedJsonArray(3)));
        } catch (Exception e) {
            e.printStackTrace();
        }
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

        // Refresh
        client.admin().indices().prepareRefresh().get();
    }

    public static SearchResponse executeSearch(Client client, QueryBuilder qb) {

        return client.prepareSearch() // index
            .setTypes()               // type
            .setQuery(qb)             // Query
            .execute()
            .actionGet();
    }

    public static void printIndexResponse(IndexResponse response) {

        System.out.println("----");
        System.out.println("_index: " + response.getIndex());
        System.out.println("_type: " + response.getType());
        System.out.println("_id: " + response.getId());
        System.out.println("_version: " + response.getVersion());
        System.out.println("created: " + response.isCreated());
        System.out.println("");

    }


    public static void printGetResponse(GetResponse response) {


        System.out.println("----");
        System.out.println("_index: " + response.getIndex());
        System.out.println("_type: " + response.getType());
        System.out.println("_id: " + response.getId());
        System.out.println("_version: " + response.getVersion());
        System.out.println("exists: " + response.isExists());

        Map<String, Object> source = response.getSource();
        for (String key : source.keySet()) {
            System.out.println(key + ": " + source.get(key));
        }
        System.out.println("");

    }

    public static void printDeleteResponse(DeleteResponse response) {

        System.out.println("----");
        System.out.println("_index: " + response.getIndex());
        System.out.println("_type: " + response.getType());
        System.out.println("_id: " + response.getId());
        System.out.println("_version: " + response.getVersion());
        System.out.println("found: " + response.isFound());
        System.out.println("");

    }

    public static void printUpdateResponse(UpdateResponse response) {

        System.out.println("----");
        System.out.println("_index: " + response.getIndex());
        System.out.println("_type: " + response.getType());
        System.out.println("_id: " + response.getId());
        System.out.println("_version: " + response.getVersion());
        System.out.println("created: " + response.isCreated());
        System.out.println("");

    }

    public static void printMultiGetResponse(MultiGetResponse responses) {

        System.out.println("----");
        for (MultiGetItemResponse itemResponse : responses) {

            // 対象のインデックスが存在しない場合、
            // MultiGetItemResponse#getResponse は null を返す
            if (itemResponse.isFailed()) {
                System.out.println(itemResponse.getFailure().getFailure().toString());
            } else {
                GetResponse response = itemResponse.getResponse();
                if (response.isExists()) {
                    String json = response.getSourceAsString();
                    System.out.println(json);
                }
            }
        }
        System.out.println("");

    }

    public static void printSearchResponse(SearchResponse response) {

        System.out.println("----");
        for (SearchHit searchHit : response.getHits().hits()) {
            System.out.println(searchHit.getSource());
        }
        System.out.println("");

    }

    public static void printMultiSearchResponse(MultiSearchResponse response) {

        int count = 1;
        for (MultiSearchResponse.Item item : response.getResponses()) {
            SearchResponse searchResponse = item.getResponse();
            System.out.println("hit: " + Integer.toString(count));
            printSearchResponse(searchResponse);
            count++;
        }

    }

    public static void printQueryDsl(QueryBuilder qb) {
        System.out.println("----");
        System.out.println("Query:");
        System.out.println(qb.toString());
        System.out.println("");

    }

}
