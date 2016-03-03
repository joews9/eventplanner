package com.event.joe.servicepackage;

import android.content.Context;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.net.URL;
import java.net.URLEncoder;


/**
 * Created by Joe Aimee 10/02/2016.
 */
public class EventSearchService extends AbstractService {
    public EventSearchService(String town, String distance, String eventCategory, String eventTimescale ) {
        this.town = URLEncoder.encode(town);
        this.distance = URLEncoder.encode(distance);
        this.eventCategory = URLEncoder.encode(eventCategory.toLowerCase());
        this.eventTimescale = URLEncoder.encode(eventTimescale);

    }

    private String town;
    private String distance;
    private JSONArray results;
    private String eventTimescale;
    private String eventCategory;

    public JSONArray getResults() {
        return results;

    }

    @Override
    public void run() {
        //local variables
        String url;
        System.out.println("URL *****************" + eventCategory);
        System.out.println("URL *****************" + eventTimescale);
        if(eventCategory == "All"){
            url = "http://api.eventful.com/json/events/search?app_key=9dqZ8fjrr8Zh24Zk&location="+town+",%20United%20Kingdom&date="+ eventTimescale+"&within="+distance+"&page_size=30";
        }else{
            url = "http://api.eventful.com/json/events/search?app_key=9dqZ8fjrr8Zh24Zk&category=" + eventCategory + "&location="+town+",%20United%20Kingdom&date="+ eventTimescale+"&within="+distance+"&page_size=30";
        }
        System.out.println(url);
        boolean error = false;
        HttpClient httpClient = new DefaultHttpClient();
        try {
            HttpResponse data = httpClient.execute(new HttpGet(url));
            HttpEntity entity = data.getEntity();
            String result = EntityUtils.toString(entity, "UTF8");
            JSONObject json = new JSONObject(result);
            if(json == null){
                System.out.println("NULL********");
            }else{
                System.out.println(json.toString());
            }

            if (json.has("Response")
                    && json.getString("Response").equals("False")) {
                error = true;
                System.out.println(error);
            } else {
                results = json.getJSONObject("events").getJSONArray("event");
            }

        } catch (Exception e) {
            System.out.println("************ERROR");
            results = null;
            error = true;
        } finally {
            httpClient.getConnectionManager().shutdown();
        }

        super.serviceCallComplete(error);

    }
}
