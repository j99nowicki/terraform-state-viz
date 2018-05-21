package com.jakub.tfutil.diagram;

import java.util.HashMap;

import com.jakub.tfutil.aws.objects.Instance;
import com.jakub.tfutil.aws.objects.Model;

public class GraphvizInstance {
	
	public static void printInstances(StringBuffer diagram, Model model, String idSubnet) {
		HashMap<String, Instance> instances = model.findInstancesInSubnet(idSubnet);

		for (String idInstance : instances.keySet()) {
			Instance instance = instances.get(idInstance);
			String style = (instance.isResource()?"filled":"dashed");
			String iconColor = (instance.isResource()?"white":"blue");
			diagram.append(
"                subgraph cluster_"+(GraphvizDiagram.c++)+" {\n"+
"                    \"icon-"+instance.id+"\" [label=EC2 shape=rpromoter style="+style+ " color="+iconColor+"]\n"+
"                    color=blue\n"+
"                    style=\""+style +",rounded\"\n"+
"                    label = \"EC2: "+ instance.tagsName+"\"\n"+
"                    \""+instance.id+"\" [label = \"{tfName: "+ instance.tfName+"|id: "+idInstance+"|public IP: "+instance.public_ip+"|private IP: "+instance.private_ip+"}\" shape = \"record\" ];\n");
			GraphvizAmi.printAmi(diagram, model, idInstance, instance.ami);
			diagram.append(					
"                }\n");
			}
		}
	
}
