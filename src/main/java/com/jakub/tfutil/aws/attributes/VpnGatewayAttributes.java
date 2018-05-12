package com.jakub.tfutil.aws.attributes;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

public class VpnGatewayAttributes extends TfAttributes{
	public String vpc_id;
	public String id;
	public int amazon_side_asn;
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
