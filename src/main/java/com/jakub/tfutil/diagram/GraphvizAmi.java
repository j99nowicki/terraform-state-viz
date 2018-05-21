package com.jakub.tfutil.diagram;

import com.jakub.tfutil.aws.objects.Ami;
import com.jakub.tfutil.aws.objects.Model;

public class GraphvizAmi {
	
	public static void printAmi(StringBuffer diagram, Model model, String idInstance, String idAmi) {
		Ami ami = model.amis.get(idAmi);
		String style = (ami.isResource()?"filled":"dashed");
		diagram.append(			
"                    \""+idInstance+"-"+idAmi+"\" [label=\"Ami\" shape=invhouse style="+style+" color=azure2]\n");
	}
}
