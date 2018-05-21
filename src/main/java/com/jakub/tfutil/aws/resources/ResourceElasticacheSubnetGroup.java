package com.jakub.tfutil.aws.resources;
import java.util.HashSet;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.google.gson.JsonElement;
import com.google.gson.annotations.SerializedName;

public class ResourceElasticacheSubnetGroup extends TfResource{
	
	public String description;
	public String id;
	public String name;
	@SerializedName("subnet_ids.#")
	public int subnetIdsCount;
	@SerializedName("tags.%")
	public int tagsCount;
	@SerializedName("tags.Name")
	public String tagsName;
	
	public HashSet<String> subnetIds;
    
	@Override
	public String toString()
	{
	  return ToStringBuilder.reflectionToString(this);
	}
	
	//custom parser for subnet ids set
	public void parseIds(Set<Entry<String, JsonElement>> entrySet ){
		subnetIds = new HashSet<String>();
		for (Entry<String, JsonElement> entry : entrySet) {
			if (entry.getKey().startsWith("subnet_ids.") && !entry.getKey().startsWith("subnet_ids.#")){
				subnetIds.add(entry.getValue().getAsString());
			}
		}
	}
	
}
