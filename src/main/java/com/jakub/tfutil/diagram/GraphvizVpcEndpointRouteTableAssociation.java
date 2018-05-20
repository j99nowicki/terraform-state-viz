package com.jakub.tfutil.diagram;

import java.util.HashMap;

import com.jakub.tfutil.aws.objects.Model;
import com.jakub.tfutil.aws.objects.VpcEndpoinRouteTableAssociation;
import com.jakub.tfutil.aws.objects.VpcEndpoint;

public class GraphvizVpcEndpointRouteTableAssociation {
	
	public static void printVpcEndpointRouteTableAssociations(StringBuffer diagram, Model model, String idRouteTable) {
		//VPC Endpoint Route Table associations - find all associations for given table
		HashMap<String, VpcEndpoinRouteTableAssociation> vpcEndpoinRouteTableAssociations = model.findVpcEndpoinRouteTableAssociationsForRouteTable(idRouteTable);
		
		for (String idVpcEndpoinRouteTableAssociation : vpcEndpoinRouteTableAssociations.keySet()) {
			VpcEndpoinRouteTableAssociation vpcEndpoinRouteTableAssociation = vpcEndpoinRouteTableAssociations.get(idVpcEndpoinRouteTableAssociation);
			VpcEndpoint vpcEndpint = model.vpcEndpoints.get(vpcEndpoinRouteTableAssociation.vpc_endpoint_id);
			if (vpcEndpint!=null){
				String idVpcEndpoint = vpcEndpoinRouteTableAssociation.vpc_endpoint_id;
				
				diagram.append("        \""+ idRouteTable +"\" -> \""+ idVpcEndpoint +"\" [label = \""+idVpcEndpoinRouteTableAssociation+"\" dir=none, style=dashed]\n");
			}
		}
	}
}
