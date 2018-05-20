package com.jakub.tfutil.aws.objects;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.jakub.tfutil.aws.resources.ResourceVpcDhcpOptionsAssociation;

public class VpcDhcpOptionsAssociation extends TfObject{

	public String dhcp_options_id;
	public String id;
	public String vpc_id;
	private ResourceVpcDhcpOptionsAssociation resourceVpcDhcpOptionsAssociation;
	
	public VpcDhcpOptionsAssociation(ResourceVpcDhcpOptionsAssociation resourceVpcDhcpOptionsAssociation) {
		this.resourceVpcDhcpOptionsAssociation = resourceVpcDhcpOptionsAssociation;
		this.dhcp_options_id = resourceVpcDhcpOptionsAssociation.dhcp_options_id;
		this.id = resourceVpcDhcpOptionsAssociation.id;
		this.vpc_id = resourceVpcDhcpOptionsAssociation.vpc_id;
		resource = true;
	}
	
	@Override
	public String toString()
	{
	  return ToStringBuilder.reflectionToString(this);
	}

	public ResourceVpcDhcpOptionsAssociation getResourceVpcDhcpOptionsAssociation() {
		return resourceVpcDhcpOptionsAssociation;
	}

}
