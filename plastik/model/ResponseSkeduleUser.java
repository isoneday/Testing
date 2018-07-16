package com.teamproject.plastikproject.plastik.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;


public class ResponseSkeduleUser{

	@SerializedName("response")
	private List<ResponseDataSkeduleUser> response;

	public void setResponse(List<ResponseDataSkeduleUser> response){
		this.response = response;
	}

	public List<ResponseDataSkeduleUser> getResponse(){
		return response;
	}
}