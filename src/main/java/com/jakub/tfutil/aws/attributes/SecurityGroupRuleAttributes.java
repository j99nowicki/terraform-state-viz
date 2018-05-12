package com.jakub.tfutil.aws.attributes;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

public class SecurityGroupRuleAttributes extends TfAttributes{
	
	@SerializedName("cidr_blocks.#")
	public String cidrBlocksCount;
    public String description;
    public int from_port;
    public String id;
    public String protocol;
    public String security_group_id;
    public boolean self;
    public int to_port;
    public String type;

    public String toString()
    {
      return ToStringBuilder.reflectionToString(this);
    }  
}
