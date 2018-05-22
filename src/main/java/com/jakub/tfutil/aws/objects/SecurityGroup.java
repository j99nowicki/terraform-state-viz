
package com.jakub.tfutil.aws.objects;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.jakub.tfutil.aws.resources.ResourceSecurityGroup;

public class SecurityGroup extends TfObject{
	public String arn;
	public String description;
	public String egressCount;
    public String id;
	public String ingressCount;
    public String name;
    public String owner_id;
    public boolean revoke_rules_on_delete;
	public int tagsCount;
	public String tagsName;
    public String vpc_id;
    private ResourceSecurityGroup resourceSecurityGroup;
    
    public SecurityGroup(ResourceSecurityGroup resourceSecurityGroup) {
    	this.resourceSecurityGroup = resourceSecurityGroup;
		this.arn = resourceSecurityGroup.arn;
		this.description = resourceSecurityGroup.description;
		this.egressCount = resourceSecurityGroup.egressCount;
		this.id = resourceSecurityGroup.id;
		this.ingressCount = resourceSecurityGroup.ingressCount;
		this.name = resourceSecurityGroup.name;
		this.owner_id = resourceSecurityGroup.owner_id;
		this.revoke_rules_on_delete = resourceSecurityGroup.revoke_rules_on_delete;
		this.tagsCount = resourceSecurityGroup.tagsCount;
		this.tagsName = resourceSecurityGroup.tagsName;
		this.vpc_id = resourceSecurityGroup.vpc_id;
		resource = true;
	}

	public String toString()
    {
      return ToStringBuilder.reflectionToString(this);
    }

	public ResourceSecurityGroup getResourceSecurityGroup() {
		return resourceSecurityGroup;
	}

}
