package com.jakub.tfutil.diagram;

import java.util.HashMap;

import com.jakub.tfutil.aws.objects.InternetGateway;
import com.jakub.tfutil.aws.objects.Model;

public class GraphvizInternetGateway {
	
	public static void printInternetGateways(StringBuffer diagram, Model model, String idVpc, HashMap<String, InternetGateway> allDisplayedIgws) {
		HashMap<String, InternetGateway> igws = model.findInternetGatewaysAttributesInVpc(idVpc);
		for (String idIgw : igws.keySet()) {
			InternetGateway igw = igws.get(idIgw);
			allDisplayedIgws.put(idIgw, igw);
			if (igw.isResource()){
				diagram.append(
"            subgraph cluster_"+(GraphvizDiagram.c++)+" {\n"+
"                \"icon"+idIgw+"\" [label=\"RInternet GW\" shape=lpromoter]\n"+
"                node [style=filled];\n"+
"                color=red;\n"+
"                style=\"filled,rounded\" label = \"Internet GW: "+ igw.tagsName+"\"\n"+
"                \""+idIgw+"\" [label = \"{tfName: "+ igw.tfName+"|id: "+idIgw +"}\" shape = \"record\" ];\n"+
"            }\n");
			} else {
				diagram.append(
"            subgraph cluster_"+(GraphvizDiagram.c++)+" {\n"+
"                \"icon"+idIgw+"\" [label=\"DInternet GW\" shape=lpromoter]\n"+
"                node [style=filled];\n"+
"                color=red;\n"+
"                style=\"dashed,rounded\" label = \"Internet GW: "+ igw.tagsName+"\"\n"+
"                \""+idIgw+"\" [label = \"{tfName: "+ igw.tfName+"|id: "+idIgw +"}\" shape = \"record\" ];\n"+
"            }\n");
				
			}
		}
	}
	
	
}
