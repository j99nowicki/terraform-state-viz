package com.jakub.tfutil.aws.resources;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class ResourceVpcEndpoinRouteTableAssociation extends TfResource{

	public String route_table_id;
	public String id;
	public String vpc_endpoint_id;
	
	@Override
	public String toString()
	{
	  return ToStringBuilder.reflectionToString(this);
	}
}
