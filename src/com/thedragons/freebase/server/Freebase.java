package com.thedragons.freebase.server;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

// TODO not getting the compete episode list

public class Freebase {

    private static String API_KEY = "AIzaSyAn395NcvsIoCIkiIH_vMwEn0dXsXKIVsw";

    private JSONObject topic;

    public JSONObject getTopic() {
        return topic;
    }

    public void search(String title, String type) {
        String filter = "";
        if (type.equals("film")) {
            filter = "(all type:/film/film)";
        } else if (type.equals("tv")) {
            filter = "(all type:/tv/tv_program)";
        }

        try {
            HttpTransport httpTransport = new NetHttpTransport();
            HttpRequestFactory requestFactory = httpTransport.createRequestFactory();
            JSONParser parser = new JSONParser();

            GenericUrl url = new GenericUrl("https://www.googleapis.com/freebase/v1/search");
            url.put("query", title);
            url.put("filter", filter);
            url.put("key", API_KEY);

            HttpRequest request = requestFactory.buildGetRequest(url);
            HttpResponse httpResponse = request.execute();

            JSONObject response = (JSONObject) parser.parse(httpResponse.parseAsString());
            JSONArray results = (JSONArray) response.get("result");
            JSONObject resultMap;
            try {
                resultMap = (JSONObject) results.get(0);
                setTopic(resultMap.get("id").toString());
            } catch (Exception e) {
                topic = new JSONObject();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void setTopic(String topicId) {
        try {
            HttpTransport httpTransport = new NetHttpTransport();
            HttpRequestFactory requestFactory = httpTransport.createRequestFactory();
            JSONParser parser = new JSONParser();

            GenericUrl url = new GenericUrl("https://www.googleapis.com/freebase/v1/topic" + topicId);
            url.put("key", API_KEY);

            HttpRequest request = requestFactory.buildGetRequest(url);
            HttpResponse httpResponse = request.execute();

            topic = (JSONObject) parser.parse(httpResponse.parseAsString());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
