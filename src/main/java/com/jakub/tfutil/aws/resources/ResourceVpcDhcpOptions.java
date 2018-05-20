package com.jakub.tfutil.aws.resources;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

public class ResourceVpcDhcpOptions extends TfResource{

	public String domain_name;
	@SerializedName("domain_name_servers.#")
	public int domainNameServersCount;
	public String id;
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
