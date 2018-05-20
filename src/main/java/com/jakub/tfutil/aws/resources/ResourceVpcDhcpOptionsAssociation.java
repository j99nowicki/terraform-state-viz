package com.jakub.tfutil.aws.resources;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class ResourceVpcDhcpOptionsAssociation extends TfResource{

	public String dhcp_options_id;
	public String id;
	public String vpc_id;
	
	@Override
	public String toString()
	{
	  return ToStringBuilder.reflectionToString(this);
	}
}
