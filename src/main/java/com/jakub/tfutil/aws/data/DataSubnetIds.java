package com.jakub.tfutil.aws.data;
import java.util.HashSet;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.google.gson.JsonElement;
import com.google.gson.annotations.SerializedName;

public class DataSubnetIds extends TfData{
	
	public String id;
	@SerializedName("ids.#")
	public int idsCount;
    public String vpc_id;

	public HashSet<String> ids;
    
	@Override
	public String toString()
	{
	  return ToStringBuilder.reflectionToString(this);
	}
	
	//custom parser for subnet ids set
	public void parseIds(Set<Entry<String, JsonElement>> entrySet ){
		ids = new HashSet<String>();
		for (Entry<String, JsonElement> entry : entrySet) {
			if (entry.getKey().startsWith("ids.") && !entry.getKey().startsWith("ids.#")){
				ids.add(entry.getValue().getAsString());
			}
		}
	}
	
}
