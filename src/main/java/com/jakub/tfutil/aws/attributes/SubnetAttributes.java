package com.jakub.tfutil.aws.attributes;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

public class SubnetAttributes extends TfAttributes{
	public boolean assign_ipv6_address_on_creation;
	public String availability_zone;
	public String cidr_block;
	public String id;
	public boolean map_public_ip_on_launch;
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
