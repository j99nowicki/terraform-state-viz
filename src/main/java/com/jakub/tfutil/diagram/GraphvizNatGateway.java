package com.jakub.tfutil.diagram;

import java.util.HashMap;

import com.jakub.tfutil.aws.objects.Eip;
import com.jakub.tfutil.aws.objects.Model;
import com.jakub.tfutil.aws.objects.NatGateway;

public class GraphvizNatGateway {
	
	public static void printNatGateways(StringBuffer diagram,  Model model, String idSubnet, HashMap<String, NatGateway> displayedNatGws) {
		HashMap<String, NatGateway> natGws = model.findNatGatewaysInSubnet(idSubnet);
		for (String idNatGw : natGws.keySet()) {
			NatGateway natGw = natGws.get(idNatGw);
			displayedNatGws.put(idNatGw, natGw);
			String style = (natGw.isResource()?"filled":"dashed");
			diagram.append(
"                subgraph cluster_"+(GraphvizDiagram.c++)+" {\n"+
"                    \"icon-"+natGw.id+"\" [label=\"NatGW\" shape=rpromoter]\n"+
"                    node [style=filled];\n"+
"                    color=salmon\n"+
"                    style="+style+"\n"+
"                    label = \"Nat GW: "+ natGw.tagsName+"\"\n"+
"                    \""+idNatGw+"\" [label = \"{tfName: "+ natGw.tfName+"|id: "+idNatGw+"|public IP: "+natGw.public_ip+"|private IP: "+natGw.private_ip+"}\" shape = \"record\" ];\n");
//Eip
			Eip eip = model.findEip(natGw.allocation_id);
			GraphvizEip.printEip(diagram, eip);

			diagram.append(					
"                }\n");
		}
	}
}
