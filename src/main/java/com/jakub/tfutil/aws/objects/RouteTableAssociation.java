package com.jakub.tfutil.aws.objects;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.jakub.tfutil.aws.resources.ResourceRouteTableAssociation;

public class RouteTableAssociation extends TfObject {

	public String route_table_id;
	public String id;
	public String subnet_id;
	private ResourceRouteTableAssociation resourceRouteTableAssociation;
	
	public RouteTableAssociation(ResourceRouteTableAssociation resourceRouteTableAssociation) {
		this.resourceRouteTableAssociation = resourceRouteTableAssociation;
		this.route_table_id = resourceRouteTableAssociation.route_table_id;
		this.id = resourceRouteTableAssociation.id;
		this.subnet_id = resourceRouteTableAssociation.subnet_id;
		resource = true;
	}
	
	@Override
	public String toString()
	{
	  return ToStringBuilder.reflectionToString(this);
	}

	public ResourceRouteTableAssociation getResourceRouteTableAssociation() {
		return resourceRouteTableAssociation;
	}
}
