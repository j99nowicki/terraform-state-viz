package com.jakub.tfutil.aws.attributes;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

public class InstanceAttributes extends TfAttributes{
	public String ami;
	public boolean associate_public_ip_address;
    public String availability_zone;
    public boolean disable_api_termination;
	@SerializedName("ebs_block_device.#")
	public int ebs_block_device_count;
    public boolean ebs_optimized;
	@SerializedName("ephemeral_block_device.#")
	public int ephemeral_block_device_count;
    public boolean get_password_data;
    public String iam_instance_profile;
    public String id;
    public String instance_state;
    public String instance_type;
	public int ipv6_address_count;
	@SerializedName("ipv6_addresses.#")
	public int ipv6_addresses_count;
    public String key_name;
    public boolean monitoring;
	@SerializedName("network_interface.#")
    public int network_interface_count;
    public String network_interface_id;
    public String password_data;
    public String placement_group;
    public String primary_network_interface_id;
    public String private_dns;
    public String private_ip;
    public String public_dns;
    public String public_ip;
	@SerializedName("root_block_device.#")
	public int root_block_device_count;
	@SerializedName("security_groups.#")
	public int security_groups_count;
    public boolean source_dest_check;
    public String subnet_id;
    public String tenancy;
    public String user_data;
	@SerializedName("tags.%")
	public int tagsCount;
	@SerializedName("tags.Name")
	public String tagsName;
	@SerializedName("vpc_security_group_ids.#")
	public int vpc_security_group_ids_count;
//    "vpc_security_group_ids.826112338": "sg-622ff51f"

    public String toString()
    {
      return ToStringBuilder.reflectionToString(this);
    }  
}
