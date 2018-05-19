package com.jakub.tfutil.diagram;

import java.util.HashMap;

import com.jakub.tfutil.aws.objects.Model;
import com.jakub.tfutil.aws.objects.VpcEndpoint;

public class GraphvizVpcEndpoint {
	
	public static void printVpcEndpoints(StringBuffer diagram, Model model, String idVpc, HashMap<String, VpcEndpoint> allDisplayedVpcEndpoints) {
		HashMap<String, VpcEndpoint> vpcEndpoints = model.findVpcEndpointsInVpc(idVpc);
		for (String idVpcEndpoint : vpcEndpoints.keySet()) {
			VpcEndpoint vpcEndpoint = vpcEndpoints.get(idVpcEndpoint);
			allDisplayedVpcEndpoints.put(idVpcEndpoint, vpcEndpoint);
			if (vpcEndpoint.isResource()){
				diagram.append(
"            subgraph cluster_"+(GraphvizDiagram.c++)+" {\n"+
"                \"icon"+vpcEndpoint.id+"\" [label=\"R VPC Endpoint\" shape=cds]\n"+
"                node [style=filled];\n"+
"                color=orchid1\n"+
"                style=\"filled,rounded\" label = \"R VPC Endpoint: "+ vpcEndpoint.vpc_endpoint_type+"\"\n"+
"                \""+idVpcEndpoint+"\" [label = \"{tfName: "+ vpcEndpoint.tfName+"|id: "+idVpcEndpoint+"|service_name: "+vpcEndpoint.service_name+"}\" shape = \"record\" ];\n"+
"            }\n");
			} else {
				diagram.append(
"            subgraph cluster_"+(GraphvizDiagram.c++)+" {\n"+
"                \"icon"+vpcEndpoint.id+"\" [label=\"D VPC Endpoint\" shape=cds]\n"+
"                node [style=dashed];\n"+
"                color=orchid1\n"+
"                style=\"filled,rounded\" label = \"D VPC Endpoint: "+ vpcEndpoint.vpc_endpoint_type+"\"\n"+
"                \""+idVpcEndpoint+"\" [label = \"{tfName: "+ vpcEndpoint.tfName+"|id: "+idVpcEndpoint+"|service_name: "+vpcEndpoint.service_name+"}\" shape = \"record\" ];\n"+
"            }\n");
				
			}
		}
	}
}
