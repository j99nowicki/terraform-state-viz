package com.jakub.tfutil.aws.objects;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.jakub.tfutil.aws.data.DataAmi;

public class Ami extends TfObject{
	
	public String architecture;
	public int block_device_mappingsCount;
	public String creation_date;
	public String description;
	public int filterCount;
	public String hypervisor;
	public String id;
	public String image_id;
	public String image_location;
	public String image_owner_alias;
	public String image_type;
	public String name;
	public String owner_id;
	public String product_codesCount;
	public boolean isPublic;
	public String root_device_name;
	public String root_device_type;
	public String root_snapshot_id;
	public String sriov_net_support;
	public String state;
	public int state_reasonCount;
	public String state_reasonCode;
	public String state_reasonMessage;
	public int tagsCount;
	public String virtualization_type;
	
	private DataAmi dataAmi;
	
	public Ami(DataAmi dataAmi) {
		this.dataAmi = dataAmi;
		this.architecture = dataAmi.architecture;
		this.block_device_mappingsCount = dataAmi.block_device_mappingsCount;
		this.creation_date = dataAmi.creation_date;
		this.description = dataAmi.description;
		this.filterCount = dataAmi.filterCount;
		this.hypervisor = dataAmi.hypervisor;
		this.id = dataAmi.id;
		this.image_id = dataAmi.image_id;
		this.image_location = dataAmi.image_location;
		this.image_owner_alias = dataAmi.image_owner_alias;
		this.image_type = dataAmi.image_type;
		this.name = dataAmi.name;
		this.owner_id = dataAmi.owner_id;
		this.product_codesCount = dataAmi.product_codesCount;
		this.isPublic = dataAmi.isPublic;
		this.root_device_name = dataAmi.root_device_name;
		this.root_device_type = dataAmi.root_device_type;
		this.root_snapshot_id = dataAmi.root_snapshot_id;
		this.sriov_net_support = dataAmi.sriov_net_support;
		this.state = dataAmi.state;
		this.state_reasonCount = dataAmi.state_reasonCount;
		this.state_reasonCode = dataAmi.state_reasonCode;
		this.state_reasonMessage = dataAmi.state_reasonMessage;
		this.tagsCount = dataAmi.tagsCount;
		this.virtualization_type = dataAmi.virtualization_type;
		resource = false;
	}
	
	@Override
    public String toString()
    {
      return ToStringBuilder.reflectionToString(this);
    }

	public DataAmi getDataAmi() {
		return dataAmi;
	} 
}
