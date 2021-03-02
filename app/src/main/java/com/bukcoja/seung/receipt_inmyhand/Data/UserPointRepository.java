package com.bukcoja.seung.receipt_inmyhand.Data;

import android.content.Context;
import android.util.Log;

import com.bukcoja.seung.receipt_inmyhand.DBHelper;
import com.bukcoja.seung.receipt_inmyhand.Callback.DataCallback;
import com.bukcoja.seung.receipt_inmyhand.DigitalReceipt.ReceiptPayloadItem.ReceiptItem;
import com.bukcoja.seung.receipt_inmyhand.DigitalReceipt.ReceiptPayloadItem.Tag0x11;
import com.bukcoja.seung.receipt_inmyhand.R;
import com.bukcoja.seung.receipt_inmyhand.RetrofitAPI;
import com.bukcoja.seung.receipt_inmyhand.VO.PointListVO;
import com.bukcoja.seung.receipt_inmyhand.VO.PointVO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserPointRepository {
    private Retrofit mRetrofit;
    private RetrofitAPI mRetrofitAPI;
    private Call<PointVO> userpoint;
    private Call<PointListVO> list;
    private Gson mGson;


    private DataCallback dataCallback;

    public UserPointRepository(DataCallback dataCallback){

        this.dataCallback=dataCallback;
    }
    public UserPointRepository(){

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

    public void getUserPoint(Context context,String id){

        setRetrofitInit(context);

        HashMap<String,Object> param=new HashMap<>();
        param.put("id",id);

        userpoint = mRetrofitAPI.getUserPoint(param);
        userpoint.enqueue(new Callback<PointVO>() {
            @Override
            public void onResponse(Call<PointVO> call, Response<PointVO> response) {

                PointVO pointVO=response.body();

                Log.e("usablepoint",String.valueOf(pointVO.getUsablepoint()));
                Log.e("totalpoint",String.valueOf(pointVO.getTotalpoint()));

                dataCallback.callbackUserPoint(pointVO);
            }

            @Override
            public void onFailure(Call<PointVO> call, Throwable t) {

            }
        });

    }

    public void updateData(final Context context,final int receiptid,final int groupid, final String id, final String date, final int usedpoint){

        setRetrofitInit(context);
        HashMap<String,Object> param=new HashMap<>();
        param.put("id",id);
        param.put("date",date);
        param.put("usedpoint",usedpoint);

        userpoint = mRetrofitAPI.updateUserPoint(param);
        userpoint.enqueue(new Callback<PointVO>() {
            @Override
            public void onResponse(Call<PointVO> call, Response<PointVO> response) {

                PointVO pointVO=response.body();

                ReceiptItem item = new Tag0x11();

                ((Tag0x11) item).setNowUsed((short) usedpoint);
                ((Tag0x11) item).setUsable((short)pointVO.getUsablepoint());
                ((Tag0x11) item).setTotal((short) pointVO.getTotalpoint());
                ((Tag0x11) item).setNowSaved((short) 100);

                DBHelper dbHelper=new DBHelper(context,"rimp.db",null,3);
                dbHelper.insertItem(receiptid, date, groupid, item);

                Log.e("usablepoint", String.valueOf(pointVO.getUsablepoint()));
                Log.e("totalpoint", String.valueOf(pointVO.getTotalpoint()));
            }

            @Override
            public void onFailure(Call<PointVO> call, Throwable t) {

            }
        });

        /*
        final String url="http://13.209.74.66/updateUserPoint.php";

        new Thread(new Runnable() {
            @Override
            public void run() {
                InputStream inputStream = null;
                BufferedReader rd=null;
                String result=null;
                try {
                    // 연결 HttpClient 객체 생성
                    HttpClient client = new DefaultHttpClient();

                    // 객체 연결 설정 부분, 연결 최대시간 등등
                    HttpParams params = client.getParams();
                    HttpConnectionParams.setConnectionTimeout(params, 5000);
                    HttpConnectionParams.setSoTimeout(params, 5000);

                    // Post객체 생성
                    HttpPost httpPost = new HttpPost(url);

                    post.add(new BasicNameValuePair("id", id));
                    post.add(new BasicNameValuePair("date", date));
                    post.add(new BasicNameValuePair("usedpoint", String.valueOf(usedpoint)));

                    UrlEncodedFormEntity entity = new UrlEncodedFormEntity(post, "UTF-8");
                    httpPost.setEntity(entity);
                    HttpResponse httpResponse = client.execute(httpPost);

                    System.out.println("post : " + post);

                    // 9. 서버로 부터 응답 메세지를 받는다.
                    inputStream = httpResponse.getEntity().getContent();
                    rd = new BufferedReader(new InputStreamReader(inputStream,"utf-8"));
                    result=rd.readLine();

                    try {
                        JSONArray jsonArray = new JSONArray(result);
                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        ReceiptItem item = new Tag0x11();

                        ((Tag0x11) item).setNowUsed((short) usedpoint);
                        ((Tag0x11) item).setUsable((short) jsonObject.getInt("usablepoint"));
                        ((Tag0x11) item).setTotal((short) jsonObject.getInt("totalpoint"));
                        ((Tag0x11) item).setNowSaved((short) 100);

                        DBHelper dbHelper=new DBHelper(context,"rimp.db",null,3);
                        dbHelper.insertItem(receiptid, date, groupid, item);

                        Log.e("usablepoint", String.valueOf(jsonObject.getInt("usablepoint")));
                        Log.e("totalpoint", String.valueOf(jsonObject.getInt("totalpoint")));
                        Log.e("updatePoint", result);
                    }catch (Exception e2){
                        e2.printStackTrace();
                    }

                }catch (ClientProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        */

    }

    public void getDataList(Context context,String id,String lastdate,final int datanum){

        setRetrofitInit(context);

        HashMap<String,Object> param=new HashMap<>();
        param.put("id",id);
        param.put("lastdate",lastdate);
        param.put("datanum",datanum);

        list=mRetrofitAPI.getPointHistory(param);
        list.enqueue(new Callback<PointListVO>() {
            @Override
            public void onResponse(Call<PointListVO> call, Response<PointListVO> response) {

                PointListVO list=response.body();
                Log.e("getDataList",String.valueOf(list.size()));

                dataCallback.callbackPointHistory(list);
            }

            @Override
            public void onFailure(Call<PointListVO> call, Throwable t) {

            }
        });
    }

}
