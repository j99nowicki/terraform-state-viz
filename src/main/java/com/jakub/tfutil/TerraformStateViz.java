package com.jakub.tfutil;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map.Entry;
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
import com.jakub.tfutil.aws.attributes.SubnetIdsAttributes;
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
			//System.out.println("type: " + type + " id: " + id + " tfName: "+tfName);
			String objectKey = id;
			if ("aws_instance".equals(type)) {
				TfAttributes tfAttr = gson.fromJson(jsonElement, InstanceAttributes.class);
				tfAttr.tfName = tfName;
				model.instances.put(objectKey, (InstanceAttributes) tfAttr);
			} else if ("aws_vpc_endpoint".equals(type)) {
				TfAttributes tfAttr = gson.fromJson(jsonElement, VpcEndpointAttributes.class);
				tfAttr.tfName = tfName;
				model.vpcEndpoints.put(objectKey, (VpcEndpointAttributes) tfAttr);
			} else if ("aws_security_group".equals(type)) {
				TfAttributes tfAttr = gson.fromJson(jsonElement, SecurityGroupAttributes.class);
				tfAttr.tfName = tfName;
				model.securityGroups.put(objectKey, (SecurityGroupAttributes) tfAttr);
			} else if ("aws_security_group_rule".equals(type)) {
				TfAttributes tfAttr = gson.fromJson(jsonElement, SecurityGroupRuleAttributes.class);
				tfAttr.tfName = tfName;
				model.securityGroupRules.put(objectKey, (SecurityGroupRuleAttributes) tfAttr);
			} else if ("aws_vpn_gateway".equals(type)) {
				TfAttributes tfAttr = gson.fromJson(jsonElement, VpnGatewayAttributes.class);
				tfAttr.tfName = tfName;
				model.vpnGateways.put(objectKey, (VpnGatewayAttributes) tfAttr);
			} else if ("aws_eip".equals(type)) {
				TfAttributes tfAttr = gson.fromJson(jsonElement, EipAttributes.class);
				tfAttr.tfName = tfName;
				model.eips.put(objectKey, (EipAttributes) tfAttr);
			} else if ("aws_internet_gateway".equals(type)) {
				TfAttributes tfAttr = gson.fromJson(jsonElement, InternetGatewayAttributes.class);
				tfAttr.tfName = tfName;
				model.internetGateways.put(objectKey, (InternetGatewayAttributes) tfAttr);
			} else if ("aws_nat_gateway".equals(type)) {
				TfAttributes tfAttr = gson.fromJson(jsonElement, NatGatewayAttributes.class);
				tfAttr.tfName = tfName;
				model.natGateways.put(objectKey, (NatGatewayAttributes) tfAttr);
			} else if ("aws_route".equals(type)) {
				TfAttributes tfAttr = gson.fromJson(jsonElement, RouteAttributes.class);
				tfAttr.tfName = tfName;
				model.routes.put(objectKey, (RouteAttributes) tfAttr);
			} else if ("aws_route_table".equals(type)) {
				TfAttributes tfAttr = gson.fromJson(jsonElement, RouteTableAttributes.class);
				tfAttr.tfName = tfName;
				model.routeTables.put(objectKey, (RouteTableAttributes) tfAttr);
			} else if ("aws_route_table_association".equals(type)) {
				TfAttributes tfAttr = gson.fromJson(jsonElement, RouteTableAssociationAttributes.class);
				tfAttr.tfName = tfName;
				model.routeTableAssociations.put(objectKey, (RouteTableAssociationAttributes) tfAttr);
			} else if ("aws_subnet".equals(type)) {
				TfAttributes tfAttr = gson.fromJson(jsonElement, SubnetAttributes.class);
				tfAttr.tfName = tfName;
				model.subnets.put(objectKey, (SubnetAttributes) tfAttr);
			} else if ("aws_subnet_ids".equals(type)) {
				SubnetIdsAttributes tfAttr = gson.fromJson(jsonElement, SubnetIdsAttributes.class);
				tfAttr.tfName = tfName;
				tfAttr.parseIds(jsonElement.getAsJsonObject().entrySet());
				model.subnetIdss.put(objectKey, tfAttr);
			} else if ("aws_vpc".equals(type)) {
				TfAttributes tfAttr = gson.fromJson(jsonElement, VpcAttributes.class);
				tfAttr.tfName = tfName;
				model.vpcs.put(objectKey, (VpcAttributes) tfAttr);
			} else {
				System.out.println("Unknown type: "+ type);
			}
		}
	}

	private String getElementAsString(JsonElement idJsonElement) {
		String idWithQuotes = idJsonElement.toString();
		String value = idWithQuotes .substring(1,idWithQuotes.length()-1);
		return value;
	}
}