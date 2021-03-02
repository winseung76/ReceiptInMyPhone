package com.bukcoja.seung.receipt_inmyhand.Presenter;

import android.content.Context;

import com.bukcoja.seung.receipt_inmyhand.Callback.RcmdCallback;
import com.bukcoja.seung.receipt_inmyhand.Data.RcmdServiceRepository;
import com.bukcoja.seung.receipt_inmyhand.VO.DateVO;
import com.bukcoja.seung.receipt_inmyhand.VO.ServiceVO;

public class RcmdPresenter implements RcmdCallback {

    private RcmdTaskContact.View view;
    private RcmdServiceRepository repository;

    public RcmdPresenter(RcmdTaskContact.View view){
        this.view=view;
    }

    public void getRcmdData(Context context,int time){
        repository=new RcmdServiceRepository(this);
        repository.getData(context,time);
    }

    public void getUpdateDate(Context context){
        repository=new RcmdServiceRepository(this);
        repository.getDateData(context);
    }

    @Override
    public void callbackRcmdData(ServiceVO serviceVO) {
        view.setRcmdData(serviceVO);
    }

    @Override
    public void callbackUpdateDate(DateVO dateVO) {
        view.setUpdateDate(dateVO);
    }
}
