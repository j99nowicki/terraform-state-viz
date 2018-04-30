package com.jakub.tfutil;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.Set;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.jakub.tfutil.aws.eip.Eip;
import com.jakub.tfutil.aws.internet_gateway.InternetGateway;
import com.jakub.tfutil.aws.nat_gateway.NatGateway;
import com.jakub.tfutil.aws.route.Route;
import com.jakub.tfutil.aws.route_table.RouteTable;
import com.jakub.tfutil.aws.route_table_association.RouteTableAssociation;
import com.jakub.tfutil.aws.subnet.Subnet;
import com.jakub.tfutil.aws.vpc.Vpc;
import com.jakub.tfutil.aws.vpc_endpoint.VpcEndpoint;
import com.jakub.tfutil.aws.vpn_gateway.VpnGateway;
import com.jakub.tfutil.diagram.GraphvizDiagram;

public class TerraformStateViz{

	static final Logger logger = Logger.getLogger(TerraformStateViz.class);
	static Gson gson = new GsonBuilder().create();

	public static void main(String[] args) throws IOException{
		logger.info("Start");
		
        Gson gson = new GsonBuilder().create();
        gson.toJson("Hello", System.out);
        gson.toJson(123, System.out);
        System.out.println();
        System.out.println();

        TerraformStateViz terraformRcsViz = new TerraformStateViz();
//        JsonObject stateJson = terraformRcsViz.parseStateFile("src\\main\\resources\\terraform.tfstate");
        JsonObject stateJson = terraformRcsViz.parseStateFile("src\\main\\resources\\terraformVpcSecGr.tfstate");
        
        Model model = terraformRcsViz.buildModel(stateJson);
		System.out.println(model);
		GraphvizDiagram graphvizDiagram = new GraphvizDiagram();
		String diagram = graphvizDiagram.draw(model);
		System.out.println(diagram);
		
	}
	
	public JsonObject parseStateFile (String stateFileName){

        // Read from File to String
        JsonObject jsonObject = new JsonObject();
        
        try {
            JsonParser parser = new JsonParser();
            JsonElement jsonElement = parser.parse(new FileReader(stateFileName));
            jsonObject = jsonElement.getAsJsonObject();
            return jsonObject;
        } catch (FileNotFoundException e) {
        }
        return jsonObject;
    }
		
	public Model buildModel(JsonObject stateJson) {
		Model model = new Model();		
		//TODO only one VPC is supported
		JsonArray modules = stateJson.getAsJsonArray("modules");
		Iterator<JsonElement> it = modules.iterator();
	    while(it.hasNext()) {
	    	JsonElement je = it.next();
	    	readModule(model, je);			
	    }
		if (model.vpc == null){
			System.out.println("TF State file empty - leaving");
			System.exit(1);
		}
	    return model;
	}

	private void readModule(Model model, JsonElement module) {
		JsonObject mainModule = module.getAsJsonObject();
		JsonObject resources = mainModule.getAsJsonObject("resources");

		Set<String> keys = resources.keySet();
		for (String tfName : keys) {
			String typeWithQuotes = resources.getAsJsonObject(tfName).get("type").toString();
			String type = typeWithQuotes.substring(1,typeWithQuotes.length()-1);
			System.out.println(type);
			if ("aws_vpc_endpoint".equals(type)) {
				model.vpcEndpoints.put(tfName, gson.fromJson(resources.getAsJsonObject(tfName), VpcEndpoint.class));
			}
			if ("aws_vpn_gateway".equals(type)) {
				model.vpnGateways.put(tfName, gson.fromJson(resources.getAsJsonObject(tfName), VpnGateway.class));
			}
			if ("aws_eip".equals(type)) {
				model.eips.put(tfName, gson.fromJson(resources.getAsJsonObject(tfName), Eip.class));
			}
			if ("aws_internet_gateway".equals(type)) {
				model.internetGateways.put(tfName, gson.fromJson(resources.getAsJsonObject(tfName), InternetGateway.class));
			}
			if ("aws_nat_gateway".equals(type)) {
				model.natGateways.put(tfName, gson.fromJson(resources.getAsJsonObject(tfName), NatGateway.class));
			}
			if ("aws_route".equals(type)) {
				model.routes.put(tfName, gson.fromJson(resources.getAsJsonObject(tfName), Route.class));
			}
			if ("aws_route_table".equals(type)) {
				model.routeTables.put(tfName, gson.fromJson(resources.getAsJsonObject(tfName), RouteTable.class));
			}
			if ("aws_route_table_association".equals(type)) {
				model.routeTableAssociations.put(tfName, gson.fromJson(resources.getAsJsonObject(tfName), RouteTableAssociation.class));
			}
			if ("aws_subnet".equals(type)) {
				model.subnets.put(tfName, gson.fromJson(resources.getAsJsonObject(tfName), Subnet.class));
			}
			if ("aws_vpc".equals(type)) {
				Vpc vpc = gson.fromJson(resources.getAsJsonObject(tfName), Vpc.class);
				vpc.setTfName(tfName);
				model.vpc = vpc;
			}
		}
	}
}