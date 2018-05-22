package com.jakub.tfutil.diagram;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import com.jakub.tfutil.TfObjectsWarehouse;
import com.jakub.tfutil.aws.objects.InternetGateway;
import com.jakub.tfutil.aws.objects.Model;
import com.jakub.tfutil.aws.objects.NatGateway;
import com.jakub.tfutil.aws.objects.SecurityGroup;
import com.jakub.tfutil.aws.objects.Vpc;
import com.jakub.tfutil.aws.objects.VpcEndpoint;
import com.jakub.tfutil.aws.objects.VpnGateway;

public class GraphvizDiagram{
	private boolean showRouteTables = true;
	private boolean showExternals = true;
	static boolean showRouteTablesAssociationsToSubnets = true;
	static boolean showRouteTablesAssociationsToVpcEndpoints = true;
	static boolean showInternalRoutesToGateways = true;
	static boolean showVpcDhcpOptions = true;
	private static boolean showElasticacheSubnetGroup = true;
	private static boolean showRedshiftSubnetGroup = true;
	static boolean showElasticacheSubnetGroupAssociationsToSubnets = true;
	static boolean showRedshiftSubnetGroupAssociationsToSubnets = true;
	private static boolean showSecurityGroups = true;
	static boolean showSecurityGroupAssociationsToinstances = true;
	
	private StringBuffer diagram = new StringBuffer();
	static int c=0;
	static HashMap<String, InternetGateway> allDisplayedIgws = new HashMap<String, InternetGateway>();
	static HashMap<String, VpnGateway> allDisplayedVpnGws = new HashMap<String, VpnGateway>();
	static HashMap<String, VpcEndpoint> allDisplayedVpcEndpoints = new HashMap<String, VpcEndpoint>();
	static HashMap<String, NatGateway> displayedNatGws = new HashMap<String, NatGateway>();
	static HashMap<String, SecurityGroup> allDisplayedSecurityGroups = new HashMap<String, SecurityGroup>();
	
	private Model model;
	
	public GraphvizDiagram(TfObjectsWarehouse tfObjectsWarehouse) {
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
		    HashSet<String> allDisplayedZonesInVpc = new HashSet<String>();
			Set<String> showZonesInVpc = vpc.getAvailabilityZones();
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
			
			
//Security Groups
			if (showSecurityGroups){
				GraphvizSecurityGroup.printSecurityGroups(diagram, model, idVpc);
			}
//Gateways		
			printGateways(idVpc);

//Zones
			for (String zone : showZonesInVpc) {
				allDisplayedZonesInVpc.add(zone);
				GraphvizAvailiabilityZone.printAvailiabilityZone(diagram, model, idVpc, zone);
			}
			
//Route tables
			if (showRouteTables){
				GraphvizRouteTable.printRouteTables(diagram, model, idVpc, allDisplayedZonesInVpc);
			}
			
			if (showVpcDhcpOptions){
				GraphvizVpcDhcpOptions.printVpcDhcpOptions(diagram, model, idVpc);
			}

//Subnet groups
			if (showElasticacheSubnetGroup){
				GraphvizElasticacheSubnetGroup.printElasticacheSubnetGroup(diagram, model, idVpc, allDisplayedZonesInVpc);
			}			
			if (showRedshiftSubnetGroup){
				GraphvizRedshiftSubnetGroup.printRedshiftSubnetGroup(diagram, model, idVpc, allDisplayedZonesInVpc);
			}				
//Vpc - end
			diagram.append(				
"    }\n");
		}
				
		if (showExternals){
			printExternals();
		}
//Diagram- end
		diagram.append(
"}");		
		return diagram.toString();
	}
	
	
	
	
	private void printExternals() {
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
			diagram.append(
"        \""+idNatGw+"\" -> Internet\n");
		}

//External Data Centre
		for (String idVpnGw : allDisplayedVpnGws.keySet()) {
			diagram.append(
"        \""+idVpnGw+"\" -> \"External Data Centre\" [dir=both]\n");
		}		
		
//Endpoints
		for (String idVpcEndpoint : allDisplayedVpcEndpoints.keySet()) {
			VpcEndpoint vpcEndpoint = model.vpcEndpoints.get(idVpcEndpoint);
			diagram.append(
"        \""+idVpcEndpoint+"\" -> \"AWS "+vpcEndpoint.service_name+"\" \n");
		}		

		diagram.append(
"    }\n");
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
