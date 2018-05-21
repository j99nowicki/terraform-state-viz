package com.jakub.tfutil.aws.objects;

import java.util.HashMap;
import java.util.HashSet;

import org.apache.commons.lang3.builder.ToStringBuilder;
import com.jakub.tfutil.TfObjectsWarehouse;
import com.jakub.tfutil.aws.data.DataAmi;
import com.jakub.tfutil.aws.data.DataSubnetIds;
import com.jakub.tfutil.aws.data.DataVpc;
import com.jakub.tfutil.aws.resources.ResourceEip;
import com.jakub.tfutil.aws.resources.ResourceInstance;
import com.jakub.tfutil.aws.resources.ResourceInternetGateway;
import com.jakub.tfutil.aws.resources.ResourceNatGateway;
import com.jakub.tfutil.aws.resources.ResourceRoute;
import com.jakub.tfutil.aws.resources.ResourceRouteTable;
import com.jakub.tfutil.aws.resources.ResourceRouteTableAssociation;
import com.jakub.tfutil.aws.resources.ResourceSubnet;
import com.jakub.tfutil.aws.resources.ResourceVpc;
import com.jakub.tfutil.aws.resources.ResourceVpcDhcpOptions;
import com.jakub.tfutil.aws.resources.ResourceVpcDhcpOptionsAssociation;
import com.jakub.tfutil.aws.resources.ResourceVpcEndpoinRouteTableAssociation;
import com.jakub.tfutil.aws.resources.ResourceVpcEndpoint;
import com.jakub.tfutil.aws.resources.ResourceVpnGateway;

public class Model {
	public HashMap<String, Vpc> vpcs = new HashMap<>();
	public HashMap<String, VpcEndpoint> vpcEndpoints = new HashMap<>();
	public HashMap<String, VpnGateway> vpnGateways = new HashMap<>();
	public HashMap<String, InternetGateway> internetGateways = new HashMap<>();
	public HashMap<String, Eip> eips = new HashMap<>();
	public HashMap<String, NatGateway> natGateways = new HashMap<>();
	public HashMap<String, Instance> instances = new HashMap<>();
	public HashMap<String, Subnet> subnets = new HashMap<>();
	public HashMap<String, Route> routes = new HashMap<>();
	public HashMap<String, RouteTableAssociation> routeTableAssociations = new HashMap<>();
	public HashMap<String, RouteTable> routeTables = new HashMap<>();
	public HashMap<String, VpcEndpoinRouteTableAssociation> vpcEndpoinRouteTableAssociation = new HashMap<>();
	public HashMap<String, VpcDhcpOptions> vpcDhcpOptionss = new HashMap<>();
	public HashMap<String, VpcDhcpOptionsAssociation> vpcDhcpOptionsAssociations = new HashMap<>();
	public HashMap<String, Ami> amis = new HashMap<>();
	
	@Override
	public String toString()
	{
	  return ToStringBuilder.reflectionToString(this);
	}	
	
