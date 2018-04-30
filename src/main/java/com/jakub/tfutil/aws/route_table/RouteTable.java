package com.jakub.tfutil.aws.route_table;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class RouteTable{
    public String type;
    public Primary primary;
    public String provider;

    @Override
    public String toString()
    {
      return ToStringBuilder.reflectionToString(this);
    }    
}