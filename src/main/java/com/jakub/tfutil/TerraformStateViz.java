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
import com.jakub.tfutil.aws.attributes.EipAttributes;
import com.jakub.tfutil.aws.attributes.InstanceAttributes;
import com.jakub.tfutil.aws.attributes.InternetGatewayAttributes;
import com.jakub.tfutil.aws.attributes.NatGatewayAttributes;
import com.jakub.tfutil.aws.attributes.RouteAttributes;
import com.jakub.tfutil.aws.attributes.RouteTableAssociationAttributes;
import com.jakub.tfutil.aws.attributes.RouteTableAttributes;
import com.jakub.tfutil.aws.attributes.SecurityGroupAttributes;
import com.jakub.tfutil.aws.attributes.SecurityGroupRuleAttributes;
import com.jakub.tfutil.aws.attributes.SubnetAttributes;
import com.jakub.tfutil.aws.attributes.TfAttributes;
import com.jakub.tfutil.aws.attributes.VpcAttributes;
import com.jakub.tfutil.aws.attributes.VpcEndpointAttributes;
import com.jakub.tfutil.aws.attributes.VpnGatewayAttributes;
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
//        JsonObject stateJson = terraformRcsViz.parseStateFile("src\\main\\resources\\terraformVpcSecGrEc2.tfstate");
        JsonObject stateJson = terraformRcsViz.parseStateFile("src\\main\\resources\\terraformDoubleVpc.tfstate");
//        JsonObject stateJson = terraformRcsViz.parseStateFile("src\\main\\resources\\terraform_ireland.tfstate");
                
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
		JsonArray modules = stateJson.getAsJsonArray("modules");
		Iterator<JsonElement> it = modules.iterator();
	    while(it.hasNext()) {
	    	JsonElement je = it.next();
	    	readModule(model, je);			
	    }
		if (model.vpcs == null){
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
			String id = getElementAsString(primary.get("id"));
			JsonElement jsonElement = primary.get("attributes");
			System.out.println("type: " + type + " id: " + id + " tfName: "+tfName);
			String objectKey = id;
			if ("aws_instance".equals(type)) {
				model.instances.put(objectKey, gson.fromJson(jsonElement, InstanceAttributes.class));
			}
			if ("aws_vpc_endpoint".equals(type)) {
				model.vpcEndpoints.put(objectKey, gson.fromJson(jsonElement, VpcEndpointAttributes.class));
			}
			if ("aws_security_group".equals(type)) {
				model.securityGroups.put(objectKey, gson.fromJson(jsonElement, SecurityGroupAttributes.class));
			}
			if ("aws_security_group_rule".equals(type)) {
				model.securityGroupRules.put(objectKey, gson.fromJson(jsonElement, SecurityGroupRuleAttributes.class));
			}
			if ("aws_vpn_gateway".equals(type)) {
				model.vpnGateways.put(objectKey, gson.fromJson(jsonElement, VpnGatewayAttributes.class));
			}
			if ("aws_eip".equals(type)) {
				model.eips.put(objectKey, gson.fromJson(jsonElement, EipAttributes.class));
			}
			if ("aws_internet_gateway".equals(type)) {
				model.internetGateways.put(objectKey, gson.fromJson(jsonElement, InternetGatewayAttributes.class));
			}
			if ("aws_nat_gateway".equals(type)) {
				model.natGateways.put(objectKey, gson.fromJson(jsonElement, NatGatewayAttributes.class));
			}
			if ("aws_route".equals(type)) {
				model.routes.put(objectKey, gson.fromJson(jsonElement, RouteAttributes.class));
			}
			if ("aws_route_table".equals(type)) {
				model.routeTables.put(objectKey, gson.fromJson(jsonElement, RouteTableAttributes.class));
			}
			if ("aws_route_table_association".equals(type)) {
				model.routeTableAssociations.put(objectKey, gson.fromJson(jsonElement, RouteTableAssociationAttributes.class));
			}
			if ("aws_subnet".equals(type)) {
				model.subnets.put(objectKey, gson.fromJson(jsonElement, SubnetAttributes.class));
			}
			if ("aws_vpc".equals(type)) {
				TfAttributes tfAttr = gson.fromJson(jsonElement, VpcAttributes.class);
				tfAttr.tfName = tfName;
				model.vpcs.put(objectKey, (VpcAttributes) tfAttr);
			}
		}
	}

	private String getElementAsString(JsonElement idJsonElement) {
		String idWithQuotes = idJsonElement.toString();
		String value = idWithQuotes .substring(1,idWithQuotes.length()-1);
		return value;
	}
}