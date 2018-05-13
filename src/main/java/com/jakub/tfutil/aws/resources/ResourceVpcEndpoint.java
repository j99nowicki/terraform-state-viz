package com.jakub.tfutil.aws.resources;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

public class ResourceVpcEndpoint extends TfResource{

	@SerializedName("cidr_blocks.#")
	public String CidrBlocksCount;
//    "cidr_blocks.0": "52.94.5.0/24",
//    "cidr_blocks.1": "52.119.240.0/21",
//    "cidr_blocks.2": "52.94.24.0/23",
	@SerializedName("dns_entry.#")
	public String dnsEntryCount;
    public String id;
	@SerializedName("network_interface_ids.#")
	public String networkInterfacesIdsCount;
    public String policy;
    public String prefix_list_id;
    public boolean private_dns_enabled;
	@SerializedName("route_table_ids.#")
	public String routeTableIdsCount;
	@SerializedName("security_group_ids.#")
	public String securityGroupIdsCount;
    public String service_name;
    public String state;
	@SerializedName("subnet_ids.#")
	public String subnetIdsCount;
    public String vpc_endpoint_type;
    public String vpc_id;
    
    
	@Override
	public String toString()
	{
	  return ToStringBuilder.reflectionToString(this);
	}
}
