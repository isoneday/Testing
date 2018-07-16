
package com.teamproject.plastikproject.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseLokasi implements Parcelable
{

    @SerializedName("response")
    @Expose
    private List<DataLokasi> response = null;
    public final static Creator<ResponseLokasi> CREATOR = new Creator<ResponseLokasi>() {


        @SuppressWarnings({
            "unchecked"
        })
        public ResponseLokasi createFromParcel(Parcel in) {
            return new ResponseLokasi(in);
        }

        public ResponseLokasi[] newArray(int size) {
            return (new ResponseLokasi[size]);
        }

    }
    ;

    protected ResponseLokasi(Parcel in) {
        in.readList(this.response, (DataLokasi.class.getClassLoader()));
    }

    public ResponseLokasi() {
    }

    public List<DataLokasi> getResponse() {
        return response;
    }

    public void setResponse(List<DataLokasi> response) {
        this.response = response;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(response);
    }

    public int describeContents() {
        return  0;
    }

}
