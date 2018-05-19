package com.jakub.tfutil.diagram;

import java.util.HashMap;

import com.jakub.tfutil.aws.objects.InternetGateway;
import com.jakub.tfutil.aws.objects.Model;

public class GraphvizInternetGateway {
	
	public static void printInternetGateways(StringBuffer diagram, Model model, String idVpc) {
		HashMap<String, InternetGateway> igws = model.findInternetGatewaysInVpc(idVpc);
		for (String idIgw : igws.keySet()) {
			InternetGateway igw = igws.get(idIgw);
			GraphvizDiagram.allDisplayedIgws.put(idIgw, igw);
			String style = (igw.isResource()?"filled":"dashed");
			diagram.append(
"            subgraph cluster_"+(GraphvizDiagram.c++)+" {\n"+
"                \"icon"+idIgw+"\" [label=\"RInternet GW\" shape=lpromoter]\n"+
"                node [style=filled];\n"+
"                color=red;\n"+
"                style=\""+style+",rounded\" label = \"Internet GW: "+ igw.tagsName+"\"\n"+
"                \""+idIgw+"\" [label = \"{tfName: "+ igw.tfName+"|id: "+idIgw +"}\" shape = \"record\" ];\n"+
"            }\n");
		}
	}
}
