package com.alibaba.dt.travel.importdata;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;




import java.util.HashMap;
import java.util.Map;

import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.ModelFactory;

import com.alibaba.dt.travel.constant.Prefix;
import com.alibaba.dt.travel.utils.Location2City;
import com.alibaba.dt.travel.utils.getLatLngFromAvenue;
import com.alibaba.dt.travel.utils.parseNumber;

public class ImportHotelSample {
	OntModel travelOntology;
	OntModel dataOntology;
	OntClass Hotel;
	private int getOntology(){
		travelOntology = ModelFactory.createOntologyModel();
		travelOntology.read("./ontology/旅游-20151210.ttl");
		dataOntology = ModelFactory.createOntologyModel();
		String className=nameToURL("酒店");
		if(className!="ERROR"){
			Hotel = travelOntology.getOntClass(Prefix.travel+className);
			return 1;
		}else{
			return -1;
		}	
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
	public void loadData(){
		getOntology();
		String filename="./input/travel_hotel_sample.txt";
		BufferedReader reader = null;
		try {
			File file = new File(filename);
			InputStreamReader isr = new InputStreamReader(new FileInputStream(file),"UTF-8");
			reader = new BufferedReader(isr);
			String tempString = null;
			int line = 1;
			while((tempString=reader.readLine())!=null){
				String[] splittedString = tempString.split(",");				
				Individual tmpHotel = dataOntology.createIndividual(Prefix.travel+nameToURL(splittedString[4]),Hotel);
//				Location common Properties
				dataOntology.add(tmpHotel,travelOntology.getProperty(Prefix.travel+"hasNameValue"),dataOntology.createTypedLiteral(splittedString[4]));
				dataOntology.add(tmpHotel,travelOntology.getProperty(Prefix.travel+"hasAddressValue"),dataOntology.createTypedLiteral(splittedString[3]));
				Map<String,String> addressMap = new HashMap<String, String>();
				addressMap=new getLatLngFromAvenue().getLatLng(splittedString[3]);
				String lat=addressMap.get("lat");
				String lng=addressMap.get("lng");
				if(lat!=null){
					dataOntology.add(tmpHotel,travelOntology.getProperty(Prefix.travel+"hasLatValue"),dataOntology.createTypedLiteral(lat));
					dataOntology.add(tmpHotel,travelOntology.getProperty(Prefix.travel+"hasLonValue"),dataOntology.createTypedLiteral(lng));
					String zone = new Location2City().LatLon2City(Double.parseDouble(addressMap.get("lat")), Double.parseDouble(addressMap.get("lng")));
					dataOntology.add(tmpHotel,travelOntology.getProperty(Prefix.travel+"hasParentLocation"),dataOntology.createTypedLiteral(zone));
				}
				
//				Hotel Properties
				dataOntology.add(tmpHotel,travelOntology.getProperty(Prefix.travel+"hasIntroductionValue"),dataOntology.createTypedLiteral(splittedString[10]));
				dataOntology.add(tmpHotel,travelOntology.getProperty(Prefix.travel+"hasTelNumberValue"),dataOntology.createTypedLiteral(splittedString[2]));
				Individual tmpComment = dataOntology.createIndividual(travelOntology.getOntClass(Prefix.travel+"Comment"));
				int score=new parseNumber().getHotelScore(splittedString[6]);
				dataOntology.add(tmpComment,travelOntology.getProperty(Prefix.travel+"hasScoreValue"),dataOntology.createTypedLiteral(score));
				dataOntology.add(tmpComment,travelOntology.getProperty(Prefix.travel+"hasCommentCountValue"),dataOntology.createTypedLiteral(Integer.parseInt(splittedString[1])));
				dataOntology.add(tmpHotel,travelOntology.getProperty(Prefix.travel+"hasHotelComment"),tmpComment);
				dataOntology.add(tmpHotel,travelOntology.getProperty(Prefix.travel+"hasHoteltypeValue"),dataOntology.createTypedLiteral(splittedString[5]));
				boolean hasAirConditioner=splittedString[0].contains("空调");
				dataOntology.add(tmpHotel,travelOntology.getProperty(Prefix.travel+"hasAirConditionerOrNot"),dataOntology.createTypedLiteral(hasAirConditioner));
				boolean hasWifi=splittedString[0].contains("Wifi");
				dataOntology.add(tmpHotel,travelOntology.getProperty(Prefix.travel+"hasWifiOrNot"),dataOntology.createTypedLiteral(hasWifi));
				boolean hasPark=splittedString[0].contains("停车场");
				dataOntology.add(tmpHotel,travelOntology.getProperty(Prefix.travel+"hasParkOrNot"),dataOntology.createTypedLiteral(hasPark));
				
				
				line++;
			}
			reader.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			dataOntology.write(new FileOutputStream("./output/hotelOutput.ttl"),"TURTLE");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
