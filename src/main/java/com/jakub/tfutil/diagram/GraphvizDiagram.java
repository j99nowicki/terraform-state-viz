package com.jakub.tfutil.diagram;

import java.util.HashMap;
import java.util.HashSet;

import com.jakub.tfutil.Model;
import com.jakub.tfutil.aws.EipAttributes;
import com.jakub.tfutil.aws.InstanceAttributes;
import com.jakub.tfutil.aws.InternetGatewayAttributes;
import com.jakub.tfutil.aws.VpnGatewayAttributes;
import com.jakub.tfutil.aws.VpcAttributes;
import com.jakub.tfutil.aws.NatGatewayAttributes;
import com.jakub.tfutil.aws.RouteAttributes;
import com.jakub.tfutil.aws.RouteTableAttributes;
import com.jakub.tfutil.aws.RouteTableAssociationAttributes;
import com.jakub.tfutil.aws.SubnetAttributes;
import com.jakub.tfutil.aws.VpcEndpointAttributes;

public class GraphvizDiagram{
		
	public String draw(Model model){
		boolean showRouteTables = true;
		
		HashSet<String> zones = model.findAvailabilityZones();
//		zones.clear();
//		zones.add("eu-west-1b");
	
		StringBuffer diagram = new StringBuffer();
		int c=0;
		diagram.append(
"digraph G {\n"+
"    rankdir=TB;\n");
		
		VpcAttributes modelsAttr = model.vpcAttributes;
		diagram.append(
//Vpc
"    subgraph cluster_"+(c++)+" {\n"+
"        style=\"filled,rounded\";\n"+
"        color=lightgrey;\n"+
"        node [style=filled,color=white, shape=box];\n"+
//"           a0 -> a1 -> a2 -> a3;\n"+
"        label = \"VPC: "+ modelsAttr.tagsName+" ("+ modelsAttr.id+")\";\n"+
"        \""+modelsAttr.id+"\" [label = \"{tfName: "+ model.vpcAttributes.getTfName()+"|id: "+modelsAttr.id+"| cidr_block: "+modelsAttr.cidr_block+"}\" shape = \"record\" ];\n");

//Gateways		
		diagram.append(
"        subgraph cluster_"+(c++)+" {\n"+
"            style=invisible\n"+
"            label=Gateways\n");
		
//Igw
		HashMap<String, InternetGatewayAttributes> igws = model.internetGatewaysAttributes;
		for (String tfName : igws.keySet()) {
			InternetGatewayAttributes attr = igws.get(tfName);
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
		HashMap<String, VpnGatewayAttributes> vpnGws = model.vpnGatewaysAttributes;
		for (String tfName : vpnGws.keySet()) {
			VpnGatewayAttributes attr = vpnGws.get(tfName);
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
		HashMap<String, VpcEndpointAttributes> vpcEndpointsAttrs = model.vpcEndpointsAttributes;
		for (String tfName : vpcEndpointsAttrs.keySet()) {
			VpcEndpointAttributes attr = vpcEndpointsAttrs.get(tfName);
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
			HashMap<String, SubnetAttributes> subnetsAttributes = model.findSubnetsAttributesInZone(zone);
			for (String tfName : subnetsAttributes.keySet()) {
				SubnetAttributes attr = subnetsAttributes.get(tfName);
				diagram.append(
"            subgraph cluster_"+(c++)+" {\n"+
"                node [style=filled];\n"+
"                color=green\n"+
"                label = \"Subnet: "+ attr.tagsName+"\"\n"+
"                \""+attr.id+"\" [label = \"{tfName: "+ tfName+"|id: "+attr.id+"|cidr_block: "+attr.cidr_block+"}\" shape = \"record\" ];\n");

//Instances
				HashMap<String, InstanceAttributes> instancesAttributes = model.findInstancesInSubnet(attr.id);
				for (String tfNameGw : instancesAttributes.keySet()) {
					InstanceAttributes attrInstance = instancesAttributes.get(tfNameGw);
					diagram.append(
"                subgraph cluster_"+(c++)+" {\n"+
"                    \"icon-"+attrInstance.id+"\" [label=EC2 shape=rpromoter]\n"+
"                    node [style=filled];\n"+
"                    color=blue\n"+
"                    label = \"EC2: "+ attr.tagsName+"\"\n"+
"                    \""+attrInstance.id+"\" [label = \"{tfName: "+ tfNameGw+"|id: "+attrInstance.id+"|public IP: "+attrInstance.public_ip+"|private IP: "+attrInstance.private_ip+"}\" shape = \"record\" ];\n");
//Instances - end
					diagram.append(					
"                }\n");
				}
//Nat - end
				
//Nat Gw
				HashMap<String, NatGatewayAttributes> ngwsAttrs = model.findNatGatewaysAttributesInSubnet(attr.id);
				for (String tfNameGw : ngwsAttrs.keySet()) {
					NatGatewayAttributes attrGw = ngwsAttrs.get(tfNameGw);
					diagram.append(
"                subgraph cluster_"+(c++)+" {\n"+
"                    \"icon-"+attrGw.id+"\" [label=NatGW shape=rpromoter]\n"+
"                    node [style=filled];\n"+
"                    color=salmon\n"+
"                    label = \"Nat GW: "+ attr.tagsName+"\"\n"+
"                    \""+attrGw.id+"\" [label = \"{tfName: "+ tfNameGw+"|id: "+attrGw.id+"|public IP: "+attrGw.public_ip+"|private IP: "+attrGw.private_ip+"}\" shape = \"record\" ];\n");

//Eip
					EipAttributes eipAttributes = model.findEipAttributes(attrGw.allocation_id);
					diagram.append(			
"                    \"icon-"+eipAttributes.id+"\" [label=\"Elastic IP\" shape=house color=yellow]\n");
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
		HashMap<String, RouteTableAttributes> routeTablesAttributes = model.routeTablesAttributes;
		if (showRouteTables){
			for (String tfName : routeTablesAttributes.keySet()) {
				RouteTableAttributes attr = routeTablesAttributes.get(tfName);
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
		HashMap<String, RouteTableAssociationAttributes> routeTableAssociationsAttributes = model.routeTableAssociationsAttributes;
		for (String tfName : routeTableAssociationsAttributes.keySet()) {
			RouteTableAssociationAttributes attr = routeTableAssociationsAttributes.get(tfName);
			SubnetAttributes subnetAttributes = model.findSubnetAttributes(attr.subnet_id);
			RouteTableAttributes routeTableAttributes = model.findRouteTableAttributes(attr.route_table_id);
			
			if (subnetAttributes!=null && routeTableAttributes!=null && zones.contains(subnetAttributes.availability_zone)){
				if (showRouteTables){
					diagram.append("        \""+subnetAttributes.id+"\" -> \""+ routeTableAttributes.id +"\" [label = \""+attr.id+"\" dir=none, style=dashed]\n");
				}
//Routes - find all routes belonging to the route table and connect it to the gateway
				HashMap<String, RouteAttributes> matchingRoutesAttributes = model.findRoutesAttributesInTable(routeTableAttributes.id);
				for (String tfNameRoute : matchingRoutesAttributes.keySet()) {
					String gatewayId = matchingRoutesAttributes.get(tfNameRoute).gateway_id;		
					String natGatewayId = matchingRoutesAttributes.get(tfNameRoute).nat_gateway_id;
					if (!"".equals(gatewayId) && gatewayId!=null){
						diagram.append("        \""+subnetAttributes.id+"\" -> \""+ gatewayId +"\" [label = \""+attr.id+"\" dir=both]\n");						
					} else if (!"".equals(natGatewayId) && natGatewayId!=null){
						diagram.append("        \""+subnetAttributes.id+"\" -> \""+ natGatewayId +"\" [label = \""+attr.id+"\" ]\n");						
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
				InternetGatewayAttributes attr = igws.get(tfName);
				diagram.append(
"           \""+attr.id+"\" -> Internet [dir=both]\n");
			}
			HashMap<String, NatGatewayAttributes> ngwsAttrs = model.findNatGatewaysAttributesInZones(zones);
			for (String tfName : ngwsAttrs.keySet()) {
				NatGatewayAttributes attr = ngwsAttrs.get(tfName);
				diagram.append(
"           \""+attr.id+"\" -> Internet\n");
			}
//Internet - end		

//External Data Centre
			for (String tfName : vpnGws.keySet()) {
				VpnGatewayAttributes attr = vpnGws.get(tfName);
				diagram.append(
"           \""+attr.id+"\" -> \"External Data Centre\" [dir=both]\n");
			}		
//External Data Centre - end
		
//Endpoints
			for (String tfName : vpcEndpointsAttrs.keySet()) {
				VpcEndpointAttributes attr = vpcEndpointsAttrs.get(tfName);
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
