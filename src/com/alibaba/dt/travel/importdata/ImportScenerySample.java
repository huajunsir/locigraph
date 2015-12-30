package com.alibaba.dt.travel.importdata;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

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
		travelOntology.read("¬√”Œ-20151210.ttl");
		dataOntology = ModelFactory.createOntologyModel();
		touristSpot = travelOntology.getOntClass(Prefix.travel+"touristSport");
	}
	public void run(){
		getOntology();
		
		String dataFileName = "scenery_samples_20151230.txt";
		File file = new File(dataFileName);
		BufferedReader reader = null;
		
		try {
		
			reader = new BufferedReader(new FileReader(file));
			String tempString = null;
			int line = 1;
			while((tempString=reader.readLine())!=null){
				System.out.println("line " + line + ": " + tempString);
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
	}
	
	
	public void run1(){
		getOntology();
		
		String dataFileName = "scenery_samples_20151230.txt";
		File file = new File(dataFileName);
		BufferedReader reader = null;
		
		try {
			reader = new BufferedReader(new FileReader(file));
			String tempString = null;
			//int line = 1;
			while((tempString=reader.readLine())!=null){
				//System.out.println("line " + line + ": " + tempString);
				//line++;
				String[] splittedString = tempString.split(",");
				Individual tmpTouristSpot = dataOntology.createIndividual(Prefix.travel+splittedString[0], touristSpot);
				dataOntology.add(tmpTouristSpot,travelOntology.getProperty(Prefix.travel+"hasNameValue"),dataOntology.createTypedLiteral(splittedString[0]));
				String [] tempCoordinate = splittedString[1].split(":"); 
				dataOntology.add(tmpTouristSpot,travelOntology.getProperty(Prefix.travel+"hasLatValue"),dataOntology.createTypedLiteral(Float.parseFloat(tempCoordinate[0])));
				dataOntology.add(tmpTouristSpot,travelOntology.getProperty(Prefix.travel+"hasLonValue"),dataOntology.createTypedLiteral(Float.parseFloat(tempCoordinate[1])));

				//,lat_lng,address,open_time,used_time,rate_count,rate_score,ticket,award,min_used_time
				//hasLonValue hasAddressValue hasOpeningTimeInterval hasTravelingHoursValue  Comment
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
		dataOntology.write(new FileOutputStream("Scenery_out.txt"),"TURTLE");
	} catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	}
}
