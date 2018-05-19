package com.jakub.tfutil.aws.objects;

import java.util.HashMap;
import java.util.HashSet;

import org.apache.commons.lang3.builder.ToStringBuilder;
import com.jakub.tfutil.TfObjectsWarehouse;
import com.jakub.tfutil.aws.data.DataVpc;
import com.jakub.tfutil.aws.resources.ResourceInternetGateway;
import com.jakub.tfutil.aws.resources.ResourceSubnet;
import com.jakub.tfutil.aws.resources.ResourceVpc;
import com.jakub.tfutil.aws.resources.ResourceVpcEndpoint;
import com.jakub.tfutil.aws.resources.ResourceVpnGateway;

public class Model {
	public HashMap<String, Vpc> vpcs = new HashMap<>();
	public HashMap<String, VpcEndpoint> vpcEndpoints = new HashMap<>();
	public HashMap<String, VpnGateway> vpnGateways = new HashMap<>();
	public HashMap<String, InternetGateway> internetGateways = new HashMap<>();
	
	@Override
	public String toString()
	{
	  return ToStringBuilder.reflectionToString(this);
	}	
	
	public Model(TfObjectsWarehouse tfObjectsWarehouse){
		for (String key : tfObjectsWarehouse.rVpcs.keySet()) {
			ResourceVpc item = tfObjectsWarehouse.rVpcs.get(key);
			vpcs.put(key, new Vpc(item));
					}
		for (String key : tfObjectsWarehouse.dVpcs.keySet()) {
			DataVpc item = tfObjectsWarehouse.dVpcs.get(key);
			vpcs.put(key, new Vpc(item));
		}
		
		for (String key : tfObjectsWarehouse.rVpcEndpoints.keySet()) {
			ResourceVpcEndpoint item = tfObjectsWarehouse.rVpcEndpoints.get(key);
			vpcEndpoints.put(key, new VpcEndpoint(item));
		}
		
		for (String key : tfObjectsWarehouse.rVpnGateways.keySet()) {
			ResourceVpnGateway item = tfObjectsWarehouse.rVpnGateways.get(key);
			vpnGateways.put(key, new VpnGateway(item));
		}

		for (String key : tfObjectsWarehouse.rInternetGateways.keySet()) {
			ResourceInternetGateway item = tfObjectsWarehouse.rInternetGateways.get(key);
			internetGateways.put(key, new InternetGateway(item));
		}
	}
	
	
	
	public HashMap<String, InternetGateway> findInternetGatewaysAttributesInVpc(String idVpc) {
		HashMap<String, InternetGateway> matchingElements = new HashMap<>();
		for (String id : internetGateways.keySet()) {
			InternetGateway attr = internetGateways.get(id);
			if (attr.vpc_id.equals(idVpc)){
				matchingElements.put(id, attr);
			}
		}
		return matchingElements;
	}
	
	
	public HashMap<String, VpcEndpoint> findVpcEndpointsInVpc(String idVpc) {
		HashMap<String, VpcEndpoint> matchingElements = new HashMap<>();
		for (String id : vpcEndpoints.keySet()) {
			VpcEndpoint element = vpcEndpoints.get(id);
			if (element.vpc_id.equals(idVpc)){
				matchingElements.put(id, element);
			}
		}
		return matchingElements;
	}
	
	public HashMap<String, VpnGateway> findVpnGatewaysAttributesInVpc(String idVpc) {
		HashMap<String, VpnGateway> matchingElements = new HashMap<>();
		for (String id : vpnGateways.keySet()) {
			VpnGateway attr = vpnGateways.get(id);
			if (attr.vpc_id.equals(idVpc)){
				matchingElements.put(id, attr);
			}
		}
		return matchingElements;
	}
	
}
