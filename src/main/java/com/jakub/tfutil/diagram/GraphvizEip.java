package com.jakub.tfutil.diagram;

import com.jakub.tfutil.aws.objects.Eip;

public class GraphvizEip {
	
	public static void printEip(StringBuffer diagram, Eip eip) {
		String style = (eip.isResource()?"filled":"dashed");
		diagram.append(			
"                    \"icon-"+eip.id+"\" [label=\"Elastic IP\" shape=house style="+style+" color=yellow]\n");
	}
}
