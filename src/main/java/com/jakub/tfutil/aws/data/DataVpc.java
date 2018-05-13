package com.jakub.tfutil.aws.data;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

public class DataVpc extends TfData{
	public String cidr_block;
	@SerializedName("default")
	public boolean isDefault;
	public String dhcp_options_id;
	public boolean enable_dns_hostnames;
	public boolean enable_dns_support;
	public String id;
	public String instance_tenancy;
	public String state;
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
