package com.jakub.tfutil.aws.nat_gateway;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

public class Attributes {
	public String id;
	public String allocation_id;
	public String network_interface_id;
	public String private_ip;
	public String public_ip;
	public String subnet_id;
	@SerializedName("tags.%")
	public int tagsCount;
	@SerializedName("tags.Name")
	public String tagsName;
	
	@Override
	public String toString()
	{
	  return ToStringBuilder.reflectionToString(this);
	}
}
