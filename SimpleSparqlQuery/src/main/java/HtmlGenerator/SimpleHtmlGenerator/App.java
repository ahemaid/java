package HtmlGenerator.SimpleHtmlGenerator;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

import javax.swing.filechooser.FileNameExtensionFilter;

import java.io.*;

import org.apache.commons.io.filefilter.FileFileFilter;
import org.apache.jena.iri.impl.Main;
import org.apache.jena.ontology.DatatypeProperty;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.ObjectProperty;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.riot.RiotException;
import org.apache.jena.riot.RiotNotFoundException;
import org.apache.jena.util.FileManager;
import org.apache.jena.util.iterator.ExtendedIterator;
import org.apache.log4j.varia.NullAppender;
import org.apache.jena.query.ResultSetFormatter;


public class App {
	static String concept = "", prevProp = "", property = "", value = "", table = "", body = "", individualsHTML = "",
			mainQuery = "", closedTag = "", splitSymbol = "", range = "", isDefinedBy = "", json="";
	static //static int ttlFilesCount = 0;
    String filesPath = "./ttlFiles/";

	public static void main(String[] args) throws IOException {
		
		
	    try {
	        //String one = args[0];
	        //String two = args[1];
	        //System.out.println (one);
	        //System.out.println (two);
	        File dir = new File(filesPath);
	        
	        String[] files = dir.list( FileFileFilter.FILE );
	        for ( int i = 0; i < files.length; i++ ) {
	        	if (files[i].contains("ttl"))
	        	{
	        		System.out.println(files[i]);
	    			GenerateHTML("./ttlFiles/" + files[i],"./out/results.JSON");
	        	}
	      
	        }
    		//System.out.println(ttlFilesCount);

	        
			//GenerateHTML("Battery.ttl","results.JSON");
			//GenerateHTML(one,two);

	    }
	    catch (ArrayIndexOutOfBoundsException e){
	        System.out.println("ArrayIndexOutOfBoundsException caught");
	    }
	    finally {

	    }
	    

	}

	@SuppressWarnings({ "null", "resource" })
	public static void GenerateHTML(String _sourceFile, String _destination) throws IOException {

		org.apache.log4j.BasicConfigurator.configure(new NullAppender());

		FileManager.get().addLocatorClassLoader(Main.class.getClassLoader());

		OntModel ontModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM);
		FileManager.get().readModel(ontModel, _sourceFile);

		mainQuery = "select distinct ?s ?p ?o Where {?s ?p ?o. } order by ASC(?s)";

		QueryExecution qexec = QueryExecutionFactory.create(mainQuery, ontModel);

		////
		//ResultSet result = qexec.execSelect();
		///

		//Get a json output of the sparql query
	
		ResultSet result = qexec.execSelect();

// write to a ByteArrayOutputStream
		ByteArrayOutputStream byteArrayoutputStream = new ByteArrayOutputStream();

		ResultSetFormatter.outputAsJSON(byteArrayoutputStream, result);

// and turn that into a String
		json += new String(byteArrayoutputStream.toByteArray());

		System.out.println(json);


		while (result.hasNext()) {

			QuerySolution soln = result.nextSolution();

			RDFNode node;

			node = soln.get("s");
			property = node.asResource().getLocalName();

		//	GenerateClass(property, node.asResource().getURI(), ontModel);
		}

		PrintWriter outputStream = new PrintWriter(new FileOutputStream(_destination));

		outputStream.println(json);

		outputStream.close();
		System.out.println("File " + _destination + " has been generated successfully.");
	}


}
