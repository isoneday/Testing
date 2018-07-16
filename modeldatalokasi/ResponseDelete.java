package com.teamproject.plastikproject.modeldatalokasi;

import com.google.gson.annotations.SerializedName;


public class ResponseDelete{

	@SerializedName("count")
	private int count;

	public void setCount(int count){
		this.count = count;
	}

	public int getCount(){
		return count;
	}
}