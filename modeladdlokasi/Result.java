
package com.teamproject.plastikproject.modeladdlokasi;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Result implements Parcelable
{

    @SerializedName("ok")
    @Expose
    private Integer ok;
    @SerializedName("n")
    @Expose
    private Integer n;
    public final static Creator<Result> CREATOR = new Creator<Result>() {


        @SuppressWarnings({
            "unchecked"
        })
        public Result createFromParcel(Parcel in) {
            return new Result(in);
        }

        public Result[] newArray(int size) {
            return (new Result[size]);
        }

    }
    ;

    protected Result(Parcel in) {
        this.ok = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.n = ((Integer) in.readValue((Integer.class.getClassLoader())));
    }

    public Result() {
    }

    public Integer getOk() {
        return ok;
    }

    public void setOk(Integer ok) {
        this.ok = ok;
    }

    public Integer getN() {
        return n;
    }

    public void setN(Integer n) {
        this.n = n;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(ok);
        dest.writeValue(n);
    }

    public int describeContents() {
        return  0;
    }

}
