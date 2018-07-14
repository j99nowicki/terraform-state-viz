package com.jakub.tfutil;
import static org.junit.Assert.*;

import org.junit.Test;

import com.jakub.tfutil.diagram.GraphvizDiagram;

import java.io.IOException;

public class TerraformStateVizTest {

	TerraformStateViz terraformStateViz = new TerraformStateViz();	

	//String fileName = "src\\test\\resources\\terraformDoubleVpc.tfstate";
	//String fileName = "src\\test\\resources\\tf-docker-ami.backup";
	//String fileName = "src\\test\\resources\\terraform6.tfstate";
	String fileName = "src\\test\\resources\\terraform.tfstate";
	//String fileName = "src\\test\\resources\\terraformVpcSecGrEc2.tfstate";
	//String fileName = "src\\test\\resources\\terraform_ireland.tfstate";
		
	@Test
	public void testMain() throws IOException {
		TfObjectWarehouseBuilder TfObjectWarehouseBuilder = new TfObjectWarehouseBuilder();
        TfObjectsWarehouse tfObjectsWarehouse = TfObjectWarehouseBuilder.buildTfObjectWarehouse(fileName);
		GraphvizDiagram graphvizDiagram = new GraphvizDiagram(tfObjectsWarehouse);
		String diagram = graphvizDiagram.draw();
		assertTrue(diagram.startsWith("digraph G {"));
   }

}

