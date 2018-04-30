package com.jakub.tfutil.aws.route_table_association;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class RouteTableAssociation{
    public String type;
    public Primary primary;
    public String provider;

    @Override
    public String toString()
    {
      return ToStringBuilder.reflectionToString(this);
    }    
}