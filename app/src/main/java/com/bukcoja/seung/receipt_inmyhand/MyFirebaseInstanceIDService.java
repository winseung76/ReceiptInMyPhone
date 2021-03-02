package com.bukcoja.seung.receipt_inmyhand;

import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = "FirebaseInstanceID";
    private Retrofit mRetrofit;
    private RetrofitAPI mRetrofitAPI;
    private Gson mGson;
    private UserInfo userInfo;

    @Override
    public void onTokenRefresh() {

        userInfo=(UserInfo)getApplication();

        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.e(TAG, "Refreshed token: " + refreshedToken);
        // 생성한 토큰을 서버로 날려서 저장
        sendRegistrationToServer(refreshedToken);

    }
    private void setRetrofitInit(){
        mGson = new GsonBuilder().setLenient().create();

        mRetrofit=new Retrofit.Builder()
                .baseUrl(getApplicationContext().getString(R.string.baseURL))
                .addConverterFactory(GsonConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(mGson))
                .build();

        mRetrofitAPI = mRetrofit.create(RetrofitAPI.class);

    }
    private void sendRegistrationToServer(String token) {

        // 만들어진 토큰을 저장한다!!!
        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("token", token);
        editor.commit();


        /*
        setRetrofitInit();

        HashMap<String,Object> param=new HashMap<>();
        param.put("userid",userInfo.getId());
        param.put("token",token);

        mRetrofitAPI.updateToken(param).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String res=response.body();

                if(res.contains("1"))
                    Log.e("register token result","success");
                else
                    Log.e("register token result","fail");
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                t.printStackTrace();
            }
        });
        */
    }

}
