package com.jakub.tfutil;

import java.util.HashMap;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.jakub.tfutil.aws.data.DataSubnetIds;
import com.jakub.tfutil.aws.data.DataVpc;
import com.jakub.tfutil.aws.resources.ResourceEip;
import com.jakub.tfutil.aws.resources.ResourceInstance;
import com.jakub.tfutil.aws.resources.ResourceInternetGateway;
import com.jakub.tfutil.aws.resources.ResourceNatGateway;
import com.jakub.tfutil.aws.resources.ResourceRoute;
import com.jakub.tfutil.aws.resources.ResourceRouteTableAssociation;
import com.jakub.tfutil.aws.resources.ResourceRouteTable;
import com.jakub.tfutil.aws.resources.ResourceSecurityGroup;
import com.jakub.tfutil.aws.resources.ResourceSecurityGroupRule;
import com.jakub.tfutil.aws.resources.ResourceSubnet;
import com.jakub.tfutil.aws.resources.ResourceVpc;
import com.jakub.tfutil.aws.resources.ResourceVpcEndpoinRouteTableAssociation;
import com.jakub.tfutil.aws.resources.ResourceVpcEndpoint;
import com.jakub.tfutil.aws.resources.ResourceVpnGateway;

public class TfObjectsWarehouse {
	public HashMap<String, ResourceVpc> rVpcs;
	public HashMap<String, ResourceSubnet> rSubnets;
	public HashMap<String, ResourceRoute> rRoutes;
	public HashMap<String, ResourceRouteTable> rRouteTables;
	public HashMap<String, ResourceRouteTableAssociation> rRouteTableAssociations;
	public HashMap<String, ResourceInternetGateway> rInternetGateways;
	public HashMap<String, ResourceNatGateway> rNatGateways;
	public HashMap<String, ResourceEip> rEips;
	public HashMap<String, ResourceVpnGateway> rVpnGateways;
	public HashMap<String, ResourceVpcEndpoint> rVpcEndpoints;
	public HashMap<String, ResourceSecurityGroup> rSecurityGroups;
	public HashMap<String, ResourceSecurityGroupRule> rSecurityGroupRules;
	public HashMap<String, ResourceInstance> rInstances;
	public HashMap<String, ResourceVpcEndpoinRouteTableAssociation> rVpcEndpoinRouteTableAssociations;

	public HashMap<String, DataSubnetIds> dSubnetIdss;
	public HashMap<String, DataVpc> dVpcs;

	public TfObjectsWarehouse() {
		this.rVpcs = new HashMap<>();
		this.rSubnets = new HashMap<>();
		this.rRoutes = new HashMap<>();
		this.rRouteTables = new HashMap<>();
		this.rRouteTableAssociations = new HashMap<>();
		this.rInternetGateways = new HashMap<>();
		this.rNatGateways = new HashMap<>();
		this.rEips = new HashMap<>();
		this.rVpnGateways = new HashMap<>();
		this.rVpcEndpoints = new HashMap<>();
		this.rSecurityGroups = new HashMap<>();
		this.rSecurityGroupRules = new HashMap<>();
		this.rInstances = new HashMap<>();
		this.rVpcEndpoinRouteTableAssociations = new HashMap<>(); 

		this.dSubnetIdss = new HashMap<>();
		this.dVpcs = new HashMap<>();
	}
	@Override
	public String toString()
	{
	  return ToStringBuilder.reflectionToString(this);
	}
	
	public String sufixFrom(String key){
		return key.substring(key.indexOf(".")+1, key.length());
	}

	
}
