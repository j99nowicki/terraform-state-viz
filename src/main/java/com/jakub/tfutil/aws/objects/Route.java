package com.jakub.tfutil.aws.objects;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.jakub.tfutil.aws.resources.ResourceRoute;

public class Route extends TfObject{

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
	private ResourceRoute resourceRoute;
	
	public Route(ResourceRoute resourceRoute) {
		this.resourceRoute = resourceRoute;
		this.destination_cidr_block = resourceRoute.destination_cidr_block ;
		this.destination_ipv6_cidr_block = resourceRoute.destination_ipv6_cidr_block;
		this.destination_prefix_list_id = resourceRoute.destination_prefix_list_id;
		this.egress_only_gateway_id = resourceRoute.egress_only_gateway_id;
		this.gateway_id = resourceRoute.gateway_id;
		this.id = resourceRoute.id;
		this.instance_id = resourceRoute.instance_id;
		this.instance_owner_id = resourceRoute.instance_owner_id;
		this.nat_gateway_id = resourceRoute.nat_gateway_id;
		this.network_interface_id = resourceRoute.network_interface_id;
		this.origin = resourceRoute.origin;
		this.route_table_id = resourceRoute.route_table_id;
		this.state = resourceRoute.state;
		this.vpc_peering_connection_id = resourceRoute.vpc_peering_connection_id;
		resource = true;
	}
	
	@Override
	public String toString()
	{
	  return ToStringBuilder.reflectionToString(this);
	}

	public ResourceRoute getResourceRoute() {
		return resourceRoute;
	}

}
