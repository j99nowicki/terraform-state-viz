package com.jakub.tfutil;

import java.io.IOException;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakub.tfutil.diagram.GraphvizDiagram;

public class TerraformStateViz{

	static final Logger logger = Logger.getLogger(TerraformStateViz.class);
	static Gson gson = new GsonBuilder().create();

	public static void main(String[] args) throws IOException{
		logger.info("Start");
		
        Gson gson = new GsonBuilder().create();
        gson.toJson("Hello", System.out);
        gson.toJson(123, System.out);
        System.out.println();
        System.out.println();
        
//        String fileName = "src\\main\\resources\\terraformDoubleVpc.tfstate";
//        String fileName = "src\\main\\resources\\tf-docker-ami.backup";
        String fileName = "src\\main\\resources\\terraform5.tfstate";
//        String fileName = "src\\main\\resources\\terraform.tfstate";
//        String fileName = "src\\main\\resources\\terraformVpcSecGrEc2.tfstate";
//        String fileName = "src\\main\\resources\\terraform_ireland.tfstate";

        TfObjectWarehouseBuilder TfObjectWarehouseBuilder = new TfObjectWarehouseBuilder();
        TfObjectsWarehouse tfObjectsWarehouse = TfObjectWarehouseBuilder.buildTfObjectWarehouse(fileName);
                
		GraphvizDiagram graphvizDiagram = new GraphvizDiagram(tfObjectsWarehouse);
		String diagram = graphvizDiagram.draw();
		System.out.println(diagram);
		
	}
}