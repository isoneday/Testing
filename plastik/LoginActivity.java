package com.teamproject.plastikproject.plastik;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.teamproject.plastikproject.R;
import com.teamproject.plastikproject.plastik.activity.HalamanBaggingActivity;
import com.teamproject.plastikproject.plastik.helper.SessionManager;
import com.teamproject.plastikproject.plastik.model.ModelForgot;
import com.teamproject.plastikproject.plastik.model.ModelUser;
import com.teamproject.plastikproject.plastik.network.MyRetrofitClient;
import com.teamproject.plastikproject.plastik.network.RestApi;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends SessionManager {

    @BindView(R.id.regUsername)
    EditText regUsername;
    @BindView(R.id.regPass)
    EditText regPass;
    //    @BindView(R.id.regAdmin)
//    RadioButton regAdmin;
//    @BindView(R.id.regUserbiasa)
//    RadioButton regUserbiasa;
    @BindView(R.id.regBtnLogin)
    Button regBtnLogin;
    @BindView(R.id.regBtnRegister)
    Button regBtnRegister;
    String strlevel, strusername, strpassword;
    @BindView(R.id.regforgotpass)
    Button regforgotpass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
//
//        if (regAdmin.isChecked()){
//            strlevel="admin";
//        }else{
//            strlevel="user biasa";
//        }
    }

    @OnClick({R.id.regBtnLogin, R.id.regBtnRegister,R.id.regforgotpass})
    public void onViewClicked(View view) {
        strusername = regUsername.getText().toString();
        strpassword = regPass.getText().toString();

        switch (view.getId()) {
//            case R.id.regAdmin:
//                strlevel="admin";
//
//                break;
//            case R.id.regUserbiasa:
//                strlevel="user biasa";
//
//                break;
            case R.id.regBtnLogin:
                if (TextUtils.isEmpty(strusername)) {
                    regUsername.setError("username tidak boleh kosong");
                } else if (TextUtils.isEmpty(strpassword)) {
                    regPass.setError("password tidak boleh kosong");
                } else if (strpassword.length() < 6) {
                    regPass.setError("minimal password 6 karakter");
                } else {
                    loginuser();
                }

                break;
            case R.id.regBtnRegister:
                myIntent(RegisterActivity.class);
                break;
            case  R.id.regforgotpass:
                final Dialog dialog = new Dialog(LoginActivity.this);
                dialog.setContentView(R.layout.lupapassword);
                dialog.setTitle("Lupa Password");
                dialog.show();
                final EditText edtemaillamat = (EditText)dialog.findViewById(R.id.edtemaillama);
                Button btnlupapas = (Button)dialog.findViewById(R.id.btnlupapass);
                btnlupapas.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String emaillama =edtemaillamat.getText().toString();
                        if (TextUtils.isEmpty(emaillama)){
                            edtemaillamat.setError("email tidak boleh kosong");
                            edtemaillamat.requestFocus();
                        }else{
                            showProgressDialog("proses .....");
            RestApi api =MyRetrofitClient.getInstaceRetrofit();
            Call<ModelForgot> userCall = api.forgotpassword(emaillama);
            userCall.enqueue(new Callback<ModelForgot>() {
                @Override
                public void onResponse(Call<ModelForgot> call, Response<ModelForgot> response) {
                    hideProgressDialog();
                    Toast.makeText(LoginActivity.this, "Check Your Email To Reset Password", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                }

                @Override
                public void onFailure(Call<ModelForgot> call, Throwable t) {
                 hideProgressDialog();
                 dialog.dismiss();
                }
            });


                        }
                    }
                });
                break;

        }
    }

    private void loginuser() {
        showProgressDialog("proses login user");
        RestApi api = MyRetrofitClient.getInstaceRetrofit();
        Call<ModelUser> modelUserCall = api.loginUser(
                strusername, strpassword);
        modelUserCall.enqueue(new Callback<ModelUser>() {
            @Override
            public void onResponse(Call<ModelUser> call, Response<ModelUser> response) {
                hideProgressDialog();

                if (response.isSuccessful()) {
                    String token = response.body().getData().getId();
                    String userid = response.body().getData().getUserId();
                    Log.d("testaja", token);
                    sessionManager.createSession(strusername);
                    sessionManager.setIdUser(userid);
                    sessionManager.setToken(token);
//ASU6fXPWzLCbrf8Wt6fiynINiISpHcndJM5dwRsZsehhyKW2KDL25rWTOGIu4RVl
                    myIntent(HalamanBaggingActivity.class);
                    finish();
                } else {
                    myToast("gagal login,cek email anda");
                }
                //                if (result.equals("1")) {
//                myToast(msg);

//                String iduser =response.body().getUser().getIdUser();
//                        sessionManager.createSession(strusername);
//                    sessionManager.setIdUser(iduser);
//                    finish();
//                }


            }

            @Override
            public void onFailure(Call<ModelUser> call, Throwable t) {
                hideProgressDialog();
                myToast("gagal koneksi :" + t.getMessage());

            }
        });

    }
}
