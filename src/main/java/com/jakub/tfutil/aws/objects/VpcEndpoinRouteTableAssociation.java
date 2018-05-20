package com.jakub.tfutil.aws.objects;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.jakub.tfutil.aws.resources.ResourceVpcEndpoinRouteTableAssociation;

public class VpcEndpoinRouteTableAssociation extends TfObject{

	public String route_table_id;
	public String id;
	public String vpc_endpoint_id;
	private ResourceVpcEndpoinRouteTableAssociation resourceVpcEndpoinRouteTableAssociation;
	
	public VpcEndpoinRouteTableAssociation(ResourceVpcEndpoinRouteTableAssociation resourceVpcEndpoinRouteTableAssociation) {
		this.resourceVpcEndpoinRouteTableAssociation = resourceVpcEndpoinRouteTableAssociation;
		this.route_table_id = resourceVpcEndpoinRouteTableAssociation.route_table_id;
		this.id = resourceVpcEndpoinRouteTableAssociation.id;
		this.vpc_endpoint_id = resourceVpcEndpoinRouteTableAssociation.vpc_endpoint_id;
		resource = true;
	}
	
	@Override
	public String toString()
	{
	  return ToStringBuilder.reflectionToString(this);
	}

	public ResourceVpcEndpoinRouteTableAssociation getResourceVpcEndpoinRouteTableAssociation() {
		return resourceVpcEndpoinRouteTableAssociation;
	}

}
