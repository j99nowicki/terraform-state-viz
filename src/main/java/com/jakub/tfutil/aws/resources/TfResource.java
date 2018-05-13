package com.jakub.tfutil.aws.resources;

import com.jakub.tfutil.aws.TfAttributes;

public abstract class TfResource extends TfAttributes {
	public boolean isRespource() {
		return true;
	}
}
