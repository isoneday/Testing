package com.teamproject.plastikproject.plastik.model;

import com.google.gson.annotations.SerializedName;

public class ModelRegister{

	@SerializedName("result")
	private String result;

	@SerializedName("data")
	private DataRegister data;

	public void setResult(String result){
		this.result = result;
	}

	public String getResult(){
		return result;
	}

	public void setData(DataRegister data){
		this.data = data;
	}

	public DataRegister getData(){
		return data;
	}
}