package com.jakub.tfutil.diagram;

import java.util.HashMap;
import java.util.HashSet;

import com.jakub.tfutil.TfObjectsWarehouse;
import com.jakub.tfutil.aws.data.DataSubnetIds;
import com.jakub.tfutil.aws.data.DataVpc;
import com.jakub.tfutil.aws.objects.InternetGateway;
import com.jakub.tfutil.aws.objects.Model;
import com.jakub.tfutil.aws.objects.NatGateway;
import com.jakub.tfutil.aws.objects.Vpc;
import com.jakub.tfutil.aws.objects.VpcEndpoint;
import com.jakub.tfutil.aws.objects.VpnGateway;
import com.jakub.tfutil.aws.resources.ResourceInstance;
import com.jakub.tfutil.aws.resources.ResourceRoute;
import com.jakub.tfutil.aws.resources.ResourceRouteTableAssociation;
import com.jakub.tfutil.aws.resources.ResourceRouteTable;
import com.jakub.tfutil.aws.resources.ResourceSubnet;
import com.jakub.tfutil.aws.resources.ResourceVpcEndpoint;

public class GraphvizDiagram{
	private boolean showRouteTables = true;
	
	private StringBuffer diagram = new StringBuffer();
	static int c=0;
	private HashMap<String, InternetGateway> allDisplayedIgws = new HashMap<String, InternetGateway>();
	private HashMap<String, VpnGateway> allDisplayedVpnGws = new HashMap<String, VpnGateway>();
	private HashMap<String, VpcEndpoint> allDisplayedVpcEndpoints = new HashMap<String, VpcEndpoint>();
	private HashMap<String, NatGateway> displayedNatGws = new HashMap<String, NatGateway>();
	private HashSet<String> allDisplayedZones = new HashSet<String>();
	private TfObjectsWarehouse tfObjectsWarehouse = null;
	
	private Model model;
	
	public GraphvizDiagram(TfObjectsWarehouse tfObjectsWarehouse) {
		this.tfObjectsWarehouse = tfObjectsWarehouse;
		this.model = new Model(tfObjectsWarehouse);
	}

