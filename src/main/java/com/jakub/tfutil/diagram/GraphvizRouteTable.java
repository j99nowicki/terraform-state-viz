package com.jakub.tfutil.diagram;

import java.util.HashMap;
import java.util.HashSet;

import com.jakub.tfutil.aws.objects.Model;
import com.jakub.tfutil.aws.objects.RouteTable;

public class GraphvizRouteTable {
	
	public static void printRouteTables(StringBuffer diagram, Model model, String idRVpc, HashSet<String> allDisplayedZonesInVpc) {
		HashMap<String, RouteTable> routeTables = model.findRouteTablesInVpc(idRVpc);
		for (String idRouteTable : routeTables.keySet()) {
			RouteTable routeTable = routeTables.get(idRouteTable);
			diagram.append(
"        subgraph cluster_"+(GraphvizDiagram.c++)+" {\n"+
"            node [style=filled];\n"+
"            color=mistyrose\n"+
"            label = \"Route Table: "+ routeTable.tagsName+"\"\n"+
"            \""+idRouteTable+"\" [label = \"{tfName: "+ routeTable.tfName+"|id: "+idRouteTable+"}\" shape = \"record\" ];\n"+
"        }\n");

			GraphvizRouteTableAssociation.printAssociationsAndRoutes(diagram, model, idRouteTable, allDisplayedZonesInVpc);
		}			
	}
	
}
