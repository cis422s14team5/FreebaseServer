package com.thedragons.database.server;

import com.google.api.client.http.*;
import com.google.api.client.http.javanet.NetHttpTransport;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class Freebase {

    private static String API_KEY = "AIzaSyAn395NcvsIoCIkiIH_vMwEn0dXsXKIVsw";

    public JSONObject search(String title, String type) {
        JSONObject topic = new JSONObject();
        String filter = "";
        int limit = 100;
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
            url.put("limit", limit);
            url.put("filter", filter);
            url.put("key", API_KEY);

            HttpRequest request = requestFactory.buildGetRequest(url);
            HttpResponse httpResponse = request.execute();

            JSONObject response = (JSONObject) parser.parse(httpResponse.parseAsString());
            JSONArray results = (JSONArray) response.get("result");
            JSONObject resultMap;

            try {
                resultMap = (JSONObject) results.get(0);
                topic = getTopic(resultMap.get("id").toString());

            } catch (Exception e) {
                System.out.println(">>> " + e);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return topic;
    }

    public JSONObject getTopic(String topicId) {
        JSONObject topic = new JSONObject();
        try {
            HttpTransport httpTransport = new NetHttpTransport();
            HttpRequestFactory requestFactory = httpTransport.createRequestFactory();
            JSONParser parser = new JSONParser();

            GenericUrl url = new GenericUrl("https://www.googleapis.com/freebase/v1/topic" + topicId);
            url.put("limit", 100);
            url.put("key", API_KEY);

            System.out.println(">>> Getting topic: " + topicId);

            HttpRequest request = requestFactory.buildGetRequest(url);
            HttpResponse httpResponse = request.execute();

            topic = (JSONObject) parser.parse(httpResponse.parseAsString());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return topic;
    }
}
