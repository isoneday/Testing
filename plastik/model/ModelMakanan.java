package com.teamproject.plastikproject.plastik.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ModelMakanan{

	@SerializedName("DataMakanan")
	private List<DataMakananItem> dataMakanan;

	public void setDataMakanan(List<DataMakananItem> dataMakanan){
		this.dataMakanan = dataMakanan;
	}

	public List<DataMakananItem> getDataMakanan(){
		return dataMakanan;
	}

	@Override
 	public String toString(){
		return 
			"ModelMakanan{" + 
			"dataMakanan = '" + dataMakanan + '\'' + 
			"}";
		}
}