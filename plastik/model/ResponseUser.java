package com.teamproject.plastikproject.plastik.model;

import com.google.gson.annotations.SerializedName;


public class ResponseUser {

	@SerializedName("emailVerified")
	private boolean emailVerified;

	@SerializedName("id_increment")
	private int idIncrement;

	@SerializedName("password")
	private String password;

	@SerializedName("verificationToken")
	private Object verificationToken;

	@SerializedName("name")
	private String name;

	@SerializedName("_id")
	private String id;

	@SerializedName("thropy")
	private int thropy;

	@SerializedName("email")
	private String email;

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

	public void setPassword(String password){
		this.password = password;
	}

	public String getPassword(){
		return password;
	}

	public void setVerificationToken(Object verificationToken){
		this.verificationToken = verificationToken;
	}

	public Object getVerificationToken(){
		return verificationToken;
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