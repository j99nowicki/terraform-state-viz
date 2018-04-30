package com.jakub.tfutil.aws.route_table_association;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class Attributes {

	public String route_table_id;
	public String id;
	public String subnet_id;
	
	@Override
	public String toString()
	{
	  return ToStringBuilder.reflectionToString(this);
	}
}
