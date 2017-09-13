package com.boot.util;

import com.boot.common.base.Charsets;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Throwables;
import com.google.common.net.MediaType;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.Validate;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * http请求工具类
 */
@Slf4j
@UtilityClass
public class HttpClientUtils {
    public static final int MAX_TOTAL = 500;
    public static final int MAX_PRE_ROUTE = 50;
    public static final int VALIDATE_AFTER_INACTIVITY = 1900;
    public static final int RETRY_COUNT = 3;
    public static final int KEEPALIVE_TIME = 20 * 1000;
    public static final int SC_TIMEOUT = 30000;
    public static final int CONNECT_TIMEOUT = 20000;
    private static final String CLOSE_ERROR_MESSAGE = "IOException thrown while closing Closeable.";

    private final static ObjectMapper MAPPER = new ObjectMapper();

    private static RequestConfig requestConfig = RequestConfig
            .custom()
            .setSocketTimeout(SC_TIMEOUT)
            .setConnectTimeout(CONNECT_TIMEOUT)
            .build();

    /**
     * GET
     *
     * @param url
     * @param params
     * @return String
     */
    public static String get(String url, Map<String, Object> params) {
        CloseableHttpResponse response = null;
        try{
            CloseableHttpClient httpClient = getHttpClient();
            List<NameValuePair> _params = generateNameValuePairs(params);
            String queryString = URLEncodedUtils.format(_params, Charsets.UTF_8_VALUE);
            url = url + "?" + queryString;
            HttpGet get = new HttpGet(url);
            //设置请求和传输超时时间
            get.setConfig(requestConfig);
            response = httpClient.execute(get);
            return getResponseData(get, response);
        } catch (Exception e) {
            log.error("get httpclient url:{}, error:{}", url, e.getMessage());
            e.printStackTrace();
        }
        return "";
    }

    public static CloseableHttpClient getHttpClient() throws Exception {
        CloseableHttpClient httpClient = HttpClients.custom()
                .setConnectionManager(ConnectionManagerHolder.connectionManager)
                // 设置keepAlive 20s
                .setKeepAliveStrategy((httpResponse, httpContext) -> KEEPALIVE_TIME)
                .setRetryHandler(retryHandler()).build();
        return httpClient;
    }

