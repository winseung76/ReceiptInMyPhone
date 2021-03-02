package com.bukcoja.seung.receipt_inmyhand.Presenter;

import android.content.Context;

import com.bukcoja.seung.receipt_inmyhand.Data.UserPointRepository;
import com.bukcoja.seung.receipt_inmyhand.Callback.DataCallback;
import com.bukcoja.seung.receipt_inmyhand.VO.PointListVO;
import com.bukcoja.seung.receipt_inmyhand.VO.PointVO;

public class PointPresenter implements PointTaskContact.Presenter, DataCallback {

    private PointTaskContact.View view; // view
    private UserPointRepository upr;

    public PointPresenter(PointTaskContact.View view){
        this.view=view;
        view.setPresenter(this);
    }

    @Override
    public void start(){

    }

    public void getPointHistory(Context context,String id,String date,int datanum){

        upr=new UserPointRepository(this);
        upr.getDataList(context,id,date,datanum);
    }
    public void getUserPoint(Context context, String id){
        upr=new UserPointRepository(this);
        upr.getUserPoint(context,id);
    }

    @Override
    public void callbackUserPoint(PointVO pointVO){
        view.showCircularProgress(pointVO);
    }

    @Override
    public void callbackPointHistory(PointListVO data){
        view.showHistoryList(data);
    }

}
