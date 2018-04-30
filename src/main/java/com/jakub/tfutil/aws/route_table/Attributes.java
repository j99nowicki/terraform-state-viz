package com.jakub.tfutil.aws.route_table;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

public class Attributes {

	public String destination_cidr_block;
	public String id;
	@SerializedName("tags.%")
	public int tagsCount;
	@SerializedName("tags.Name")
	public String tagsName;
	public String vpc_id;
	
	@Override
	public String toString()
	{
	  return ToStringBuilder.reflectionToString(this);
	}
}
