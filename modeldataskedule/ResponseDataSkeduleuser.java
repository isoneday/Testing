
package com.teamproject.plastikproject.modeldataskedule;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class ResponseDataSkeduleuser implements Serializable, Parcelable
{

    @SerializedName("response")
    @Expose
    private List<Responsedaske> response = null;
    public final static Creator<ResponseDataSkeduleuser> CREATOR = new Creator<ResponseDataSkeduleuser>() {


        @SuppressWarnings({
            "unchecked"
        })
        public ResponseDataSkeduleuser createFromParcel(Parcel in) {
            return new ResponseDataSkeduleuser(in);
        }

        public ResponseDataSkeduleuser[] newArray(int size) {
            return (new ResponseDataSkeduleuser[size]);
        }

    }
    ;
    private final static long serialVersionUID = -1022331843595614819L;

    protected ResponseDataSkeduleuser(Parcel in) {
        in.readList(this.response, (Responsedaske.class.getClassLoader()));
    }

    public ResponseDataSkeduleuser() {
    }

    public List<Responsedaske> getResponse() {
        return response;
    }

    public void setResponse(List<Responsedaske> response) {
        this.response = response;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(response);
    }

    public int describeContents() {
        return  0;
    }

}
