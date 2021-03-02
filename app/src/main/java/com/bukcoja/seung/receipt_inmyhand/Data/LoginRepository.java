package com.bukcoja.seung.receipt_inmyhand.Data;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.bukcoja.seung.receipt_inmyhand.Callback.LoginCallback;
import com.bukcoja.seung.receipt_inmyhand.DBHelper;
import com.bukcoja.seung.receipt_inmyhand.R;
import com.bukcoja.seung.receipt_inmyhand.RetrofitAPI;
import com.bukcoja.seung.receipt_inmyhand.VO.LoginVO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginRepository {

    private Retrofit mRetrofit;
    private RetrofitAPI mRetrofitAPI;
    private Call<LoginVO> userInfo;
    private Gson mGson;
    private LoginCallback callback;
    private final String TAG="LoginRepository";

    public LoginRepository(LoginCallback callback){
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

    // 사용자 아이디와 비밀번호를 기반으로 사용자 정보를 가져오는 메서드
    public void getData(Context context,String id,String password,int state){

        setRetrofitInit(context);

        Log.e(TAG, "callUserInfo");
        HashMap<String,Object> param=new HashMap<>();

        if(password==null){
            password="";
        }

        param.put("id",id);
        param.put("password",password);
        param.put("state",state);

        userInfo = mRetrofitAPI.getUserInfo(param);
        userInfo.enqueue(new Callback<LoginVO>() {
            @Override
            public void onResponse(Call<LoginVO> call, Response<LoginVO> response) {
                LoginVO loginVO=response.body();

                String res=loginVO.getId();
                Log.e(TAG,res);

                callback.callbackUserInfo(loginVO);
            }

            @Override
            public void onFailure(Call<LoginVO> call, Throwable t) {
                Log.e(TAG, "onFailure");
                t.printStackTrace();
                callback.callbackUserInfo(null);
            }
        });
    }


    // 로그인한 사용자 정보를 안드로이드 내의 로컬 DB에 업데이트
    public void updateUserState(Context context,int state, String id){

        DBHelper dbHelper=new DBHelper(context, "rimp.db", null, 3);

        Cursor cursor=dbHelper.getResult("select * from login where id='"+id+"';");

        Log.e("updateUserState",String.valueOf(cursor.getCount()));

        if(cursor.getCount()==0)
            dbHelper.insert("insert into login values('"+id+"', "+state+");");
        else
            dbHelper.insert("update login set state="+state+" where id='"+id+"';");


        //cursor=dbHelper.getResult("select * from login where state=1");
        //Log.e("updateUserInfo",String.valueOf(cursor.getCount()));
    }

    // 자동로그인 상태인 사용자의 아이디를 얻어옴
    public void getState(Context context){

        DBHelper dbHelper=new DBHelper(context, "rimp.db", null, 3);
        Cursor cursor=dbHelper.getResult("select id from login where state=1");
        Log.e("getState query count",String.valueOf(cursor.getCount()));

        // 자동 로그인 상태의 아이디가 있는 경우
        if(cursor.getCount()>0) {
            cursor.moveToNext();
            String id = cursor.getString(0);
            callback.callbackAutoLogin(id);

        }
        // 자동 로그인 상태인 아이디가 없는 경우
        else {
            callback.callbackAutoLogin(null);
            //checkUserInfo(context, null, null, 1);
        }
    }

}
