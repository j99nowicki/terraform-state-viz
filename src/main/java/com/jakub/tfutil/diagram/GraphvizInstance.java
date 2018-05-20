package com.jakub.tfutil.diagram;

import java.util.HashMap;

import com.jakub.tfutil.aws.objects.Instance;
import com.jakub.tfutil.aws.objects.Model;
import com.jakub.tfutil.aws.objects.VpnGateway;

public class GraphvizInstance {
	
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
	
	
	public static void printInstances(StringBuffer diagram, Model model, String idSubnet) {
		HashMap<String, Instance> instances = model.findInstancesInSubnet(idSubnet);

		for (String idInstance : instances.keySet()) {
			Instance instance = instances.get(idInstance);
			String style = (instance.isResource()?"filled":"dashed");
			String iconColor = (instance.isResource()?"white":"blue");
			diagram.append(
"                subgraph cluster_"+(GraphvizDiagram.c++)+" {\n"+
"                    \"icon-"+instance.id+"\" [label=EC2 shape=rpromoter style="+style+ " color="+iconColor+"]\n"+
"                    node [style=filled];\n"+
"                    color=blue\n"+
"                    style=\""+style +",rounded\"\n"+
"                    label = \"EC2: "+ instance.tagsName+"\"\n"+
"                    \""+instance.id+"\" [label = \"{tfName: "+ instance.tfName+"|id: "+idInstance+"|public IP: "+instance.public_ip+"|private IP: "+instance.private_ip+"}\" shape = \"record\" ];\n");
			diagram.append(					
"                }\n");
			}
		}
	
}
