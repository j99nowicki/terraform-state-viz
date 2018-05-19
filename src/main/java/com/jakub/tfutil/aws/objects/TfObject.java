package com.jakub.tfutil.aws.objects;

import com.jakub.tfutil.aws.TfAttributes;

public abstract class TfObject extends TfAttributes {
	public boolean resource;
	
	public boolean isResource(){
		return resource;
	};
	
}
