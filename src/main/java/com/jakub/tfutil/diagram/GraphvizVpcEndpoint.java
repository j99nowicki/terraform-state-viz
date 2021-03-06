package com.jakub.tfutil.diagram;

import java.util.HashMap;

import com.jakub.tfutil.aws.objects.Model;
import com.jakub.tfutil.aws.objects.VpcEndpoint;

public class GraphvizVpcEndpoint {
	
	public static void printVpcEndpoints(StringBuffer diagram, Model model, String idVpc) {
		HashMap<String, VpcEndpoint> vpcEndpoints = model.findVpcEndpointsInVpc(idVpc);
		for (String idVpcEndpoint : vpcEndpoints.keySet()) {
			VpcEndpoint vpcEndpoint = vpcEndpoints.get(idVpcEndpoint);
			GraphvizDiagram.allDisplayedVpcEndpoints.put(idVpcEndpoint, vpcEndpoint);
			String style = (vpcEndpoint.isResource()?"filled":"dashed");
			diagram.append(
"            subgraph cluster_"+(GraphvizDiagram.c++)+" {\n"+
"                \"icon"+vpcEndpoint.id+"\" [label=\"VPC Endpoint\" shape=cds]\n"+
"                node [style=filled];\n"+
"                color=orchid1\n"+
"                style=\""+style+",rounded\" label = \"VPC Endpoint: "+ vpcEndpoint.vpc_endpoint_type+"\"\n"+
"                \""+idVpcEndpoint+"\" [label = \"{tfName: "+ vpcEndpoint.tfName+"|id: "+idVpcEndpoint+"|service_name: "+vpcEndpoint.service_name+"}\" shape = \"record\" ];\n"+
"            }\n");
		}
	}
}
