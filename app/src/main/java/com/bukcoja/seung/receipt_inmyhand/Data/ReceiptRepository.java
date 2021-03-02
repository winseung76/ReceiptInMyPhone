package com.bukcoja.seung.receipt_inmyhand.Data;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.widget.Toast;

import com.bukcoja.seung.receipt_inmyhand.Callback.GroupCallback;
import com.bukcoja.seung.receipt_inmyhand.DBHelper;
import com.bukcoja.seung.receipt_inmyhand.DigitalReceipt.ReceiptPayloadItem.BasicInfo;
import com.bukcoja.seung.receipt_inmyhand.DigitalReceipt.ReceiptPayloadItem.Product;
import com.bukcoja.seung.receipt_inmyhand.DigitalReceipt.ReceiptPayloadItem.Tag0x21;
import com.bukcoja.seung.receipt_inmyhand.Presenter.ReceiptPresenter;
import com.bukcoja.seung.receipt_inmyhand.R;
import com.bukcoja.seung.receipt_inmyhand.RetrofitAPI;
import com.bukcoja.seung.receipt_inmyhand.VO.ReceiptVO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ReceiptRepository {

    private Retrofit mRetrofit;
    private RetrofitAPI mRetrofitAPI;
    private Call<ReceiptVO> receiptInfo;
    private Call<String> callString;
    private Gson mGson;
    private final String TAG="ReceiptRepository";
    private ReceiptPresenter presenter;
    private GroupCallback callback;

    public ReceiptRepository(GroupCallback groupCallback){
        this.callback=groupCallback;
    }
    public ReceiptRepository(ReceiptPresenter presenter){

        this.presenter=presenter;
    }

    public void getData(Context context,String userid,String startdate,String enddate,int groupid){

        getDataFromCenter(context,userid,startdate,enddate,groupid);
        //getDataFromLocal(context,userid,startdate,enddate,groupid);

    }

    private void getDataFromLocal(Context context,String userid,String startdate,String enddate,int groupid){
        //ReceiptContent.ITEMS=new ArrayList<>();
        // 날짜+시각 에서 '날짜'만 비교할 것이므로 WHERE절에서 date(date)를 사용
        ReceiptVO receiptVO=new ReceiptVO();

        Log.d("record groupid",String.valueOf(groupid));
        DBHelper dbHelper=new DBHelper(context, "rimp.db", null, 3);

        Cursor cursor=dbHelper.getResult("SELECT * FROM receipt_info JOIN membership ON receipt_info._id = membership._id " +
                "WHERE receipt_info.groupid="+groupid+" "+
                "AND strftime('%Y-%m-%d',receipt_info.date) BETWEEN '"+startdate+"' AND '"+enddate+"';");

        Log.e("receipt from LOCAL",String.valueOf(cursor.getCount()));

        //System.out.println("DB 레코드 수 : "+cursor.getCount());
        while(cursor.moveToNext()){
            ReceiptVO.Receipt receipt=receiptVO.createReceipt();

            int receiptid=cursor.getInt(cursor.getColumnIndex("_id"));

            receipt.setReceiptid(receiptid);
            receipt.setTotalprice(cursor.getInt(cursor.getColumnIndex("price")));
            receipt.setDate(cursor.getString(cursor.getColumnIndex("date")));
            receipt.setUsablepoint(cursor.getInt(cursor.getColumnIndex("usable")));
            receipt.setSavedpoint(cursor.getInt(cursor.getColumnIndex("nowsaved")));
            receipt.setUsedpoint(cursor.getInt(cursor.getColumnIndex("nowused")));

            Cursor cursor2=dbHelper.getResult("SELECT * FROM product WHERE _id="+receiptid+";");

            while(cursor2.moveToNext()){
                ReceiptVO.Product product=receipt.createProduct();

                product.setProductname(cursor2.getString(cursor2.getColumnIndex("pname")));
                product.setUnitprice(cursor2.getInt(cursor2.getColumnIndex("unitprice")));
                product.setCount(cursor2.getInt(cursor2.getColumnIndex("num")));
                receipt.add(product);
            }

            receiptVO.add(receipt);
        }
        presenter.callbackReceiptData(receiptVO);
        dbHelper.close();
    }

    private void getDataFromCenter(Context context,String userid,String startdate,String enddate,int groupid){

        setRetrofitInit(context);

        Log.e(TAG, "callUserInfo");

        HashMap<String,Object> param=new HashMap<>();
        param.put("userid",userid);
        param.put("groupid",groupid);
        param.put("startdate",startdate);
        param.put("enddate",enddate);

        receiptInfo = mRetrofitAPI.getReceipt(param);
        receiptInfo.enqueue(new Callback<ReceiptVO>() {
            @Override
            public void onResponse(Call<ReceiptVO> call, Response<ReceiptVO> response) {
                ReceiptVO receiptVO=response.body();
                Log.e(TAG,"receiptVO count : "+receiptVO.size());
                for(int i=0;i<receiptVO.size();i++){
                    ReceiptVO.Receipt receipt=receiptVO.get(i);
                    //Log.e(TAG,String.valueOf(receipt.getReceiptid()));
                }

                presenter.callbackReceiptData(receiptVO);
            }

            @Override
            public void onFailure(Call<ReceiptVO> call, Throwable t) {
                Log.e(TAG, "onFailure");
                t.printStackTrace();
            }
        });

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


    public void getDataCount(Context context){

        DBHelper dbHelper=new DBHelper(context, "rimp.db", null, 3);
        Cursor cursor=dbHelper.getResult("SELECT * FROM receipt_info JOIN membership ON receipt_info._id = membership._id ");

        //Log.e("receipt count in local",String.valueOf(cursor.getCount()));

        presenter.callbackReceiptCount(cursor.getCount());
        dbHelper.close();
    }

    // 로컬에 있는 영수증 정보를 중앙으로 보내는 메서드
    // 인자로 받은 reptid가 0인 경우, -> 로컬의 모든 영수증 정보를 중앙으로 이동
    // 인자로 받은 reptid가 0보다 큰 경우 -> 해당 전자 영수증'만' 중앙으로 이동
    public void moveDataFromLocalToCenter(final Context context,final int reptid, final String userid,final int groupid){
        Log.e(TAG,"moveDataFromLocalToCenter is called");
        //Toast.makeText(context,"moveDataFromLocalToCenter "+groupid,Toast.LENGTH_SHORT).show();
        Cursor cursor;
        DBHelper dbHelper=new DBHelper(context, "rimp.db", null, 3);

        // 로컬의 전자 영수증 전체를 이동하느냐 or 선택적으로 이동하느냐

        // 전자 영수증 번호가 0이면 로컬의 전체 전자 영수증 정보 이동
        if(reptid==0) {
            cursor = dbHelper.getResult("SELECT * FROM receipt_info JOIN membership ON receipt_info._id = membership._id ");
        }
        // 전자 영수증 번호가 0이 아니면 로컬의 해당 전자 영수증 번호에 대해서만 이동
        else
            cursor=dbHelper.getResult("SELECT * FROM receipt_info JOIN membership ON receipt_info._id = membership._id WHERE receipt_info._id="+reptid);

        setRetrofitInit(context);

        HashMap<String,Object> param=new HashMap<>();
        while(cursor.moveToNext()){

            int receiptid=cursor.getInt(cursor.getColumnIndex("_id"));
            param.put("receiptid",receiptid);
            param.put("userid",userid);
            param.put("groupid",groupid);
            param.put("regnum","0123456789");
            param.put("totalprice",cursor.getInt(cursor.getColumnIndex("price")));
            param.put("date",cursor.getString(cursor.getColumnIndex("date")));

            Cursor cursor2=dbHelper.getResult("SELECT * FROM product WHERE _id="+receiptid+";");

            ArrayList<String> prodnames=new ArrayList<>();
            ArrayList<Integer> unitprices=new ArrayList<>();
            ArrayList<Integer> counts=new ArrayList<>();
            ArrayList<Integer> ctgs=new ArrayList<>();
            ArrayList<Integer> codes=new ArrayList<>();

            while(cursor2.moveToNext()){
                prodnames.add(cursor2.getString(cursor2.getColumnIndex("pname")));
                unitprices.add(cursor2.getInt(cursor2.getColumnIndex("unitprice")));
                counts.add(cursor2.getInt(cursor2.getColumnIndex("num")));
                ctgs.add(cursor2.getInt(cursor2.getColumnIndex("ctg")));

            }

            mRetrofitAPI.saveReceipt(param,codes,counts).enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    String result=response.body();
                    Log.e(TAG,"saveReceipt: "+result);
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Log.e(TAG,"saveReceipt: fail");
                    t.printStackTrace();
                }
            });
            dbHelper.delete(receiptid);

        }
        dbHelper.close();
    }

    public void deleteData(final Context context, final List<Integer> receiptids, final String userid, int groupid){
        // 전자 영수증이 중앙에 있는지, 로컬에 있는지를 먼저 검사
        DBHelper dbHelper = new DBHelper(context, "rimp.db", null, 3);
        for(int i=0;i<receiptids.size();i++) {

            Cursor cursor = dbHelper.getResult("SELECT COUNT(*) FROM receipt_info WHERE _id=" + receiptids.get(i));

            // 전자 영수증이 로컬에 있는 경우
            if (cursor.getCount() == 1) {
                // 전자 영수증을 중앙으로 이동 후 로컬의 전자 영수증 삭제
                moveDataFromLocalToCenter(context, receiptids.get(i), userid, groupid);

            }
        }

        // 전자 영수증이 중앙에 있는 경우
        // 진짜 데이터를 삭제하는 것이 아닌 중앙의 전자 영수증 정보를 어플리케이션에서 비활성화
        // delete flag를 변경한다.
        setRetrofitInit(context);
        //callDeleteResult(receiptid);
        //Log.e(TAG, "callUserInfo");

        HashMap<String,Object> param=new HashMap<>();
        //param.put("receiptid",receiptid);
        param.put("groupid",groupid);
        param.put("userid",userid);

        //Log.e(TAG, "receiptid:"+receiptid+", groupid:"+groupid+", userid:"+userid);

        mRetrofitAPI.deleteReceipt(param,receiptids).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String result=response.body();
                Log.e(TAG,"deleteReceiptResult: "+result);
                if(result.contains("1")){
                    Toast.makeText(context,"삭제 완료",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e(TAG, "onFailure");
                t.printStackTrace();
            }
        });
        dbHelper.close();
    }

    // '나의 영수증'에서 새롭게 영수증을 발급받았을 때 필요한 메서드
    public void saveDataToLocal(Context context,final int receiptid, final String userid, final int groupid,final BasicInfo item, final Tag0x21 tag0x21){
        DBHelper dbHelper=new DBHelper(context, "rimp.db", null, 3);

        boolean flag1=dbHelper.insertItem(receiptid,item.getDate(),groupid,item);
        boolean flag2=dbHelper.insertItem(receiptid,item.getDate(),groupid,tag0x21);

        // flag1, flag2 둘 다 테이블에 insert가 성공해야 true가 보내짐
        callback.callbackSaveResult(flag1&flag2);
        dbHelper.close();
    }

    // 한 그룹에서 다른 그룹으로 영수증을 공유할 때 필요한 메서드
    // 특정 영수증만을 다른 그룹에 복사
    public void shareDataToCenter(Context context, String userid, int groupid, int receiptid){
        DBHelper dbHelper=new DBHelper(context, "rimp.db", null, 3);

        Cursor cursor=dbHelper.getResult("SELECT * FROM receipt_info WHERE _id="+receiptid+";");
        // 공유하려는 영수증이 로컬에 없는 경우
        // == 중앙에 영수증에 있는 경우
        Log.e("saveDataToCenter","count:"+String.valueOf(cursor.getCount()));

        if(cursor.getCount()==0){
            // 중앙에서 해당 영수증을 찾아서 groupid만 변경하여
            // 영수증을 복사한다.
            setRetrofitInit(context);

            HashMap<String,Object> param=new HashMap<>();
            param.put("receiptid",receiptid);
            param.put("groupid",groupid);

            //callString=mRetrofitAPI.shareReceipt(param);
            mRetrofitAPI.shareReceipt(param).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    try {
                        String result = response.body().string();
                        Log.e("saveDataToCenter", "result : " + result);
                        int res;
                        if (result.contains("-1"))
                            res = -1;
                        else if (result.contains("1"))
                            res = 1;
                        else
                            res = 0;
                        presenter.callbackShareResult(res);
                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        }
        // 공유하려는 영수증이 로컬에 있는 경우
        else if(cursor.getCount()==1){
            // 해당 영수증을 로컬에서 중앙으로 보냄
            moveDataFromLocalToCenter(context,receiptid,userid,groupid);
        }
        dbHelper.close();
    }

    public void saveDataToCenter(Context context,int receiptid,String userid,int groupid,BasicInfo item,Tag0x21 tag0x21){
        setRetrofitInit(context);

        HashMap<String,Object> param=new HashMap<>();


        param.put("receiptid",receiptid);
        param.put("userid",userid);
        param.put("groupid",groupid);
        param.put("regnum","0123456789");
        param.put("totalprice",item.getPrice());
        param.put("date",item.getDate());


        ArrayList<String> prodnames=new ArrayList<>();
        ArrayList<Integer> unitprices=new ArrayList<>();
        ArrayList<Integer> counts=new ArrayList<>();
        ArrayList<Integer> ctgs=new ArrayList<>();
        // code 정보 추가
        ArrayList<Integer> codes=new ArrayList<>();

        for(int i=0;i<tag0x21.getProdNum();i++){
            Product p=tag0x21.getProduct(i);
            //prodnames.add(p.getName());
            //unitprices.add((int)p.getUnitPrice());
            counts.add((int)p.getNum());
            //ctgs.add((int)p.getInfo());
            codes.add(p.getCode());
        }


        mRetrofitAPI.saveReceipt(param,codes,counts).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String result=response.body();
                Log.e(TAG,"saveReceipt: "+result);
                if(result.contains("1")){
                    callback.callbackSaveResult(true);
                }
                else
                    callback.callbackSaveResult(false);
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e(TAG,"saveReceipt: fail");
                callback.callbackSaveResult(false);
                t.printStackTrace();
            }
        });
    }

}
