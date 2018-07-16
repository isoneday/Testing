package com.teamproject.plastikproject.modelupdatelokasi;

import com.google.gson.annotations.SerializedName;

public class Responsedataupdatelokasi {

	@SerializedName("nModified")
	private int nModified;

	@SerializedName("ok")
	private int ok;

	@SerializedName("n")
	private int N;

	public void setNModified(int nModified){
		this.nModified = nModified;
	}

	public int getNModified(){
		return nModified;
	}

	public void setOk(int ok){
		this.ok = ok;
	}

	public int getOk(){
		return ok;
	}

	public void setN(int N){
		this.N = N;
	}

	public int getN(){
		return N;
	}
}