	public String draw(){
		
//Diagram
		diagram.append(
"digraph G {\n"+
"    rankdir=TB;\n");
		
//RVpc
		HashMap<String, Vpc> vpcs = model.vpcs;

		for (String idVpc : vpcs.keySet()) {
			Vpc vpc = vpcs.get(idVpc);

			HashSet<String> visibleRZones = tfObjectsWarehouse.findAvailabilityZonesInRVpc(idVpc);
//			zones.clear();
//			zones.add("eu-west-1c");
			
			diagram.append(				
"    subgraph cluster_"+(c++)+" {\n"+
"        style=\"filled,rounded\";\n"+
"        color=lightgrey;\n"+
"        node [style=filled,color=white, shape=box];\n"+
"        label = \"VPC: "+ vpc.tagsName+" ("+ vpc.id+")\";\n"+
"        \""+vpc.id+"\" [label = \"{tfName: "+ vpc.tfName +"|id: "+idVpc+"| cidr_block: "+vpc.cidr_block+"}\" shape = \"record\" ];\n");

//Gateways		
			printGateways(idVpc);

//Zones
			for (String zone : visibleRZones) {
				allDisplayedZones.add(zone);
				diagram.append(
"        subgraph cluster_"+(c++)+" {\n"+
"            node [style=filled];\n"+
"            color=turquoise\n"+
"            label = \"Availability zone: "+ zone +"\"\n");
		
//Subnets		
				HashMap<String, ResourceSubnet> subnets = tfObjectsWarehouse.findSubnetsAttributesInVpcInZone(idVpc, zone);
				for (String idSubnet : subnets.keySet()) {
					ResourceSubnet subnet = subnets.get(idSubnet);
					diagram.append(
"            subgraph cluster_"+(c++)+" {\n"+
"                node [style=filled];\n"+
"                color=green\n"+
"                label = \"Subnet: "+ subnet.tagsName+"\"\n"+
"                \""+subnet.id+"\" [label = \"{tfName: "+ subnet.tfName+"|id: "+idSubnet+"|cidr_block: "+subnet.cidr_block+"}\" shape = \"record\" ];\n");

//Instances
					printRInstances(subnet);	
//Nat Gw
					GraphvizNatGateway.printNatGateways(diagram, model, subnet, displayedNatGws);
//Subnets - end
					diagram.append(						
"            }\n");
				}
//Zones - end
				diagram.append(
"        }\n");
			}

//Route tables
			printRRouteTables(idVpc, visibleRZones);
			
//RVpc - end
			diagram.append(				
"    }\n");
		}
		
//DVpc
		HashMap<String, DataVpc> dVpcs = tfObjectsWarehouse.dVpcs;
		for (String idDVpc : dVpcs.keySet()) {
			DataVpc dVpc = dVpcs.get(idDVpc);
			diagram.append(				
"    subgraph cluster_"+(c++)+" {\n"+
"        style=\"dashed, rounded\";\n"+
"        color=lightgrey;\n"+
"        node [style=dashed,color=white, shape=box];\n"+
"        label = \"VPC: "+ dVpc.tagsName+" ("+ idDVpc+")\";\n"+
"        \""+idDVpc+"\" [label = \"{tfName: "+ dVpc.tfName +"|id: "+idDVpc+"| cidr_block: "+dVpc.cidr_block+"}\" shape = \"record\" ];\n");

//D Subnet IDs
			HashMap<String, DataSubnetIds> dSubnetIdss = tfObjectsWarehouse.findDSubnetIdssAttributesInDVpc(idDVpc);
			for (String idDSubnetIds : dSubnetIdss.keySet()) {
				DataSubnetIds dSubnetIds = dSubnetIdss.get(idDSubnetIds);
				HashSet<String> visibleDZones = tfObjectsWarehouse.findAvailabilityZonesForInstancesInDVpc(dSubnetIds);
//				visibleDZones.clear();
//				visibleDZones.add("eu-west-1c");

//D Zones
				for (String zone : visibleDZones) {
					allDisplayedZones.add(zone);
					diagram.append(
"        subgraph cluster_"+(c++)+" {\n"+
"            node [style=\"dashed,filled\"];\n"+
"            color=turquoise\n"+
"            label = \"Availability zone: "+ zone +"\"\n");

				
//D Subnets
				
					for (String idDSubnet : dSubnetIds.ids) {
						diagram.append(
"        subgraph cluster_"+(c++)+" {\n"+
"            node [style=dashed];\n"+
"            style=\"dashed, rounded\";\n"+
"            color=green\n"+
"            label = \"DSubnetId: "+ idDSubnet+"\"\n"+
"            \""+idDSubnet+"\" [label = \"{id: "+idDSubnet+"}\" shape = \"record\" ];\n");
//D Instances 
						HashMap<String, ResourceInstance> instances = tfObjectsWarehouse.findInstancesInSubnet(idDSubnet);
						for (String idInstance : instances.keySet()) {
							ResourceInstance instance = instances.get(idInstance);
							diagram.append(
"                subgraph cluster_"+(c++)+" {\n"+
"                    \"icon-"+idInstance+"\" [label=EC2 shape=rpromoter style=filled]\n"+
"                    node [style=filled];\n"+
"                    style=\"filled, rounded\";\n"+
"                    color=blue\n"+
"                    label = \"EC2: "+ instance.tagsName+"\"\n"+
"                    \""+idInstance+"\" [label = \"{tfName: "+ instance.tfName+"|id: "+idInstance+"|public IP: "+instance.public_ip+"|private IP: "+instance.private_ip+"}\" shape = \"record\" ];\n");
							diagram.append(					
"                }\n");
						}
//D Instances - end

//D Subnets - end
						diagram.append(
"            }\n");
					}
					
//Zones - end
					diagram.append(
"        }\n");
				}	
				
//D Subnet IDs - end
			}


//DVpc - end
			diagram.append(				
"    }\n");
		}			
			
		printRExternals();
		
//Diagram- end
		diagram.append(
"}");		
		return diagram.toString();
	}
	
	
//Helpers

	
	
