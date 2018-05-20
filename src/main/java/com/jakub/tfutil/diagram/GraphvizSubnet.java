package com.jakub.tfutil.diagram;

import java.util.HashMap;

import com.jakub.tfutil.aws.objects.Model;
import com.jakub.tfutil.aws.objects.Subnet;
import com.jakub.tfutil.aws.objects.VpnGateway;

public class GraphvizSubnet {
	
	public static void printSubnets(StringBuffer diagram, Model model, String idVpc, String zone) {
//Subnets		
		HashMap<String, Subnet> subnets = model.findSubnetsInVpcInZone(idVpc, zone);
		for (String idSubnet : subnets.keySet()) {

			Subnet subnet = subnets.get(idSubnet);
			String style = (subnet.isResource()?"filled":"dashed");	
			diagram.append(
"            subgraph cluster_"+(GraphvizDiagram.c++)+" {\n"+
"                node [style=filled];\n"+
"                color=green\n"+
"                penwidth=1\n"+
"                style=\""+style+",rounded\"\n");
			if (subnet.isResource()){
				diagram.append(
"                label = \"Subnet: "+ subnet.getResourceSubnet().tagsName+"\"\n"+
"                \""+subnet.id+"\" [label = \"{tfName: "+ subnet.tfName+"|id: "+idSubnet+"|cidr_block: "+subnet.getResourceSubnet().cidr_block+"}\" shape = \"record\" ];\n");
			} else {
				diagram.append(
"                label = \"SubnetId: "+ idSubnet+"\"\n"+
"                \""+idSubnet+"\" [label = \"{tfName: "+ subnet.tfName+"|id: "+idSubnet+"}\" shape = \"record\" ];\n");
			}
//Instances
			GraphvizInstance.printInstances(diagram, model, subnet.id);	
//Nat Gw
			GraphvizNatGateway.printNatGateways(diagram, model, subnet.id, GraphvizDiagram.displayedNatGws);
//Subnets - end
			diagram.append(						
"            }\n");
		}

	}
	
}
