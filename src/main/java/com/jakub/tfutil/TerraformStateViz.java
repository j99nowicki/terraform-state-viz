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
import com.jakub.tfutil.aws.TfAttributes;
import com.jakub.tfutil.aws.data.DataSubnetIds;
import com.jakub.tfutil.aws.data.DataVpc;
import com.jakub.tfutil.aws.resources.ResourceEip;
import com.jakub.tfutil.aws.resources.ResourceInstance;
import com.jakub.tfutil.aws.resources.ResourceInternetGateway;
import com.jakub.tfutil.aws.resources.ResourceNatGateway;
import com.jakub.tfutil.aws.resources.ResourceRoute;
import com.jakub.tfutil.aws.resources.ResourceRouteTableAssociation;
import com.jakub.tfutil.aws.resources.ResourceRouteTable;
import com.jakub.tfutil.aws.resources.ResourceSecurityGroup;
import com.jakub.tfutil.aws.resources.ResourceSecurityGroupRule;
import com.jakub.tfutil.aws.resources.ResourceSubnet;
import com.jakub.tfutil.aws.resources.ResourceVpc;
import com.jakub.tfutil.aws.resources.ResourceVpcEndpoint;
import com.jakub.tfutil.aws.resources.ResourceVpnGateway;
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
                
        TfObjectsWarehouse tfObjectsWarehouse = terraformRcsViz.buildTfObjectWarehouse(stateJson);
		GraphvizDiagram graphvizDiagram = new GraphvizDiagram(tfObjectsWarehouse);
		String diagram = graphvizDiagram.draw();
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
		
	public TfObjectsWarehouse buildTfObjectWarehouse(JsonObject stateJson) {
		TfObjectsWarehouse tfObjectsWarehouse = new TfObjectsWarehouse();		
		JsonArray modules = stateJson.getAsJsonArray("modules");
		Iterator<JsonElement> it = modules.iterator();
	    while(it.hasNext()) {
	    	JsonElement je = it.next();
	    	readModule(tfObjectsWarehouse, je);
	    }
		if (tfObjectsWarehouse.rVpcs == null){
			System.out.println("TF State file empty - leaving");
			System.exit(1);
		}
	    return tfObjectsWarehouse;
	}

	private void readModule(TfObjectsWarehouse tfObjectsWarehouse, JsonElement module) {
		JsonObject mainModule = module.getAsJsonObject();
		JsonObject resources = mainModule.getAsJsonObject("resources");

		Set<String> keys = resources.keySet();
		for (String tfName : keys) {
			String type = getElementAsString(resources.getAsJsonObject(tfName).get("type"));
			JsonObject resource = resources.getAsJsonObject(tfName);
			JsonObject primary = resource.getAsJsonObject("primary");
			String id = getElementAsString(primary.get("id"));
			JsonElement jsonElement = primary.get("attributes");
			String objectKey = id;
			if (tfName.startsWith("data.")) {
//				System.out.println("Data: type: " + type + " id: " + id + " tfName: " + tfName);
				if ("aws_subnet_ids".equals(type)) {
					DataSubnetIds tfAttr = gson.fromJson(jsonElement, DataSubnetIds.class);
					tfAttr.tfName = tfName;
					tfAttr.parseIds(jsonElement.getAsJsonObject().entrySet());
					tfObjectsWarehouse.dSubnetIdss.put(objectKey, tfAttr);
				} else if ("aws_vpc".equals(type)) {
					TfAttributes tfAttr = gson.fromJson(jsonElement, DataVpc.class);
					tfAttr.tfName = tfName;
					tfObjectsWarehouse.dVpcs.put(objectKey, (DataVpc) tfAttr);
				} else {
					System.out.println("Unknown data type: " + type);
				}
			} else {
				if ("aws_instance".equals(type)) {
					TfAttributes tfAttr = gson.fromJson(jsonElement, ResourceInstance.class);
					tfAttr.tfName = tfName;
					tfObjectsWarehouse.rInstances.put(objectKey, (ResourceInstance) tfAttr);
				} else if ("aws_vpc_endpoint".equals(type)) {
					TfAttributes tfAttr = gson.fromJson(jsonElement, ResourceVpcEndpoint.class);
					tfAttr.tfName = tfName;
					tfObjectsWarehouse.rVpcEndpoints.put(objectKey, (ResourceVpcEndpoint) tfAttr);
				} else if ("aws_security_group".equals(type)) {
					TfAttributes tfAttr = gson.fromJson(jsonElement, ResourceSecurityGroup.class);
					tfAttr.tfName = tfName;
					tfObjectsWarehouse.rSecurityGroups.put(objectKey, (ResourceSecurityGroup) tfAttr);
				} else if ("aws_security_group_rule".equals(type)) {
					TfAttributes tfAttr = gson.fromJson(jsonElement, ResourceSecurityGroupRule.class);
					tfAttr.tfName = tfName;
					tfObjectsWarehouse.rSecurityGroupRules.put(objectKey, (ResourceSecurityGroupRule) tfAttr);
				} else if ("aws_vpn_gateway".equals(type)) {
					TfAttributes tfAttr = gson.fromJson(jsonElement, ResourceVpnGateway.class);
					tfAttr.tfName = tfName;
					tfObjectsWarehouse.rVpnGateways.put(objectKey, (ResourceVpnGateway) tfAttr);
				} else if ("aws_eip".equals(type)) {
					TfAttributes tfAttr = gson.fromJson(jsonElement, ResourceEip.class);
					tfAttr.tfName = tfName;
					tfObjectsWarehouse.rEips.put(objectKey, (ResourceEip) tfAttr);
				} else if ("aws_internet_gateway".equals(type)) {
					TfAttributes tfAttr = gson.fromJson(jsonElement, ResourceInternetGateway.class);
					tfAttr.tfName = tfName;
					tfObjectsWarehouse.rInternetGateways.put(objectKey, (ResourceInternetGateway) tfAttr);
				} else if ("aws_nat_gateway".equals(type)) {
					TfAttributes tfAttr = gson.fromJson(jsonElement, ResourceNatGateway.class);
					tfAttr.tfName = tfName;
					tfObjectsWarehouse.rNatGateways.put(objectKey, (ResourceNatGateway) tfAttr);
				} else if ("aws_route".equals(type)) {
					TfAttributes tfAttr = gson.fromJson(jsonElement, ResourceRoute.class);
					tfAttr.tfName = tfName;
					tfObjectsWarehouse.rRoutes.put(objectKey, (ResourceRoute) tfAttr);
				} else if ("aws_route_table".equals(type)) {
					TfAttributes tfAttr = gson.fromJson(jsonElement, ResourceRouteTable.class);
					tfAttr.tfName = tfName;
					tfObjectsWarehouse.rRouteTables.put(objectKey, (ResourceRouteTable) tfAttr);
				} else if ("aws_route_table_association".equals(type)) {
					TfAttributes tfAttr = gson.fromJson(jsonElement, ResourceRouteTableAssociation.class);
					tfAttr.tfName = tfName;
					tfObjectsWarehouse.rRouteTableAssociations.put(objectKey, (ResourceRouteTableAssociation) tfAttr);
				} else if ("aws_subnet".equals(type)) {
					TfAttributes tfAttr = gson.fromJson(jsonElement, ResourceSubnet.class);
					tfAttr.tfName = tfName;
					tfObjectsWarehouse.rSubnets.put(objectKey, (ResourceSubnet) tfAttr);
				} else if ("aws_vpc".equals(type)) {
					TfAttributes tfAttr = gson.fromJson(jsonElement, ResourceVpc.class);
					tfAttr.tfName = tfName;
					tfObjectsWarehouse.rVpcs.put(objectKey, (ResourceVpc) tfAttr);
				} else {
					System.out.println("Unknown respource type: " + type);
				}
			}
		}
	}

	private String getElementAsString(JsonElement idJsonElement) {
		String idWithQuotes = idJsonElement.toString();
		String value = idWithQuotes .substring(1,idWithQuotes.length()-1);
		return value;
	}
}