package com.ym.materials.httpClient;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by ym on 2018/5/17.
 */
public class HttpClientClientMain {

    public static void main(String[] args) throws URISyntaxException, IOException {
        URI uri = new URIBuilder()
                .setScheme("http")
                .setHost("www.baidu.com")
                .setPort(80)
                .setPath("/")
                .build();

        HttpGet get = new HttpGet(uri);

        CloseableHttpClient httpClient = HttpClients.createDefault();

        ApiResult<String> apiResult = httpClient.execute(get, new ResponseHandler<ApiResult<String>>() {
            @Override
            public ApiResult handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
                String body = EntityUtils.toString(response.getEntity(), "UTF-8");
                return ApiResult.buildSussess(body);
            }
        });
        System.out.println(apiResult.getData());
    }

    @Test
    public void testFlow() throws URISyntaxException {
        URI uri = new URIBuilder()
                .setScheme("http")
                .setHost("www.baidu.com")
                .setPort(80)
                .setPath("/")
                .build();


    }
}
