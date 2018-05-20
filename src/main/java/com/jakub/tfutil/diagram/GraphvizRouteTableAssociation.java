package com.jakub.tfutil.diagram;

import java.util.HashMap;
import java.util.HashSet;

import com.jakub.tfutil.aws.objects.Model;
import com.jakub.tfutil.aws.objects.RouteTableAssociation;
import com.jakub.tfutil.aws.objects.Subnet;

public class GraphvizRouteTableAssociation {
	
	public static void printAssociationsAndRoutes(StringBuffer diagram, Model model, String idRouteTable, HashSet<String> allDisplayedZonesInVpc) {
		//Route table associations - find all associations for given table
		HashMap<String, RouteTableAssociation> routeTableAssociations = model.findRouteTablesAssociationForRouteTable(idRouteTable);
		
		for (String idRouteTableAssociation : routeTableAssociations.keySet()) {
			RouteTableAssociation routeTableAssociation = routeTableAssociations.get(idRouteTableAssociation);
			Subnet subnet = model.subnets.get(routeTableAssociation.subnet_id);
			if (subnet!=null && allDisplayedZonesInVpc.contains(subnet.availability_zone)){
				String idSubnet = subnet.id;
				
				if (GraphvizDiagram.showRouteTablesAssociationsToSubnets){
					diagram.append("        \""+idSubnet+"\" -> \""+ idRouteTable +"\" [label = \""+idRouteTableAssociation+"\" dir=none, style=dashed]\n");
				}
				if (GraphvizDiagram.showInternalRoutesToGateways){
					GraphvizRoute.printRoute(diagram, model, idRouteTable, idSubnet);
				}
			}
		}
	}
	
}
