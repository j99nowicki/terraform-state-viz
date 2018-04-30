package com.jakub.tfutil.aws.subnet;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class Subnet{
    public String type;
    public Primary primary;
    public String provider;

	@Override
    public String toString()
    {
      return ToStringBuilder.reflectionToString(this);
    }    
}