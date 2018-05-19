package com.jakub.tfutil.aws.objects;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.jakub.tfutil.aws.resources.ResourceSubnet;

public class Subnet extends TfObject{
	public String id;
	public String vpc_id;
	private ResourceSubnet resourceSubnet;
	
	public Subnet(ResourceSubnet resourceSubnet) {
		this.resourceSubnet = resourceSubnet;
		this.id = resourceSubnet.id;
		this.vpc_id = resourceSubnet.vpc_id;
		resource = true;
	}
	
	public Subnet(String idVpc, String idSubnet) {
		this.id = idSubnet;
		this.vpc_id = idVpc;
		resource = false;
	}
	
	@Override
	public String toString()
	{
	  return ToStringBuilder.reflectionToString(this);
	}

	public ResourceSubnet getResourceSubnet() {
		return resourceSubnet;
	}

}
