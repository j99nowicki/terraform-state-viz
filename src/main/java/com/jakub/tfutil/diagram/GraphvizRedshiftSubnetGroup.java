package com.jakub.tfutil.diagram;

import java.util.HashMap;
import java.util.HashSet;

import com.jakub.tfutil.aws.objects.Model;
import com.jakub.tfutil.aws.objects.RedshiftSubnetGroup;

public class GraphvizRedshiftSubnetGroup {
	
	public static void printRedshiftSubnetGroup(StringBuffer diagram, Model model, String idVpc, HashSet<String> allDisplayedZonesInVpc) {
		HashMap<String, RedshiftSubnetGroup> redshiftSubnetGroups = model.findRedshiftSubnetGroupInVpc(idVpc);
		for (String idRedshiftSubnetGroup : redshiftSubnetGroups.keySet()) {
			RedshiftSubnetGroup redshiftSubnetGroup = redshiftSubnetGroups.get(idRedshiftSubnetGroup);
			String style = (redshiftSubnetGroup.isResource()?"filled":"dashed");
			String printedId = "redshift-"+idRedshiftSubnetGroup;
			diagram.append(
"        subgraph cluster_"+(GraphvizDiagram.c++)+" {\n"+
"            node [style=filled];\n"+
"            color=greenyellow\n"+
"            style=\""+style+",rounded\" label = \"RedshiftSubnetGroup: "+ redshiftSubnetGroup.tagsName+"\"\n"+
"            \""+printedId+"\" [label = \"{tfName: "+ redshiftSubnetGroup.tfName+"|id: "+printedId+"}\" shape = \"record\" ];\n"+
"        }\n");

			if (GraphvizDiagram.showRedshiftSubnetGroupAssociationsToSubnets){
				for (String idSubnet : redshiftSubnetGroup.subnetIds) {
					if (allDisplayedZonesInVpc.contains(model.subnets.get(idSubnet).availability_zone)){
						diagram.append("        \""+ printedId +"\" -> \""+ idSubnet +"\" [label = \""+printedId+"\" dir=none, style=dashed]\n");
					}
				}	
			}
		}
	}
}
