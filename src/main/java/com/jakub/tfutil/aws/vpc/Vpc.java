package com.jakub.tfutil.aws.vpc;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class Vpc{
    public String type;
    public Primary primary;
    public  String provider;
    public String tfName;

	public String getTfName() {
		return tfName;
	}

	public void setTfName(String tfName) {
		this.tfName = tfName;
	} 
	
    @Override
    public String toString()
    {
      return ToStringBuilder.reflectionToString(this);
    }

}