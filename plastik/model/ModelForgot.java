package com.teamproject.plastikproject.plastik.model;

import com.google.gson.annotations.SerializedName;


public class ModelForgot{

	@SerializedName("result")
	private String result;

	public void setResult(String result){
		this.result = result;
	}

	public String getResult(){
		return result;
	}
}