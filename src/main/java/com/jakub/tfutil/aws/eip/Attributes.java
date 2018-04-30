package com.jakub.tfutil.aws.eip;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

public class Attributes {

	public String association_id;
	public String domain;
	public String id;
	public String instance;
	public String network_interface;
	public String private_ip;
	public String public_ip;
	public boolean vpc;
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
