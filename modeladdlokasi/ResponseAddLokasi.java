
package com.teamproject.plastikproject.modeladdlokasi;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseAddLokasi implements Parcelable
{

    @SerializedName("response")
    @Expose
    private Response response;
    public final static Creator<ResponseAddLokasi> CREATOR = new Creator<ResponseAddLokasi>() {


        @SuppressWarnings({
            "unchecked"
        })
        public ResponseAddLokasi createFromParcel(Parcel in) {
            return new ResponseAddLokasi(in);
        }

        public ResponseAddLokasi[] newArray(int size) {
            return (new ResponseAddLokasi[size]);
        }

    }
    ;

    protected ResponseAddLokasi(Parcel in) {
        this.response = ((Response) in.readValue((Response.class.getClassLoader())));
    }

    public ResponseAddLokasi() {
    }

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(response);
    }

    public int describeContents() {
        return  0;
    }

}
