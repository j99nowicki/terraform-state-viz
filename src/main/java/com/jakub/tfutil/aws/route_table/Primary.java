package com.jakub.tfutil.aws.route_table;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class Primary {
	public String id;
	public Attributes attributes;
	@Override
	public String toString()
	{
	  return ToStringBuilder.reflectionToString(this);
	}
}
