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
import com.jakub.tfutil.aws.EipAttributes;
import com.jakub.tfutil.aws.InstanceAttributes;
import com.jakub.tfutil.aws.InternetGatewayAttributes;
import com.jakub.tfutil.aws.VpnGatewayAttributes;
import com.jakub.tfutil.aws.NatGatewayAttributes;
import com.jakub.tfutil.aws.RouteAttributes;
import com.jakub.tfutil.aws.RouteTableAttributes;
import com.jakub.tfutil.aws.RouteTableAssociationAttributes;
import com.jakub.tfutil.aws.SecurityGroupAttributes;
import com.jakub.tfutil.aws.SecurityGroupRuleAttributes;
import com.jakub.tfutil.aws.SubnetAttributes;
import com.jakub.tfutil.aws.VpcAttributes;
import com.jakub.tfutil.aws.VpcEndpointAttributes;
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
        JsonObject stateJson = terraformRcsViz.parseStateFile("src\\main\\resources\\terraformVpcSecGrEc2.tfstate");
        
        Model model = terraformRcsViz.buildModel(stateJson);
		//System.out.println(model);
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
		if (model.vpcAttributes == null){
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
			String type = getElementAsString(resources.getAsJsonObject(tfName).get("type"));
			JsonObject resource = resources.getAsJsonObject(tfName);
			JsonObject primary = resource.getAsJsonObject("primary");
//			String id = getElementAsString(primary.get("id"));
			JsonElement jsonElement = primary.get("attributes");
			System.out.println(type);
			if ("aws_instance".equals(type)) {
				model.instancesAttributes.put(tfName, gson.fromJson(jsonElement, InstanceAttributes.class));
			}
			if ("aws_vpc_endpoint".equals(type)) {
				model.vpcEndpointsAttributes.put(tfName, gson.fromJson(jsonElement, VpcEndpointAttributes.class));
			}
			if ("aws_security_group".equals(type)) {
				model.securityGroupsAttributes.put(tfName, gson.fromJson(jsonElement, SecurityGroupAttributes.class));
			}
			if ("aws_security_group_rule".equals(type)) {
				model.securityGroupRulesAttributes.put(tfName, gson.fromJson(jsonElement, SecurityGroupRuleAttributes.class));
			}
			if ("aws_vpn_gateway".equals(type)) {
				model.vpnGatewaysAttributes.put(tfName, gson.fromJson(jsonElement, VpnGatewayAttributes.class));
			}
			if ("aws_eip".equals(type)) {
				model.eipsAttributes.put(tfName, gson.fromJson(jsonElement, EipAttributes.class));
			}
			if ("aws_internet_gateway".equals(type)) {
				model.internetGatewaysAttributes.put(tfName, gson.fromJson(jsonElement, InternetGatewayAttributes.class));
			}
			if ("aws_nat_gateway".equals(type)) {
				model.natGatewaysAttributes.put(tfName, gson.fromJson(jsonElement, NatGatewayAttributes.class));
			}
			if ("aws_route".equals(type)) {
				model.routesAttributes.put(tfName, gson.fromJson(jsonElement, RouteAttributes.class));
			}
			if ("aws_route_table".equals(type)) {
				model.routeTablesAttributes.put(tfName, gson.fromJson(jsonElement, RouteTableAttributes.class));
			}
			if ("aws_route_table_association".equals(type)) {
				model.routeTableAssociationsAttributes.put(tfName, gson.fromJson(jsonElement, RouteTableAssociationAttributes.class));
			}
			if ("aws_subnet".equals(type)) {
				model.subnetsAttributes.put(tfName, gson.fromJson(jsonElement, SubnetAttributes.class));
			}
			if ("aws_vpc".equals(type)) {
				VpcAttributes vpcAttributes = gson.fromJson(jsonElement, VpcAttributes.class);
				vpcAttributes.setTfName(tfName);
				model.vpcAttributes = vpcAttributes;
			}
		}
	}

	private String getElementAsString(JsonElement idJsonElement) {
		String idWithQuotes = idJsonElement.toString();
		String value = idWithQuotes .substring(1,idWithQuotes.length()-1);
		return value;
	}
}