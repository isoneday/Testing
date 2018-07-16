package com.teamproject.plastikproject.plastik.fragment;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.teamproject.plastikproject.R;
import com.teamproject.plastikproject.plastik.helper.SessionManager;
import com.teamproject.plastikproject.plastik.model.ResponseChangeUsername;
import com.teamproject.plastikproject.plastik.model.ResponseDataUser;
import com.teamproject.plastikproject.plastik.model.ResponseUser;
import com.teamproject.plastikproject.plastik.network.MyRetrofitClient;
import com.teamproject.plastikproject.plastik.network.RestApi;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyAccountFragment extends Fragment {


    @BindView(R.id.txtusername)
    TextView txtusername;
    @BindView(R.id.btnchangeusername)
    Button btnchangeusername;
    @BindView(R.id.txtnama)
    TextView txtnama;
    @BindView(R.id.txtemail)
    TextView txtemail;
    Unbinder unbinder;
    private List<ResponseUser> listdatauser;

    public MyAccountFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_account, container, false);
        unbinder = ButterKnife.bind(this, view);
        final ProgressDialog dialog =ProgressDialog.show(getActivity(),"","loading . . .");
        btnchangeusername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog d = new Dialog(getContext());
                d.setContentView(R.layout.changeusername);
                final EditText edtusernamebaru = (EditText)d.findViewById(R.id.edtusernamebaru);
                Button btnok =(Button)d.findViewById(R.id.btnok);
                d.show();
                btnok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String usernamebaru =edtusernamebaru.getText().toString();
                        if (TextUtils.isEmpty(usernamebaru)){
                            edtusernamebaru.setError("username tidak boleh kosong");
                            edtusernamebaru.requestFocus();
                        }else{
                            final ProgressDialog dialog1 =ProgressDialog.show(getContext(),"loading","");
                            RestApi api = MyRetrofitClient.getInstaceRetrofit();
                            SessionManager manager =new SessionManager(getActivity());
                            String idtoken =manager.getToken();
                            String iduser1= manager.getIdUser();
                            Call<ResponseChangeUsername> usernameCall=api.changeusername(iduser1,idtoken,usernamebaru,"iswandi.saputra11@gmail.com","0");
                            usernameCall.enqueue(new Callback<ResponseChangeUsername>() {
                                @Override
                                public void onResponse(Call<ResponseChangeUsername> call, Response<ResponseChangeUsername> response) {
                                 d.dismiss();
                                   dialog1.dismiss();
                                    if (response.isSuccessful()){
                                        Toast.makeText(getContext(), "berhasil", Toast.LENGTH_SHORT).show();
                                    }else{
                                        Toast.makeText(getContext(), "gagal", Toast.LENGTH_SHORT).show();

                                    }
                                }

                                @Override
                                public void onFailure(Call<ResponseChangeUsername> call, Throwable t) {
            dialog1.dismiss();
            d.dismiss();
                                    Toast.makeText(getContext(), "sorry"+t.getMessage(), Toast.LENGTH_SHORT).show();

                                }
                            });
                        }
                    }
                });
            }
        });
        RestApi api = MyRetrofitClient.getInstaceRetrofit();
        SessionManager ge = new SessionManager(getActivity());
        String id = ge.getIdUser();
        Call<ResponseDataUser> userCall =api.getdatauser(id);
        userCall.enqueue(new Callback<ResponseDataUser>() {
            @Override
            public void onResponse(Call<ResponseDataUser> call, Response<ResponseDataUser> response) {
                dialog.dismiss();

                if (response.isSuccessful()){
                    listdatauser = new ArrayList<ResponseUser>();

                    listdatauser = response.body().getResponse();
                    final String[] itemsemail = new String[listdatauser.size()];
                    final String[] itemsnama = new String[listdatauser.size()];
   final String[] itemsusername = new String[listdatauser.size()];


                    for (int i = 0; i < listdatauser.size(); i++) {
                        //Storing names to string array
                        itemsemail[i] = listdatauser.get(i).getEmail().toString();
                        itemsnama[i] = listdatauser.get(i).getName().toString();
                        itemsusername[i] = listdatauser.get(i).getName().toString();
                        //       Toast.makeText(getApplicationContext(), " banyak button" + itemsnama, Toast.LENGTH_LONG).show();

                    }
                    String email = itemsemail[0].toString();
                    String name =itemsnama[0].toString();
                    String usernmae = itemsusername[0].toString();
                    txtnama.setText(name);
                    txtemail.setText(email);
                    txtusername.setText(usernmae);
                }
                else{
                    Toast.makeText(getActivity(), "gagal ya"+response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseDataUser> call, Throwable t) {
                dialog.dismiss();

            }
        });
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
