package com.jakub.tfutil;

import java.util.HashMap;
import java.util.HashSet;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.jakub.tfutil.aws.data.DataSubnetIds;
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
import com.jakub.tfutil.aws.resources.ResourceVpcEndpoint;
import com.jakub.tfutil.aws.resources.ResourceVpnGateway;

public class Model {
	public HashMap<String, ResourceVpc> vpcs;
	public HashMap<String, ResourceSubnet> subnets;
	public HashMap<String, DataSubnetIds> subnetIdss;
	public HashMap<String, ResourceRoute> routes;
	public HashMap<String, ResourceRouteTable> routeTables;
	public HashMap<String, ResourceRouteTableAssociation> routeTableAssociations;
	public HashMap<String, ResourceInternetGateway> internetGateways;
	public HashMap<String, ResourceNatGateway> natGateways;
	public HashMap<String, ResourceEip> eips;
	public HashMap<String, ResourceVpnGateway> vpnGateways;
	public HashMap<String, ResourceVpcEndpoint> vpcEndpoints;
	public HashMap<String, ResourceSecurityGroup> securityGroups;
	public HashMap<String, ResourceSecurityGroupRule> securityGroupRules;
	public HashMap<String, ResourceInstance> instances;
	
	public Model() {
		this.vpcs = new HashMap<>();
		this.subnets = new HashMap<>();
		this.subnetIdss = new HashMap<>();
		this.routes = new HashMap<>();
		this.routeTables = new HashMap<>();
		this.routeTableAssociations = new HashMap<>();
		this.internetGateways = new HashMap<>();
		this.natGateways = new HashMap<>();
		this.eips = new HashMap<>();
		this.vpnGateways = new HashMap<>();
		this.vpcEndpoints = new HashMap<>();
		this.securityGroups = new HashMap<>();
		this.securityGroupRules = new HashMap<>();
		this.instances = new HashMap<>();
	}
	@Override
	public String toString()
	{
	  return ToStringBuilder.reflectionToString(this);
	}
	
	public String sufixFrom(String key){
		return key.substring(key.indexOf(".")+1, key.length());
	}
	
	public ResourceSubnet findSubnetAttributes(String idSubnet){
		for (String id : subnets.keySet()) {
			ResourceSubnet attr = subnets.get(id);
			if (attr.id.equals(idSubnet)){
				return subnets.get(id);
			}
		}
		return null;
	}
	
	public ResourceRouteTable findRouteTableAttributes(String idRouteTable){
		for (String id : routeTables.keySet()) {
			ResourceRouteTable attr = routeTables.get(id);
			if (attr.id.equals(idRouteTable)){
				return routeTables.get(id);
			}
		}
		return null;
	}
	public HashMap<String, ResourceRoute> findRoutesAttributesInTable(String idRouteTable){
		HashMap<String, ResourceRoute> matchingAttributes = new HashMap<>();
		for (String id : routes.keySet()) {
			ResourceRoute attr = routes.get(id);
			if (attr.route_table_id.equals(idRouteTable)){
				matchingAttributes.put(attr.id, attr);
			}
		}
		return matchingAttributes;
	}


	public HashSet<String> findAvailabilityZonesInVpc(String vpcId){
		HashSet<String> zones = new HashSet<String>();
		for (String id : subnets.keySet()) {
			ResourceSubnet attr = subnets.get(id);
			if (vpcId.equals(attr.vpc_id)){
				zones.add(attr.availability_zone);
			}
		}
		
		return zones;
	}
	
	public HashMap<String, ResourceSubnet> findSubnetsAttributesInVpcInZone(String idVpc, String zone){
		HashMap<String, ResourceSubnet> matchingAttributes = new HashMap<>();
		for (String id : subnets.keySet()) {
			ResourceSubnet attr = subnets.get(id);
			if (attr.vpc_id.equals(idVpc) && attr.availability_zone.equals(zone)){
				matchingAttributes.put(id, attr);
			}
		}
		return matchingAttributes;
	}
	
