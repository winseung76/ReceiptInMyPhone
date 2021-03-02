package com.bukcoja.seung.receipt_inmyhand.Data;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.bukcoja.seung.receipt_inmyhand.Callback.ImageFileCallback;
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

public class ImageFileRepository {

    private Retrofit mRetrofit;
    private RetrofitAPI mRetrofitAPI;
    private Gson mGson;
    public static final String UPLOAD_KEY = "image";
    private ImageFileCallback callback;

    public ImageFileRepository(ImageFileCallback callback){
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

    public void uploadReceiptFile(Context context,String uploadImage,String encodedImage,String userid){
        setRetrofitInit(context);

        HashMap<String,Object> param = new HashMap<>();
        // 이미지 이름과 경로, 이미지를 올린 사용자 정보를 서버에 전달
        param.put("image", uploadImage);
        param.put("name",encodedImage);
        param.put("userid",userid);

        mRetrofitAPI.uploadFile(param).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String result=response.body();
                Log.e("image size",response.body());
                int res;

                if(result.contains("1")) {
                    res=1;
                    Log.e("uploadFile", "image upload is success");
                }
                else {
                    res=0;
                    Log.e("uploadFIle", "image upload is failed");
                }
                callback.callbackImageUploadResult(res,0);
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void uploadProfile(Context context,String uploadImage,String encodedImage,String userid){
        setRetrofitInit(context);

        HashMap<String,Object> param = new HashMap<>();
        param.put("image", uploadImage);
        param.put("name",encodedImage);
        param.put("userid",userid);

        mRetrofitAPI.uploadProfile(param).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String result=response.body();

                Log.e("uploadProfile",response.body());
                int res;

                if(result.contains("1")) {
                    res=1;
                    Log.e("uploadFile", "profile image upload is success");
                }
                else {
                    res=0;
                    Log.e("uploadFIle", "profile image upload is failed");
                }
                callback.callbackImageUploadResult(res,1);
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    public void getProfile(Context context,String userid){
        setRetrofitInit(context);

        //setRetrofitInit(context);

        HashMap<String,Object> param = new HashMap<>();
        param.put("userid",userid);

        Call<ResponseBody> call = mRetrofitAPI.getProfile(param);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()){
                    //boolean writtenToDisk = writeResponseBodyToDisk(response.body());
                    try {
                        //Log.e("getProfile", response.body().string());
                        callback.callbackGetProfileImage(response.body().string());
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                    //Bitmap bitmap=writeResponseBodyToDisk(response.body());
                    Bitmap bitmap=BitmapFactory.decodeStream(response.body().byteStream());

                    if(bitmap==null)
                        Log.d("getProfile a failed: ", "");
                    else{
                        Log.d("getProfile a success: ", "");
                    }

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    public void changeDefaultImage(Context context,String userid){
        setRetrofitInit(context);

        HashMap<String,Object> param=new HashMap<>();
        param.put("userid",userid);

        mRetrofitAPI.setDefaultProfile(param).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if (response.body().string().contains("1"))
                        callback.callbackDefaultProfile(1);
                    else
                        callback.callbackDefaultProfile(0);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
            }
        });


    }


}