	public Model(TfObjectsWarehouse tfObjectsWarehouse){
		for (String key : tfObjectsWarehouse.rVpcs.keySet()) {
			ResourceVpc item = tfObjectsWarehouse.rVpcs.get(key);
			vpcs.put(key, new Vpc(item));
		}
		for (String key : tfObjectsWarehouse.dVpcs.keySet()) {
			DataVpc item = tfObjectsWarehouse.dVpcs.get(key);
			vpcs.put(key, new Vpc(item));
		}
		
		for (String key : tfObjectsWarehouse.rVpcEndpoints.keySet()) {
			ResourceVpcEndpoint item = tfObjectsWarehouse.rVpcEndpoints.get(key);
			vpcEndpoints.put(key, new VpcEndpoint(item));
		}
		
		for (String key : tfObjectsWarehouse.rVpnGateways.keySet()) {
			ResourceVpnGateway item = tfObjectsWarehouse.rVpnGateways.get(key);
			vpnGateways.put(key, new VpnGateway(item));
		}

		for (String key : tfObjectsWarehouse.rInternetGateways.keySet()) {
			ResourceInternetGateway item = tfObjectsWarehouse.rInternetGateways.get(key);
			internetGateways.put(key, new InternetGateway(item));
		}
		
		for (String key : tfObjectsWarehouse.rEips.keySet()) {
			ResourceEip item = tfObjectsWarehouse.rEips.get(key);
			eips.put(key, new Eip(item));
		}

		for (String key : tfObjectsWarehouse.rNatGateways.keySet()) {
			ResourceNatGateway item = tfObjectsWarehouse.rNatGateways.get(key);
			natGateways.put(key, new NatGateway(item));
		}

		for (String key : tfObjectsWarehouse.rSubnets.keySet()) {
			ResourceSubnet item = tfObjectsWarehouse.rSubnets.get(key);
			subnets.put(key, new Subnet(item));
		}
		for (String key : tfObjectsWarehouse.dSubnetIdss.keySet()) {
			DataSubnetIds item = tfObjectsWarehouse.dSubnetIdss.get(key);
			HashSet<String> ids = item.ids;
			for (String idSubnet : ids) {
				subnets.put(idSubnet, new Subnet(item.vpc_id, idSubnet));				
			}
		}	
		
		for (String key : tfObjectsWarehouse.rInstances.keySet()) {
			ResourceInstance item = tfObjectsWarehouse.rInstances.get(key);
			Instance instance = new Instance(item); 
			instances.put(key, instance);
			// move subnet to known AZ if instance references it
			Subnet subnet = subnets.get(instance.subnet_id);
			if (Subnet.CONST_AZ_UNKNOWN.equals(subnet.availability_zone)){
				subnet.availability_zone = instance.availability_zone;
			}
		}
		
		for (String idVpc : vpcs.keySet()) {
			vpcs.get(idVpc).setAvailabilityZones(findAvailabilityZonesInVpc(idVpc));
		}	
		
		for (String key : tfObjectsWarehouse.rRoutes.keySet()){
			ResourceRoute item = tfObjectsWarehouse.rRoutes.get(key);
			routes.put(key, new Route(item));
		}

		for (String key : tfObjectsWarehouse.rRouteTableAssociations.keySet()){
			ResourceRouteTableAssociation item = tfObjectsWarehouse.rRouteTableAssociations.get(key);
			routeTableAssociations.put(key, new RouteTableAssociation(item));
		}
		
		for (String key : tfObjectsWarehouse.rRouteTables.keySet()){
			ResourceRouteTable item = tfObjectsWarehouse.rRouteTables.get(key);
			routeTables.put(key, new RouteTable(item));
		}
		
		for (String key : tfObjectsWarehouse.rVpcEndpoinRouteTableAssociations.keySet()){
			ResourceVpcEndpoinRouteTableAssociation item = tfObjectsWarehouse.rVpcEndpoinRouteTableAssociations.get(key);
			vpcEndpoinRouteTableAssociation.put(key, new VpcEndpoinRouteTableAssociation(item));
		}
		
		for (String key : tfObjectsWarehouse.rVpcDhcpOptionss.keySet()){
			ResourceVpcDhcpOptions item = tfObjectsWarehouse.rVpcDhcpOptionss.get(key);
			vpcDhcpOptionss.put(key, new VpcDhcpOptions(item));
		}
		
		for (String key : tfObjectsWarehouse.rVpcDhcpOptionsAssociations.keySet()){
			ResourceVpcDhcpOptionsAssociation item = tfObjectsWarehouse.rVpcDhcpOptionsAssociations.get(key);
			vpcDhcpOptionsAssociations.put(key, new VpcDhcpOptionsAssociation(item));
		}	
		
		for (String key : tfObjectsWarehouse.dAmis.keySet()) {
			DataAmi item = tfObjectsWarehouse.dAmis.get(key);
			amis.put(key, new Ami(item));
		}
				
	}

	private HashSet<String> findAvailabilityZonesInVpc(String idVpc){
		HashSet<String> zones = new HashSet<String>();
		//search for AZ in subnets
		for (String idSubnet : subnets.keySet()) {
			Subnet subnet = subnets.get(idSubnet);
			if (idVpc.equals(subnet.vpc_id)){
				zones.add(subnet.availability_zone);
				//and in instances in each subnet
				for (String idInstance : instances.keySet()) {
					Instance instance = instances.get(idInstance);
					if (idSubnet.equals(instance.subnet_id)){
						zones.add(instance.availability_zone);
					}
				}
			}
		}
		return zones;
	}
	
	
	public HashMap<String, InternetGateway> findInternetGatewaysInVpc(String idVpc) {
		HashMap<String, InternetGateway> matchingElements = new HashMap<>();
		for (String id : internetGateways.keySet()) {
			InternetGateway element = internetGateways.get(id);
			if (element.vpc_id.equals(idVpc)){
				matchingElements.put(id, element);
			}
		}
		return matchingElements;
	}
	
	
	public HashMap<String, VpcEndpoint> findVpcEndpointsInVpc(String idVpc) {
		HashMap<String, VpcEndpoint> matchingElements = new HashMap<>();
		for (String id : vpcEndpoints.keySet()) {
			VpcEndpoint element = vpcEndpoints.get(id);
			if (element.vpc_id.equals(idVpc)){
				matchingElements.put(id, element);
			}
		}
		return matchingElements;
	}
	
