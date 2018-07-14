package com.jakub.tfutil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.log4j.Logger;
import com.jakub.tfutil.diagram.GraphvizDiagram;

public class TerraformStateViz{

	static final Logger logger = Logger.getLogger(TerraformStateViz.class);
	static String sourceFilePathName = null;
	static String outputFileName = null;

	public static void main(String[] args){
		logger.info("Start");
		
	    boolean invocationError = parseArguments(args);
  	       
	    if (invocationError){
	    	logger.error("Exiting");
	    	return;        	
	    }

	    TfObjectWarehouseBuilder TfObjectWarehouseBuilder = new TfObjectWarehouseBuilder();
        TfObjectsWarehouse tfObjectsWarehouse = TfObjectWarehouseBuilder.buildTfObjectWarehouse(sourceFilePathName);

        if (tfObjectsWarehouse==null){
        	System.out.println("Exiting");
        	invocationError = true;
        }
                
		GraphvizDiagram graphvizDiagram = new GraphvizDiagram(tfObjectsWarehouse);
		String diagram = graphvizDiagram.draw();
		
		logger.info("Writing graphviz diagram to file: " + outputFileName);
		
		try (PrintWriter out = new PrintWriter(outputFileName)) {
			out.println(diagram);
		} catch (FileNotFoundException e) {
			e.getMessage();
		}		
		logger.info("Success");

	}

	private static boolean parseArguments(String[] args) {
		Option option_i = Option.builder("i").required(true).argName("input").hasArg().desc("Input file name: tfstate file").build();
	    Option option_o = Option.builder("o").argName("output").hasArg().desc("Output file name").build();
	    Options options = new Options();
	    CommandLineParser parser = new DefaultParser();
	    options.addOption(option_i);
	    options.addOption(option_o);
	    
	    String header = "Produces graphviz diagram from tfstate file.";
	    String footer = "";
	    HelpFormatter formatter = new HelpFormatter();
	    
	    boolean invocationError = false;
	    CommandLine commandLine;
	    try
	    {
	        commandLine = parser.parse(options, args);

	        //requred options are checked by parser
	        sourceFilePathName = commandLine.getOptionValue("i");
	        
	        if (commandLine.hasOption("o"))
	        {
				outputFileName = commandLine.getOptionValue("o");
	        } else {
	    		File sourceFile = new File(sourceFilePathName);
	    		String sourceFileName = sourceFile.getName();
    			outputFileName = sourceFileName .substring(0, sourceFileName.lastIndexOf("."))+".graphviz";
	        }
	    }
	    catch (ParseException exception)
	    {
	    	logger.error("Parse error: ");
	    	logger.error(exception.getMessage());
    	    formatter.printHelp("java -jar terraform-state-viz-jar-with-dependencies.jar", header, options, footer, true); 
        	invocationError = true;
	    }
		return invocationError;
	}
}