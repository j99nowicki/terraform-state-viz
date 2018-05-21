package com.jakub.tfutil.aws.objects;
import java.util.HashSet;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.jakub.tfutil.aws.resources.ResourceElasticacheSubnetGroup;

public class ElasticacheSubnetGroup extends TfObject{
	
	public String description;
	public String id;
	public String name;
	public int subnetIdsCount;
	public int tagsCount;
	public String tagsName;
	public HashSet<String> subnetIds;
	private ResourceElasticacheSubnetGroup resourceElasticacheSubnetGroup;

	public ElasticacheSubnetGroup(ResourceElasticacheSubnetGroup resourceElasticacheSubnetGroup) {
		this.resourceElasticacheSubnetGroup = resourceElasticacheSubnetGroup;
		this.description = resourceElasticacheSubnetGroup.description;
		this.id = resourceElasticacheSubnetGroup.id;
		this.name = resourceElasticacheSubnetGroup.name;
		this.subnetIdsCount = resourceElasticacheSubnetGroup.subnetIdsCount;
		this.tagsCount = resourceElasticacheSubnetGroup.tagsCount;
		this.tagsName = resourceElasticacheSubnetGroup.tagsName;
		this.subnetIds = resourceElasticacheSubnetGroup.subnetIds;
		
		resource = true;
	}

	@Override
	public String toString()
	{
	  return ToStringBuilder.reflectionToString(this);
	}

	public ResourceElasticacheSubnetGroup getResourceElasticacheSubnetGroup() {
		return resourceElasticacheSubnetGroup;
	}
		
}
