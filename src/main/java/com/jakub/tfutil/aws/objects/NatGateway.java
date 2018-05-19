package com.jakub.tfutil.aws.objects;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.jakub.tfutil.aws.resources.ResourceNatGateway;

public class NatGateway extends TfObject{
	public String id;
	public String allocation_id;
	public String network_interface_id;
	public String private_ip;
	public String public_ip;
	public String subnet_id;
	public int tagsCount;
	public String tagsName;
	private ResourceNatGateway resourceNatGateway;
	
	public NatGateway(ResourceNatGateway resourceNatGateway) {
		this.resourceNatGateway = resourceNatGateway;
		this.id = resourceNatGateway.id;
		this.allocation_id = resourceNatGateway.allocation_id;
		this.network_interface_id = resourceNatGateway.network_interface_id;
		this.private_ip = resourceNatGateway.private_ip;
		this.public_ip = resourceNatGateway.public_ip;
		this.subnet_id = resourceNatGateway.subnet_id;
		this.tagsCount = resourceNatGateway.tagsCount;
		this.tagsName = resourceNatGateway.tagsName;

		resource = true;
	}
	
	@Override
	public String toString()
	{
	  return ToStringBuilder.reflectionToString(this);
	}

	public ResourceNatGateway getResourceNatGateway() {
		return resourceNatGateway;
	}

}
