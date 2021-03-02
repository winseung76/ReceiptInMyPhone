package com.bukcoja.seung.receipt_inmyhand.Data;

import android.content.Context;

import com.bukcoja.seung.receipt_inmyhand.Callback.RcmdCallback;
import com.bukcoja.seung.receipt_inmyhand.R;
import com.bukcoja.seung.receipt_inmyhand.RetrofitAPI;
import com.bukcoja.seung.receipt_inmyhand.VO.DateVO;
import com.bukcoja.seung.receipt_inmyhand.VO.ServiceVO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RcmdServiceRepository {

    private Retrofit mRetrofit;
    private RetrofitAPI mRetrofitAPI;
    private Gson mGson;
    private RcmdCallback callback;

    public RcmdServiceRepository(RcmdCallback callback){
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

    public void getData(Context context,int time){
        setRetrofitInit(context);

        HashMap<String,Object> param=new HashMap<>();
        // php로 time 정보를 전달
        param.put("time",time);

        mRetrofitAPI.getStoreInfo(param).enqueue(new Callback<ServiceVO>() {
            @Override
            public void onResponse(Call<ServiceVO> call, Response<ServiceVO> response) {

                ServiceVO serviceVO=response.body();

                callback.callbackRcmdData(serviceVO);
            }

            @Override
            public void onFailure(Call<ServiceVO> call, Throwable t) {

            }
        });
    }

    public void getDateData(Context context){
        setRetrofitInit(context);
        HashMap<String,Object> param=new HashMap<>();
        mRetrofitAPI.getUpdateDate(param).enqueue(new Callback<DateVO>() {
            @Override
            public void onResponse(Call<DateVO> call, Response<DateVO> response) {

                DateVO dateVO=response.body();

                callback.callbackUpdateDate(dateVO);
            }

            @Override
            public void onFailure(Call<DateVO> call, Throwable t) {

            }
        });

    }

}
