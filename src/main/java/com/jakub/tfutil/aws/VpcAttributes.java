package com.jakub.tfutil.aws;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

public class VpcAttributes {
	public boolean assign_generated_ipv6_cidr_block;
	public String cidr_block;
	public String default_network_acl_id;
	public String default_route_table_id;
	public String default_security_group_id;
	public String dhcp_options_id;
	public boolean enable_classiclink;
	public boolean enable_classiclink_dns_support;
	public boolean enable_dns_hostnames;
	public boolean enable_dns_support;
	public String id;
	public String instance_tenancy;
	public String main_route_table_id;
	@SerializedName("tags.%")
	public int tagsCount;
	@SerializedName("tags.Name")
	public String tagsName;

	//custom - TODO try removing
    public String tfName;
	public String getTfName() {
		return tfName;
	}
	public void setTfName(String tfName) {
		this.tfName = tfName;
	} 	
	
	@Override
    public String toString()
    {
      return ToStringBuilder.reflectionToString(this);
    }  
}
