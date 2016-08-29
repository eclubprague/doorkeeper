package cz.eclub.iot.http;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;

public class HTTPClient {

    private HttpClient client;

    public HTTPClient() {
       client = HttpClientBuilder.create().build();
    }

    public HttpResponse get(String url) {
        HttpGet request = new HttpGet(url);
        HttpResponse response = null;
        try {
            response = client.execute(request);
        } catch (IOException e) {
            System.err.println("Could not send request!");
        }
        return response;
    }
}
