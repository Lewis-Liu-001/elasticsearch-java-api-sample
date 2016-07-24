package my.sample.elasticsearch.util;

import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;

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

    public static void printIndexResponse(IndexResponse response) {

        System.out.println("----");
        System.out.println("_index: " + response.getIndex());
        System.out.println("_type: " + response.getType());
        System.out.println("_id: " + response.getId());
        System.out.println("_version: " + response.getVersion());
        System.out.println("created: " + response.isCreated());
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

    }

    public static void printDeleteResponse(DeleteResponse response) {

        System.out.println("----");
        System.out.println("_index: " + response.getIndex());
        System.out.println("_type: " + response.getType());
        System.out.println("_id: " + response.getId());
        System.out.println("_version: " + response.getVersion());
        System.out.println("found: " + response.isFound());

    }

    public static void printUpdateResponse(UpdateResponse response) {

        System.out.println("----");
        System.out.println("_index: " + response.getIndex());
        System.out.println("_type: " + response.getType());
        System.out.println("_id: " + response.getId());
        System.out.println("_version: " + response.getVersion());
        System.out.println("created: " + response.isCreated());

    }

}
