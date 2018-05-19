package com.jakub.tfutil.aws.objects;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.jakub.tfutil.aws.resources.ResourceVpnGateway;

public class VpnGateway extends TfObject{
	public String vpc_id;
	public String id;
	public int amazon_side_asn;
	public int tagsCount;
	public String tagsName;
	private ResourceVpnGateway resourceVpnGateway;
	
	public VpnGateway(ResourceVpnGateway resourceVpnGateway) {
		this.resourceVpnGateway = resourceVpnGateway;
		this.vpc_id = resourceVpnGateway.vpc_id;
		this.id = resourceVpnGateway.id;
		this.amazon_side_asn = resourceVpnGateway.amazon_side_asn;
		this.tagsCount = resourceVpnGateway.tagsCount;
		this.tagsName = resourceVpnGateway.tagsName;
		resource = true;
	}
	
	@Override
	public String toString()
	{
	  return ToStringBuilder.reflectionToString(this);
	}

	public ResourceVpnGateway getResourceVpnGateway() {
		return resourceVpnGateway;
	}

}
