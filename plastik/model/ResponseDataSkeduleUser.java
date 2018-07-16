package com.teamproject.plastikproject.plastik.model;

import com.google.gson.annotations.SerializedName;


public class ResponseDataSkeduleUser {

	@SerializedName("_id")
	private String id;

	@SerializedName("id_user")
	private String idUser;

	@SerializedName("time")
	private String time;

	@SerializedName("day")
	private String day;

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setIdUser(String idUser){
		this.idUser = idUser;
	}

	public String getIdUser(){
		return idUser;
	}

	public void setTime(String time){
		this.time = time;
	}

	public String getTime(){
		return time;
	}

	public void setDay(String day){
		this.day = day;
	}

	public String getDay(){
		return day;
	}
}