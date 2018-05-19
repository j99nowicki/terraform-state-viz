package com.jakub.tfutil.aws.objects;

import java.util.HashMap;
import java.util.HashSet;

import org.apache.commons.lang3.builder.ToStringBuilder;
import com.jakub.tfutil.TfObjectsWarehouse;
import com.jakub.tfutil.aws.data.DataVpc;
import com.jakub.tfutil.aws.resources.ResourceSubnet;
import com.jakub.tfutil.aws.resources.ResourceVpc;
import com.jakub.tfutil.aws.resources.ResourceVpcEndpoint;

public class Model {
	public HashMap<String, Vpc> vpcs = new HashMap<>();
	public HashMap<String, VpcEndpoint> vpcEndpoints = new HashMap<>();
	
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
	
	
}
