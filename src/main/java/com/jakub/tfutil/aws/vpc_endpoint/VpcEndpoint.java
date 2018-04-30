package com.jakub.tfutil.aws.vpc_endpoint;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class VpcEndpoint{
    public String type;
    public Primary primary;
    public String provider;
    
    @Override
    public String toString()
    {
      return ToStringBuilder.reflectionToString(this);
    }    
}