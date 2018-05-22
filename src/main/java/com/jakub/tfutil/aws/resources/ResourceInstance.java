package com.jakub.tfutil.aws.resources;

import java.util.HashSet;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.google.gson.JsonElement;
import com.google.gson.annotations.SerializedName;

public class ResourceInstance extends TfResource{
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

	public HashSet<String> vpSecurityGroupIds;
	
	//custom parser for vpc_security_group_ids set
	public void parseVpSecurityGroupIds(Set<Entry<String, JsonElement>> entrySet ){
		vpSecurityGroupIds = new HashSet<String>();
		for (Entry<String, JsonElement> entry : entrySet) {
			if (entry.getKey().startsWith("vpc_security_group_ids.") && !entry.getKey().startsWith("vpc_security_group_ids.#")){
				vpSecurityGroupIds.add(entry.getValue().getAsString());
			}
		}
	}
    public String toString()
    {
      return ToStringBuilder.reflectionToString(this);
    }  
}
