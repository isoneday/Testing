
package com.teamproject.plastikproject.modeladdlokasi;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class InsertedIds implements Parcelable
{

    @SerializedName("0")
    @Expose
    private String _0;
    public final static Creator<InsertedIds> CREATOR = new Creator<InsertedIds>() {


        @SuppressWarnings({
            "unchecked"
        })
        public InsertedIds createFromParcel(Parcel in) {
            return new InsertedIds(in);
        }

        public InsertedIds[] newArray(int size) {
            return (new InsertedIds[size]);
        }

    }
    ;

    protected InsertedIds(Parcel in) {
        this._0 = ((String) in.readValue((String.class.getClassLoader())));
    }

    public InsertedIds() {
    }

    public String get0() {
        return _0;
    }

    public void set0(String _0) {
        this._0 = _0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(_0);
    }

    public int describeContents() {
        return  0;
    }

}
