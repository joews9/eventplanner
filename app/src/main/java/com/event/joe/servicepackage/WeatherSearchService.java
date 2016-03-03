package com.event.joe.servicepackage;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLEncoder;

/**
 * Created by Joe Aimee and Baby on 10/02/2016.
 */
public class WeatherSearchService extends AbstractService {
    public WeatherSearchService(String town) {
        this.town = URLEncoder.encode(town);

    }

    private String town;
    private JSONArray results;

    public JSONArray getResults() {
        return results;
    }

    @Override
    public void run() {
        String url = "http://api.openweathermap.org/data/2.5/forecast?q="
                + town + ",us&mode=json&appid=55b25e38be034b600f9db98aab728351";
        boolean error = false;
        ;
        HttpClient httpClient = null;
        try {
            httpClient = new DefaultHttpClient();
            HttpResponse data = httpClient.execute(new HttpGet(url));
            HttpEntity entity = data.getEntity();
            String result = EntityUtils.toString(entity, "UTF8");
            JSONObject json = new JSONObject(result);

            if (json.has("Response")
                    && json.getString("Response").equals("False")) {
                error = true;
            } else {
                results = json.getJSONArray("list");
            }

        } catch (Exception e) {
            results = null;
            error = true;
        } finally {
            httpClient.getConnectionManager().shutdown();
        }

        super.serviceCallComplete(error);

    }
}
