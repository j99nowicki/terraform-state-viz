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
		HashMap<String, InternetGatewayAttributes> allDisplayedIgws = new HashMap<String, InternetGatewayAttributes>();
		HashMap<String, VpnGatewayAttributes> allDisplayedVpnGws = new HashMap<String, VpnGatewayAttributes>();
		HashMap<String, VpcEndpointAttributes> allDisplayedVpcEndpoints = new HashMap<String, VpcEndpointAttributes>();
		HashSet<String> allDisplayedZones = new HashSet<String>();

//Diagram
		diagram.append(
"digraph G {\n"+
"    rankdir=TB;\n");
		
//Vpc
		HashMap<String, VpcAttributes> vpcs = model.vpcs;

		for (String idVpc : vpcs.keySet()) {
			VpcAttributes vpc = vpcs.get(idVpc);
			
			HashSet<String> zones = model.findAvailabilityZonesInVpc(idVpc);
//			zones.clear();
//			zones.add("eu-west-1b");
			
			diagram.append(				
"    subgraph cluster_"+(c++)+" {\n"+
"        style=\"filled,rounded\";\n"+
"        color=lightgrey;\n"+
"        node [style=filled,color=white, shape=box];\n"+
//"           a0 -> a1 -> a2 -> a3;\n"+
"        label = \"VPC: "+ vpc.tagsName+" ("+ vpc.id+")\";\n"+
"        \""+vpc.id+"\" [label = \"{tfName: "+ vpc.tfName +"|id: "+idVpc+"| cidr_block: "+vpc.cidr_block+"}\" shape = \"record\" ];\n");

//Gateways		
			diagram.append(
"        subgraph cluster_"+(c++)+" {\n"+
"            style=invisible\n"+
"            label=Gateways\n");
		
//Igw
			HashMap<String, InternetGatewayAttributes> igws = model.findInternetGatewaysAttributesInVpc(idVpc);
			for (String idIgw : igws.keySet()) {
				InternetGatewayAttributes igw = igws.get(idIgw);
				allDisplayedIgws.put(idIgw, igw);
				diagram.append(
"            subgraph cluster_"+(c++)+" {\n"+
"                \"icon"+igw.id+"\" [label=\"Internet GW\" shape=lpromoter]\n"+
"                node [style=filled];\n"+
"                color=red;\n"+
"                style=\"filled,rounded\" label = \"Internet GW: "+ igw.tagsName+"\"\n"+
"                \""+igw.id+"\" [label = \"{tfName: "+ idIgw+"|id: "+idIgw +"}\" shape = \"record\" ];\n"+
"            }\n");
			}
//Igw - end

//Vpn Gw
			HashMap<String, VpnGatewayAttributes> vpnGws = model.findVpnGatewaysAttributesInVpc(idVpc);
			for (String idVpnGw : vpnGws.keySet()) {
				VpnGatewayAttributes vpnGw = vpnGws.get(idVpnGw);
				allDisplayedVpnGws.put(idVpnGw, vpnGw);
				diagram.append(
"            subgraph cluster_"+(c++)+" {\n"+
"                \"icon"+vpnGw.id+"\" [label=\"VPN GW\" shape=cds]\n"+
"                node [style=filled];\n"+
"                color=royalblue1\n"+
"                style=\"filled,rounded\" label = \"VPN GW: "+ vpnGw.tagsName+"\"\n"+
"                \""+vpnGw.id+"\" [label = \"{tfName: "+ idVpnGw+"|id: "+idVpnGw+"|amazon side ASN: "+vpnGw.amazon_side_asn+"}\" shape = \"record\" ];\n"+
"            }\n");
			}
//Vpn Gw - end
		
//Vpc Endpoints			
			HashMap<String, VpcEndpointAttributes> vpcEndpoints = model.findVpcEndpointAttributesInVpc(idVpc);
			for (String idVpcEndpoint : vpcEndpoints.keySet()) {
				VpcEndpointAttributes vpcEndpoint = vpcEndpoints.get(idVpcEndpoint);
				allDisplayedVpcEndpoints.put(idVpcEndpoint, vpcEndpoint);
				diagram.append(
"            subgraph cluster_"+(c++)+" {\n"+
"                \"icon"+vpcEndpoint.id+"\" [label=\"VPC Endpoint\" shape=cds]\n"+
"                node [style=filled];\n"+
"                color=orchid1\n"+
"                style=\"filled,rounded\" label = \"VPC Endpoint: "+ vpcEndpoint.vpc_endpoint_type+"\"\n"+
"                \""+vpcEndpoint.id+"\" [label = \"{tfName: "+ idVpcEndpoint+"|id: "+idVpcEndpoint+"|service_name: "+vpcEndpoint.service_name+"}\" shape = \"record\" ];\n"+
"            }\n");
			}
//Vpc Endpoints	- end

//Gateways - end		
			diagram.append(
"        }\n");

//Zones
			for (String zone : zones) {
				allDisplayedZones.add(zone);
				diagram.append(
"        subgraph cluster_"+(c++)+" {\n"+
"            node [style=filled];\n"+
"            color=turquoise\n"+
"            label = \"Availability zone: "+ zone +"\"\n");
		
//Subnets		
				HashMap<String, SubnetAttributes> subnets = model.findSubnetsAttributesInVpcInZone(idVpc, zone);
				for (String idSubnet : subnets.keySet()) {
					SubnetAttributes subnet = subnets.get(idSubnet);
					diagram.append(
"            subgraph cluster_"+(c++)+" {\n"+
"                node [style=filled];\n"+
"                color=green\n"+
"                label = \"Subnet: "+ subnet.tagsName+"\"\n"+
"                \""+subnet.id+"\" [label = \"{tfName: "+ idSubnet+"|id: "+idSubnet+"|cidr_block: "+subnet.cidr_block+"}\" shape = \"record\" ];\n");

//Instances
					HashMap<String, InstanceAttributes> instances = model.findInstancesInSubnet(subnet.id);
					for (String idInstance : instances.keySet()) {
						InstanceAttributes instance = instances.get(idInstance);
						diagram.append(
"                subgraph cluster_"+(c++)+" {\n"+
"                    \"icon-"+instance.id+"\" [label=EC2 shape=rpromoter]\n"+
"                    node [style=filled];\n"+
"                    color=blue\n"+
"                    label = \"EC2: "+ subnet.tagsName+"\"\n"+
"                    \""+instance.id+"\" [label = \"{tfName: "+ idInstance+"|id: "+idInstance+"|public IP: "+instance.public_ip+"|private IP: "+instance.private_ip+"}\" shape = \"record\" ];\n");
//Instances - end
						diagram.append(					
"                }\n");
					}
//Nat - end
				
//Nat Gw
					HashMap<String, NatGatewayAttributes> natGws = model.findNatGatewaysAttributesInSubnet(subnet.id);
					for (String idNatGw : natGws.keySet()) {
						NatGatewayAttributes natGw = natGws.get(idNatGw);
						diagram.append(
"                subgraph cluster_"+(c++)+" {\n"+
"                    \"icon-"+natGw.id+"\" [label=NatGW shape=rpromoter]\n"+
"                    node [style=filled];\n"+
"                    color=salmon\n"+
"                    label = \"Nat GW: "+ subnet.tagsName+"\"\n"+
"                    \""+natGw.id+"\" [label = \"{tfName: "+ idNatGw+"|id: "+idNatGw+"|public IP: "+natGw.public_ip+"|private IP: "+natGw.private_ip+"}\" shape = \"record\" ];\n");

//Eip
						EipAttributes eipAttributes = model.findEipAttributes(natGw.allocation_id);
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
			HashMap<String, RouteTableAttributes> routeTables = model.findRouteTablesAttributesInVpc(idVpc);
			if (showRouteTables){
				for (String idRouteTable : routeTables.keySet()) {
					RouteTableAttributes routeTable = routeTables.get(idRouteTable);
					diagram.append(
"        subgraph cluster_"+(c++)+" {\n"+
"            node [style=filled];\n"+
"            color=mistyrose\n"+
"            label = \"Route Table: "+ routeTable.tagsName+"\"\n"+
"            \""+routeTable.id+"\" [label = \"{tfName: "+ idRouteTable+"|id: "+idRouteTable+"}\" shape = \"record\" ];\n"+
"        }\n");
				}
			}	
//Route tables - end		

//TODO Separate RTA by VPC
//Route table associations
			HashMap<String, RouteTableAssociationAttributes> routeTableAssociations = model.routeTableAssociations;
			for (String idRouteTableAssociation : routeTableAssociations.keySet()) {
				RouteTableAssociationAttributes routeTableAssociation = routeTableAssociations.get(idRouteTableAssociation);
				SubnetAttributes subnet = model.findSubnetAttributes(routeTableAssociation.subnet_id);
				RouteTableAttributes routeTable = model.findRouteTableAttributes(routeTableAssociation.route_table_id);
				
				if (subnet!=null && routeTable!=null && zones.contains(subnet.availability_zone)){
					if (showRouteTables){
						diagram.append("        \""+subnet.id+"\" -> \""+ routeTable.id +"\" [label = \""+idRouteTableAssociation+"\" dir=none, style=dashed]\n");
					}
//Routes - find all routes belonging to the route table and connect it to the gateway
					HashMap<String, RouteAttributes> matchingRoutes = model.findRoutesAttributesInTable(routeTable.id);
					for (String idRoute : matchingRoutes.keySet()) {
						String gatewayId = matchingRoutes.get(idRoute).gateway_id;		
						String natGatewayId = matchingRoutes.get(idRoute).nat_gateway_id;
						if (!"".equals(gatewayId) && gatewayId!=null){
							diagram.append("        \""+subnet.id+"\" -> \""+ gatewayId +"\" [label = \""+idRouteTableAssociation+"\" dir=both]\n");						
						} else if (!"".equals(natGatewayId) && natGatewayId!=null){
							diagram.append("        \""+subnet.id+"\" -> \""+ natGatewayId +"\" [label = \""+idRouteTableAssociation+"\" ]\n");						
						}
					}
				
//Routes -end
				}
			}
				
//Route table associations - end
			
//Vpc - end
			diagram.append(				
"    }\n");	
		}
			
//External
		diagram.append(
"    subgraph cluster_"+(c++)+" {\n"+
"        style=\"invisible\";\n"+
"        label=External\n");
			
//Internet
		for (String idIgw : allDisplayedIgws.keySet()) {
//			InternetGatewayAttributes igw = model.internetGateways.get(idIgw);
			diagram.append(
"        \""+idIgw+"\" -> Internet [dir=both]\n");
		}
		HashMap<String, NatGatewayAttributes> displayedNatGws = model.findNatGatewaysAttributesInZones(allDisplayedZones);
		for (String idNatGw : displayedNatGws.keySet()) {
//			NatGatewayAttributes natGw = displayedNatGws.get(idNatGw);
			diagram.append(
"        \""+idNatGw+"\" -> Internet\n");
		}
//Internet - end		

//External Data Centre
		for (String idVpnGw : allDisplayedVpnGws.keySet()) {
//			VpnGatewayAttributes vpnGw = model.vpnGateways.get(idVpnGw);
			diagram.append(
"        \""+idVpnGw+"\" -> \"External Data Centre\" [dir=both]\n");
		}		
//External Data Centre - end
		
//Endpoints
		for (String idVpcEndpoint : allDisplayedVpcEndpoints.keySet()) {
			VpcEndpointAttributes vpcEndpoint = model.vpcEndpoints.get(idVpcEndpoint);
			diagram.append(
"        \""+idVpcEndpoint+"\" -> \"AWS "+vpcEndpoint.service_name+"\" \n");
		}		
//Endpoints - end

//External - end		
			diagram.append(
"    }\n");


		
//Diagram- end
		diagram.append(
"}");		
		return diagram.toString();
	}

}
