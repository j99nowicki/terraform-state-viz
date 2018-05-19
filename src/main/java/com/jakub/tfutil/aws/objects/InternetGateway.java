package com.jakub.tfutil.aws.objects;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.jakub.tfutil.aws.resources.ResourceInternetGateway;

public class InternetGateway extends TfObject{
	public String id;
	public int tagsCount;
	public String tagsName;
	public String vpc_id;
	private ResourceInternetGateway resourceInternetGateway;
	
	public InternetGateway(ResourceInternetGateway resourceInternetGateway) {
		this.resourceInternetGateway = resourceInternetGateway;
		this.id = resourceInternetGateway.id;
		this.tagsCount = resourceInternetGateway.tagsCount;
		this.tagsName =resourceInternetGateway.tagsName;
		this.vpc_id =resourceInternetGateway.vpc_id;
		resource = true;
	}
	
	@Override
	public String toString()
	{
	  return ToStringBuilder.reflectionToString(this);
	}

	public ResourceInternetGateway getResourceInternetGateway() {
		return resourceInternetGateway;
	}

}
