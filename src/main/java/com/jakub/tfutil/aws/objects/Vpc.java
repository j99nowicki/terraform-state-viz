package com.jakub.tfutil.aws.objects;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.jakub.tfutil.aws.data.DataVpc;
import com.jakub.tfutil.aws.resources.ResourceVpc;

public class Vpc extends TfObject{
	public String cidr_block;
	public String dhcp_options_id;
	public boolean enable_dns_hostnames;
	public boolean enable_dns_support;
	public String id;
	public String instance_tenancy;
	public int tagsCount;
	public String tagsName;

	private Set<String> availabilityZones= new HashSet<>();
	
	private DataVpc dataVpc = null;
	private ResourceVpc resourcsVpc = null;
			
	@Override
	public String toString()
	{
		return ToStringBuilder.reflectionToString(this);
	}  
	
	public Vpc(DataVpc dataVpc) {
		this.dataVpc = dataVpc;
		this.cidr_block = dataVpc.cidr_block;
		this.dhcp_options_id = dataVpc.dhcp_options_id;
		this.enable_dns_hostnames = dataVpc.enable_dns_hostnames ;
		this.enable_dns_support = dataVpc.enable_dns_support;
		this.id = dataVpc.id;
		this.instance_tenancy = dataVpc.instance_tenancy;
		this.tagsCount = dataVpc.tagsCount;
		this.tagsName = dataVpc.tagsName;
		resource = false;
	}

	public Vpc(ResourceVpc resourcsVpc) {
		this.resourcsVpc = resourcsVpc;
		this.cidr_block = resourcsVpc.cidr_block;
		this.dhcp_options_id = resourcsVpc.dhcp_options_id;
		this.enable_dns_hostnames = resourcsVpc.enable_dns_hostnames ;
		this.enable_dns_support = resourcsVpc.enable_dns_support;
		this.id = resourcsVpc.id;
		this.instance_tenancy = resourcsVpc.instance_tenancy;
		this.tagsCount = resourcsVpc.tagsCount;
		this.tagsName = resourcsVpc.tagsName;
		resource = true;
	}

	public DataVpc getDataVpc() {
		return dataVpc;
	}

	public ResourceVpc getResourcsVpc() {
		return resourcsVpc;
	}

	public Set<String> getAvailabilityZones() {
		return availabilityZones;
	}

	public void setAvailabilityZones(Set<String> availabilityZones) {
		this.availabilityZones = availabilityZones;
	}

}
