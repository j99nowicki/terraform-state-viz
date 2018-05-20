package com.jakub.tfutil.aws.objects;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.jakub.tfutil.aws.resources.ResourceRouteTable;

public class RouteTable extends TfObject{

	public String destination_cidr_block;
	public String id;
	public int tagsCount;
	public String tagsName;
	public String vpc_id;
	private ResourceRouteTable resourceRouteTable;
	
	public RouteTable(ResourceRouteTable resourceRouteTable) {
		this.resourceRouteTable = resourceRouteTable;
		this.destination_cidr_block = resourceRouteTable.destination_cidr_block;
		this.id = resourceRouteTable.id;
		this.tagsCount = resourceRouteTable.tagsCount;
		this.tagsName = resourceRouteTable.tagsName;
		this.vpc_id = resourceRouteTable.vpc_id;		
		resource = true;
	}
	
	@Override
	public String toString()
	{
	  return ToStringBuilder.reflectionToString(this);
	}

	public ResourceRouteTable getResourceRouteTable() {
		return resourceRouteTable;
	}

}
