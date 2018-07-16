package com.teamproject.plastikproject.plastik.model;

import com.google.gson.annotations.SerializedName;

public class ResponseChangeUsername{

	@SerializedName("is_admin")
	private String isAdmin;

	@SerializedName("emailVerified")
	private boolean emailVerified;

	@SerializedName("id_increment")
	private int idIncrement;

	@SerializedName("name")
	private String name;

	@SerializedName("id")
	private String id;

	@SerializedName("thropy")
	private int thropy;

	@SerializedName("email")
	private String email;

	public void setIsAdmin(String isAdmin){
		this.isAdmin = isAdmin;
	}

	public String getIsAdmin(){
		return isAdmin;
	}

	public void setEmailVerified(boolean emailVerified){
		this.emailVerified = emailVerified;
	}

	public boolean isEmailVerified(){
		return emailVerified;
	}

	public void setIdIncrement(int idIncrement){
		this.idIncrement = idIncrement;
	}

	public int getIdIncrement(){
		return idIncrement;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setThropy(int thropy){
		this.thropy = thropy;
	}

	public int getThropy(){
		return thropy;
	}

	public void setEmail(String email){
		this.email = email;
	}

	public String getEmail(){
		return email;
	}
}