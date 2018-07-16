
package com.teamproject.plastikproject.modelupdateskedule;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Responseupdateskdle implements Serializable, Parcelable
{

    @SerializedName("time")
    @Expose
    private String time;
    @SerializedName("id_user")
    @Expose
    private String idUser;
    @SerializedName("day")
    @Expose
    private String day;
    @SerializedName("status_on")
    @Expose
    private String statusOn;
    @SerializedName("id_increment")
    @Expose
    private String idIncrement;
    @SerializedName("result")
    @Expose
    private Result result;
    public final static Creator<Responseupdateskdle> CREATOR = new Creator<Responseupdateskdle>() {


        @SuppressWarnings({
            "unchecked"
        })
        public Responseupdateskdle createFromParcel(Parcel in) {
            return new Responseupdateskdle(in);
        }

        public Responseupdateskdle[] newArray(int size) {
            return (new Responseupdateskdle[size]);
        }

    }
    ;
    private final static long serialVersionUID = 5551588091189168778L;

    protected Responseupdateskdle(Parcel in) {
        this.time = ((String) in.readValue((String.class.getClassLoader())));
        this.idUser = ((String) in.readValue((String.class.getClassLoader())));
        this.day = ((String) in.readValue((String.class.getClassLoader())));
        this.statusOn = ((String) in.readValue((String.class.getClassLoader())));
        this.idIncrement = ((String) in.readValue((String.class.getClassLoader())));
        this.result = ((Result) in.readValue((Result.class.getClassLoader())));
    }

    public Responseupdateskdle() {
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getStatusOn() {
        return statusOn;
    }

    public void setStatusOn(String statusOn) {
        this.statusOn = statusOn;
    }

    public String getIdIncrement() {
        return idIncrement;
    }

    public void setIdIncrement(String idIncrement) {
        this.idIncrement = idIncrement;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(time);
        dest.writeValue(idUser);
        dest.writeValue(day);
        dest.writeValue(statusOn);
        dest.writeValue(idIncrement);
        dest.writeValue(result);
    }

    public int describeContents() {
        return  0;
    }

}
