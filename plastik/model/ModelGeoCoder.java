package com.teamproject.plastikproject.plastik.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ModelGeoCoder{

	@SerializedName("results")
	private List<ResultsItem> results;

	@SerializedName("status")
	private String status;

	public void setResults(List<ResultsItem> results){
		this.results = results;
	}

	public List<ResultsItem> getResults(){
		return results;
	}

	public void setStatus(String status){
		this.status = status;
	}

	public String getStatus(){
		return status;
	}

	@Override
 	public String toString(){
		return 
			"ModelGeoCoder{" + 
			"results = '" + results + '\'' + 
			",status = '" + status + '\'' + 
			"}";
		}
}