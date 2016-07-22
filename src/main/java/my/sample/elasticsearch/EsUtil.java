package my.sample.elasticsearch;

import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;

import java.net.InetAddress;
import java.net.UnknownHostException;

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
        // Index name
        String _index = response.getIndex();
        // Type name
        String _type = response.getType();
        // Document ID (generated or not)
        String _id = response.getId();
        // Version (if it's the first time you index this document, you will get: 1)
        long _version = response.getVersion();
        // isCreated() is true if the document is a new one, false if it has been updated
        boolean created = response.isCreated();

        System.out.println("_index: " + _index);
        System.out.println("_type: " + _type);
        System.out.println("_id: " + _id);
        System.out.println("_version: " + _version);
        System.out.println("created: " + created);

    }

    public static void printDeleteResponse(DeleteResponse response) {
        // Index name
        String _index = response.getIndex();
        // Type name
        String _type = response.getType();
        // Document ID (generated or not)
        String _id = response.getId();
        // Version (if it's the first time you index this document, you will get: 1)
        long _version = response.getVersion();
        // isCreated() is true if the document is a new one, false if it has been updated
        boolean found = response.isFound();

        System.out.println("_index: " + _index);
        System.out.println("_type: " + _type);
        System.out.println("_id: " + _id);
        System.out.println("_version: " + _version);
        System.out.println("found: " + found);

    }
}
