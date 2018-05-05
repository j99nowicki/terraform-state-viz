package com.jakub.tfutil;

import java.util.HashMap;
import java.util.HashSet;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.jakub.tfutil.aws.EipAttributes;
import com.jakub.tfutil.aws.InstanceAttributes;
import com.jakub.tfutil.aws.InternetGatewayAttributes;
import com.jakub.tfutil.aws.VpnGatewayAttributes;
import com.jakub.tfutil.aws.NatGatewayAttributes;
import com.jakub.tfutil.aws.RouteAttributes;
import com.jakub.tfutil.aws.SecurityGroupRuleAttributes;
import com.jakub.tfutil.aws.RouteTableAttributes;
import com.jakub.tfutil.aws.RouteTableAssociationAttributes;
import com.jakub.tfutil.aws.SecurityGroupAttributes;
import com.jakub.tfutil.aws.SubnetAttributes;
import com.jakub.tfutil.aws.VpcAttributes;
import com.jakub.tfutil.aws.VpcEndpointAttributes;

public class Model {
	public VpcAttributes vpcAttributes;
	public HashMap<String, SubnetAttributes> subnetsAttributes;
	public HashMap<String, RouteAttributes> routesAttributes;
	public HashMap<String, RouteTableAttributes> routeTablesAttributes;
	public HashMap<String, RouteTableAssociationAttributes> routeTableAssociationsAttributes;
	public HashMap<String, InternetGatewayAttributes> internetGatewaysAttributes;
	public HashMap<String, NatGatewayAttributes> natGatewaysAttributes;
	public HashMap<String, EipAttributes> eipsAttributes;
	public HashMap<String, VpnGatewayAttributes> vpnGatewaysAttributes;
	public HashMap<String, VpcEndpointAttributes> vpcEndpointsAttributes;
	public HashMap<String, SecurityGroupAttributes> securityGroupsAttributes;
	public HashMap<String, SecurityGroupRuleAttributes> securityGroupRulesAttributes;
	public HashMap<String, InstanceAttributes> instancesAttributes;
	
	public Model() {
		this.vpcAttributes = null;
		this.subnetsAttributes = new HashMap<>();
		this.routesAttributes = new HashMap<>();
		this.routeTablesAttributes = new HashMap<>();
		this.routeTableAssociationsAttributes = new HashMap<>();
		this.internetGatewaysAttributes = new HashMap<>();
		this.natGatewaysAttributes = new HashMap<>();
		this.eipsAttributes = new HashMap<>();
		this.vpnGatewaysAttributes = new HashMap<>();
		this.vpcEndpointsAttributes = new HashMap<>();
		this.securityGroupsAttributes = new HashMap<>();
		this.securityGroupRulesAttributes = new HashMap<>();
		this.instancesAttributes = new HashMap<>();
	}
	@Override
	public String toString()
	{
	  return ToStringBuilder.reflectionToString(this);
	}
	
	public String sufixFrom(String key){
		return key.substring(key.indexOf(".")+1, key.length());
	}
	
	public SubnetAttributes findSubnetAttributes(String subnetId){
		for (String tfName : subnetsAttributes.keySet()) {
			SubnetAttributes attr = subnetsAttributes.get(tfName);
			if (attr.id.equals(subnetId)){
				return subnetsAttributes.get(tfName);
			}
		}
		return null;
	}
	
	public RouteTableAttributes findRouteTableAttributes(String routeTableId){
		for (String tfName : routeTablesAttributes.keySet()) {
			RouteTableAttributes attr = routeTablesAttributes.get(tfName);
			if (attr.id.equals(routeTableId)){
				return routeTablesAttributes.get(tfName);
			}
		}
		return null;
	}
	public HashMap<String, RouteAttributes> findRoutesAttributesInTable(String routeTableId){
		HashMap<String, RouteAttributes> matchingRoutesAttributes = new HashMap<>();
		for (String tfName : routesAttributes.keySet()) {
			RouteAttributes attr = routesAttributes.get(tfName);
			if (attr.route_table_id.equals(routeTableId)){
				matchingRoutesAttributes.put(attr.id, routesAttributes.get(tfName));
			}
		}
		return matchingRoutesAttributes;
	}

	public HashSet<String> findAvailabilityZones(){
		HashSet<String> zones = new HashSet<String>();
		for (String tfName : subnetsAttributes.keySet()) {
			SubnetAttributes attr = subnetsAttributes.get(tfName);
			zones.add(attr.availability_zone);
		}
		return zones;
	}
	
	public HashMap<String, SubnetAttributes> findSubnetsAttributesInZone(String zone){
		HashMap<String, SubnetAttributes> matchingSubnetsAttributes = new HashMap<>();
		for (String tfName : subnetsAttributes.keySet()) {
			SubnetAttributes attr = subnetsAttributes.get(tfName);
			if (attr.availability_zone.equals(zone)){
				matchingSubnetsAttributes.put(tfName, subnetsAttributes.get(tfName));
			}
		}
		return matchingSubnetsAttributes;
	}
	
	public HashMap<String, NatGatewayAttributes> findNatGatewaysAttributesInSubnet(String subnteId){
		HashMap<String, NatGatewayAttributes> matchingNatGatewaysAttributes = new HashMap<>();
		for (String tfName : natGatewaysAttributes.keySet()) {
			NatGatewayAttributes attr = natGatewaysAttributes.get(tfName);
			if (attr.subnet_id.equals(subnteId)){
				matchingNatGatewaysAttributes.put(tfName, natGatewaysAttributes.get(tfName));
			}
		}
		return matchingNatGatewaysAttributes;
	}
	
	public HashMap<String, InstanceAttributes> findInstancesInSubnet(String subnteId){
		HashMap<String, InstanceAttributes> matchingInstancesAttributes = new HashMap<>();
		for (String tfName : instancesAttributes.keySet()) {
			InstanceAttributes attr = instancesAttributes.get(tfName);
			if (attr.subnet_id.equals(subnteId)){
				matchingInstancesAttributes.put(tfName, instancesAttributes.get(tfName));
			}
		}
		return matchingInstancesAttributes;
	}	
	
	public EipAttributes findEipAttributes(String eipId){
		EipAttributes eipAttributes = null;
		for (String tfName : eipsAttributes.keySet()) {
			eipAttributes = eipsAttributes.get(tfName);
			if (eipAttributes.id.equals(eipId)){
				return eipAttributes;
			}
		}
		return null;
	}
	
	public HashMap<String, NatGatewayAttributes> findNatGatewaysAttributesInZones(HashSet<String> zones){
		HashMap<String, NatGatewayAttributes> matchingNatGatewaysAttributes = new HashMap<>();
		HashMap<String, NatGatewayAttributes> allNatGatewaysAttributes = new HashMap<>();
		for (String tfName : allNatGatewaysAttributes.keySet()) {
			NatGatewayAttributes natGatewayAttributes = natGatewaysAttributes.get(tfName);
			SubnetAttributes subnetAttributes = findSubnetAttributes(natGatewayAttributes.subnet_id);
			if (zones.contains(subnetAttributes.availability_zone)){
				matchingNatGatewaysAttributes.put(tfName, natGatewayAttributes);
			}
		}
		return matchingNatGatewaysAttributes;
	}
	
}