	private void printRExternals() {
//External
		diagram.append(
"    subgraph cluster_"+(c++)+" {\n"+
"        style=\"invisible\";\n"+
"        label=External\n");
			
//Internet
		for (String idIgw : allDisplayedIgws.keySet()) {
			diagram.append(
"        \""+idIgw+"\" -> Internet [dir=both]\n");
		}
		for (String idNatGw : displayedNatGws.keySet()) {
//			NatGatewayAttributes natGw = displayedNatGws.get(idNatGw);
			diagram.append(
"        \""+idNatGw+"\" -> Internet\n");
		}
//Internet - end		

//External Data Centre
		for (String idVpnGw : allDisplayedVpnGws.keySet()) {
			diagram.append(
"        \""+idVpnGw+"\" -> \"External Data Centre\" [dir=both]\n");
		}		
//External Data Centre - end
		
//Endpoints
		for (String idVpcEndpoint : allDisplayedVpcEndpoints.keySet()) {
			ResourceVpcEndpoint vpcEndpoint = tfObjectsWarehouse.rVpcEndpoints.get(idVpcEndpoint);
			diagram.append(
"        \""+idVpcEndpoint+"\" -> \"AWS "+vpcEndpoint.service_name+"\" \n");
		}		
//Endpoints - end

//External - end		
		diagram.append(
"    }\n");
	}

	private void printRRouteTables(String idRVpc, HashSet<String> visibleZones) {
		if (showRouteTables){

			HashMap<String, ResourceRouteTable> routeTables = tfObjectsWarehouse.findRouteTablesAttributesInVpc(idRVpc);
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
				HashMap<String, ResourceRouteTableAssociation> routeTableAssociations = tfObjectsWarehouse.findRouteTablesAssociationAttributesForRouteTables(idRouteTable);
				
				for (String idRouteTableAssociation : routeTableAssociations.keySet()) {
					ResourceRouteTableAssociation routeTableAssociation = routeTableAssociations.get(idRouteTableAssociation);
					ResourceSubnet subnet = tfObjectsWarehouse.findSubnetAttributes(routeTableAssociation.subnet_id);
					
					if (subnet!=null && routeTable!=null && visibleZones.contains(subnet.availability_zone)){
						if (showRouteTables){
							diagram.append("        \""+subnet.id+"\" -> \""+ routeTable.id +"\" [label = \""+idRouteTableAssociation+"\" dir=none, style=dashed]\n");
						}
//Routes - find all routes belonging to the route table and connect it to the gateway
						HashMap<String, ResourceRoute> matchingRoutes = tfObjectsWarehouse.findRoutesAttributesInTable(routeTable.id);
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
	}

	private void printRInstances(ResourceSubnet subnet) {
		HashMap<String, ResourceInstance> instances = tfObjectsWarehouse.findInstancesInSubnet(subnet.id);
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
			diagram.append(					
"                }\n");
		}
	}

		
	private void printGateways(String idVpc) {
		diagram.append(
"        subgraph cluster_"+(c++)+" {\n"+
"            style=invisible\n"+
"            label=Gateways\n");
		GraphvizInternetGateway.printInternetGateways(diagram, model, idVpc, allDisplayedIgws);
		GraphvizVpnGateway.printVpnGateways(diagram, model, idVpc, allDisplayedVpnGws);
		GraphvizVpcEndpoint.printVpcEndpoints(diagram, model, idVpc, allDisplayedVpcEndpoints);
		diagram.append(
"        }\n");
	}

}
