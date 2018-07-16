package com.teamproject.plastikproject.modelupdatelokasi;

import com.google.gson.annotations.SerializedName;

public class ResponseUpdatelokasi{

	@SerializedName("response")
	private Responsedataupdatelokasi response;

	public void setResponse(Responsedataupdatelokasi response){
		this.response = response;
	}

	public Responsedataupdatelokasi getResponse(){
		return response;
	}
}