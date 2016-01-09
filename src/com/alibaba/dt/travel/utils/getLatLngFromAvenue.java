package com.alibaba.dt.travel.utils;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class getLatLngFromAvenue {
	public Map<String, String> getLatLng(String avenue){
		String url="http://maps.googleapis.com/maps/api/geocode/json?address="+nameToURL(avenue);
		String result="";
		result = callAPI(url);
		Map<String,String> addressMap = new HashMap<String, String>();
		addressMap=parseResult(result);
		return addressMap;
	}
	public String nameToURL(String className){
		String errorFlag="ERROR";
		try {
			String URL=URLEncoder.encode(className, "utf-8");
			return URL;
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return errorFlag;
		}
	}
	public String callAPI(String pageURL) {
        String line = "";
        String result = "";
        try {
            URL url = new URL(pageURL);
            URLConnection connection =  url.openConnection();
            InputStream is = connection.getInputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(is,"UTF-8"));
            while ((line = in.readLine()) != null) {
                result = result+line+'\n';
            }
        } catch (MalformedURLException ex) {
            System.out.println("in callAPI, failed to call API");
            System.out.println(pageURL);
            return "failed";
        } catch (IOException ex) {
            System.out.println("in callAPI, failed to call API");
            System.out.println(pageURL);
            return "failed";
        }
        return result;
    }
	private Map<String, String> parseResult(String result) {
        Map<String, String> loc_google = new HashMap<String, String>();
        if (result.contains("error_message")) {
            loc_google.put("message", "error in requesting data from google map");
            return loc_google;
        }
        if (result.equals("location is empty")) {
            loc_google.put("message", "location is empty");
            return loc_google;
        }
        try {
            JSONObject obj = new JSONObject(result);
            JSONArray results = obj.getJSONArray("results");
            if (results.length() == 0) {
                loc_google.put("message", "google returns no result");
                return loc_google;
            }
            JSONArray address_components = results.getJSONObject(0).getJSONArray("address_components");
            for (int i = 0; i < address_components.length(); i++) {
                JSONObject tmp = address_components.getJSONObject(i);
                String long_name = tmp.getString("long_name");
                JSONArray types = tmp.getJSONArray("types");
                String type = types.getString(0);
                loc_google.put(type, long_name);
            }
            JSONObject geometry = results.getJSONObject(0).getJSONObject("geometry");
            JSONObject location = geometry.getJSONObject("location");
            String lat = location.get("lat").toString();
            String lng = location.get("lng").toString();
            loc_google.put("lat", lat);
            loc_google.put("lng", lng);
        } catch (JSONException ex) {
            ex.printStackTrace();
            System.err.println("in parseResult, error in parasing result");
            System.out.println(result);
            loc_google.put("message", "error in parasing");
        }
        return loc_google;
    }
}

