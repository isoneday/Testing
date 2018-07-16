
package com.teamproject.plastikproject.modeldatalokasi;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ResponseDataLokasi implements Parcelable
{

    @SerializedName("response")
    @Expose
    private ArrayList<Response> response = null;
    public final static Creator<ResponseDataLokasi> CREATOR = new Creator<ResponseDataLokasi>() {


        @SuppressWarnings({
            "unchecked"
        })
        public ResponseDataLokasi createFromParcel(Parcel in) {
            return new ResponseDataLokasi(in);
        }

        public ResponseDataLokasi[] newArray(int size) {
            return (new ResponseDataLokasi[size]);
        }

    }
    ;

    protected ResponseDataLokasi(Parcel in) {
        in.readList(this.response, (Response.class.getClassLoader()));
    }

    public ResponseDataLokasi() {
    }

    public ArrayList<Response> getResponse() {
        return response;
    }

    public void setResponse(ArrayList<Response> response) {
        this.response = response;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(response);
    }

    public int describeContents() {
        return  0;
    }

}
