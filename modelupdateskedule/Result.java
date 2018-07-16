
package com.teamproject.plastikproject.modelupdateskedule;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Result implements Serializable, Parcelable
{

    @SerializedName("result")
    @Expose
    private String result;
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
    private final static long serialVersionUID = 4852797197436229749L;

    protected Result(Parcel in) {
        this.result = ((String) in.readValue((String.class.getClassLoader())));
    }

    public Result() {
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(result);
    }

    public int describeContents() {
        return  0;
    }

}
