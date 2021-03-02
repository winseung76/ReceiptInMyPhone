package com.bukcoja.seung.receipt_inmyhand.Data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.bukcoja.seung.receipt_inmyhand.DBHelper;
import com.bukcoja.seung.receipt_inmyhand.Presenter.MainPresenter;
import com.bukcoja.seung.receipt_inmyhand.R;
import com.bukcoja.seung.receipt_inmyhand.RetrofitAPI;
import com.bukcoja.seung.receipt_inmyhand.VO.CategoryListVO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CategoryRepository {

    private MainPresenter presenter;
    private Retrofit mRetrofit;
    private RetrofitAPI mRetrofitAPI;
    private Call<CategoryListVO> category;
    private Gson mGson;
    private Context context;
    private final String TAG="CategoryRepository";

    public CategoryRepository(MainPresenter presenter){
        this.presenter=presenter;
    }

    public void getCtgData(Context con){
        //Log.e("InitDataLoader","updateCategoryInfo");

        this.context=con;
        setRetrofitInit();
        callCategory();

    }
    private void setRetrofitInit(){
        mGson = new GsonBuilder().setLenient().create();

        mRetrofit=new Retrofit.Builder()
                .baseUrl(context.getString(R.string.baseURL))
                .addConverterFactory(GsonConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(mGson))
                .build();

        mRetrofitAPI = mRetrofit.create(RetrofitAPI.class);

    }
    private void callCategory(){
        Log.e(TAG, "callUserInfo");

        category = mRetrofitAPI.getCategory();
        category.enqueue(mRetrofitCallback);
    }
    public Callback<CategoryListVO> mRetrofitCallback = new Callback<CategoryListVO>() {

        @Override
        public void onResponse(Call<CategoryListVO> call, Response<CategoryListVO> response) {

            CategoryListVO list=response.body();

            for(int i=0;i<list.size();i++) {

                CategoryListVO.Category categoryVO=list.get(i);

                int ctgId = categoryVO.getCategoryid();
                String ctg = categoryVO.getCategory();
                Log.e(TAG,"ctgId : "+ctgId+", ctg : "+ctg);

                DBHelper dbHelper = new DBHelper(context, "rimp.db", null, 3);

                SQLiteDatabase db = dbHelper.getWritableDatabase();
                dbHelper.createCtgTable(db);
                Cursor cursor = dbHelper.getResult("SELECT * FROM category WHERE categoryid=" + ctgId + " AND category='" + ctg + "';");
                Log.e("count", String.valueOf(cursor.getCount()));

                if (cursor.getCount() == 0) {
                    dbHelper.insert("INSERT INTO category VALUES(" + ctgId + ", '" + ctg + "');");
                    Log.e("updateCtgInfo", "insert success");
                }
            }
        }

        @Override
        public void onFailure(Call<CategoryListVO> call, Throwable t) {
            Log.e(TAG, "onFailure");
            t.printStackTrace();
        }
    };
}
