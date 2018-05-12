package com.jakub.tfutil;

import java.util.HashMap;
import java.util.HashSet;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.jakub.tfutil.aws.attributes.EipAttributes;
import com.jakub.tfutil.aws.attributes.InstanceAttributes;
import com.jakub.tfutil.aws.attributes.InternetGatewayAttributes;
import com.jakub.tfutil.aws.attributes.NatGatewayAttributes;
import com.jakub.tfutil.aws.attributes.RouteAttributes;
import com.jakub.tfutil.aws.attributes.RouteTableAssociationAttributes;
import com.jakub.tfutil.aws.attributes.RouteTableAttributes;
import com.jakub.tfutil.aws.attributes.SecurityGroupAttributes;
import com.jakub.tfutil.aws.attributes.SecurityGroupRuleAttributes;
import com.jakub.tfutil.aws.attributes.SubnetAttributes;
import com.jakub.tfutil.aws.attributes.SubnetIdsAttributes;
import com.jakub.tfutil.aws.attributes.VpcAttributes;
import com.jakub.tfutil.aws.attributes.VpcEndpointAttributes;
import com.jakub.tfutil.aws.attributes.VpnGatewayAttributes;

public class Model {
	public HashMap<String, VpcAttributes> vpcs;
	public HashMap<String, SubnetAttributes> subnets;
	public HashMap<String, SubnetIdsAttributes> subnetIdss;
	public HashMap<String, RouteAttributes> routes;
	public HashMap<String, RouteTableAttributes> routeTables;
	public HashMap<String, RouteTableAssociationAttributes> routeTableAssociations;
	public HashMap<String, InternetGatewayAttributes> internetGateways;
	public HashMap<String, NatGatewayAttributes> natGateways;
	public HashMap<String, EipAttributes> eips;
	public HashMap<String, VpnGatewayAttributes> vpnGateways;
	public HashMap<String, VpcEndpointAttributes> vpcEndpoints;
	public HashMap<String, SecurityGroupAttributes> securityGroups;
	public HashMap<String, SecurityGroupRuleAttributes> securityGroupRules;
	public HashMap<String, InstanceAttributes> instances;
	
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
	
	public SubnetAttributes findSubnetAttributes(String idSubnet){
		for (String id : subnets.keySet()) {
			SubnetAttributes attr = subnets.get(id);
			if (attr.id.equals(idSubnet)){
				return subnets.get(id);
			}
		}
		return null;
	}
	
	public RouteTableAttributes findRouteTableAttributes(String idRouteTable){
		for (String id : routeTables.keySet()) {
			RouteTableAttributes attr = routeTables.get(id);
			if (attr.id.equals(idRouteTable)){
				return routeTables.get(id);
			}
		}
		return null;
	}
	public HashMap<String, RouteAttributes> findRoutesAttributesInTable(String idRouteTable){
		HashMap<String, RouteAttributes> matchingAttributes = new HashMap<>();
		for (String id : routes.keySet()) {
			RouteAttributes attr = routes.get(id);
			if (attr.route_table_id.equals(idRouteTable)){
				matchingAttributes.put(attr.id, attr);
			}
		}
		return matchingAttributes;
	}


	public HashSet<String> findAvailabilityZonesInVpc(String vpcId){
		HashSet<String> zones = new HashSet<String>();
		for (String id : subnets.keySet()) {
			SubnetAttributes attr = subnets.get(id);
			if (vpcId.equals(attr.vpc_id)){
				zones.add(attr.availability_zone);
			}
		}
		
		return zones;
	}
	
	public HashMap<String, SubnetAttributes> findSubnetsAttributesInVpcInZone(String idVpc, String zone){
		HashMap<String, SubnetAttributes> matchingAttributes = new HashMap<>();
		for (String id : subnets.keySet()) {
			SubnetAttributes attr = subnets.get(id);
			if (attr.vpc_id.equals(idVpc) && attr.availability_zone.equals(zone)){
				matchingAttributes.put(id, attr);
			}
		}
		return matchingAttributes;
	}
	
