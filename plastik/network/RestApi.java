package com.teamproject.plastikproject.plastik.network;


import com.teamproject.plastikproject.model.PurchaseListModelbar;
import com.teamproject.plastikproject.modeladdlokasi.ResponseAddLokasi;
import com.teamproject.plastikproject.modeldatalokasi.ResponseDataLokasi;
import com.teamproject.plastikproject.modeldatalokasi.ResponseDelete;
import com.teamproject.plastikproject.modeldataskedule.ResponseDataSkeduleuser;
import com.teamproject.plastikproject.modelupdatelokasi.ResponseUpdatelokasi;
import com.teamproject.plastikproject.modelupdateskedule.ModelUpdateSkedule;
import com.teamproject.plastikproject.plastik.model.ModelForgot;
import com.teamproject.plastikproject.plastik.model.ModelGeoCoder;
import com.teamproject.plastikproject.plastik.model.ModelKategoriMakanan;
import com.teamproject.plastikproject.plastik.model.ModelRegister;
import com.teamproject.plastikproject.plastik.model.ModelUser;
import com.teamproject.plastikproject.plastik.model.ResponseChangeUsername;
import com.teamproject.plastikproject.plastik.model.ResponseDataUser;
import com.teamproject.plastikproject.plastik.model.ServerResponse;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Blackswan on 9/12/2017.
 */

public interface RestApi {


    @GET("json")
    Call<ModelGeoCoder> get_adress(
            @Query("latlng") String latlng


    );

    @FormUrlEncoded
    @POST("smart_schedules")
    Call<PurchaseListModelbar> insertskedule(
            @Field("id_user") String id_user,
            @Field("day") String day,
            @Field("time") String time,
            @Field("status_on") String status
    );
    @POST("smart_schedules/UpdateByIdIncrement")
    @FormUrlEncoded
    Call<ModelUpdateSkedule> updatedata(@Field("time") String time,
                                        @Field("id_user") String iduser,
                                        @Field("day") String hari,
                                        @Field("status_on") String status,
                                        @Field("id_increment") String idincre

    );

    @GET("smart_areas/GetAllArea2DSphere")
    Call<ResponseDataLokasi> getalllokasi();

    @FormUrlEncoded
    @POST("Users")
    Call<ModelRegister> registerUser(
            @Field("email") String stremail,
            @Field("password") String strpassword,
            @Field("is_admin") String strisadmin,
            @Field("name") String strname
    );

    @GET("UserManagements/GetUser")
    Call<ResponseDataUser> getdatauser(
            @Query("id") String strid);

 @GET("smart_schedules/GetScheduleByUser")
    Call<ResponseDataSkeduleuser> getdataskeduleuser(
            @Query("id_user") String strid);


    @PUT("Users/{id}")
    @FormUrlEncoded
    Call<ResponseChangeUsername> changeusername(@Path("id") String id,
                                                @Query("access_token") String token,
                                                @Field("name") String username,
                                                @Field("email") String email,
                                                @Field("is_admin") String isadmin);
    @FormUrlEncoded
    @POST("smart_areas/UpdateArea2DSphere")
    Call<ResponseUpdatelokasi>  updatelokasi(@Field("long") String strlong,
                                              @Field("lat") String strlat,
                                              @Field("description") String strdesc,
                                              @Field("_id") String strid);

    @DELETE("smart_areas/{IDSmartArea}")
    Call<ResponseDelete> deletedatalokasi(@Path("IDSmartArea") String id);


    @FormUrlEncoded
    @POST("Users/login")
    Call<ModelUser> loginUser(
            @Field("email") String stremail,
            @Field("password") String strpassword
    );

    @FormUrlEncoded
    @POST("Users/logout")
    Call<ModelUser> logout(
            @Field("access_token") String access_token
    );
    @FormUrlEncoded
    @POST("Users/reset")
    Call<ModelForgot> forgotpassword(
            @Field("email") String email
    );

    @FormUrlEncoded
    @POST("deletedatamakanan.php/")
    Call<ModelUser> deletedata(
            @Field("vsidmakanan") String stridmakanan
    );

    @FormUrlEncoded
    @POST("smart_areas/InsertArea2DSphere")
    Call<ResponseAddLokasi> addlokasi(
            @Field("long") String strlong,
            @Field("lat") String strlat,
            @Field("description") String strdescription
    );

//    @FormUrlEncoded
//    @POST("users/reset")
//    Call<ModelUser> deletedata(
//            @Field("vsidmakanan") String stridmakanan
//    );


    @GET("ambildataCarikategorimakanan.php/")
    public Call<ModelKategoriMakanan> getDataCarikategoriMakanan();

    @Multipart
    @POST("uploadmakanan1.php")
    Call<ServerResponse> uploadFile(@Part MultipartBody.Part file, @Part("image") RequestBody name);


}
