package com.bukcoja.seung.receipt_inmyhand.Presenter;

import android.content.Context;

import com.bukcoja.seung.receipt_inmyhand.Callback.ChartDataCallback;
import com.bukcoja.seung.receipt_inmyhand.Data.ChartRepository;
import com.bukcoja.seung.receipt_inmyhand.VO.ChartDetailVO;
import com.bukcoja.seung.receipt_inmyhand.VO.ChartListVO;
import com.bukcoja.seung.receipt_inmyhand.VO.UpperLimitVO;

public class ChartPresenter implements ChartTaskContact.Presenter, ChartDataCallback {

    private ChartTaskContact.View view;
    private ChartRepository chartRepository;
    private Context context;

    public ChartPresenter(Context context,ChartTaskContact.View view){
        this.context=context;
        this.view=view;
        view.setPresenter(this);
    }
    @Override
    public void start() {

    }

    public void getChartData(Context context,String userid,int groupid,String startdate,String enddate){
        chartRepository=new ChartRepository(this);
        chartRepository.getData(context,userid,groupid,startdate,enddate);
    }

    public void getChartDetailData(Context context,String userid,int groupid,String date1,String date2,int ctgid){
        chartRepository=new ChartRepository(this);
        chartRepository.getDetailData(context,userid,groupid,date1,date2,ctgid);
    }

    public void getUpperLimit(Context context,String userid,int ctgid){
        chartRepository=new ChartRepository(this);
        chartRepository.getUpperLimit(context,userid,ctgid);
    }

    public void saveUpperLimit(Context context,String userid,int ctgid,int value){
        chartRepository=new ChartRepository(this);
        chartRepository.saveUpperLimit(context,userid,ctgid,value);
    }

    @Override
    public void callbackPieChartData(ChartListVO chartListVO) {

        view.setPieChartData(chartListVO);
    }

    @Override
    public void callbackListData(ChartListVO chartListVO) {
        view.setList(chartListVO);
    }

    @Override
    public void callbackDetailData(ChartDetailVO chartDetailVO) {
        view.setDetailView(chartDetailVO);
    }

    @Override
    public void callbackUpperLimitData(UpperLimitVO upperLimitVO) {
        view.setUpperLimit(upperLimitVO);
    }

    @Override
    public void callbackSaveLimitResult(int result) {
        view.getSaveLimitResult(result);
    }
}
