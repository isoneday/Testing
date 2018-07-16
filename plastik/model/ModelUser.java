package com.teamproject.plastikproject.plastik.model;

import com.google.gson.annotations.SerializedName;


public class ModelUser{

	@SerializedName("result")
	private String result;

	@SerializedName("data")
	private DataLogin data;



	public void setResult(String result){
		this.result = result;
	}

	public String getResult(){
		return result;
	}

	public void setData(DataLogin data){
		this.data = data;
	}

	public DataLogin getData(){
		return data;
	}


}