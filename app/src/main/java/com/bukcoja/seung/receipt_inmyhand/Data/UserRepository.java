package com.bukcoja.seung.receipt_inmyhand.Data;

import android.content.Context;
import android.util.Log;

import com.bukcoja.seung.receipt_inmyhand.Callback.UserCallback;
import com.bukcoja.seung.receipt_inmyhand.R;
import com.bukcoja.seung.receipt_inmyhand.RetrofitAPI;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserRepository {

    private UserCallback callback;
    private Retrofit mRetrofit;
    private RetrofitAPI mRetrofitAPI;
    private Call<String> state;
    private Gson mGson;
    private final String TAG="UserRepository";


    public UserRepository(UserCallback callback){
        this.callback=callback;
    }
    private void setRetrofitInit(Context context){
        mGson = new GsonBuilder().setLenient().create();

        mRetrofit=new Retrofit.Builder()
                .baseUrl(context.getString(R.string.baseURL))
                .addConverterFactory(GsonConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(mGson))
                .build();

        mRetrofitAPI = mRetrofit.create(RetrofitAPI.class);

    }
    public void sendData(Context context,String id,String password,String name,String phone){

        setRetrofitInit(context);

        HashMap<String,Object> param=new HashMap<>();
        param.put("id",id);
        param.put("password",password);
        param.put("name",name);
        param.put("phone",phone);
        //param.put("email",email);

        //state=mRetrofitAPI.registerUser(param);

        mRetrofitAPI.registerUser(param).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                try {
                    String result = response.body().string();
                    Log.e(TAG,result);

                    int res;
                    if(result.contains("1"))
                        res=1;
                    else
                        res=0;

                    callback.callbackRegisterResult(res);


                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
        /*
        state.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String result=response.body();
                Log.e(TAG,result);

                int res;
                if(result.contains("1"))
                    res=1;
                else
                    res=0;

                callback.callbackRegisterResult(res);
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                t.printStackTrace();
            }
        });

         */
    }

    // 중복된 아이디인지 아닌지 결과를 얻어오는 메서드
    public void getResult(Context context,String id){

        setRetrofitInit(context);
        HashMap<String,Object> param=new HashMap<>();
        param.put("id",id);

        state=mRetrofitAPI.checkIDOverlay(param);

        state.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String result=response.body();
                Log.e(TAG,result);

                int res;
                if(result.contains("1"))
                    res=1;
                else
                    res=0;

                callback.callbackCheckResult(res);

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    // 회원 정보를 삭제
    public void removeData(Context context,String userid){
        setRetrofitInit(context);
        HashMap<String,Object> param=new HashMap<>();
        param.put("userid",userid);

        state=mRetrofitAPI.removeUser(param);
        state.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                String result=response.body();

                callback.callbackRemoveResult(result);
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    // 토큰 업데이트
    public void updateToken(Context context,String userid,String token){
        setRetrofitInit(context);

        HashMap<String,Object> param=new HashMap<>();
        param.put("userid",userid);
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
                Log.e("register token result","fail");
                t.printStackTrace();
            }
        });
    }


}
