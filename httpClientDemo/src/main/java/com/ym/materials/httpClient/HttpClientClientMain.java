package com.ym.materials.httpClient;

import com.alibaba.fastjson.JSON;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.fluent.Executor;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
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
    public void testFlow() throws Exception {
        URI uri = new URIBuilder()
                .setScheme("http")
                .setHost("www.baidu.com")
                .setPort(80)
                .setPath("/")
                .build();

        String body = Request.Get(uri).execute().returnContent().asString();
        System.out.println(body);

    }

    @Test
    public void testFlowWithResponseHander() throws Exception {
        URI uri = new URIBuilder()
                .setScheme("http")
                .setHost("www.baidu.com")
                .setPort(80)
                .setPath("/")
                .build();

        ApiResult<String> apiResult = Request.Get(uri).execute().handleResponse(new ResponseHandler<ApiResult<String>>() {
            @Override
            public ApiResult<String> handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
                String body = EntityUtils.toString(response.getEntity(), "UTF-8");
                return ApiResult.buildSussess(body);
            }
        });

        System.out.println(JSON.toJSONString(apiResult));
    }
}