    public static List<NameValuePair> generateNameValuePairs(Map<String, Object> paramMap) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        if (paramMap != null) {
            for (String name : paramMap.keySet()) {
                Object value = paramMap.get(name);
                params.add(new BasicNameValuePair(name,
                        String.valueOf(Optional.ofNullable(value)
                                .orElse(""))));
            }
        }
        return params;
    }

    /**
     * 解决无效请求连接不释放问题，引起大量Close wait
     * @param request
     * @param response
     * @return
     * @throws IOException
     */
    public String getResponseData(HttpRequestBase request, CloseableHttpResponse response) throws IOException {
        try {
            if (response == null || !(response.getStatusLine().getStatusCode() >= 200
                    && response.getStatusLine().getStatusCode() < 300)) {
                throw new RuntimeException("error get http request, " + response.getStatusLine().getStatusCode());
            }
            return EntityUtils.toString(response.getEntity(), Charsets.UTF_8);
        } catch (Exception e) {
            request.abort();
            throw e;
        } finally {
            try {
                if (response != null) {
                    EntityUtils.consume(response.getEntity()); //会自动释放连接
                }
            } finally {
                if (response != null) {
                    try {
                        response.close();
                    } catch (IOException e) {
                        log.warn(CLOSE_ERROR_MESSAGE, e);
                    }
                }
            }
        }
    }

    public static final SSLConnectionSocketFactory ssl() throws Exception {
        //注册https的connectionSocketFactory
        SSLContext sslcontext = SSLContexts
                .custom()
                .loadTrustMaterial(null,
                        new TrustSelfSignedStrategy())
                .build();
        HostnameVerifier hostnameVerifier = SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;
        return new SSLConnectionSocketFactory(
                sslcontext, hostnameVerifier);
    }

    static class ConnectionManagerHolder {

        static PoolingHttpClientConnectionManager connectionManager;

        static {
            RegistryBuilder<ConnectionSocketFactory> builder = RegistryBuilder.create();
            //注册http的connectionSocketFactory
            ConnectionSocketFactory connectionSocketFactory = PlainConnectionSocketFactory
                    .getSocketFactory();
            builder.register("http", connectionSocketFactory);
            try {
                builder.register("https", ssl());
            } catch (Exception e) {
                e.printStackTrace();
            }
            Registry<ConnectionSocketFactory> registry = builder.build();
            connectionManager = new PoolingHttpClientConnectionManager(registry);
            //最大连接数
            connectionManager.setMaxTotal(MAX_TOTAL);
            connectionManager.setDefaultMaxPerRoute(MAX_PRE_ROUTE);
            connectionManager.setValidateAfterInactivity(VALIDATE_AFTER_INACTIVITY);
        }
    }

    /**
     * POST
     *
     * @param url
     * @param paramMap
     * @return
     * @throws Exception
     */
    public String post(String url, Map<String, Object> paramMap) throws Exception {
        CloseableHttpClient httpClient = getHttpClient();
        HttpPost post = new HttpPost(url);
        List<NameValuePair> params = generateNameValuePairs(paramMap);
        post.setEntity(new UrlEncodedFormEntity(params, Charsets.UTF_8_VALUE));
        //设置请求和传输超时时间
        post.setConfig(requestConfig);
        CloseableHttpResponse response = httpClient.execute(post);
        return getResponseData(post, response);
    }

    public static String postJson(String url, Map<String, Object> json) throws JsonProcessingException{
        return postJson(url, MAPPER.writeValueAsString(json));
    }

    /**
     * post json 字符串
     *
     * @param url
     * @param json json字符串
     * @return
     */
    public static String postJson(String url, String json) {
        try {
            CloseableHttpClient httpClient = getHttpClient();
            HttpPost post = postFormStr(url, json);
            post.setConfig(requestConfig);
            CloseableHttpResponse response = httpClient.execute(post);
            return getResponseData(post, response);
        } catch (Exception e) {
            log.error(Throwables.getStackTraceAsString(e));
            throw new RuntimeException("post json request error:" + e.getMessage());
        }
    }

    private static HttpPost postFormStr(String url, String json) throws Exception {
        Validate.notEmpty(url, "Request url can't be empty.");
        HttpPost post = new HttpPost(url);
        StringEntity entity = new StringEntity(json, Charsets.UTF_8_VALUE);
        entity.setContentType(MediaType.JSON_UTF_8.toString());
        entity.setContentEncoding(Charsets.UTF_8_VALUE);
        post.setEntity(entity);
        return post;
    }

    /**
     * https 带加密证书请求
     *
     * @param url
     * @param json         Map<String,Object>
     * @param certUrl
     * @param pwd
     * @param keyStoreType
     * @return
     */
    public static String postJson(String url, Map<String, Object> json, String certUrl, String pwd,
                                  String keyStoreType) throws JsonProcessingException{
        return sslPost(url, MAPPER.writeValueAsString(json), certUrl, pwd, keyStoreType);
    }

    /**
     * https 带加密证书请求
     *
     * @param url
     * @param json         json串
     * @param certUrl
     * @param pwd
     * @param keyStoreType
     * @return
     */
    public static String sslPost(String url, String json, String certUrl, String pwd,
                                 String keyStoreType) {

        CloseableHttpClient httpclient = null;
        try {
            httpclient = getHttpClient(certUrl, pwd, keyStoreType);
            HttpPost post = new HttpPost(url);
            StringEntity entity = new StringEntity(json, Charsets.UTF_8_VALUE);
            entity.setContentType(MediaType.JSON_UTF_8.toString());
            entity.setContentEncoding(Charsets.UTF_8_VALUE);
            post.setEntity(entity);
            post.setConfig(requestConfig);
            CloseableHttpResponse response = httpclient.execute(post);
            return getResponseData(post, response);
        } catch (Exception e) {
            log.error(Throwables.getStackTraceAsString(e));
            throw new RuntimeException("ssl post request error:" + e.getMessage());
        }
    }

    /**
     * https 带价密证书的请求
     *
     * @param cert
     * @param pwd
     * @param keyStoreType
     * @return
     * @throws Exception
     */
    public static CloseableHttpClient getHttpClient(String cert, String pwd, String keyStoreType)
            throws Exception {
        Validate.notEmpty(cert, "https cert file dir can't be empty");
        Validate.notEmpty(cert, "pwd can't be empty");
        Validate.notEmpty(cert, "keyStoreType  can't be empty");

        CloseableHttpClient httpClient = HttpClients.custom()
                .setConnectionManager(ConnectionManagerHolder.connectionManager)
                // 设置keepAlive 20s
                .setKeepAliveStrategy((httpResponse, httpContext) -> KEEPALIVE_TIME)
                .setRetryHandler(retryHandler())
                .setSSLSocketFactory(ssl(cert, pwd, keyStoreType))
                .build();
        return httpClient;
    }

    public static HttpRequestRetryHandler retryHandler() {
        return (e, count, ctx) -> {
            if (count > RETRY_COUNT)
                return false;
            //请求超时重试
            if (e instanceof ConnectTimeoutException)
                return true;
            HttpClientContext clientContext = HttpClientContext.adapt(ctx);
            HttpRequest request = clientContext.getRequest();
            // 如果请求是幂等的，就再次尝试
            return !(request instanceof HttpEntityEnclosingRequest);
        };
    }

    public static final SSLConnectionSocketFactory ssl(String cert, String pwd, String keyStoreType)
            throws Exception {
        KeyStore keyStore = KeyStore.getInstance(keyStoreType);
        // 加载证书
        keyStore.load(new FileInputStream(new File(cert)), pwd.toCharArray());
        //注册https的connectionSocketFactory
        SSLContext sslcontext = SSLContexts
                .custom()
                .loadKeyMaterial(keyStore, pwd.toCharArray())
                .build();
        HostnameVerifier hostnameVerifier = SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;
        return new SSLConnectionSocketFactory(
                sslcontext, hostnameVerifier);
    }
}
