package com.teamproject.plastikproject.plastik.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;


public class ResponseDataUser{

	@SerializedName("response")
	private List<ResponseUser> response;

	public void setResponse(List<ResponseUser> response){
		this.response = response;
	}

	public List<ResponseUser> getResponse(){
		return response;
	}
}