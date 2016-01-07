package com.alibaba.dt.travel.utils;

import org.json.JSONException;
import org.json.JSONObject;

public class Location2City {
	private static String ak = "E4805d16520de693a3fe707cdc962045"; //网上找的access_key，不一定可靠！
	private static String urlPrefix = "http://api.map.baidu.com/geocoder";
	
	public static String LatLon2City(double lat, double lon){
		String param = "ak="+ak+"&callback=renderReverse&location="+lat+","+lon+"&output=json";
		String result = CustomizedHttpRequest.sendGet(urlPrefix, param);
		String city = null;
		try {
			JSONObject resultJson = new JSONObject(result);
			String tmp = resultJson.getString("status");
			city = resultJson.getJSONObject("result").getJSONObject("addressComponent").getString("district");
			//错误处理还没写
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return city;
	}
	
	public String Address2City(String address){
		
		return "";
	}
}
