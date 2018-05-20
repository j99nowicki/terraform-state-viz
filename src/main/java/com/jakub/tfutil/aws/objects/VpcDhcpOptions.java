package com.jakub.tfutil.aws.objects;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.jakub.tfutil.aws.resources.ResourceVpcDhcpOptions;

public class VpcDhcpOptions extends TfObject{

	public String domain_name;
	public int domainNameServersCount;
	public String id;
	public int tagsCount;
	public String tagsName;
	private ResourceVpcDhcpOptions resourceVpcDhcpOptions;

	public VpcDhcpOptions(ResourceVpcDhcpOptions resourceVpcDhcpOptions) {
		this.resourceVpcDhcpOptions = resourceVpcDhcpOptions;
		this.domain_name = resourceVpcDhcpOptions.domain_name;
		this.domainNameServersCount = resourceVpcDhcpOptions.domainNameServersCount;
		this.id = resourceVpcDhcpOptions.id;
		this.tagsCount = resourceVpcDhcpOptions.tagsCount;
		this.tagsName = resourceVpcDhcpOptions.tagsName;
		resource = true;
	}
	
	@Override
	public String toString()
	{
	  return ToStringBuilder.reflectionToString(this);
	}

	public ResourceVpcDhcpOptions getResourceVpcDhcpOptions() {
		return resourceVpcDhcpOptions;
	}
}
