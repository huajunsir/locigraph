package com.alibaba.dt.travel.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class parseNumber {
	public int getHotelScore(String source){
		Pattern p = Pattern.compile("[^0-9.]");
		Matcher m = p.matcher(source);
		String result = m.replaceAll("");
		return Integer.parseInt(result);
	}
	public float getAreaScore(String source){
		Pattern p = Pattern.compile("[^0-9.]");
		Matcher m = p.matcher(source);
		String result = m.replaceAll("");
		return Float.parseFloat(result);
	}

}