	public HashMap<String, ResourceNatGateway> findNatGatewaysAttributesInSubnet(String idSubnet){
		HashMap<String, ResourceNatGateway> matchingAttributes = new HashMap<>();
		for (String id : natGateways.keySet()) {
			ResourceNatGateway attr = natGateways.get(id);
			if (attr.subnet_id.equals(idSubnet)){
				matchingAttributes.put(id, attr);
			}
		}
		return matchingAttributes;
	}
	
	public HashMap<String, ResourceInstance> findInstancesInSubnet(String idSubnte){
		HashMap<String, ResourceInstance> matchingAttributes = new HashMap<>();
		for (String id : instances.keySet()) {
			ResourceInstance attr = instances.get(id);
			if (attr.subnet_id.equals(idSubnte)){
				matchingAttributes.put(id, attr);
			}
		}
		return matchingAttributes;
	}	
	
	public ResourceEip findEipAttributes(String idEip){
		ResourceEip eipAttributes = null;
		for (String id : eips.keySet()) {
			eipAttributes = eips.get(id);
			if (eipAttributes.id.equals(idEip)){
				return eipAttributes;
			}
		}
		return null;
	}
	
	public HashMap<String, ResourceInternetGateway> findInternetGatewaysAttributesInVpc(String idVpc) {
		HashMap<String, ResourceInternetGateway> matchingAttributes = new HashMap<>();
		for (String id : internetGateways.keySet()) {
			ResourceInternetGateway attr = internetGateways.get(id);
			if (attr.vpc_id.equals(idVpc)){
				matchingAttributes.put(id, attr);
			}
		}
		return matchingAttributes;
	}

	public HashMap<String, ResourceVpnGateway> findVpnGatewaysAttributesInVpc(String idVpc) {
		HashMap<String, ResourceVpnGateway> matchingAttributes = new HashMap<>();
		for (String id : vpnGateways.keySet()) {
			ResourceVpnGateway attr = vpnGateways.get(id);
			if (attr.vpc_id.equals(idVpc)){
				matchingAttributes.put(id, attr);
			}
		}
		return matchingAttributes;
	}
	
	public HashMap<String, ResourceVpcEndpoint> findVpcEndpointAttributesInVpc(String idVpc) {
		HashMap<String, ResourceVpcEndpoint> matchingAttributes = new HashMap<>();
		for (String id : vpcEndpoints.keySet()) {
			ResourceVpcEndpoint attr = vpcEndpoints.get(id);
			if (attr.vpc_id.equals(idVpc)){
				matchingAttributes.put(id, attr);
			}
		}
		return matchingAttributes;
	}

	public HashMap<String, ResourceRouteTable> findRouteTablesAttributesInVpc(String idVpc) {
		HashMap<String, ResourceRouteTable> matchingAttributes = new HashMap<>();
		for (String id : routeTables.keySet()) {
			ResourceRouteTable attr = routeTables.get(id);
			if (attr.vpc_id.equals(idVpc)){
				matchingAttributes.put(id, attr);
			}
		}
		return matchingAttributes;
	}

	public HashMap<String, ResourceRouteTableAssociation> findRouteTablesAssociationAttributesForRouteTables(String idRouteTable) {
		HashMap<String, ResourceRouteTableAssociation> matchingAttributes = new HashMap<>();
		for (String id : routeTableAssociations.keySet()) {
			ResourceRouteTableAssociation attr = routeTableAssociations.get(id);
			if (attr.route_table_id.equals(idRouteTable)){
				matchingAttributes.put(id, attr);
			}
		}
		return matchingAttributes;
	}
	
	public HashMap<String, DataSubnetIds> findSubnetIdssAttributesInVpc(String idVpc) {
		HashMap<String, DataSubnetIds> matchingAttributes = new HashMap<>();
		for (String id : subnetIdss.keySet()) {
			DataSubnetIds attr = subnetIdss.get(id);
			if (attr.vpc_id.equals(idVpc)){
				matchingAttributes.put(id, attr);
			}
		}
		return matchingAttributes;
	}
			
}
