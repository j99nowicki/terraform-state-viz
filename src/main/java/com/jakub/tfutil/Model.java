package com.jakub.tfutil;

import java.util.HashMap;
import java.util.HashSet;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.jakub.tfutil.aws.eip.Eip;
import com.jakub.tfutil.aws.internet_gateway.InternetGateway;
import com.jakub.tfutil.aws.nat_gateway.NatGateway;
import com.jakub.tfutil.aws.route.Route;
import com.jakub.tfutil.aws.route_table.RouteTable;
import com.jakub.tfutil.aws.route_table_association.RouteTableAssociation;
import com.jakub.tfutil.aws.subnet.Subnet;
import com.jakub.tfutil.aws.vpc.Vpc;
import com.jakub.tfutil.aws.vpc_endpoint.VpcEndpoint;
import com.jakub.tfutil.aws.vpn_gateway.VpnGateway;

public class Model {
	public Vpc vpc;
	public HashMap<String, Subnet> subnets;
	public HashMap<String, Route> routes;
	public HashMap<String, RouteTable> routeTables;
	public HashMap<String, RouteTableAssociation> routeTableAssociations;
	public HashMap<String, InternetGateway> internetGateways;
	public HashMap<String, NatGateway> natGateways;
	public HashMap<String, Eip> eips;
	public HashMap<String, VpnGateway> vpnGateways;
	public HashMap<String, VpcEndpoint> vpcEndpoints;
	
	public Model() {
		this.vpc = null;
		this.subnets = new HashMap<>();
		this.routes = new HashMap<>();
		this.routeTables = new HashMap<>();
		this.routeTableAssociations = new HashMap<>();
		this.internetGateways = new HashMap<>();
		this.natGateways = new HashMap<>();
		this.eips = new HashMap<>();
		this.vpnGateways = new HashMap<>();
		this.vpcEndpoints = new HashMap<>();
	}
	@Override
	public String toString()
	{
	  return ToStringBuilder.reflectionToString(this);
	}
	
	public String sufixFrom(String key){
		return key.substring(key.indexOf(".")+1, key.length());
	}
	
	public Subnet findSubnet(String subnetId){
		for (String tfName : subnets.keySet()) {
			com.jakub.tfutil.aws.subnet.Attributes attr = subnets.get(tfName).primary.attributes;
			if (attr.id.equals(subnetId)){
				return subnets.get(tfName);
			}
		}
		return null;
	}
	
	public RouteTable findRouteTable(String routeTableId){
		for (String tfName : routeTables.keySet()) {
			com.jakub.tfutil.aws.route_table.Attributes attr = routeTables.get(tfName).primary.attributes;
			if (attr.id.equals(routeTableId)){
				return routeTables.get(tfName);
			}
		}
		return null;
	}
	public HashMap<String, Route> findRoutesInTable(String routeTableId){
		HashMap<String, Route> matchingRoutes = new HashMap<>();
		for (String tfName : routes.keySet()) {
			com.jakub.tfutil.aws.route.Attributes attr = routes.get(tfName).primary.attributes;
			if (attr.route_table_id.equals(routeTableId)){
				matchingRoutes.put(attr.id, routes.get(tfName));
			}
		}
		return matchingRoutes;
	}

	public HashSet<String> findAvailabilityZones(){
		HashSet<String> zones = new HashSet<String>();
		for (String tfName : subnets.keySet()) {
			com.jakub.tfutil.aws.subnet.Attributes attr = subnets.get(tfName).primary.attributes;
			zones.add(attr.availability_zone);
		}
		return zones;
	}
	
	public HashMap<String, Subnet> findSubnetsInZone(String zone){
		HashMap<String, Subnet> matchingSubnets = new HashMap<>();
		for (String tfName : subnets.keySet()) {
			com.jakub.tfutil.aws.subnet.Attributes attr = subnets.get(tfName).primary.attributes;
			if (attr.availability_zone.equals(zone)){
				matchingSubnets.put(tfName, subnets.get(tfName));
			}
		}
		return matchingSubnets;
	}
	
	public HashMap<String, NatGateway> findNatGatewaysInSubnet(String subnteId){
		HashMap<String, NatGateway> matchingNatGateways = new HashMap<>();
		for (String tfName : natGateways.keySet()) {
			com.jakub.tfutil.aws.nat_gateway.Attributes attr = natGateways.get(tfName).primary.attributes;
			if (attr.subnet_id.equals(subnteId)){
				matchingNatGateways.put(tfName, natGateways.get(tfName));
			}
		}
		return matchingNatGateways;
	}

	public Eip findEip(String eipId){
		Eip eip = null;
		for (String tfName : eips.keySet()) {
			eip = eips.get(tfName);
			com.jakub.tfutil.aws.eip.Attributes attr = eip.primary.attributes;
			if (attr.id.equals(eipId)){
				return eip;
			}
		}
		return null;
	}
	
	public HashMap<String, NatGateway> findNatGatewaysInZones(HashSet<String> zones){
		HashMap<String, NatGateway> matchingNatGateways = new HashMap<>();
		HashMap<String, NatGateway> allNatGateways = new HashMap<>();
		for (String tfName : allNatGateways.keySet()) {
			NatGateway natGateway = natGateways.get(tfName);
			Subnet subnet = findSubnet(natGateway.primary.attributes.subnet_id);
			if (zones.contains(subnet.primary.attributes.availability_zone)){
				matchingNatGateways.put(tfName, natGateway);
			}
		}
		return matchingNatGateways;
	}
	
}
