package com.jakub.tfutil.diagram;

import java.util.HashMap;

import com.jakub.tfutil.aws.objects.Model;
import com.jakub.tfutil.aws.objects.VpcDhcpOptions;

public class GraphvizVpcDhcpOptions {
	
	public static void printVpcDhcpOptions(StringBuffer diagram, Model model, String idVpc) {
		HashMap<String, VpcDhcpOptions> vpcDhcpOptionss = model.findVpcDhcpOptionsInVpc(idVpc);
		for (String idVpcDhcpOptions : vpcDhcpOptionss.keySet()) {
			VpcDhcpOptions vpcDhcpOptions = vpcDhcpOptionss.get(idVpcDhcpOptions);
			diagram.append(
"        subgraph cluster_"+(GraphvizDiagram.c++)+" {\n"+
"            node [style=filled];\n"+
"            color=lavender\n"+
"            label = \"DHCP Options: "+ vpcDhcpOptions.tagsName+"\"\n"+
"            \""+idVpcDhcpOptions+"\" [label = \"{tfName: "+ vpcDhcpOptions.tfName+"|id: "+idVpcDhcpOptions+"}\" shape = \"record\" ];\n"+
"        }\n");
		}			
	}
	
}
