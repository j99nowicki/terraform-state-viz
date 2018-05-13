package com.jakub.tfutil.aws.resources;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class ResourceRoute extends TfResource{

	public String destination_cidr_block;
	public String destination_ipv6_cidr_block;
	public String destination_prefix_list_id;
	public String egress_only_gateway_id;
	public String gateway_id;
	public String id;
	public String instance_id;
	public String instance_owner_id;
	public String nat_gateway_id;
	public String network_interface_id;
	public String origin;
	public String route_table_id;
	public String state;
	public String vpc_peering_connection_id;
	
	@Override
	public String toString()
	{
	  return ToStringBuilder.reflectionToString(this);
	}
}
