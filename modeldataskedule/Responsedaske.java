
package com.teamproject.plastikproject.modeldataskedule;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Responsedaske implements Serializable, Parcelable
{

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("id_user")
    @Expose
    private String idUser;
    @SerializedName("day")
    @Expose
    private String day;
    @SerializedName("time")
    @Expose
    private String time;
    @SerializedName("status_on")
    @Expose
    private Boolean statusOn;
    @SerializedName("id_increment")
    @Expose
    private Integer idIncrement;
    @SerializedName("milisecond_time")
    @Expose
    private Integer milisecondTime;
    public final static Creator<Responsedaske> CREATOR = new Creator<Responsedaske>() {


        @SuppressWarnings({
            "unchecked"
        })
        public Responsedaske createFromParcel(Parcel in) {
            return new Responsedaske(in);
        }

        public Responsedaske[] newArray(int size) {
            return (new Responsedaske[size]);
        }

    }
    ;
    private final static long serialVersionUID = 2794456936085234849L;

    protected Responsedaske(Parcel in) {
        this.id = ((String) in.readValue((String.class.getClassLoader())));
        this.idUser = ((String) in.readValue((String.class.getClassLoader())));
        this.day = ((String) in.readValue((String.class.getClassLoader())));
        this.time = ((String) in.readValue((String.class.getClassLoader())));
        this.statusOn = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
        this.idIncrement = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.milisecondTime = ((Integer) in.readValue((Integer.class.getClassLoader())));
    }

    public Responsedaske() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Boolean getStatusOn() {
        return statusOn;
    }

    public void setStatusOn(Boolean statusOn) {
        this.statusOn = statusOn;
    }

    public Integer getIdIncrement() {
        return idIncrement;
    }

    public void setIdIncrement(Integer idIncrement) {
        this.idIncrement = idIncrement;
    }

    public Integer getMilisecondTime() {
        return milisecondTime;
    }

    public void setMilisecondTime(Integer milisecondTime) {
        this.milisecondTime = milisecondTime;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(idUser);
        dest.writeValue(day);
        dest.writeValue(time);
        dest.writeValue(statusOn);
        dest.writeValue(idIncrement);
        dest.writeValue(milisecondTime);
    }

    public int describeContents() {
        return  0;
    }

}
