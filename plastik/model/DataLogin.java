package com.teamproject.plastikproject.plastik.model;

import com.google.gson.annotations.SerializedName;


public class DataLogin {

	@SerializedName("created")
	private String created;

	@SerializedName("id")
	private String id;

	@SerializedName("ttl")
	private int ttl;

	@SerializedName("userId")
	private String userId;

	public void setCreated(String created){
		this.created = created;
	}

	public String getCreated(){
		return created;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setTtl(int ttl){
		this.ttl = ttl;
	}

	public int getTtl(){
		return ttl;
	}

	public void setUserId(String userId){
		this.userId = userId;
	}

	public String getUserId(){
		return userId;
	}
}