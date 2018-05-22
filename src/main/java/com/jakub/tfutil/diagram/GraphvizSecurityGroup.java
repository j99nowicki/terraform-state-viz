package com.jakub.tfutil.diagram;

import java.util.HashMap;

import com.jakub.tfutil.aws.objects.Model;
import com.jakub.tfutil.aws.objects.SecurityGroup;

public class GraphvizSecurityGroup {
	
	public static void printSecurityGroups(StringBuffer diagram, Model model, String idVpc) {
		HashMap<String, SecurityGroup> securityGroups = model.findSecurityGroupsInVpc(idVpc);
		for (String idSecurityGroup : securityGroups.keySet()) {
			SecurityGroup securityGroup = securityGroups.get(idSecurityGroup);
			GraphvizDiagram.allDisplayedSecurityGroups.put(idSecurityGroup, securityGroup);
			String style = (securityGroup.isResource()?"filled":"dashed");
			String iconColor = (securityGroup.isResource()?"white":"bisque3");
			diagram.append(
"            subgraph cluster_"+(GraphvizDiagram.c++)+" {\n"+
"                \"icon"+idSecurityGroup+"\" [label=\"SG\" shape=hexagon style="+style+ " color="+iconColor+"]\n"+
"                node [style=filled];\n"+
"                color=bisque3;\n"+
"                style=\""+style+",rounded\" label = \"SecurityGroup: "+ securityGroup.tagsName+"\"\n"+
"                \""+idSecurityGroup+"\" [label = \"{tfName: "+ securityGroup.tfName+"|id: "+idSecurityGroup +"}\" shape = \"record\" ];\n"+
"            }\n");
		}
	}
}
