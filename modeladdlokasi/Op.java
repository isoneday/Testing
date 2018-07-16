
package com.teamproject.plastikproject.modeladdlokasi;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Op implements Parcelable
{

    @SerializedName("area")
    @Expose
    private Area area;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("_id")
    @Expose
    private String id;
    public final static Creator<Op> CREATOR = new Creator<Op>() {


        @SuppressWarnings({
            "unchecked"
        })
        public Op createFromParcel(Parcel in) {
            return new Op(in);
        }

        public Op[] newArray(int size) {
            return (new Op[size]);
        }

    }
    ;

    protected Op(Parcel in) {
        this.area = ((Area) in.readValue((Area.class.getClassLoader())));
        this.description = ((String) in.readValue((String.class.getClassLoader())));
        this.id = ((String) in.readValue((String.class.getClassLoader())));
    }

    public Op() {
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(area);
        dest.writeValue(description);
        dest.writeValue(id);
    }

    public int describeContents() {
        return  0;
    }

}
