package com.alibaba.dt.travel.preprocess;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import com.alibaba.dt.travel.utils.Location2City;

public class PreScenerySample {
	public static void preprocess(){
		String inputFileName = "input/scenery_samples_20151230.txt";
		String outputFileName = "input/scenery_samples.txt";
		try {
			File inputfile = new File(inputFileName);
			InputStreamReader isr = new InputStreamReader(new FileInputStream(inputfile),"UTF-8");
			BufferedReader reader = new BufferedReader(isr);
			
			OutputStream outputStream = new FileOutputStream(outputFileName);
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
			
			String line = " ";
			int lineCount = 1;
			while((line=reader.readLine())!=null){
				String[] splittedLine = line.split(",");
				String [] Coordinate = splittedLine[1].split(":"); 
				float lat = Float.parseFloat(Coordinate[0]);
				float lon = Float.parseFloat(Coordinate[1]);
				String city = Location2City.LatLon2City(lat, lon);
				if(city==null||"".equals(city)){
					System.out.println(lineCount+":"+line);
					city = "δ֪";
				}
				writer.write(line.trim()+","+city+"\n");
				lineCount++;
			}
			reader.close();
			writer.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
