package my.sample.elasticsearch;

import my.sample.elasticsearch.util.EsUtil;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

public class QueryDslSample {
    public static void main(String[] args) {

        try (Client client = EsUtil.clientBuilder()) {

            // 準備

            EsUtil.prepareIndex(client);

            // Match All Query
            QueryBuilder qb = QueryBuilders.matchAllQuery();
            EsUtil.printQueryDsl(qb);
            EsUtil.printSearchResponse(EsUtil.executeSearch(client, qb));

            // Match Query
            qb = QueryBuilders.matchQuery(
                "message",                 // field
                "trying out Elasticsearch" // text
            )
                .operator(MatchQueryBuilder.Operator.AND)              // operator
                .zeroTermsQuery(MatchQueryBuilder.ZeroTermsQuery.NONE) // zero_terms_query"
                .cutoffFrequency(0.001f);                              // cutoff_frequency"
            EsUtil.printQueryDsl(qb);
            EsUtil.printSearchResponse(EsUtil.executeSearch(client, qb));

            // Multi Match Query
            qb = QueryBuilders.multiMatchQuery(
                "charlie parent-1", // text
                "user", "name"      // fields
            );
            EsUtil.printQueryDsl(qb);
            EsUtil.printSearchResponse(EsUtil.executeSearch(client, qb));

            //  Common Terms Query
            qb = QueryBuilders.commonTermsQuery(
                "message",
                "trying out Elasticsearch"
            ).cutoffFrequency(0.001f);
            EsUtil.printQueryDsl(qb);
            EsUtil.printSearchResponse(EsUtil.executeSearch(client, qb));

            // Type Query
            qb = QueryBuilders.typeQuery("tweet");
            EsUtil.printQueryDsl(qb);
            EsUtil.printSearchResponse(EsUtil.executeSearch(client, qb));

            // Ids Query
            qb = QueryBuilders.idsQuery() // type is optional
                .addIds("1", "4", "parent-uuid-1");
            EsUtil.printQueryDsl(qb);
            EsUtil.printSearchResponse(EsUtil.executeSearch(client, qb));

            // Nested Query
            qb = QueryBuilders.nestedQuery(
                "children",
                QueryBuilders.boolQuery()
                    .must(QueryBuilders.matchQuery("children.name", "child-5")))
                .scoreMode("avg");
            EsUtil.printQueryDsl(qb);
            EsUtil.printSearchResponse(EsUtil.executeSearch(client, qb));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
