package com.jakub.tfutil.diagram;

import java.util.HashMap;
import java.util.HashSet;

import com.jakub.tfutil.aws.objects.ElasticacheSubnetGroup;
import com.jakub.tfutil.aws.objects.Model;

public class GraphvizElasticacheSubnetGroup {
	
	public static void printElasticacheSubnetGroup(StringBuffer diagram, Model model, String idVpc, HashSet<String> allDisplayedZonesInVpc) {
		HashMap<String, ElasticacheSubnetGroup> elasticacheSubnetGroups = model.findElasticacheSubnetGroupInVpc(idVpc);
		for (String idElasticacheSubnetGroup : elasticacheSubnetGroups.keySet()) {
			ElasticacheSubnetGroup elasticacheSubnetGroup = elasticacheSubnetGroups.get(idElasticacheSubnetGroup);
			String style = (elasticacheSubnetGroup.isResource()?"filled":"dashed");
			String printedId = "elasticache-"+idElasticacheSubnetGroup;
			diagram.append(
"        subgraph cluster_"+(GraphvizDiagram.c++)+" {\n"+
"            node [style=filled];\n"+
"            color=aquamarine\n"+
"            style=\""+style+",rounded\" label = \"ElasticacheSubnetGroup: "+ elasticacheSubnetGroup.tagsName+"\"\n"+
"            \""+printedId+"\" [label = \"{tfName: "+ elasticacheSubnetGroup.tfName+"|id: "+printedId+"}\" shape = \"record\" ];\n"+
"        }\n");

			if (GraphvizDiagram.showElasticacheSubnetGroupAssociationsToSubnets){
				for (String idSubnet : elasticacheSubnetGroup.subnetIds) {
					if (allDisplayedZonesInVpc.contains(model.subnets.get(idSubnet).availability_zone)){
						diagram.append("        \""+ printedId +"\" -> \""+ idSubnet +"\" [label = \""+printedId+"\" dir=none, style=dashed]\n");
					}
				}	
			}
		}			
	}
}
