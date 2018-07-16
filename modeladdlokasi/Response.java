
package com.teamproject.plastikproject.modeladdlokasi;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Response implements Parcelable
{

    @SerializedName("result")
    @Expose
    private Result result;
    @SerializedName("ops")
    @Expose
    private List<Op> ops = null;
    @SerializedName("insertedCount")
    @Expose
    private Integer insertedCount;
    @SerializedName("insertedIds")
    @Expose
    private InsertedIds insertedIds;
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

    }
    ;

    protected Response(Parcel in) {
        this.result = ((Result) in.readValue((Result.class.getClassLoader())));
        in.readList(this.ops, (com.teamproject.plastikproject.modeladdlokasi.Op.class.getClassLoader()));
        this.insertedCount = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.insertedIds = ((InsertedIds) in.readValue((InsertedIds.class.getClassLoader())));
    }

    public Response() {
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public List<Op> getOps() {
        return ops;
    }

    public void setOps(List<Op> ops) {
        this.ops = ops;
    }

    public Integer getInsertedCount() {
        return insertedCount;
    }

    public void setInsertedCount(Integer insertedCount) {
        this.insertedCount = insertedCount;
    }

    public InsertedIds getInsertedIds() {
        return insertedIds;
    }

    public void setInsertedIds(InsertedIds insertedIds) {
        this.insertedIds = insertedIds;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(result);
        dest.writeList(ops);
        dest.writeValue(insertedCount);
        dest.writeValue(insertedIds);
    }

    public int describeContents() {
        return  0;
    }

}
