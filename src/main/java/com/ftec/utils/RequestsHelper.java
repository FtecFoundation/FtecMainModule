package com.ftec.utils;

import com.ftec.logger.Logger;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RequestsHelper {
    @Autowired
    Logger logger;

    /**
     *  Method sends a post request to the indicated url with data in url_encoded form
     * @param url url to send post request
     * @param params parameters that will be added to the request body
     * @param headers additional headers that should be used for request
     * @return server response in string form
     */
    public String postHttp(String url, List<NameValuePair> params, List<NameValuePair> headers)
    {
        try {
            HttpPost post = new HttpPost(url);
            post.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
            if (headers != null) {
                for (NameValuePair header : headers) {
                    post.addHeader(header.getName(), header.getValue());
                }
            }

            HttpClient httpClient = HttpClientBuilder.create().build();
            HttpResponse response = httpClient.execute(post);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                return EntityUtils.toString(entity);

            }
        } catch (Exception e){
            logger.logException("Making url_encoded post request to url "+url, e, true);
        }
        return null;
    }

    /**
     *  Method sends a post request to the indicated url with data in raw string form
     * @param url url to send post request
     * @param params parameters that will be added to the request body
     * @param headers additional headers that should be used for request
     * @return server response in string form
     */
    public String postHttp(String url, String params, List<NameValuePair> headers)
    {
        try {
            HttpPost post = new HttpPost(url);
            post.setEntity(new StringEntity(params, "UTF-8"));
            if (headers != null)
            {
                for (NameValuePair header : headers)
                {
                    post.addHeader(header.getName(), header.getValue());
                }
            }

            HttpClient httpClient = HttpClientBuilder.create().build();
            HttpResponse response = httpClient.execute(post);
            HttpEntity entity = response.getEntity();
            if (entity != null)
            {
                return EntityUtils.toString(entity);

            }
        } catch (Exception e){
            logger.logException("Making raw string request to url "+url, e, true);
        }
        return null;
    }

    /**
     *  Method makes http request to indicated resource with query parameters included in url string
     * @param url url to send get request. Should already contain any query parameters for request
     * @param headers additional headers should be used for request
     * @return raw string response from requested resource
     */
    public String getHttp(String url, List<NameValuePair> headers)
    {
        try {
            HttpRequestBase request = new HttpGet(url);

            if (headers != null) {
                for (NameValuePair header : headers) {
                    request.addHeader(header.getName(), header.getValue());
                }
            }

            HttpClient httpClient = HttpClientBuilder.create().build();
            HttpResponse response = httpClient.execute(request);

            HttpEntity entity = response.getEntity();
            if (entity != null) {
                return EntityUtils.toString(entity);

            }
        } catch (Exception e){
            logger.logException("While sending Get httpRequest: "+url,e, true);
        }
        return null;
    }
}
