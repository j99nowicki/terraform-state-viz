package com.jakub.tfutil.aws.data;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

public class DataAmi extends TfData{
	
	public String architecture;
	@SerializedName("block_device_mappings.#")
	public int block_device_mappingsCount;
	public String creation_date;
	public String description;
	@SerializedName("filter.#")
	public int filterCount;
	public String hypervisor;
	public String id;
	public String image_id;
	public String image_location;
	public String image_owner_alias;
	public String image_type;
	public boolean most_recent;
	public String name;
	public String owner_id;
	@SerializedName("product_codes.#")
	public String product_codesCount;
	@SerializedName("public")
	public boolean isPublic;
	public String root_device_name;
	public String root_device_type;
	public String root_snapshot_id;
	public String sriov_net_support;
	public String state;
	@SerializedName("state_reason.%")
	public int state_reasonCount;
	@SerializedName("state_reason.code")
	public String state_reasonCode;
	@SerializedName("state_reason.message")
	public String state_reasonMessage;
	@SerializedName("tags.%")
	public int tagsCount;
	public String virtualization_type;
	
	@Override
    public String toString()
    {
      return ToStringBuilder.reflectionToString(this);
    }  
}
