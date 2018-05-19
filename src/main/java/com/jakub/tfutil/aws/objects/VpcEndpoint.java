package com.jakub.tfutil.aws.objects;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.jakub.tfutil.aws.resources.ResourceVpcEndpoint;

public class VpcEndpoint extends TfObject{

	public String cidrBlocksCount;
	public String dnsEntryCount;
    public String id;
	public String networkInterfacesIdsCount;
    public String policy;
    public String prefix_list_id;
    public boolean private_dns_enabled;
	public String routeTableIdsCount;
	public String securityGroupIdsCount;
    public String service_name;
    public String state;
	public String subnetIdsCount;
    public String vpc_endpoint_type;
    public String vpc_id;
    
    private ResourceVpcEndpoint resourceVpcEndpoint;
    
    public VpcEndpoint(ResourceVpcEndpoint resourceVpcEndpoint) {
    	this.resourceVpcEndpoint = resourceVpcEndpoint;
		this.cidrBlocksCount = resourceVpcEndpoint.cidrBlocksCount;
		this.dnsEntryCount= resourceVpcEndpoint.dnsEntryCount;
		this.id= resourceVpcEndpoint.id;
		this.networkInterfacesIdsCount= resourceVpcEndpoint.networkInterfacesIdsCount;
		this.policy= resourceVpcEndpoint.policy;
		this.prefix_list_id= resourceVpcEndpoint.prefix_list_id;
		this.private_dns_enabled= resourceVpcEndpoint.private_dns_enabled;
		this.routeTableIdsCount= resourceVpcEndpoint.routeTableIdsCount;
		this.securityGroupIdsCount= resourceVpcEndpoint.securityGroupIdsCount;
		this.service_name= resourceVpcEndpoint.service_name;
		this.state= resourceVpcEndpoint.state;
		this.subnetIdsCount= resourceVpcEndpoint.subnetIdsCount;
		this.vpc_endpoint_type= resourceVpcEndpoint.vpc_endpoint_type;
		this.vpc_id= resourceVpcEndpoint.vpc_id;
		this.resource = true;
	}
    
	@Override
	public String toString()
	{
	  return ToStringBuilder.reflectionToString(this);
	}

	public ResourceVpcEndpoint getResourceVpcEndpoint() {
		return resourceVpcEndpoint;
	}
	
	
}
