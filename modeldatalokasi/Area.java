
package com.teamproject.plastikproject.modeldatalokasi;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Area implements Parcelable
{

    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("coordinates")
    @Expose
    private List<Double> coordinates = null;
    public final static Creator<Area> CREATOR = new Creator<Area>() {


        @SuppressWarnings({
            "unchecked"
        })
        public Area createFromParcel(Parcel in) {
            return new Area(in);
        }

        public Area[] newArray(int size) {
            return (new Area[size]);
        }

    }
    ;

    protected Area(Parcel in) {
        this.type = ((String) in.readValue((String.class.getClassLoader())));
        in.readList(this.coordinates, (Double.class.getClassLoader()));
    }

    public Area() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Double> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(List<Double> coordinates) {
        this.coordinates = coordinates;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(type);
        dest.writeList(coordinates);
    }

    public int describeContents() {
        return  0;
    }

}
