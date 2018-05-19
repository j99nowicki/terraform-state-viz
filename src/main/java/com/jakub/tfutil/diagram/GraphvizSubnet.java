package com.jakub.tfutil.diagram;

import java.util.HashMap;

import com.jakub.tfutil.aws.objects.Model;
import com.jakub.tfutil.aws.objects.Subnet;
import com.jakub.tfutil.aws.objects.VpnGateway;

public class GraphvizSubnet {
	
	public static void printVpnGateways(StringBuffer diagram, Model model, String idVpc, HashMap<String, VpnGateway> allDisplayedVpnGws) {
		HashMap<String, VpnGateway> vpnGws = model.findVpnGatewaysInVpc(idVpc);
		for (String idVpnGw : vpnGws.keySet()) {
			VpnGateway vpnGw = vpnGws.get(idVpnGw);
			allDisplayedVpnGws.put(idVpnGw, vpnGw);
			String style = (vpnGw.isResource()?"filled":"dashed");
			diagram.append(
"            subgraph cluster_"+(GraphvizDiagram.c++)+" {\n"+
"                \"icon"+vpnGw.id+"\" [label=\"VPN GW\" shape=cds]\n"+
"                node [style=filled];\n"+
"                color=royalblue1\n"+
"                style=\""+style+",rounded\" label = \"VPN GW: "+ vpnGw.tagsName+"\"\n"+
"                \""+idVpnGw+"\" [label = \"{tfName: "+ vpnGw.tfName+"|id: "+idVpnGw+"|amazon side ASN: "+vpnGw.amazon_side_asn+"}\" shape = \"record\" ];\n"+
"            }\n");
		}
	}
	
	
	
	public static void printSubnets(StringBuffer diagram, Model model, String idVpc, String zone) {
//Subnets		
		HashMap<String, Subnet> subnets = model.findSubnetsInVpcInZone(idVpc, zone);
		for (String idSubnet : subnets.keySet()) {

			Subnet subnet = subnets.get(idSubnet);
			String style = (subnet.isResource()?"filled":"dashed");	
			diagram.append(
"            subgraph cluster_"+(GraphvizDiagram.c++)+" {\n"+
"                node [style=filled];\n"+
"                color=green\n"+
"                style=\""+style+",rounded\"\n");
			if (subnet.isResource()){
				diagram.append(
"                label = \"Subnet: "+ subnet.getResourceSubnet().tagsName+"\"\n"+
"                \""+subnet.id+"\" [label = \"{tfName: "+ subnet.tfName+"|id: "+idSubnet+"|cidr_block: "+subnet.getResourceSubnet().cidr_block+"}\" shape = \"record\" ];\n");
			} else {
				diagram.append(
"                label = \"SubnetId: "+ idSubnet+"\"\n"+
"                \""+idSubnet+"\" [label = \"{tfName: "+ subnet.tfName+"|id: "+idSubnet+"}\" shape = \"record\" ];\n");
			}
//Instances
			GraphvizInstance.printInstances(diagram, model, subnet.id);	
//Nat Gw
			GraphvizNatGateway.printNatGateways(diagram, model, subnet.id, GraphvizDiagram.displayedNatGws);
//Subnets - end
			diagram.append(						
"            }\n");
		}

	}
	
}
