package com.bukcoja.seung.receipt_inmyhand.Data;

import android.content.Context;
import android.util.Log;

import com.bukcoja.seung.receipt_inmyhand.Callback.GroupCallback;
import com.bukcoja.seung.receipt_inmyhand.R;
import com.bukcoja.seung.receipt_inmyhand.RetrofitAPI;
import com.bukcoja.seung.receipt_inmyhand.VO.GroupListVO;
import com.bukcoja.seung.receipt_inmyhand.VO.GroupMemberListVO;
import com.bukcoja.seung.receipt_inmyhand.VO.GroupVO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GroupRepository {

    private GroupCallback callback;
    private Retrofit mRetrofit;
    private RetrofitAPI mRetrofitAPI;
    private Call<String> result;
    private Call<GroupListVO> groups;
    private Call<GroupVO> group;

    private final String TAG="GroupRepository";

    private Gson mGson;

    public GroupRepository(GroupCallback callback){
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
    // 그룹을 등록하는 메서드
    public void register(Context context,String userid,int groupid,String groupname){

        setRetrofitInit(context);

        HashMap<String,Object> param=new HashMap<>();
        param.put("id",userid);
        param.put("groupid",groupid);
        param.put("group",groupname);

        result = mRetrofitAPI.registerGroup(param);
        result.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                String result=response.body();

                if(result==null)
                    Log.e(TAG,"NULL");
                else
                    Log.e(TAG,result);

                callback.callbackResult(result);
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }
    // 그룹 정보를 불러오는 메서드
    public void load(Context context,String id){

        setRetrofitInit(context);
        HashMap<String,Object> param=new HashMap<>();
        param.put("id",id);

        groups = mRetrofitAPI.getGroupList(param);
        groups.enqueue(new Callback<GroupListVO>() {
            @Override
            public void onResponse(Call<GroupListVO> call, Response<GroupListVO> response) {

                GroupListVO groupVO=response.body();

                callback.callbackGroupList(groupVO);
            }

            @Override
            public void onFailure(Call<GroupListVO> call, Throwable t) {

            }
        });
    }

    // 그룹을 제거하는 메서드
    public void remove(Context context, String userid,int groupid){
        setRetrofitInit(context);

        HashMap<String,Object> param=new HashMap<>();
        param.put("userid",userid);
        param.put("groupid",groupid);

        result=mRetrofitAPI.removeGroup(param);
        result.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                String result=response.body();

                if(result.contains("1"))
                    callback.callbackRemoveResult(true);
                else
                    callback.callbackRemoveResult(false);
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                callback.callbackRemoveResult(false);
            }
        });

    }

    public void dataExistCheck(Context context,String userid,int groupid){
        setRetrofitInit(context);
        HashMap<String,Object> param=new HashMap<>();
        param.put("userid",userid);
        param.put("groupid",groupid);

        group = mRetrofitAPI.getGroup(param);
        group.enqueue(new Callback<GroupVO>() {
            @Override
            public void onResponse(Call<GroupVO> call, Response<GroupVO> response) {

                GroupVO groupVO=response.body();

                callback.callbackExistResult(groupVO);
            }
            @Override
            public void onFailure(Call<GroupVO> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void getMembers(Context context,int groupid){
        setRetrofitInit(context);

        HashMap<String,Object> param=new HashMap<>();
        param.put("groupid",groupid);

        mRetrofitAPI.getGroupMembers(param).enqueue(new Callback<GroupMemberListVO>() {
            @Override
            public void onResponse(Call<GroupMemberListVO> call, Response<GroupMemberListVO> response) {
                GroupMemberListVO groupMemberListVO=response.body();

                Log.e("getMembers",String.valueOf(groupMemberListVO.size()));

                callback.callbackGetMembersResult(groupMemberListVO);
            }

            @Override
            public void onFailure(Call<GroupMemberListVO> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }

}
