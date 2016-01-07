package com.alibaba.dt.travel.importdata;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLEncoder;

import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.ModelFactory;

import com.alibaba.dt.travel.constant.Prefix;

public class ImportScenerySample {
	OntModel travelOntology;
	OntModel dataOntology;
	OntClass touristSpot;
	
	private void getOntology(){
		travelOntology = ModelFactory.createOntologyModel();
		travelOntology.read("Ontology/¬√”Œ-20151210.ttl");
		dataOntology = ModelFactory.createOntologyModel();
		touristSpot = travelOntology.getOntClass(Prefix.travel+"touristSport");
	}

	public void run(){
		getOntology();
		
		String dataFileName = "input/scenery_samples.txt";
		try {
			File file = new File(dataFileName);
			InputStreamReader isr = new InputStreamReader(new FileInputStream(file),"UTF-8");
			BufferedReader reader = new BufferedReader(isr);
			String line = null;
			int lineCount = 1;
			while((line=reader.readLine())!=null){
				String[] splittedLine = line.trim().split(",");
				String encodedName = URLEncoder.encode(splittedLine[0],"UTF-8");
				Individual tmpTouristSpot = dataOntology.createIndividual(Prefix.travel+encodedName, touristSpot);
				
				//POI common properties
				dataOntology.add(tmpTouristSpot,travelOntology.getProperty(Prefix.travel,"hasNameValue"),dataOntology.createTypedLiteral(splittedLine[0]));
				String [] Coordinate = splittedLine[1].split(":"); 
				dataOntology.add(tmpTouristSpot,travelOntology.getProperty(Prefix.travel,"hasLatValue"),dataOntology.createTypedLiteral(Float.parseFloat(Coordinate[0])));
				dataOntology.add(tmpTouristSpot,travelOntology.getProperty(Prefix.travel,"hasLonValue"),dataOntology.createTypedLiteral(Float.parseFloat(Coordinate[1])));
				dataOntology.add(tmpTouristSpot,travelOntology.getProperty(Prefix.travel,"hasAddressValues"),dataOntology.createTypedLiteral(splittedLine[2]));
				dataOntology.add(tmpTouristSpot,travelOntology.getProperty(Prefix.travel,"hasParentLocation"),dataOntology.createTypedLiteral(splittedLine[10]));
				lineCount++;
				
				//TouristSpot Properties
				float travelingHour = (Float.parseFloat(splittedLine[4])+Float.parseFloat(splittedLine[9]))/2;
				dataOntology.add(tmpTouristSpot,travelOntology.getProperty(Prefix.travel,"hasTravelingHoursValue"),dataOntology.createTypedLiteral(travelingHour));
				Individual tmpComment = dataOntology.createIndividual(travelOntology.getOntClass(Prefix.travel+"Comment"));
				dataOntology.add(tmpComment,travelOntology.getProperty(Prefix.travel,"hasScoreValue"),dataOntology.createTypedLiteral(splittedLine[6]));
				dataOntology.add(tmpComment,travelOntology.getProperty(Prefix.travel,"hasCommentCount"),dataOntology.createTypedLiteral(splittedLine[5]));
				dataOntology.add(tmpTouristSpot,travelOntology.getProperty(Prefix.travel,"hasComment"),tmpComment);
				dataOntology.add(tmpTouristSpot,travelOntology.getProperty(Prefix.travel,"hasPriceValue"),dataOntology.createTypedLiteral(Float.parseFloat(splittedLine[7])));
				//open_time[3]
				Individual tmpHourTimeInterval = dataOntology.createIndividual(travelOntology.getOntClass(Prefix.travel+"HourTimeInterval"));
				String[] timeInterval = splittedLine[3].split("-");
				String[] tmp0 = timeInterval[0].split(":");
				float startHour = (float) (Integer.parseInt(tmp0[0])+Integer.parseInt(tmp0[1])/60.0);
				String[] tmp1 = timeInterval[1].split(":");
				float endHour = (float) (Integer.parseInt(tmp1[0])+Integer.parseInt(tmp1[1])/60.0);
				dataOntology.add(tmpHourTimeInterval,travelOntology.getProperty(Prefix.travel,"startHourValue"),dataOntology.createTypedLiteral(startHour));
				dataOntology.add(tmpHourTimeInterval,travelOntology.getProperty(Prefix.travel,"endHourValue"),dataOntology.createTypedLiteral(endHour));
				dataOntology.add(tmpTouristSpot,travelOntology.getProperty(Prefix.travel,"hasOpeningTimeInterval"),tmpHourTimeInterval);
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
			dataOntology.write(new FileOutputStream("output/Scenery_out.txt"),"TURTLE");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
