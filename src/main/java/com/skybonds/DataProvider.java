package com.skybonds;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * @author Ildar Gafarov (ildar@skybonds.com)
 */
public class DataProvider {

    private final String HOST = "http://sit.skybonds.net";

    private final String FILTERS_VALUES_URL = HOST + "/api/bonds/filters";

    private String FILTER_RESULTS_URL(String name, String value) {
        try {
            return HOST + "/api/bonds/filters/" + URLEncoder.encode(name, "UTF-8") + "/" + URLEncoder.encode(value, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Set<String> getFilterResults(String name, String value) {
        Set<String> result = new HashSet<>();
        JSONParser parser = new JSONParser();
        try {
            String response = get(FILTER_RESULTS_URL(name, value));
            JSONArray filters = (JSONArray)parser.parse(response);
            for (Object isinRaw : filters) {
                result.add((String) isinRaw);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }

    public Map<String, Set<String>> getFiltersValues() {
        Map<String, Set<String>> result = new HashMap<>();
        JSONParser parser = new JSONParser();
        try {
            String response = get(FILTERS_VALUES_URL);
            if(response != null) {
                JSONArray filters = (JSONArray)parser.parse(response);
                for (Object filterRaw : filters) {

                    JSONObject filter = (JSONObject) filterRaw;

                    Set<String> values = new HashSet<>();
                    JSONArray valuesObj = (JSONArray) filter.get("values");
                    for (Object valueRaw : valuesObj) {
                        values.add((String) valueRaw);
                    }

                    result.put((String) filter.get("name"), values);
                }
            } else {
                System.out.println("Empty response "+ FILTERS_VALUES_URL);
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }

    private String get(String url) {
        CloseableHttpResponse response = null;
        try {
            CloseableHttpClient httpclient = HttpClients.createDefault();
            HttpGet request = new HttpGet(url);
            response = httpclient.execute(request);
            HttpEntity entity = response.getEntity();
            return toString(entity.getContent());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

//    private String post(String url, String body) {
//        CloseableHttpResponse response2 = null;
//        try {
//            CloseableHttpClient httpclient = HttpClients.createDefault();
//            HttpPost httpPost = new HttpPost("http://targethost/login");
//            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
//            nvps.add(new BasicNameValuePair("username", "vip"));
//            nvps.add(new BasicNameValuePair("password", "secret"));
//            httpPost.setEntity(new UrlEncodedFormEntity(nvps));
//            response2 = httpclient.execute(httpPost);
//            HttpEntity entity2 = response2.getEntity();
//
//            EntityUtils.consume(entity2);
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            if (response2 != null) {
//                try {
//                    response2.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//
//        }
//    }

    private static String toString(InputStream stream) {
        try {
            return IOUtils.toString(stream);
        } catch (IOException e) {
            return null;
        }
    }

}
