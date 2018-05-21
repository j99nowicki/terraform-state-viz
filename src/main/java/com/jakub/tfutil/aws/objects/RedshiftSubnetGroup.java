package com.jakub.tfutil.aws.objects;
import java.util.HashSet;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.jakub.tfutil.aws.resources.ResourceRedshiftSubnetGroup;

public class RedshiftSubnetGroup extends TfObject{
	
	public String description;
	public String id;
	public String name;
	public int subnetIdsCount;
	public int tagsCount;
	public String tagsName;
	public HashSet<String> subnetIds;
	private ResourceRedshiftSubnetGroup resourceRedshiftSubnetGroup;

	public RedshiftSubnetGroup(ResourceRedshiftSubnetGroup resourceRedshiftSubnetGroup) {
		this.resourceRedshiftSubnetGroup = resourceRedshiftSubnetGroup;
		this.description = resourceRedshiftSubnetGroup.description;
		this.id = resourceRedshiftSubnetGroup.id;
		this.name = resourceRedshiftSubnetGroup.name;
		this.subnetIdsCount = resourceRedshiftSubnetGroup.subnetIdsCount;
		this.tagsCount = resourceRedshiftSubnetGroup.tagsCount;
		this.tagsName = resourceRedshiftSubnetGroup.tagsName;
		this.subnetIds = resourceRedshiftSubnetGroup.subnetIds;
		
		resource = true;
	}

	@Override
	public String toString()
	{
	  return ToStringBuilder.reflectionToString(this);
	}

	public ResourceRedshiftSubnetGroup getResourceRedshiftSubnetGroup() {
		return resourceRedshiftSubnetGroup;
	}
		
}
