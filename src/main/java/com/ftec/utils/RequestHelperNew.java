package com.ftec.utils;

import javax.net.ssl.SSLParameters;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.ExecutionException;

public class RequestHelperNew {

    private static HttpClient httpClient;

    public static HttpClient getHttpClient() {
        if(httpClient == null) {
            SSLParameters parameters = new SSLParameters();
            parameters.setProtocols(new String[]{"TLSv1.2"});
            httpClient = HttpClient.newBuilder()
                    .connectTimeout(Duration.ofMinutes(1))
                    .sslParameters(parameters)
                    .build();
        }
        return httpClient;
    }

    public static String simpleGet(String url){
        try{
            HttpRequest request = HttpRequest.newBuilder().version(HttpClient.Version.HTTP_1_1).timeout(Duration.ofMinutes(1)).GET().uri(new URI(url)).build();
            HttpResponse<String> send = getHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

            return send.body();
        }catch (URISyntaxException ignored){
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String proxyRequest(String url, int tryingCount){

        while (tryingCount != 0) {
            //  JSONObject jo = new JSONObject(simpleGet("https://api.getproxylist.com/proxy"));

        /*    Proxy proxy = new Proxy(jo.getString("protocol").equals("http") ? Proxy.Type.HTTP : Proxy.Type.SOCKS,
                    new InetSocketAddress(jo.getString("ip"), jo.getInt("port")));*/
            Proxy proxy = new Proxy(Proxy.Type.HTTP ,
                    new InetSocketAddress("110.137.26.249", 8080));
            try {

                HttpURLConnection.setFollowRedirects(true); // defaults to true

                URL request_url = new URL(url);
                HttpURLConnection http_conn = (HttpURLConnection) request_url.openConnection(proxy);
                http_conn.setConnectTimeout(10000);
                http_conn.setReadTimeout(10000);
                http_conn.setInstanceFollowRedirects(true);
                if (String.valueOf(http_conn.getResponseCode()).startsWith("2")) {
                    return readFullyAsString(http_conn.getInputStream(), "UTF-8");
                }else tryingCount--;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "";

    }

    public static String readFullyAsString(InputStream inputStream, String encoding) throws IOException {
        return readFully(inputStream).toString(encoding);
    }

    private static ByteArrayOutputStream readFully(InputStream inputStream) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length = 0;
        while ((length = inputStream.read(buffer)) != -1) {
            baos.write(buffer, 0, length);
        }
        return baos;
    }


    public static String simplePost(String url, String params, String... headers){
        try{
            HttpRequest.Builder request = HttpRequest.newBuilder()
                    .version(HttpClient.Version.HTTP_1_1)
                    .timeout(Duration.ofMinutes(1))
                    .POST(HttpRequest.BodyPublishers.ofString(params))
                    .uri(new URI(url));
            if(headers!=null && headers.length!=0) request.headers(headers);
            HttpResponse<String> stringHttpResponse = getHttpClient().sendAsync(request.build(), HttpResponse.BodyHandlers.ofString()).get();
            String code = String.valueOf(stringHttpResponse.statusCode());
            return stringHttpResponse.body();
        }catch (URISyntaxException ignored){
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String PUT_Request(String url, String params, String... headers){
        try{
            HttpRequest.Builder request = HttpRequest.newBuilder()
                    .version(HttpClient.Version.HTTP_1_1)
                    .timeout(Duration.ofMinutes(1))
                    .PUT(HttpRequest.BodyPublishers.ofString(params))
                    .uri(new URI(url));
            if(headers!=null && headers.length!=0) request.headers(headers);
            HttpResponse<String> stringHttpResponse = getHttpClient().sendAsync(request.build(), HttpResponse.BodyHandlers.ofString()).get();

            return stringHttpResponse.body();
        }catch (URISyntaxException ignored){
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String simpleDelete(String url){
        try{
            HttpRequest request = HttpRequest.newBuilder().version(HttpClient.Version.HTTP_1_1).timeout(Duration.ofMinutes(1)).DELETE().uri(new URI(url)).build();
            return getHttpClient().send(request, HttpResponse.BodyHandlers.ofString()).body();
        }catch (URISyntaxException ignored){
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String GET_Request(String url, String... headers){
        try{
            HttpRequest.Builder request = HttpRequest.newBuilder().version(HttpClient.Version.HTTP_1_1).timeout(Duration.ofMinutes(1)).GET().uri(new URI(url));
            if(headers!=null && headers.length!=0) request.headers(headers);
            HttpResponse<String> send = getHttpClient().send(request.build(), HttpResponse.BodyHandlers.ofString());
            System.out.println(send.statusCode());
            System.out.println(send.headers().firstValue("Location"));
            return send.body();
        }catch (URISyntaxException ignored){
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
        return "";
    }


    public static String DELETE_Request(String url, String... headers){
        try{
            HttpRequest.Builder request = HttpRequest.newBuilder().version(HttpClient.Version.HTTP_1_1).timeout(Duration.ofMinutes(1)).DELETE().uri(new URI(url));
            if(headers!=null && headers.length!=0) request.headers(headers);
            HttpResponse<String> send = getHttpClient().send(request.build(), HttpResponse.BodyHandlers.ofString());
            System.out.println(send.statusCode());
            return send.body();
        }catch (URISyntaxException ignored){
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
        return "";
    }

}
