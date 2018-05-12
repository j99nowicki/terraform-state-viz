package com.jakub.tfutil.aws.attributes;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.google.gson.annotations.SerializedName;

public class SecurityGroupAttributes extends TfAttributes{
	public String arn;
	public String description;
	@SerializedName("egress.#")
	public String egressCount;
    public String id;
	@SerializedName("ingress.#")
	public String ingressCount;
    public String name;
    public String owner_id;
    public boolean revoke_rules_on_delete;
	@SerializedName("tags.%")
	public int tagsCount;
	@SerializedName("tags.Name")
	public String tagsName;
    public String vpc_id;
	
    public String toString()
    {
      return ToStringBuilder.reflectionToString(this);
    }  
}
