package com.jakub.tfutil.aws.objects;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.jakub.tfutil.aws.resources.ResourceInstance;

public class Instance extends TfObject{
	public String ami;
	public boolean associate_public_ip_address;
    public String availability_zone;
    public boolean disable_api_termination;
	public int ebs_block_device_count;
    public boolean ebs_optimized;
	public int ephemeral_block_device_count;
    public boolean get_password_data;
    public String iam_instance_profile;
    public String id;
    public String instance_state;
    public String instance_type;
	public int ipv6_address_count;
	public int ipv6_addresses_count;
    public String key_name;
    public boolean monitoring;
    public int network_interface_count;
    public String network_interface_id;
    public String password_data;
    public String placement_group;
    public String primary_network_interface_id;
    public String private_dns;
    public String private_ip;
    public String public_dns;
    public String public_ip;
	public int root_block_device_count;
	public int security_groups_count;
    public boolean source_dest_check;
    public String subnet_id;
    public String tenancy;
    public String user_data;
	public int tagsCount;
	public String tagsName;
	public int vpc_security_group_ids_count;
	
	private ResourceInstance resourceInstance;
	
	public Instance(ResourceInstance resourceInstance) {
		this.resourceInstance = resourceInstance;
		this.ami = resourceInstance.ami;
		this.associate_public_ip_address = resourceInstance.associate_public_ip_address;
		this.availability_zone = resourceInstance.availability_zone;
		this.disable_api_termination = resourceInstance.disable_api_termination;
		this.ebs_block_device_count = resourceInstance.ebs_block_device_count;
		this.ebs_optimized = resourceInstance.ebs_optimized;
		this.ephemeral_block_device_count = resourceInstance.ephemeral_block_device_count;
		this.get_password_data = resourceInstance.get_password_data;
		this.iam_instance_profile = resourceInstance.iam_instance_profile;
		this.id = resourceInstance.id;
		this.instance_state = resourceInstance.instance_state;
		this.instance_type = resourceInstance.instance_type;
		this.ipv6_address_count = resourceInstance.ipv6_address_count;
		this.ipv6_addresses_count = resourceInstance.ipv6_addresses_count;
		this.key_name = resourceInstance.key_name;
		this.monitoring = resourceInstance.monitoring;
		this.network_interface_count = resourceInstance.network_interface_count;
		this.network_interface_id = resourceInstance.network_interface_id;
		this.password_data = resourceInstance.password_data;
		this.placement_group = resourceInstance.placement_group;
		this.primary_network_interface_id = resourceInstance.primary_network_interface_id;
		this.private_dns = resourceInstance.private_dns;
		this.private_ip = resourceInstance.private_ip;
		this.public_dns = resourceInstance.public_dns;
		this.public_ip = resourceInstance.public_ip;
		this.root_block_device_count = resourceInstance.root_block_device_count;
		this.security_groups_count = resourceInstance.security_groups_count;
		this.source_dest_check = resourceInstance.source_dest_check;
		this.subnet_id = resourceInstance.subnet_id;
		this.tenancy = resourceInstance.tenancy;
		this.user_data = resourceInstance.user_data;
		this.tagsCount = resourceInstance.tagsCount;
		this.tagsName = resourceInstance.tagsName;
		this.vpc_security_group_ids_count = resourceInstance.vpc_security_group_ids_count;
		
		resource = true;
	}

    public String toString()
    {
      return ToStringBuilder.reflectionToString(this);
    }

	public ResourceInstance getResourceInstance() {
		return resourceInstance;
	}

}
