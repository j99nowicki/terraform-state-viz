package com.jakub.tfutil.diagram;

import java.util.HashMap;
import java.util.HashSet;

import com.jakub.tfutil.Model;
import com.jakub.tfutil.aws.eip.Eip;
import com.jakub.tfutil.aws.internet_gateway.InternetGateway;
import com.jakub.tfutil.aws.nat_gateway.NatGateway;
import com.jakub.tfutil.aws.route.Route;
import com.jakub.tfutil.aws.route_table.RouteTable;
import com.jakub.tfutil.aws.route_table_association.RouteTableAssociation;
import com.jakub.tfutil.aws.subnet.Subnet;
import com.jakub.tfutil.aws.vpc_endpoint.VpcEndpoint;
import com.jakub.tfutil.aws.vpn_gateway.VpnGateway;

public class GraphvizDiagram{
		
	public String draw(Model model){
		boolean showRouteTables = true;
		
		HashSet<String> zones = model.findAvailabilityZones();
		zones.clear();
		zones.add("eu-west-1b");
	
		StringBuffer diagram = new StringBuffer();
		int c=0;
		diagram.append(
"digraph G {\n"+
"    rankdir=TB;\n");
		
		com.jakub.tfutil.aws.vpc.Attributes modelsAttr = model.vpc.primary.attributes;
		diagram.append(
//Vpc
"    subgraph cluster_"+(c++)+" {\n"+
"        style=\"filled,rounded\";\n"+
"        color=lightgrey;\n"+
"        node [style=filled,color=white, shape=box];\n"+
//"           a0 -> a1 -> a2 -> a3;\n"+
"        label = \"VPC: "+ modelsAttr.tagsName+" ("+ modelsAttr.id+")\";\n"+
"        \""+modelsAttr.id+"\" [label = \"{tfName: "+ model.vpc.getTfName()+"|id: "+modelsAttr.id+"| cidr_block: "+modelsAttr.cidr_block+"}\" shape = \"record\" ];\n");

//Gateways		
		diagram.append(
"        subgraph cluster_"+(c++)+" {\n"+
"            style=invisible\n"+
"            label=Gateways\n");
		
//Igw
		HashMap<String, InternetGateway> igws = model.internetGateways;
		for (String tfName : igws.keySet()) {
			com.jakub.tfutil.aws.internet_gateway.Attributes attr = igws.get(tfName).primary.attributes;
			diagram.append(
"            subgraph cluster_"+(c++)+" {\n"+
"                \"icon"+attr.id+"\" [label=\"Internet GW\" shape=lpromoter]\n"+
"                node [style=filled];\n"+
"                color=red;\n"+
"                style=\"filled,rounded\""+
"                label = \"Internet GW: "+ attr.tagsName+"\"\n"+
"                \""+attr.id+"\" [label = \"{tfName: "+ tfName+"|id: "+attr.id+"}\" shape = \"record\" ];\n"+
"            }\n");
		}
//Igw - end

//Vpn Gw
		HashMap<String, VpnGateway> vpnGws = model.vpnGateways;
		for (String tfName : vpnGws.keySet()) {
			com.jakub.tfutil.aws.vpn_gateway.Attributes attr = vpnGws.get(tfName).primary.attributes;
			diagram.append(
"            subgraph cluster_"+(c++)+" {\n"+
"                \"icon"+attr.id+"\" [label=\"VPN GW\" shape=cds]\n"+
"                node [style=filled];\n"+
"                color=royalblue1\n"+
"                style=\"filled,rounded\""+
"                label = \"VPN GW: "+ attr.tagsName+"\"\n"+
"                \""+attr.id+"\" [label = \"{tfName: "+ tfName+"|id: "+attr.id+"|amazon side ASN: "+attr.amazon_side_asn+"}\" shape = \"record\" ];\n"+
"            }\n");
		}
//Vpn Gw - end
		
//Vpc Endpoints
		HashMap<String, VpcEndpoint> vpcEndpoints = model.vpcEndpoints;
		for (String tfName : vpcEndpoints.keySet()) {
			com.jakub.tfutil.aws.vpc_endpoint.Attributes attr = vpcEndpoints.get(tfName).primary.attributes;
			diagram.append(
"            subgraph cluster_"+(c++)+" {\n"+
"                \"icon"+attr.id+"\" [label=\"VPC Endpoint\" shape=cds]\n"+
"                node [style=filled];\n"+
"                color=orchid1\n"+
"                style=\"filled,rounded\""+
"                label = \"VPC Endpoint: "+ attr.vpc_endpoint_type+"\"\n"+
"                \""+attr.id+"\" [label = \"{tfName: "+ tfName+"|id: "+attr.id+"|service_name: "+attr.service_name+"}\" shape = \"record\" ];\n"+
"            }\n");
		}
//Vpc Endpoints	- end

//Gateways - end		
		diagram.append(
"        }\n");

//Zones
		for (String zone : zones) {
			diagram.append(
"        subgraph cluster_"+(c++)+" {\n"+
"            node [style=filled];\n"+
"            color=turquoise\n"+
"            label = \"Availability zone: "+ zone +"\"\n");
		
//Subnets		
			HashMap<String, Subnet> subnets = model.findSubnetsInZone(zone);
			for (String tfName : subnets.keySet()) {
				com.jakub.tfutil.aws.subnet.Attributes attr = subnets.get(tfName).primary.attributes;
				diagram.append(
"            subgraph cluster_"+(c++)+" {\n"+
"                node [style=filled];\n"+
"                color=green\n"+
"                label = \"Subnet: "+ attr.tagsName+"\"\n"+
"                \""+attr.id+"\" [label = \"{tfName: "+ tfName+"|id: "+attr.id+"|cidr_block: "+attr.cidr_block+"}\" shape = \"record\" ];\n");

//Nat Gw
				HashMap<String, NatGateway> ngws = model.findNatGatewaysInSubnet(attr.id);
				for (String tfNameGw : ngws.keySet()) {
					com.jakub.tfutil.aws.nat_gateway.Attributes attrGw = ngws.get(tfNameGw).primary.attributes;
					diagram.append(
"                subgraph cluster_"+(c++)+" {\n"+
"                    \"icon-"+attrGw.id+"\" [label=NatGW shape=rpromoter]\n"+
"                    node [style=filled];\n"+
"                    color=salmon\n"+
"                    label = \"Nat GW: "+ attr.tagsName+"\"\n"+
"                    \""+attrGw.id+"\" [label = \"{tfName: "+ tfNameGw+"|id: "+attrGw.id+"|public IP: "+attrGw.public_ip+"|private IP: "+attrGw.private_ip+"}\" shape = \"record\" ];\n");

//Eip
					Eip eip = model.findEip(attrGw.allocation_id);
					com.jakub.tfutil.aws.eip.Attributes attrEip = eip.primary.attributes;
					diagram.append(			
"                    \"icon-"+attrEip.id+"\" [label=\"Elastic IP\" shape=house color=yellow]\n");
//Eip - end
					diagram.append(					
"                }\n");
				}
//Nat - end
						
				diagram.append(						
"            }\n");
			}
//Subnets - end

//Zones - end
			diagram.append(
"        }\n");
		}

//Route tables
		HashMap<String, RouteTable> routeTables = model.routeTables;
		if (showRouteTables){
			for (String tfName : routeTables.keySet()) {
				com.jakub.tfutil.aws.route_table.Attributes attr = routeTables.get(tfName).primary.attributes;
				diagram.append(
"        subgraph cluster_"+(c++)+" {\n"+
"            node [style=filled];\n"+
"            color=mistyrose\n"+
"            label = \"Route Table: "+ attr.tagsName+"\"\n"+
"            \""+attr.id+"\" [label = \"{tfName: "+ tfName+"|id: "+attr.id+"}\" shape = \"record\" ];\n"+
"        }\n");
			}
		}	
//Route tables - end		
		
//Route table associations
		HashMap<String, RouteTableAssociation> routeTableAssociations = model.routeTableAssociations;
		for (String tfName : routeTableAssociations.keySet()) {
			com.jakub.tfutil.aws.route_table_association.Attributes attr = routeTableAssociations.get(tfName).primary.attributes;
			Subnet subnet = model.findSubnet(attr.subnet_id);
			RouteTable routeTable = model.findRouteTable(attr.route_table_id);
			
			if (subnet!=null && routeTable!=null && zones.contains(subnet.primary.attributes.availability_zone)){
				if (showRouteTables){
					diagram.append("        \""+subnet.primary.attributes.id+"\" -> \""+ routeTable.primary.attributes.id +"\" [label = \""+attr.id+"\" dir=none, style=dashed]\n");
				}
//Routes - find all routes belonging to the route table and connect it to the gateway
				HashMap<String, Route> matchingRoutes = model.findRoutesInTable(routeTable.primary.attributes.id);
				for (String tfNameRoute : matchingRoutes.keySet()) {
					String gatewayId = matchingRoutes.get(tfNameRoute).primary.attributes.gateway_id;		
					String natGatewayId = matchingRoutes.get(tfNameRoute).primary.attributes.nat_gateway_id;
					if (!"".equals(gatewayId) && gatewayId!=null){
						diagram.append("        \""+subnet.primary.attributes.id+"\" -> \""+ gatewayId +"\" [label = \""+attr.id+"\" dir=both]\n");						
					} else if (!"".equals(natGatewayId) && natGatewayId!=null){
						diagram.append("        \""+subnet.primary.attributes.id+"\" -> \""+ natGatewayId +"\" [label = \""+attr.id+"\" ]\n");						
					}
				}
				
//Routes -end
			}
		}
				
//Route table associations - end
			
				
//Vpc - end
		diagram.append("       }\n");

//External
		diagram.append(
"       subgraph cluster_"+(c++)+" {\n"+
"           style=\"invisible\";\n"+
"           label=External\n");
			
//Internet
			for (String tfName : igws.keySet()) {
				com.jakub.tfutil.aws.internet_gateway.Attributes attr = igws.get(tfName).primary.attributes;
				diagram.append(
"           \""+attr.id+"\" -> Internet [dir=both]\n");
			}
			HashMap<String, NatGateway> ngws = model.findNatGatewaysInZones(zones);
			for (String tfName : ngws.keySet()) {
				com.jakub.tfutil.aws.nat_gateway.Attributes attr = ngws.get(tfName).primary.attributes;
				diagram.append(
"           \""+attr.id+"\" -> Internet\n");
			}
//Internet - end		

//External Data Centre
			for (String tfName : vpnGws.keySet()) {
				com.jakub.tfutil.aws.vpn_gateway.Attributes attr = vpnGws.get(tfName).primary.attributes;
				diagram.append(
"           \""+attr.id+"\" -> \"External Data Centre\" [dir=both]\n");
			}		
//External Data Centre - end
		
//Endpoints
			for (String tfName : vpcEndpoints.keySet()) {
				com.jakub.tfutil.aws.vpc_endpoint.Attributes attr = vpcEndpoints.get(tfName).primary.attributes;
				diagram.append(
"           \""+attr.id+"\" -> \"AWS "+attr.service_name+"\" \n");
			}		
//Endpoints - end

//External - end		
		diagram.append(
"    }\n");
		
		diagram.append(
"}");		
		return diagram.toString();
	}

}
