package com.jakub.tfutil.diagram;

import java.util.HashMap;

import com.jakub.tfutil.aws.objects.Model;
import com.jakub.tfutil.aws.objects.VpnGateway;

public class GraphvizVpnGateway {
	
	public static void printVpnGateways(StringBuffer diagram, Model model, String idVpc) {
		HashMap<String, VpnGateway> vpnGws = model.findVpnGatewaysInVpc(idVpc);
		for (String idVpnGw : vpnGws.keySet()) {
			VpnGateway vpnGw = vpnGws.get(idVpnGw);
			GraphvizDiagram.allDisplayedVpnGws.put(idVpnGw, vpnGw);
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
	
	
	
}
