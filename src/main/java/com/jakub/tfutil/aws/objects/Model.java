package com.jakub.tfutil.aws.objects;

import java.util.HashMap;
import org.apache.commons.lang3.builder.ToStringBuilder;
import com.jakub.tfutil.TfObjectsWarehouse;
import com.jakub.tfutil.aws.data.DataVpc;
import com.jakub.tfutil.aws.resources.ResourceVpc;

public class Model {
	public HashMap<String, Vpc> vpcs = new HashMap<>();
	
	@Override
	public String toString()
	{
	  return ToStringBuilder.reflectionToString(this);
	}	
	
	public Model(TfObjectsWarehouse tfObjectsWarehouse){
		for (String key : tfObjectsWarehouse.rVpcs.keySet()) {
			ResourceVpc res = tfObjectsWarehouse.rVpcs.get(key);
			Vpc vpc = new Vpc(res);
			vpcs.put(key, vpc);
			
			System.out.println(key + ": " + vpc.isResource());
		}
		for (String key : tfObjectsWarehouse.dVpcs.keySet()) {
			DataVpc res = tfObjectsWarehouse.dVpcs.get(key);
			Vpc vpc = new Vpc(res);
			vpcs.put(key, vpc);
			
			System.out.println(key + ": " + vpc.isResource());

		}

	}
}
