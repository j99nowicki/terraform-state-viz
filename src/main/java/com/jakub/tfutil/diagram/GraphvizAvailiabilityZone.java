package com.jakub.tfutil.diagram;

import com.jakub.tfutil.aws.objects.Model;

public class GraphvizAvailiabilityZone {
	
	public static void printAvailiabilityZone(StringBuffer diagram, Model model, String idVpc, String zone) {

		diagram.append(
"        subgraph cluster_"+(GraphvizDiagram.c++)+" {\n"+
"            node [style=filled];\n"+
"            style=\"dashed,rounded\";\n"+
"            color=turquoise\n"+
"            label = \"Availability zone: "+ zone +"\"\n" +
"            \""+zone+idVpc+"\" [label = \"{}\" style=invisible ];\n");
					
		//Subnets - end
			GraphvizSubnet.printSubnets(diagram, model, idVpc, zone);
		
		//Zones - end
			diagram.append(
		"        }\n");	
	}			
}