	public HashMap<String, NatGatewayAttributes> findNatGatewaysAttributesInSubnet(String idSubnet){
		HashMap<String, NatGatewayAttributes> matchingAttributes = new HashMap<>();
		for (String id : natGateways.keySet()) {
			NatGatewayAttributes attr = natGateways.get(id);
			if (attr.subnet_id.equals(idSubnet)){
				matchingAttributes.put(id, attr);
			}
		}
		return matchingAttributes;
	}
	
	public HashMap<String, InstanceAttributes> findInstancesInSubnet(String idSubnte){
		HashMap<String, InstanceAttributes> matchingAttributes = new HashMap<>();
		for (String id : instances.keySet()) {
			InstanceAttributes attr = instances.get(id);
			if (attr.subnet_id.equals(idSubnte)){
				matchingAttributes.put(id, attr);
			}
		}
		return matchingAttributes;
	}	
	
	public EipAttributes findEipAttributes(String idEip){
		EipAttributes eipAttributes = null;
		for (String id : eips.keySet()) {
			eipAttributes = eips.get(id);
			if (eipAttributes.id.equals(idEip)){
				return eipAttributes;
			}
		}
		return null;
	}
	
	public HashMap<String, InternetGatewayAttributes> findInternetGatewaysAttributesInVpc(String idVpc) {
		HashMap<String, InternetGatewayAttributes> matchingAttributes = new HashMap<>();
		for (String id : internetGateways.keySet()) {
			InternetGatewayAttributes attr = internetGateways.get(id);
			if (attr.vpc_id.equals(idVpc)){
				matchingAttributes.put(id, attr);
			}
		}
		return matchingAttributes;
	}

	public HashMap<String, VpnGatewayAttributes> findVpnGatewaysAttributesInVpc(String idVpc) {
		HashMap<String, VpnGatewayAttributes> matchingAttributes = new HashMap<>();
		for (String id : vpnGateways.keySet()) {
			VpnGatewayAttributes attr = vpnGateways.get(id);
			if (attr.vpc_id.equals(idVpc)){
				matchingAttributes.put(id, attr);
			}
		}
		return matchingAttributes;
	}
	
	public HashMap<String, VpcEndpointAttributes> findVpcEndpointAttributesInVpc(String idVpc) {
		HashMap<String, VpcEndpointAttributes> matchingAttributes = new HashMap<>();
		for (String id : vpcEndpoints.keySet()) {
			VpcEndpointAttributes attr = vpcEndpoints.get(id);
			if (attr.vpc_id.equals(idVpc)){
				matchingAttributes.put(id, attr);
			}
		}
		return matchingAttributes;
	}

	public HashMap<String, RouteTableAttributes> findRouteTablesAttributesInVpc(String idVpc) {
		HashMap<String, RouteTableAttributes> matchingAttributes = new HashMap<>();
		for (String id : routeTables.keySet()) {
			RouteTableAttributes attr = routeTables.get(id);
			if (attr.vpc_id.equals(idVpc)){
				matchingAttributes.put(id, attr);
			}
		}
		return matchingAttributes;
	}

	public HashMap<String, RouteTableAssociationAttributes> findRouteTablesAssociationAttributesForRouteTables(String idRouteTable) {
		HashMap<String, RouteTableAssociationAttributes> matchingAttributes = new HashMap<>();
		for (String id : routeTableAssociations.keySet()) {
			RouteTableAssociationAttributes attr = routeTableAssociations.get(id);
			if (attr.route_table_id.equals(idRouteTable)){
				matchingAttributes.put(id, attr);
			}
		}
		return matchingAttributes;
	}
	
	public HashMap<String, SubnetIdsAttributes> findSubnetIdssAttributesInVpc(String idVpc) {
		HashMap<String, SubnetIdsAttributes> matchingAttributes = new HashMap<>();
		for (String id : subnetIdss.keySet()) {
			SubnetIdsAttributes attr = subnetIdss.get(id);
			if (attr.vpc_id.equals(idVpc)){
				matchingAttributes.put(id, attr);
			}
		}
		return matchingAttributes;
	}
			
}
