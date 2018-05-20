package com.jakub.tfutil.diagram;

import java.util.HashMap;

import com.jakub.tfutil.aws.objects.Model;
import com.jakub.tfutil.aws.objects.Route;

public class GraphvizRoute {
	
	//Routes - find all routes belonging to the route table and connect it to the gateway
	public static void printRoute(StringBuffer diagram, Model model, String idRouteTable, String idSubnet) {
		HashMap<String, Route> matchingRoutes = model.findRoutesInTable(idRouteTable);
		for (String idRoute : matchingRoutes.keySet()) {
			String idGateway = matchingRoutes.get(idRoute).gateway_id;		
			String idNatGateway = matchingRoutes.get(idRoute).nat_gateway_id;
			if (!"".equals(idGateway) && idGateway!=null){
				diagram.append("        \""+idSubnet+"\" -> \""+ idGateway +"\" [label = \""+idRoute+"\" dir=both]\n");						
			} else if (!"".equals(idNatGateway) && idNatGateway!=null){
				diagram.append("        \""+idSubnet+"\" -> \""+ idNatGateway +"\" [label = \""+idRoute+"\" ]\n");						
			}
		}				
	}	
	
}
