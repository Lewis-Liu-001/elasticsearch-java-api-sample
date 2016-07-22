package my.sample.elasticsearch;

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
}
