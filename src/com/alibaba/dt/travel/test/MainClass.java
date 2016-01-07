package com.alibaba.dt.travel.test;

import com.alibaba.dt.travel.importdata.ImportAdministrativeRegion;
import com.alibaba.dt.travel.importdata.ImportScenerySample;
import com.alibaba.dt.travel.preprocess.PreScenerySample;
import com.alibaba.dt.travel.utils.CustomizedHttpRequest;
import com.alibaba.dt.travel.utils.Location2City;

public class MainClass {
	public static void main(String[] args){
		//new PreScenerySample();
		new ImportAdministrativeRegion().run();
		//new TestQuery().run();
		//new ImportScenerySample().run();
		//String result = Location2City.LatLon2City(30.186701, 120.117853);
		//System.out.println(result);
		//PreScenerySample.preprocess();
		System.out.println("end");
	}
}