	public HashMap<String, VpnGateway> findVpnGatewaysInVpc(String idVpc) {
		HashMap<String, VpnGateway> matchingElements = new HashMap<>();
		for (String id : vpnGateways.keySet()) {
			VpnGateway element = vpnGateways.get(id);
			if (element.vpc_id.equals(idVpc)){
				matchingElements.put(id, element);
			}
		}
		return matchingElements;
	}
	
	public Eip findEip(String idEip){
		Eip eip = null;
		for (String id : eips.keySet()) {
			eip = eips.get(id);
			if (eip.id.equals(idEip)){
				return eip;
			}
		}
		return null;
	}
	
	
	public HashMap<String, NatGateway> findNatGatewaysInSubnet(String idSubnet){
		HashMap<String, NatGateway> matchingElements = new HashMap<>();
		for (String id : natGateways.keySet()) {
			NatGateway element = natGateways.get(id);
			if (element.subnet_id.equals(idSubnet)){
				matchingElements.put(id, element);
			}
		}
		return matchingElements;
	}
	
	
	public HashMap<String, Instance> findInstancesInSubnet(String idSubnet){
		HashMap<String, Instance> matchingElements = new HashMap<>();
		for (String id : instances.keySet()) {
			Instance element = instances.get(id);
			if (element.subnet_id.equals(idSubnet)){
				matchingElements.put(id, element);
			}
		}
		return matchingElements;
	}	
	
	public HashMap<String, Subnet> findSubnetsInVpcInZone(String idVpc, String zone){
		HashMap<String, Subnet> matchingElements = new HashMap<>();
		for (String id : subnets.keySet()) {
			Subnet element = subnets.get(id);
			if (element.vpc_id.equals(idVpc) && element.availability_zone.equals(zone)){
				matchingElements.put(id, element);
			}
		}
		return matchingElements;
	}

	public HashMap<String, Route> findRoutesInTable(String idRouteTable){
		HashMap<String, Route> matchingElements = new HashMap<>();
		for (String id : routes.keySet()) {
			Route element = routes.get(id);
			if (element.route_table_id.equals(idRouteTable)){
				matchingElements.put(element.id, element);
			}
		}
		return matchingElements;
	}	
	
	public HashMap<String, RouteTableAssociation> findRouteTablesAssociationsForRouteTable(String idRouteTable) {
		HashMap<String, RouteTableAssociation> matchingElements = new HashMap<>();
		for (String id : routeTableAssociations.keySet()) {
			RouteTableAssociation item = routeTableAssociations.get(id);
			if (item.route_table_id.equals(idRouteTable)){
				matchingElements.put(id, item);
			}
		}
		return matchingElements;
	}
	
	public HashMap<String, RouteTable> findRouteTablesInVpc(String idVpc) {
		HashMap<String, RouteTable> matchingElements = new HashMap<>();
		for (String id : routeTables.keySet()) {
			RouteTable element = routeTables.get(id);
			if (element.vpc_id.equals(idVpc)){
				matchingElements.put(id, element);
			}
		}
		return matchingElements;
	}
	
	public HashMap<String, VpcEndpoinRouteTableAssociation> findVpcEndpoinRouteTableAssociationsForRouteTable(String idRouteTable) {
		HashMap<String, VpcEndpoinRouteTableAssociation> matchingElements = new HashMap<>();
		for (String id : vpcEndpoinRouteTableAssociation.keySet()) {
			VpcEndpoinRouteTableAssociation item = vpcEndpoinRouteTableAssociation.get(id);
			if (item.route_table_id.equals(idRouteTable)){
				matchingElements.put(id, item);
			}
		}
		return matchingElements;
	}
		
	public HashMap<String, VpcDhcpOptions> findVpcDhcpOptionsInVpc(String idVpc){
		HashMap<String, VpcDhcpOptions> matchingElements = new HashMap<>();
		for (String idVpcDhcpOptions : vpcDhcpOptionss.keySet()) {
			VpcDhcpOptions vpcDhcpOptions = vpcDhcpOptionss.get(idVpcDhcpOptions);
			for (String idVpcDhcpOptionsAssociation : vpcDhcpOptionsAssociations.keySet()) {
				if (idVpc.equals(vpcDhcpOptionsAssociations.get(idVpcDhcpOptionsAssociation).vpc_id) &&
					idVpcDhcpOptions.equals(vpcDhcpOptionsAssociations.get(idVpcDhcpOptionsAssociation).dhcp_options_id)){
					matchingElements.put(idVpcDhcpOptions, vpcDhcpOptions);
				}
			}
		}
		return matchingElements;		
	}
		
}
