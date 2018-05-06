package com.jakub.tfutil.diagram;

import java.util.HashMap;
import java.util.HashSet;

import com.jakub.tfutil.Model;
import com.jakub.tfutil.aws.attributes.EipAttributes;
import com.jakub.tfutil.aws.attributes.InstanceAttributes;
import com.jakub.tfutil.aws.attributes.InternetGatewayAttributes;
import com.jakub.tfutil.aws.attributes.NatGatewayAttributes;
import com.jakub.tfutil.aws.attributes.RouteAttributes;
import com.jakub.tfutil.aws.attributes.RouteTableAssociationAttributes;
import com.jakub.tfutil.aws.attributes.RouteTableAttributes;
import com.jakub.tfutil.aws.attributes.SubnetAttributes;
import com.jakub.tfutil.aws.attributes.VpcAttributes;
import com.jakub.tfutil.aws.attributes.VpcEndpointAttributes;
import com.jakub.tfutil.aws.attributes.VpnGatewayAttributes;

public class GraphvizDiagram{
		
	public String draw(Model model){
		boolean showRouteTables = true;
		
		StringBuffer diagram = new StringBuffer();
		int c=0;

//Diagram
		diagram.append(
"digraph G {\n"+
"    rankdir=TB;\n");
		
//Vpc
		HashMap<String, VpcAttributes> vpcs = model.vpcAttributes;

		for (String tfNameVpc : vpcs.keySet()) {
			VpcAttributes vpcAttr = vpcs.get(tfNameVpc);
			
			HashSet<String> zones = model.findAvailabilityZonesInVpc(vpcAttr.id);
//			zones.clear();
//			zones.add("eu-west-1b");
			
			diagram.append(				
"    subgraph cluster_"+(c++)+" {\n"+
"        style=\"filled,rounded\";\n"+
"        color=lightgrey;\n"+
"        node [style=filled,color=white, shape=box];\n"+
//"           a0 -> a1 -> a2 -> a3;\n"+
"        label = \"VPC: "+ vpcAttr.tagsName+" ("+ vpcAttr.id+")\";\n"+
"        \""+vpcAttr.id+"\" [label = \"{tfName: "+ tfNameVpc +"|id: "+vpcAttr.id+"| cidr_block: "+vpcAttr.cidr_block+"}\" shape = \"record\" ];\n");

//Gateways		
			diagram.append(
"        subgraph cluster_"+(c++)+" {\n"+
"            style=invisible\n"+
"            label=Gateways\n");
		
//Igw
			HashMap<String, InternetGatewayAttributes> igws = model.findInternetGatewaysAttributesInVpc(vpcAttr.id);
			for (String tfName : igws.keySet()) {
				InternetGatewayAttributes attr = igws.get(tfName);
				diagram.append(
"            subgraph cluster_"+(c++)+" {\n"+
"                \"icon"+attr.id+"\" [label=\"Internet GW\" shape=lpromoter]\n"+
"                node [style=filled];\n"+
"                color=red;\n"+
"                style=\"filled,rounded\" label = \"Internet GW: "+ attr.tagsName+"\"\n"+
"                \""+attr.id+"\" [label = \"{tfName: "+ tfName+"|id: "+attr.id+"}\" shape = \"record\" ];\n"+
"            }\n");
			}
//Igw - end

//Vpn Gw
			HashMap<String, VpnGatewayAttributes> vpnGws = model.findVpnGatewaysAttributesInVpc(vpcAttr.id);
			for (String tfName : vpnGws.keySet()) {
				VpnGatewayAttributes attr = vpnGws.get(tfName);
				diagram.append(
"            subgraph cluster_"+(c++)+" {\n"+
"                \"icon"+attr.id+"\" [label=\"VPN GW\" shape=cds]\n"+
"                node [style=filled];\n"+
"                color=royalblue1\n"+
"                style=\"filled,rounded\" label = \"VPN GW: "+ attr.tagsName+"\"\n"+
"                \""+attr.id+"\" [label = \"{tfName: "+ tfName+"|id: "+attr.id+"|amazon side ASN: "+attr.amazon_side_asn+"}\" shape = \"record\" ];\n"+
"            }\n");
			}
//Vpn Gw - end
		
//Vpc Endpoints			
			HashMap<String, VpcEndpointAttributes> vpcEndpointsAttrs = model.findVpcEndpointAttributesInVpc(vpcAttr.id);
			for (String tfName : vpcEndpointsAttrs.keySet()) {
				VpcEndpointAttributes attr = vpcEndpointsAttrs.get(tfName);
				diagram.append(
"            subgraph cluster_"+(c++)+" {\n"+
"                \"icon"+attr.id+"\" [label=\"VPC Endpoint\" shape=cds]\n"+
"                node [style=filled];\n"+
"                color=orchid1\n"+
"                style=\"filled,rounded\" label = \"VPC Endpoint: "+ attr.vpc_endpoint_type+"\"\n"+
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
				HashMap<String, SubnetAttributes> subnetsAttributes = model.findSubnetsAttributesInVpcInZone(vpcAttr.id, zone);
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
			HashMap<String, RouteTableAttributes> routeTablesAttributes = model.findRouteTablesAttributesInVpc(vpcAttr.id);
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

//TODO Separate RTA by VPC
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
			
				
//External
			diagram.append(
"        subgraph cluster_"+(c++)+" {\n"+
"            style=\"invisible\";\n"+
"            label=External\n");
			
//Internet
			for (String tfName : igws.keySet()) {
				InternetGatewayAttributes attr = model.internetGatewaysAttributes.get(tfName);
				diagram.append(
"            \""+attr.id+"\" -> Internet [dir=both]\n");
			}
			HashMap<String, NatGatewayAttributes> ngwsAttrs = model.findNatGatewaysAttributesInZones(zones);
			for (String tfName : ngwsAttrs.keySet()) {
				NatGatewayAttributes attr = ngwsAttrs.get(tfName);
				diagram.append(
"            \""+attr.id+"\" -> Internet\n");
			}
//Internet - end		

//External Data Centre
			for (String tfName : vpnGws.keySet()) {
				VpnGatewayAttributes attr = model.vpnGatewaysAttributes.get(tfName);
				diagram.append(
"            \""+attr.id+"\" -> \"External Data Centre\" [dir=both]\n");
			}		
//External Data Centre - end
		
//Endpoints
			for (String tfName : vpcEndpointsAttrs.keySet()) {
				VpcEndpointAttributes attr = model.vpcEndpointsAttributes.get(tfName);
				diagram.append(
"            \""+attr.id+"\" -> \"AWS "+attr.service_name+"\" \n");
			}		
//Endpoints - end

//External - end		
			diagram.append(
"        }\n");

//Vpc - end
			diagram.append(				
"    }\n");	
		}
		
//Diagram- end
		diagram.append(
"}");		
		return diagram.toString();
	}

}
