
package com.teamproject.plastikproject.modeldatalokasi;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Response implements Parcelable
{

    @SerializedName("_id")
    @Expose
    private String id;

    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("long")
    @Expose
    private Double _long;
    @SerializedName("lat")
    @Expose
    private Double lat;

    public final static Creator<Response> CREATOR = new Creator<Response>() {
        @SuppressWarnings({
            "unchecked"
        })
        public Response createFromParcel(Parcel in) {
            return new Response(in);
        }
        public Response[] newArray(int size) {
            return (new Response[size]);
        }
    };

    protected Response(Parcel in) {
        this.id = ((String) in.readValue((String.class.getClassLoader())));
//        this.area = ((Area) in.readValue((Area.class.getClassLoader())));
        this.description = ((String) in.readValue((String.class.getClassLoader())));
        this._long = ((Double) in.readValue((Double.class.getClassLoader())));
        this.lat = ((Double) in.readValue((Double.class.getClassLoader())));
    }

    public Response() {
    }
    public Response(long dbId,
                       String shopDescription,
                       double gpsLatitude,
                       double gpsLongitude
                    ) {
        this.id = String.valueOf(dbId);
        this.description = shopDescription;
        this.lat = gpsLatitude;
        this._long = gpsLongitude;
    }

    public Response(long aLong, String string, String string1, double aDouble, double aDouble1) {
        id= String.valueOf(aLong);
        description=string;
        description=string1;
        lat=aDouble;
        _long=aDouble1;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

//    public Area getArea() {
//        return area;
//    }
//
//    public void setArea(Area area) {
//        this.area = area;
//    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getLong() {
        return _long;
    }

    public void setLong(Double _long) {
        this._long = _long;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        //dest.writeValue(area);
        dest.writeValue(description);
        dest.writeValue(_long);
        dest.writeValue(lat);
    }

    public int describeContents() {
        return  0;
    }

}
