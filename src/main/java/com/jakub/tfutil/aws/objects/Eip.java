package com.jakub.tfutil.aws.objects;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.jakub.tfutil.aws.resources.ResourceEip;

public class Eip extends TfObject{

	public String association_id;
	public String domain;
	public String id;
	public String instance;
	public String network_interface;
	public String private_ip;
	public String public_ip;
	public boolean vpc;
	public int tagsCount;
	public String tagsName;
	private ResourceEip resourceEip;
	
	public Eip(ResourceEip resourceEip) {
		this.resourceEip = resourceEip;
		this.association_id = resourceEip.association_id;
		this.domain = resourceEip.domain;
		this.id = resourceEip.id;
		this.instance = resourceEip.instance;
		this.network_interface = resourceEip.network_interface;
		this.private_ip = resourceEip.private_ip;
		this.public_ip = resourceEip.public_ip;
		this.vpc = resourceEip.vpc;
		this.tagsCount = resourceEip.tagsCount;
		this.tagsName = resourceEip.tagsName;
		resource = true;
	}
	
	@Override
	public String toString()
	{
	  return ToStringBuilder.reflectionToString(this);
	}

	public ResourceEip getResourceEip() {
		return resourceEip;
	}

}
