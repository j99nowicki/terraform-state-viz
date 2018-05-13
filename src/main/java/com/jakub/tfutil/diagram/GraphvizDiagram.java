package com.jakub.tfutil.diagram;

import java.util.HashMap;
import java.util.HashSet;

import com.jakub.tfutil.Model;
import com.jakub.tfutil.aws.data.DataSubnetIds;
import com.jakub.tfutil.aws.data.DataVpc;
import com.jakub.tfutil.aws.resources.ResourceEip;
import com.jakub.tfutil.aws.resources.ResourceInstance;
import com.jakub.tfutil.aws.resources.ResourceInternetGateway;
import com.jakub.tfutil.aws.resources.ResourceNatGateway;
import com.jakub.tfutil.aws.resources.ResourceRoute;
import com.jakub.tfutil.aws.resources.ResourceRouteTableAssociation;
import com.jakub.tfutil.aws.resources.ResourceRouteTable;
import com.jakub.tfutil.aws.resources.ResourceSubnet;
import com.jakub.tfutil.aws.resources.ResourceVpc;
import com.jakub.tfutil.aws.resources.ResourceVpcEndpoint;
import com.jakub.tfutil.aws.resources.ResourceVpnGateway;

public class GraphvizDiagram{
		
	public String draw(Model model){
		boolean showRouteTables = true;
		
		StringBuffer diagram = new StringBuffer();
		int c=0;
		HashMap<String, ResourceInternetGateway> allDisplayedIgws = new HashMap<String, ResourceInternetGateway>();
		HashMap<String, ResourceVpnGateway> allDisplayedVpnGws = new HashMap<String, ResourceVpnGateway>();
		HashMap<String, ResourceVpcEndpoint> allDisplayedVpcEndpoints = new HashMap<String, ResourceVpcEndpoint>();
		HashMap<String, ResourceNatGateway> displayedNatGws = new HashMap<String, ResourceNatGateway>();
		HashSet<String> allDisplayedZones = new HashSet<String>();

//Diagram
		diagram.append(
"digraph G {\n"+
"    rankdir=TB;\n");
		
//RVpc
		HashMap<String, ResourceVpc> rVpcs = model.rVpcs;

		for (String idRVpc : rVpcs.keySet()) {
			ResourceVpc rVpc = rVpcs.get(idRVpc);
			System.out.println("rVpc: " + idRVpc);

			HashSet<String> zones = model.findAvailabilityZonesInVpc(idRVpc);
//			zones.clear();
//			zones.add("eu-west-1c");
			
			diagram.append(				
"    subgraph cluster_"+(c++)+" {\n"+
"        style=\"filled,rounded\";\n"+
"        color=lightgrey;\n"+
"        node [style=filled,color=white, shape=box];\n"+
//"           a0 -> a1 -> a2 -> a3;\n"+
"        label = \"VPC: "+ rVpc.tagsName+" ("+ rVpc.id+")\";\n"+
"        \""+rVpc.id+"\" [label = \"{tfName: "+ rVpc.tfName +"|id: "+idRVpc+"| cidr_block: "+rVpc.cidr_block+"}\" shape = \"record\" ];\n");

//Gateways		
			diagram.append(
"        subgraph cluster_"+(c++)+" {\n"+
"            style=invisible\n"+
"            label=Gateways\n");
		
//Igw
			HashMap<String, ResourceInternetGateway> igws = model.findInternetGatewaysAttributesInVpc(idRVpc);
			for (String idIgw : igws.keySet()) {
				ResourceInternetGateway igw = igws.get(idIgw);
				allDisplayedIgws.put(idIgw, igw);
				diagram.append(
"            subgraph cluster_"+(c++)+" {\n"+
"                \"icon"+igw.id+"\" [label=\"Internet GW\" shape=lpromoter]\n"+
"                node [style=filled];\n"+
"                color=red;\n"+
"                style=\"filled,rounded\" label = \"Internet GW: "+ igw.tagsName+"\"\n"+
"                \""+igw.id+"\" [label = \"{tfName: "+ igw.tfName+"|id: "+idIgw +"}\" shape = \"record\" ];\n"+
"            }\n");
			}
//Igw - end

//Vpn Gw
			HashMap<String, ResourceVpnGateway> vpnGws = model.findVpnGatewaysAttributesInVpc(idRVpc);
			for (String idVpnGw : vpnGws.keySet()) {
				ResourceVpnGateway vpnGw = vpnGws.get(idVpnGw);
				allDisplayedVpnGws.put(idVpnGw, vpnGw);
				diagram.append(
"            subgraph cluster_"+(c++)+" {\n"+
"                \"icon"+vpnGw.id+"\" [label=\"VPN GW\" shape=cds]\n"+
"                node [style=filled];\n"+
"                color=royalblue1\n"+
"                style=\"filled,rounded\" label = \"VPN GW: "+ vpnGw.tagsName+"\"\n"+
"                \""+vpnGw.id+"\" [label = \"{tfName: "+ vpnGw.tfName+"|id: "+idVpnGw+"|amazon side ASN: "+vpnGw.amazon_side_asn+"}\" shape = \"record\" ];\n"+
"            }\n");
			}
//Vpn Gw - end
		
//Vpc Endpoints			
			HashMap<String, ResourceVpcEndpoint> vpcEndpoints = model.findVpcEndpointAttributesInVpc(idRVpc);
			for (String idVpcEndpoint : vpcEndpoints.keySet()) {
				ResourceVpcEndpoint vpcEndpoint = vpcEndpoints.get(idVpcEndpoint);
				allDisplayedVpcEndpoints.put(idVpcEndpoint, vpcEndpoint);
				diagram.append(
"            subgraph cluster_"+(c++)+" {\n"+
"                \"icon"+vpcEndpoint.id+"\" [label=\"VPC Endpoint\" shape=cds]\n"+
"                node [style=filled];\n"+
"                color=orchid1\n"+
"                style=\"filled,rounded\" label = \"VPC Endpoint: "+ vpcEndpoint.vpc_endpoint_type+"\"\n"+
"                \""+vpcEndpoint.id+"\" [label = \"{tfName: "+ vpcEndpoint.tfName+"|id: "+idVpcEndpoint+"|service_name: "+vpcEndpoint.service_name+"}\" shape = \"record\" ];\n"+
"            }\n");
			}
//Vpc Endpoints	- end

//Gateways - end		
			diagram.append(
"        }\n");

//Zones
			for (String zone : zones) {
				allDisplayedZones.add(zone);
				System.out.println("vpc: " + idRVpc +" zone: " + zone);
				diagram.append(
"        subgraph cluster_"+(c++)+" {\n"+
"            node [style=filled];\n"+
"            color=turquoise\n"+
"            label = \"Availability zone: "+ zone +"\"\n");
		
//Subnets		
				HashMap<String, ResourceSubnet> subnets = model.findSubnetsAttributesInVpcInZone(idRVpc, zone);
				for (String idSubnet : subnets.keySet()) {
					ResourceSubnet subnet = subnets.get(idSubnet);
					diagram.append(
"            subgraph cluster_"+(c++)+" {\n"+
"                node [style=filled];\n"+
"                color=green\n"+
"                label = \"Subnet: "+ subnet.tagsName+"\"\n"+
"                \""+subnet.id+"\" [label = \"{tfName: "+ subnet.tfName+"|id: "+idSubnet+"|cidr_block: "+subnet.cidr_block+"}\" shape = \"record\" ];\n");

//Instances
					HashMap<String, ResourceInstance> instances = model.findInstancesInSubnet(subnet.id);
//					System.out.println("vpc: " + idVpc +" zone: " + zone + " subnet: " + idSubnet + " instances: " + instances.size() );

					for (String idInstance : instances.keySet()) {
						ResourceInstance instance = instances.get(idInstance);
//						System.out.println("vpc: " + idVpc +" zone: " + zone + " subnet: " + idSubnet + " instance: " +idInstance );

						diagram.append(
"                subgraph cluster_"+(c++)+" {\n"+
"                    \"icon-"+instance.id+"\" [label=EC2 shape=rpromoter]\n"+
"                    node [style=filled];\n"+
"                    color=blue\n"+
"                    label = \"EC2: "+ subnet.tagsName+"\"\n"+
"                    \""+instance.id+"\" [label = \"{tfName: "+ instance.tfName+"|id: "+idInstance+"|public IP: "+instance.public_ip+"|private IP: "+instance.private_ip+"}\" shape = \"record\" ];\n");
//Instances - end
						diagram.append(					
"                }\n");
					}
//Nat - end
				
//Nat Gw
					HashMap<String, ResourceNatGateway> natGws = model.findNatGatewaysAttributesInSubnet(subnet.id);
					for (String idNatGw : natGws.keySet()) {
						ResourceNatGateway natGw = natGws.get(idNatGw);
						displayedNatGws.put(idNatGw, natGw);
						diagram.append(
"                subgraph cluster_"+(c++)+" {\n"+
"                    \"icon-"+natGw.id+"\" [label=NatGW shape=rpromoter]\n"+
"                    node [style=filled];\n"+
"                    color=salmon\n"+
"                    label = \"Nat GW: "+ subnet.tagsName+"\"\n"+
"                    \""+natGw.id+"\" [label = \"{tfName: "+ natGw.tfName+"|id: "+idNatGw+"|public IP: "+natGw.public_ip+"|private IP: "+natGw.private_ip+"}\" shape = \"record\" ];\n");

//Eip
						ResourceEip eipAttributes = model.findEipAttributes(natGw.allocation_id);
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
			if (showRouteTables){

				HashMap<String, ResourceRouteTable> routeTables = model.findRouteTablesAttributesInVpc(idRVpc);
				for (String idRouteTable : routeTables.keySet()) {
					ResourceRouteTable routeTable = routeTables.get(idRouteTable);
					diagram.append(
"        subgraph cluster_"+(c++)+" {\n"+
"            node [style=filled];\n"+
"            color=mistyrose\n"+
"            label = \"Route Table: "+ routeTable.tagsName+"\"\n"+
"            \""+idRouteTable+"\" [label = \"{tfName: "+ routeTable.tfName+"|id: "+idRouteTable+"}\" shape = \"record\" ];\n"+
"        }\n");

//Route table associations - find all associations for given table
					HashMap<String, ResourceRouteTableAssociation> routeTableAssociations = model.findRouteTablesAssociationAttributesForRouteTables(idRouteTable);
					
					for (String idRouteTableAssociation : routeTableAssociations.keySet()) {
						ResourceRouteTableAssociation routeTableAssociation = routeTableAssociations.get(idRouteTableAssociation);
						ResourceSubnet subnet = model.findSubnetAttributes(routeTableAssociation.subnet_id);
						
						if (subnet!=null && routeTable!=null && zones.contains(subnet.availability_zone)){
							if (showRouteTables){
								diagram.append("        \""+subnet.id+"\" -> \""+ routeTable.id +"\" [label = \""+idRouteTableAssociation+"\" dir=none, style=dashed]\n");
							}
//Routes - find all routes belonging to the route table and connect it to the gateway
							HashMap<String, ResourceRoute> matchingRoutes = model.findRoutesAttributesInTable(routeTable.id);
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
//Route tables - end		
				}
			}	
				
//Route table associations - end
			
//RVpc - end
			diagram.append(				
"    }\n");
		}
		
//DVpc
		HashMap<String, DataVpc> dVpcs = model.dVpcs;
		for (String idDVpc : dVpcs.keySet()) {
			DataVpc dVpc = dVpcs.get(idDVpc);
			System.out.println("dVpc: " + idDVpc);
			diagram.append(				
"    subgraph cluster_"+(c++)+" {\n"+
"        style=\"dashed, rounded\";\n"+
"        color=lightgrey;\n"+
"        node [style=dashed,color=white, shape=box];\n"+
//"           a0 -> a1 -> a2 -> a3;\n"+
"        label = \"VPC: "+ dVpc.tagsName+" ("+ idDVpc+")\";\n"+
"        \""+idDVpc+"\" [label = \"{tfName: "+ dVpc.tfName +"|id: "+idDVpc+"| cidr_block: "+dVpc.cidr_block+"}\" shape = \"record\" ];\n");

//Subnet IDs
			HashMap<String, DataSubnetIds> dSubnetIdss = model.findDSubnetIdssAttributesInDVpc(idDVpc);
			for (String idDSubnetIds : dSubnetIdss.keySet()) {
				DataSubnetIds dSubnetIds = dSubnetIdss.get(idDSubnetIds);
				for (String dSubnetId : dSubnetIds.ids) {
					System.out.println("dVpc: " + idDVpc + " subnetId: " + dSubnetId);
					diagram.append(
	"        subgraph cluster_"+(c++)+" {\n"+
	"            node [style=dashed];\n"+
	"            style=\"dashed, rounded\";\n"+
	"            color=green\n"+
	"            label = \"DSubnetId: "+ dSubnetId+"\"\n"+
	"            \""+dSubnetId+"\" [label = \"{id: "+dSubnetId+"}\" shape = \"record\" ];\n"+
	"            }\n");
				}
			}
				
	//Subnet IDs - end
			


//DVpc - end
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
//		HashMap<String, NatGatewayAttributes> displayedNatGws = model.findNatGatewaysAttributesInZones(allDisplayedZones);
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
			ResourceVpcEndpoint vpcEndpoint = model.rVpcEndpoints.get(idVpcEndpoint);
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
