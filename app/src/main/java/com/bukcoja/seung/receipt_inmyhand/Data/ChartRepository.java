package com.bukcoja.seung.receipt_inmyhand.Data;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.bukcoja.seung.receipt_inmyhand.DBHelper;
import com.bukcoja.seung.receipt_inmyhand.Presenter.ChartPresenter;
import com.bukcoja.seung.receipt_inmyhand.R;
import com.bukcoja.seung.receipt_inmyhand.RetrofitAPI;
import com.bukcoja.seung.receipt_inmyhand.VO.ChartDetailVO;
import com.bukcoja.seung.receipt_inmyhand.VO.ChartListVO;
import com.bukcoja.seung.receipt_inmyhand.VO.UpperLimitVO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChartRepository {

    private Retrofit mRetrofit;
    private RetrofitAPI mRetrofitAPI;
    private Call<ChartListVO> prod;
    private Gson mGson;
    private final String TAG="ChartRepository";


    private ChartPresenter presenter;

    private ChartListVO chartListVO;

    public ChartRepository(ChartPresenter presenter){

        this.presenter=presenter;
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

    private void callChartInfo(final Context context, final String userid,int groupid,String startdate,String enddate){
        Log.e(TAG, "callChartInfo");

        HashMap<String,Object> param=new HashMap<>();
        param.put("userid",userid);
        param.put("groupid",groupid);
        param.put("startdate",startdate);
        param.put("enddate",enddate);

        DBHelper dbHelper=new DBHelper(context, "rimp.db", null, 3);
        Cursor cursor=dbHelper.getResult("select * from category");

        ArrayList<Integer> categories=new ArrayList<>();
        while(cursor.moveToNext()){
            categories.add(cursor.getInt(cursor.getColumnIndex("categoryid")));
        }

        prod = mRetrofitAPI.getChartInfo(param,categories);
        prod.enqueue(new Callback<ChartListVO>() {
            @Override
            public void onResponse(Call<ChartListVO> call, Response<ChartListVO> response) {
                chartListVO =response.body();
                //Log.e("price1",String.valueOf(chartListVO.get(0).getCtgname()));

                //getDataFromLocal(context,userid);
                presenter.callbackPieChartData(chartListVO);
                presenter.callbackListData(chartListVO);

            }

            @Override
            public void onFailure(Call<ChartListVO> call, Throwable t) {
                Log.e(TAG, "onFailure");
                t.printStackTrace();
            }
        });

    }

    public void getDataFromLocal(Context context,final String userid){
        DBHelper dbHelper=new DBHelper(context, "rimp.db", null, 3);

        for(int i = 0; i< chartListVO.size(); i++){
            int ctgid= chartListVO.get(i).getCtgid();

            Cursor cursor=dbHelper.getResult("select sum(num*unitprice) from product where ctg="+ctgid);

            if(cursor.moveToNext()){
                int price=cursor.getInt(0);

                //Log.e("price3",String.valueOf(price));

                if(chartListVO.get(i).getCtgid()==ctgid) {
                    chartListVO.get(i).setPrice(chartListVO.get(i).getPrice() + price);
                    Log.e("price4",String.valueOf(chartListVO.get(i).getPrice()));
                }
            }
        }
    }
    public void getData(Context context,final String userid,int groupid,String startdate,String enddate) {

        setRetrofitInit(context);
        callChartInfo(context, userid,groupid,startdate,enddate);
    }

    public void getDetailData(Context context,String userid,int groupid,String date1,String date2,int ctgid){
        setRetrofitInit(context);

        HashMap<String,Object> param=new HashMap<>();
        param.put("userid",userid);
        param.put("groupid",groupid);
        param.put("startdate",date1);
        param.put("enddate",date2);
        param.put("ctgid",ctgid);

        mRetrofitAPI.getChartDetailInfo(param).enqueue(new Callback<ChartDetailVO>() {
            @Override
            public void onResponse(Call<ChartDetailVO> call, Response<ChartDetailVO> response) {
                Log.e("getDetailData",String.valueOf(response.body().size()));
                presenter.callbackDetailData(response.body());
            }

            @Override
            public void onFailure(Call<ChartDetailVO> call, Throwable t) {
                presenter.callbackDetailData(null);
                t.printStackTrace();
            }
        });

    }
    public void getUpperLimit(Context context,String userid,int ctgid){
        setRetrofitInit(context);

        HashMap<String,Object> param=new HashMap<>();
        param.put("userid",userid);
        param.put("ctgid",ctgid);

        mRetrofitAPI.getUpperLimit(param).enqueue(new Callback<UpperLimitVO>() {
            @Override
            public void onResponse(Call<UpperLimitVO> call, Response<UpperLimitVO> response) {
                presenter.callbackUpperLimitData(response.body());
            }

            @Override
            public void onFailure(Call<UpperLimitVO> call, Throwable t) {

            }
        });

    }

    public void saveUpperLimit(Context context,String userid,int ctgid,int value){
        setRetrofitInit(context);

        HashMap<String,Object> param=new HashMap<>();
        param.put("userid",userid);
        param.put("ctgid",ctgid);
        param.put("value",value);

        mRetrofitAPI.saveUpperLimit(param).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String result=response.body();

                int res;
                if(result.contains("1"))
                    res=1;
                else
                    res=0;

                presenter.callbackSaveLimitResult(res);
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }
}
