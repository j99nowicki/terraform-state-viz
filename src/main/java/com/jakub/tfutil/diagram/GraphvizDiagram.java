package com.jakub.tfutil.diagram;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import com.jakub.tfutil.TfObjectsWarehouse;
import com.jakub.tfutil.aws.objects.InternetGateway;
import com.jakub.tfutil.aws.objects.Model;
import com.jakub.tfutil.aws.objects.NatGateway;
import com.jakub.tfutil.aws.objects.Vpc;
import com.jakub.tfutil.aws.objects.VpcEndpoint;
import com.jakub.tfutil.aws.objects.VpnGateway;
import com.jakub.tfutil.aws.resources.ResourceRoute;
import com.jakub.tfutil.aws.resources.ResourceRouteTableAssociation;
import com.jakub.tfutil.aws.resources.ResourceRouteTable;
import com.jakub.tfutil.aws.resources.ResourceSubnet;
import com.jakub.tfutil.aws.resources.ResourceVpcEndpoint;

public class GraphvizDiagram{
	private boolean showRouteTables = true;
	
	private StringBuffer diagram = new StringBuffer();
	static int c=0;
	public static HashMap<String, InternetGateway> allDisplayedIgws = new HashMap<String, InternetGateway>();
	public static HashMap<String, VpnGateway> allDisplayedVpnGws = new HashMap<String, VpnGateway>();
	public static HashMap<String, VpcEndpoint> allDisplayedVpcEndpoints = new HashMap<String, VpcEndpoint>();
	public static HashMap<String, NatGateway> displayedNatGws = new HashMap<String, NatGateway>();
	public static HashSet<String> allDisplayedZones = new HashSet<String>();
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
		
//Vpc
		HashMap<String, Vpc> vpcs = model.vpcs;

		for (String idVpc : vpcs.keySet()) {
			Vpc vpc = vpcs.get(idVpc);

			Set<String> visibleZones = vpc.getAvailabilityZones();
//			zones.clear();
//			zones.add("eu-west-1c");
			String style = (vpc.isResource()?"filled":"dashed");

			diagram.append(				
"    subgraph cluster_"+(c++)+" {\n"+
"        style=\""+style+",rounded\";\n"+
"        color=lightgrey;\n"+
"        node [style=filled,color=white, shape=box];\n"+
"        label = \"VPC: "+ vpc.tagsName+" ("+ vpc.id+")\";\n"+
"        \""+vpc.id+"\" [label = \"{tfName: "+ vpc.tfName +"|id: "+idVpc+"| cidr_block: "+vpc.cidr_block+"}\" shape = \"record\" ];\n");

//Gateways		
			printGateways(idVpc);

//Zones
			for (String zone : visibleZones) {
				allDisplayedZones.add(zone);
				GraphvizAvailiabilityZone.printAvailiabilityZone(diagram, model, idVpc, zone);
			}

//Route tables
			printRRouteTables(idVpc, visibleZones);
			
//Vpc - end
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

	private void printRRouteTables(String idRVpc, Set<String> visibleZones) {
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

	
	
	private void printGateways(String idVpc) {
		diagram.append(
"        subgraph cluster_"+(c++)+" {\n"+
"            style=invisible\n"+
"            label=Gateways\n");
		GraphvizInternetGateway.printInternetGateways(diagram, model, idVpc);
		GraphvizVpnGateway.printVpnGateways(diagram, model, idVpc);
		GraphvizVpcEndpoint.printVpcEndpoints(diagram, model, idVpc);
		diagram.append(
"        }\n");
	}

}
