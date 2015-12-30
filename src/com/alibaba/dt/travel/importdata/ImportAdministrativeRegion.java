package com.alibaba.dt.travel.importdata;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.ReadWrite;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.tdb.TDB;
import org.apache.jena.tdb.TDBFactory;
import org.w3c.dom.*;


public class ImportAdministrativeRegion {
	final String prefix = "http://alibaba.com/dt/ontology/travel#";
	final String regionPrefix = "http://alibaba.com/dt/ontology/travel#";
	
	OntModel travelOntology;
	OntModel dataOntology;
	OntClass provClass;
	OntClass cityClass;
	OntClass townClass;
	OntClass countyClass;
	OntClass villageClass;
	OntClass countryClass;
	Property hasRegionCode;
	Property hasRegionLevel;
	Property hasRegionName;
	Property hasParentRegion;
	Dataset dataset;
	int count=0;
	
	public void run1(){
		getOntology();
		parseXML();
		try {
			//dataOntology.write(new BufferedWriter(new OutputStreamWriter(new FileOutputStream("output.ttl"), true)));
			dataOntology.write(new FileOutputStream("output.ttl"),"TURTLE");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void run(){
		getOntology1();
		parseXML();
	}
	private void getOntology1(){
		String directory = "./tdb";
		dataset = TDBFactory.createDataset(directory);
		Model tdb = dataset.getDefaultModel();
		
		dataOntology = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM, tdb);
		//dataOntology = ModelFactory.createOntologyModel();
		travelOntology = ModelFactory.createOntologyModel();
		travelOntology.read("旅游-20151210.ttl");
		provClass = travelOntology.getOntClass(prefix+"Province");
		cityClass = travelOntology.getOntClass(prefix+"City");
		townClass = travelOntology.getOntClass(prefix+"Town");
		countyClass = travelOntology.getOntClass(prefix+"County");
		villageClass = travelOntology.getOntClass(prefix+"Village");
		countryClass = travelOntology.getOntClass(prefix+"Country");
		hasRegionCode = travelOntology.getProperty(prefix+"hasRegionCodeValue");
		hasRegionLevel = travelOntology.getProperty(prefix+"hasRegionLevelValue");
		hasRegionName = travelOntology.getProperty(prefix+"hasRegionNameValue");
		hasParentRegion = travelOntology.getProperty(prefix+"hasParentRegion");
	}
	//load ontmodel
	private void getOntology(){
		dataOntology = ModelFactory.createOntologyModel();
		travelOntology = ModelFactory.createOntologyModel();
		travelOntology.read("旅游-20151210.ttl");
		provClass = travelOntology.getOntClass(prefix+"Province");
		cityClass = travelOntology.getOntClass(prefix+"City");
		townClass = travelOntology.getOntClass(prefix+"Town");
		countyClass = travelOntology.getOntClass(prefix+"County");
		villageClass = travelOntology.getOntClass(prefix+"Village");
		countryClass = travelOntology.getOntClass(prefix+"Country");
		hasRegionCode = travelOntology.getProperty(prefix+"hasRegionCodeValue");
		hasRegionLevel = travelOntology.getProperty(prefix+"hasRegionLevelValue");
		hasRegionName = travelOntology.getProperty(prefix+"hasRegionNameValue");
		hasParentRegion = travelOntology.getProperty(prefix+"hasParentRegion");
	}
	
	public void parseXML(){
		//create county individual
		Individual china = dataOntology.createIndividual(prefix+"china", countryClass);
		china.addLiteral(hasRegionCode,"0");
		china.addLiteral(hasRegionLevel, 0);
		china.addLiteral(hasRegionName,"中国");
		
		//load xml file
		File file = new File("All.xml");
		DocumentBuilder db = null;
		DocumentBuilderFactory dbf = null;
		try{
			dbf = DocumentBuilderFactory.newInstance();
			db = dbf.newDocumentBuilder();
			Document doc = db.parse(file);
			
			Element root = doc.getDocumentElement();
			NodeList provList = root.getChildNodes();
			for(int i0=0;i0<provList.getLength();i0++){
				Node tmpProv = provList.item(i0);
				//province node
				if("area".equals(tmpProv.getNodeName())){
					parseNode( china, tmpProv);
					TDB.sync(dataset);
					System.out.println(count);
					count=0;
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		System.out.println("end");
	}
	private void parseNode(Individual parent,Node root){
		

		String name="";
		String code="";
		int level=-1;
		NodeList childNodes=null;
		Individual ind = null;
		
		NodeList attrList = root.getChildNodes();
		for(int i=0;i<attrList.getLength();i++){
			Node tmpAttr = attrList.item(i);
			if("name".equals(tmpAttr.getNodeName())){
				name = tmpAttr.getFirstChild().getNodeValue();
				continue;
			}
			if("code".equals(tmpAttr.getNodeName())){
				code = tmpAttr.getFirstChild().getNodeValue();
				continue;
			}
			if("level".equals(tmpAttr.getNodeName())){
				level = Integer.parseInt(tmpAttr.getFirstChild().getNodeValue());
				continue;
			}
			if("areas".equals(tmpAttr.getNodeName())){
				childNodes = tmpAttr.getChildNodes();
				continue;
			}
		}
		//if(level==1&&!("浙江".equals(name)))
		//	return;
		switch(level){
		case 1:
			ind = dataOntology.createIndividual(prefix+"region_"+code, provClass);
			break;
		case 2:
			ind = dataOntology.createIndividual(prefix+"region_"+code, cityClass);
			break;
		case 3:
			ind = dataOntology.createIndividual(prefix+"region_"+code, townClass);
			break;
		case 4:
			ind = dataOntology.createIndividual(prefix+"region_"+code, countyClass);
			break;
		case 5:
			ind = dataOntology.createIndividual(prefix+"region_"+code, villageClass);
			break;
		default:
			System.out.println("unknown region level: "+level);
		}
		count++;
		//System.out.println(count+": "+name);
		ind.addLiteral(hasRegionName, name);
		ind.addLiteral(hasRegionLevel, level);
		ind.addLiteral(hasRegionCode, code);
		ind.addProperty(hasParentRegion, parent);
		
		if(childNodes!=null){
			for(int i=0;i<childNodes.getLength();i++){
				Node tmpNode = childNodes.item(i);
				if("area".equals(tmpNode.getNodeName()))
					parseNode(ind, tmpNode);
			}
		}
		
	}

}
