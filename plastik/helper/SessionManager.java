package com.teamproject.plastikproject.plastik.helper;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.VisibleForTesting;

import com.google.gson.Gson;
import com.teamproject.plastikproject.modeldatalokasi.Response;
import com.teamproject.plastikproject.plastik.LoginActivity;
import com.teamproject.plastikproject.plastik.activity.HalamanBaggingActivity;


public class SessionManager extends MyFuction {
    @VisibleForTesting

    /*variable sharepreference*/
    SharedPreferences pref;

    public SharedPreferences.Editor editor;
    public SessionManager sessionManager;
    Gson gson ;
    /*mode share preference*/
    int mode = 0;

    /*nama dari share preference*/
    private static final String pref_name = "crudpref";

    /*kunci share preference*/
    private static final String is_login = "islogin";
    public static final String kunci_email = "keyemail";
    String json;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //  setContentView(R.layout.activity_main);
        sessionManager = new SessionManager(getApplicationContext());
      //  setTheme(R.style.MyAppTheme);
        gson = new Gson();
        Response response = new Response();
       // List<ResponseDataLokasi> Objec =response.get

    }
    public SessionManager(){

    }

    /*construktor*/
    public SessionManager(Context context) {
        /*mengakses class ini*/
        c = context;
        /*share preference dari class ini*/ /*(nama, mode)*/
        pref = context.getSharedPreferences(pref_name, mode);
        editor = pref.edit();
    }

    /*methode membuat session*/
    public void createSession(String email){
        /*login value menjadi true*/
        editor.putBoolean(is_login, true);
        /*memasukkan email ke dalam variable kunci email*/
        editor.putString(kunci_email, email);
        editor.commit();
    }
    public void setIdUser(String iduser) {
        editor.putBoolean(is_login, true);
        editor.putString("iduser", iduser);
        editor.commit();
    }

    public String getIdUser() {
        return pref.getString("iduser", "");
    }
    public void setidincre(String iduser) {
        editor.putBoolean(is_login, true);
        editor.putString("incre", iduser);
        editor.commit();
    }

    public String getIdincre() {
        return pref.getString("incre", "");
    }
  public void setDay(String day) {
        editor.putBoolean(is_login, true);
        editor.putString("day", day);
        editor.commit();
    }

    public String getDay() {
        return pref.getString("day", "");
    }

     public void setTime(String time) {
        editor.putBoolean(is_login, true);
        editor.putString("time", time);
        editor.commit();
    }

    public String getTime() {
        return pref.getString("time", "");
    }
  public void setMili(String time) {
        editor.putBoolean(is_login, true);
        editor.putString("mili", time);
        editor.commit();
    }

    public String getMili() {
        return pref.getString("mili", "");
    }

  public void setDataPlace(Object response) {


        editor.putBoolean(is_login, true);
        json = gson.toJson(response);
        editor.putString("MyObject", json);
        editor.commit();
    }

    public Object getDataPlace() {


        Gson gson = new Gson();
        String json = pref.getString("MyObject", "");
        Response obj = gson.fromJson(json, Response.class);
        return pref.getString("MyObject", "");
    }

  public void setToken(String token) {
        editor.putBoolean(is_login, true);
        editor.putString("token", token);
        editor.commit();
    }

    public String getToken() {
        return pref.getString("token", "");
    }


    public void checkLogin(){
        /*jika is_login = false*/
        if (!this.islogin()){
            /*pergi ke loginactivity*/
            Intent i = new Intent(c, LoginActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            c.startActivity(i);
        }else {
            /*jika true, pergi ke mainactivity*/
            Intent i = new Intent(c, HalamanBaggingActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            c.startActivity(i);
        }
    }

    /*set is_login menjadi false*/
    public boolean islogin() {
        return pref.getBoolean(is_login, false);
    }





    public void logout(){

        /*hapus semua data dan kunci*/
        editor.clear();
        editor.commit();

        //gmail logout


        /*pergi ke loginactivity*/
//        Intent i = new Intent(c, LoginActivity.class);
//        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        c.startActivity(i);
    }

     @Override
    public void onStop() {
        super.onStop();
        hideProgressDialog();
    }






}
