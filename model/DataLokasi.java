
package com.teamproject.plastikproject.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DataLokasi implements Parcelable
{

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("area")
    @Expose
    private Area area;
    @SerializedName("description")
    @Expose
    private String description;
    public final static Creator<DataLokasi> CREATOR = new Creator<DataLokasi>() {


        @SuppressWarnings({
            "unchecked"
        })
        public DataLokasi createFromParcel(Parcel in) {
            return new DataLokasi(in);
        }

        public DataLokasi[] newArray(int size) {
            return (new DataLokasi[size]);
        }

    }
    ;

    protected DataLokasi(Parcel in) {
        this.id = ((String) in.readValue((String.class.getClassLoader())));
        this.area = ((Area) in.readValue((Area.class.getClassLoader())));
        this.description = ((String) in.readValue((String.class.getClassLoader())));
    }

    public DataLokasi() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Area getArea() {
        return area;
    }

    public void setArea(Area area) {
        this.area = area;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(area);
        dest.writeValue(description);
    }

    public int describeContents() {
        return  0;
    }

}
