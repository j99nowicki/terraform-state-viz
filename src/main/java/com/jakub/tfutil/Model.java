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
import com.jakub.tfutil.aws.attributes.VpcAttributes;
import com.jakub.tfutil.aws.attributes.VpcEndpointAttributes;
import com.jakub.tfutil.aws.attributes.VpnGatewayAttributes;

public class Model {
	public HashMap<String, VpcAttributes> vpcAttributes;
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
		this.vpcAttributes = new HashMap<>();
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
		HashMap<String, RouteAttributes> matchingAttributes = new HashMap<>();
		for (String tfName : routesAttributes.keySet()) {
			RouteAttributes attr = routesAttributes.get(tfName);
			if (attr.route_table_id.equals(routeTableId)){
				matchingAttributes.put(attr.id, attr);
			}
		}
		return matchingAttributes;
	}

	public HashSet<String> findAvailabilityZonesInVpc(String vpcId){
		HashSet<String> zones = new HashSet<String>();
		for (String tfName : subnetsAttributes.keySet()) {
			SubnetAttributes attr = subnetsAttributes.get(tfName);
			if (vpcId.equals(attr.vpc_id)){
				zones.add(attr.availability_zone);
			}
		}
		return zones;
	}
	
	public HashMap<String, SubnetAttributes> findSubnetsAttributesInVpcInZone(String vpcId, String zone){
		HashMap<String, SubnetAttributes> matchingAttributes = new HashMap<>();
		for (String tfName : subnetsAttributes.keySet()) {
			SubnetAttributes attr = subnetsAttributes.get(tfName);
			if (attr.vpc_id.equals(vpcId) && attr.availability_zone.equals(zone)){
				matchingAttributes.put(tfName, attr);
			}
		}
		return matchingAttributes;
	}
	
	public HashMap<String, NatGatewayAttributes> findNatGatewaysAttributesInSubnet(String subnteId){
		HashMap<String, NatGatewayAttributes> matchingAttributes = new HashMap<>();
		for (String tfName : natGatewaysAttributes.keySet()) {
			NatGatewayAttributes attr = natGatewaysAttributes.get(tfName);
			if (attr.subnet_id.equals(subnteId)){
				matchingAttributes.put(tfName, attr);
			}
		}
		return matchingAttributes;
	}
	
	public HashMap<String, InstanceAttributes> findInstancesInSubnet(String subnteId){
		HashMap<String, InstanceAttributes> matchingAttributes = new HashMap<>();
		for (String tfName : instancesAttributes.keySet()) {
			InstanceAttributes attr = instancesAttributes.get(tfName);
			if (attr.subnet_id.equals(subnteId)){
				matchingAttributes.put(tfName, attr);
			}
		}
		return matchingAttributes;
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
		HashMap<String, NatGatewayAttributes> matchingAttributes = new HashMap<>();
		HashMap<String, NatGatewayAttributes> allNatGatewaysAttributes = new HashMap<>();
		for (String tfName : allNatGatewaysAttributes.keySet()) {
			NatGatewayAttributes natGatewayAttributes = natGatewaysAttributes.get(tfName);
			SubnetAttributes subnetAttributes = findSubnetAttributes(natGatewayAttributes.subnet_id);
			if (zones.contains(subnetAttributes.availability_zone)){
				matchingAttributes.put(tfName, natGatewayAttributes);
			}
		}
		return matchingAttributes;
	}
	
	public HashMap<String, InternetGatewayAttributes> findInternetGatewaysAttributesInVpc(String vpcId) {
		HashMap<String, InternetGatewayAttributes> matchingAttributes = new HashMap<>();
		for (String tfName : internetGatewaysAttributes.keySet()) {
			InternetGatewayAttributes attr = internetGatewaysAttributes.get(tfName);
			if (attr.vpc_id.equals(vpcId)){
				matchingAttributes.put(tfName, attr);
			}
		}
		return matchingAttributes;
	}

	public HashMap<String, VpnGatewayAttributes> findVpnGatewaysAttributesInVpc(String vpcId) {
		HashMap<String, VpnGatewayAttributes> matchingAttributes = new HashMap<>();
		for (String tfName : vpnGatewaysAttributes.keySet()) {
			VpnGatewayAttributes attr = vpnGatewaysAttributes.get(tfName);
			if (attr.vpc_id.equals(vpcId)){
				matchingAttributes.put(tfName, attr);
			}
		}
		return matchingAttributes;
	}
	
	public HashMap<String, VpcEndpointAttributes> findVpcEndpointAttributesInVpc(String vpcId) {
		HashMap<String, VpcEndpointAttributes> matchingAttributes = new HashMap<>();
		for (String tfName : vpcEndpointsAttributes.keySet()) {
			VpcEndpointAttributes attr = vpcEndpointsAttributes.get(tfName);
			if (attr.vpc_id.equals(vpcId)){
				matchingAttributes.put(tfName, attr);
			}
		}
		return matchingAttributes;
	}

	public HashMap<String, RouteTableAttributes> findRouteTablesAttributesInVpc(String vpcId) {
		HashMap<String, RouteTableAttributes> matchingAttributes = new HashMap<>();
		for (String tfName : routeTablesAttributes.keySet()) {
			RouteTableAttributes attr = routeTablesAttributes.get(tfName);
			if (attr.vpc_id.equals(vpcId)){
				matchingAttributes.put(tfName, attr);
			}
		}
		return matchingAttributes;
	}
	
}
