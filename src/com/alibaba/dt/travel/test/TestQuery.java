package com.alibaba.dt.travel.test;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.ReadWrite;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.tdb.TDBFactory;

public class TestQuery {
	public void run(){
		String directory = "./tdb";
		Dataset tdb = TDBFactory.createDataset(directory);
		
		tdb.begin(ReadWrite.READ);
		String qs1 = "SELECT (count(*) AS ?count) where {?s ?p ?o}" ;
		try(QueryExecution qExec = QueryExecutionFactory.create(qs1, tdb)) {
		     ResultSet rs = qExec.execSelect() ;
		     ResultSetFormatter.out(rs) ;
		 }
		System.out.println("end");
	}
}
