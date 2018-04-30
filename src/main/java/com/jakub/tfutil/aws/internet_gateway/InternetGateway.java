package com.jakub.tfutil.aws.internet_gateway;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class InternetGateway{
    public String type;
    public Primary primary;
    public String provider;
    @Override
    public String toString()
    {
      return ToStringBuilder.reflectionToString(this);
    }    
}