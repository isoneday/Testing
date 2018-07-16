package com.teamproject.plastikproject.plastik;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.teamproject.plastikproject.R;
import com.teamproject.plastikproject.plastik.activity.HalamanBaggingActivity;
import com.teamproject.plastikproject.plastik.helper.MyFuction;
import com.teamproject.plastikproject.plastik.model.ModelRegister;
import com.teamproject.plastikproject.plastik.network.MyRetrofitClient;
import com.teamproject.plastikproject.plastik.network.RestApi;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends MyFuction implements GoogleApiClient.OnConnectionFailedListener {

    @BindView(R.id.edtnama)
    EditText edtnama;
    @BindView(R.id.edtemail)
    EditText edtemail;
    @BindView(R.id.edtpassword)
    EditText edtpassword;
    @BindView(R.id.edtpasswordconfirm)
    EditText edtpasswordconfirm;
//    @BindView(R.id.regAdmin)
//    RadioButton regAdmin;
//    @BindView(R.id.regUserbiasa)
//    RadioButton regUserbiasa;
    @BindView(R.id.btnregister)
    Button btnregister;
    //String jenkel[]={"laki-laki","perempuan"};
    String strnama,strusername,strpassword,strconpassword,stralamat,strlevel,strjenkel,strnohp;



    private static final String TAG = "GoogleActivity";
    private static final int RC_SIGN_IN = 9001;

    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]

    // [START declare_auth_listener]
    private FirebaseAuth.AuthStateListener mAuthListener;
    // [END declare_auth_listener]

    private GoogleApiClient mGoogleApiClient;
    private TextView mStatusTextView;
    private TextView mDetailTextView;
    private CallbackManager mCallbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        // [START config_signin]
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        // [END config_signin]

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        // [START initialize_auth]
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]

        // [START auth_state_listener]
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // [START_EXCLUDE]
              //  updateUI(user);
                // [END_EXCLUDE]
            }
        };

        //fb
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        // Initialize Facebook Login button
        mCallbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("email", "public_profile");
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
                // ...
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);
                // ...
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
        FirebaseUser currentUser = mAuth.getCurrentUser();
   //     updateUI(currentUser);
        if (currentUser!=null) {
            updateUI();
        }

    }
    private void updateUI() {
        Toast.makeText(this, "you're logged in", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(RegisterActivity.this,HalamanBaggingActivity.class);
        startActivity(i);
        finish();

    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            //          startActivity(new Intent(MainActivity.this, HalamanBaggingActivity.class));
                            updateUI();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI();
                        }

                        // ...
                    }
                });
    }



    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Pass the activity result back to the Facebook SDK
        mCallbackManager.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                // Google Sign In failed, update UI appropriately
                // [START_EXCLUDE]
         //       updateUI(null);
                // [END_EXCLUDE]
            }
        }
    }


    // [START auth_with_google]
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        // [START_EXCLUDE silent]
        showProgressDialog("informasi");
        // [END_EXCLUDE]

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                          //  updateUI(null);

                        }else{
                            FirebaseUser user = mAuth.getCurrentUser();
                        //    updateUI(user);
                            startActivity(new Intent(RegisterActivity.this, HalamanBaggingActivity.class));
                       finish();
                        }
                        // [START_EXCLUDE]
                        hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
    }
    // [END auth_with_google]


    @OnClick({R.id.btnregister, R.id.btnlogin})
    public void onViewClicked(View view) {
        switch (view.getId()) {
//            case R.id.regAdmin:
//                strlevel="admin";
//                break;
            case R.id.btnlogin:
//                strlevel="user biasa";
                myIntent(LoginActivity.class);
                break;

            case R.id.btnregister:
                strnama =edtnama.getText().toString();
//                stralamat =edtalamat.getText().toString();
//                strnohp =edtnotelp.getText().toString();
                strusername = edtemail.getText().toString();
                strpassword =edtpassword.getText().toString();
                strconpassword =edtpasswordconfirm.getText().toString();
                if (TextUtils.isEmpty(strnama)){
                    edtnama.setError("nama tidak boleh kosong");
                    edtnama.requestFocus();
                    myanimation(edtnama);
                }
//                else if(TextUtils.isEmpty(stralamat)){
//                    edtalamat.requestFocus();
//                    edtalamat.setError("alamt tidak boleh kosong");
//                    myanimation(edtalamat);
//                }else if (TextUtils.isEmpty(strnohp)){
//                    edtnotelp.requestFocus();
//                    myanimation(edtnotelp);
//                    edtnotelp.setError("no hp tidak boleh kosong");
//                }
                else if(TextUtils.isEmpty(strusername)){
                    edtemail.requestFocus();
                    myanimation(edtemail);
                    edtemail.setError("username tidak boleh kosong");
                }else if (TextUtils.isEmpty(strpassword)){
                    edtpassword.requestFocus();
                    myanimation(edtpassword);
                    edtpassword.setError("password tidak boleh kosong");
                }else if (strpassword.length()<6){
                    myanimation(edtpassword);
                    edtpassword.setError("password minimal 6 karakter");
                }else if (TextUtils.isEmpty(strconpassword)){
                    edtpasswordconfirm.requestFocus();
                    myanimation(edtpasswordconfirm);
                    edtpasswordconfirm.setError("password confirm tidak boleh kosong");
                }else if (!strpassword.equals(strconpassword)){
                    edtpasswordconfirm.requestFocus();
                    myanimation(edtpasswordconfirm);
                    edtpasswordconfirm.setError("password tidak sama");
                }else{
                    registeruser();
                }

                    break;
        }
    }

    private void registeruser() {
            final ProgressDialog dialog =ProgressDialog.show(c,"process register user","harap bersabar");

            RestApi api = MyRetrofitClient.getInstaceRetrofit();
            Call<ModelRegister> ModelUserCall =api.registerUser(strusername,strpassword,strlevel,strnama
        );
        //nangkap callback
    ModelUserCall.enqueue(new Callback<ModelRegister>() {
        @Override
        public void onResponse(Call<ModelRegister> call, Response<ModelRegister> response) {
                dialog.dismiss();
                if (response.isSuccessful()){
                    myIntent(LoginActivity.class);
                    finish();

                } else{
                    myToast("gagal register");
                }
        }

        @Override
        public void onFailure(Call<ModelRegister> call, Throwable t) {
            dialog.dismiss();
            myToast("gagal koneksi :"+t.getMessage());
        }
    });
    }
    public void onGoogle(View view) {
        signIn();
    }
    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
        Toast.makeText(this, "Google Play Services error.", Toast.LENGTH_SHORT).show();

    }
}
