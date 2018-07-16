
package com.teamproject.plastikproject.modelupdateskedule;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ModelUpdateSkedule implements Serializable, Parcelable
{

    @SerializedName("response")
    @Expose
    private Responseupdateskdle response;
    public final static Creator<ModelUpdateSkedule> CREATOR = new Creator<ModelUpdateSkedule>() {


        @SuppressWarnings({
            "unchecked"
        })
        public ModelUpdateSkedule createFromParcel(Parcel in) {
            return new ModelUpdateSkedule(in);
        }

        public ModelUpdateSkedule[] newArray(int size) {
            return (new ModelUpdateSkedule[size]);
        }

    }
    ;
    private final static long serialVersionUID = -1714395642759630895L;

    protected ModelUpdateSkedule(Parcel in) {
        this.response = ((Responseupdateskdle) in.readValue((Responseupdateskdle.class.getClassLoader())));
    }

    public ModelUpdateSkedule() {
    }

    public Responseupdateskdle getResponse() {
        return response;
    }

    public void setResponse(Responseupdateskdle response) {
        this.response = response;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(response);
    }

    public int describeContents() {
        return  0;
    }

}
