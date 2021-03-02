package com.bukcoja.seung.receipt_inmyhand.View;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.WindowManager;

import com.bukcoja.seung.receipt_inmyhand.ListAdapter.PointSavingItemRecyclerViewAdapter;
import com.bukcoja.seung.receipt_inmyhand.Presenter.PointPresenter;
import com.bukcoja.seung.receipt_inmyhand.Presenter.PointTaskContact;
import com.bukcoja.seung.receipt_inmyhand.R;
import com.bukcoja.seung.receipt_inmyhand.UserInfo;
import com.bukcoja.seung.receipt_inmyhand.VO.PointListVO;
import com.bukcoja.seung.receipt_inmyhand.VO.PointVO;
import com.bukcoja.seung.receipt_inmyhand.dummy.PointSavingContent;

import java.text.SimpleDateFormat;
import java.util.Date;

public class PointListActivity extends AppCompatActivity implements PointTaskContact.View {

    private RecyclerView recyclerView;
    private PointPresenter presenter;
    private UserInfo userInfo;
    private int datanum;
    private int itemSize=10;
    private ProgressDialog pd;
    private PointSavingItemRecyclerViewAdapter adapter;
    private int mColumnCount = 1;
    private int radioBtnState=0;
    private String lastDate;
    private boolean flag=false;  // 스크롤 맨 밑 감지 이벤트가 두 번 연속 일어나는 것을 방지하기 위해
    private PointSavingContent pointSavingContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_point_list);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Intent intent=getIntent();
        radioBtnState=intent.getIntExtra("radioBtnState",0);
        Log.e("radioBtnState",String.valueOf(radioBtnState));

        presenter=new PointPresenter(PointListActivity.this);
        userInfo=(UserInfo)getApplication();
        initView();

        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        lastDate=format.format(new Date());
        pointSavingContent.ITEMS.clear(); // 목록 리스트 초기화하기
        if(radioBtnState==0)
            itemSize=10;
        else
            itemSize=5;

        presenter.getPointHistory(getApplicationContext(),userInfo.getId(),lastDate,itemSize);
        recyclerView.scrollToPosition(0);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //Log.e("called","onScrolled");

                // recyclerview의 스크롤을 끝까지 내렸을 때
                if(recyclerView.canScrollVertically(1) && !flag){
                    flag=true;
                    //Log.e("called","canScrollVertically");

                    // 전에 받아온 데이터의 개수가 요구한 데이터의 개수와 같으면 새로운 데이터를 다시 로드
                    if(datanum==itemSize) {
                        Log.e("itemSize",String.valueOf(itemSize));

                        pd.setMessage("불러오는 중...");
                        pd.show();

                        Handler delayHandler = new Handler();
                        delayHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                pd.dismiss();
                            }
                        }, 2000);

                        presenter.getPointHistory(getApplicationContext(),userInfo.getId(),lastDate,itemSize);
                    }
                    else {
                        Log.e("PointListAcitivity", "all data is loaded!");
                    }
                }
            }
        });
    }

    public void initView(){
        pointSavingContent=new PointSavingContent();
        recyclerView = findViewById(R.id.recyclerview);
        if (mColumnCount <= 1) {
            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        }
        else {
            recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), mColumnCount));
        }
        pointSavingContent.ITEMS.clear(); // 목록 리스트 초기화하기
        //mListener = (ItemFragment.OnListFragmentInteractionListener)getApplication();
        adapter=new PointSavingItemRecyclerViewAdapter(pointSavingContent.ITEMS);
        recyclerView.setAdapter(adapter);
        pd=new ProgressDialog(PointListActivity.this);
    }

    @Override
    public void setPresenter(PointTaskContact.Presenter presenter) {
        this.presenter=(PointPresenter)presenter;
    }

    @Override
    public void showCircularProgress(PointVO pointVO) {

    }

    @Override
    public void showHistoryList(PointListVO data) {

        for(int i=0;i<data.size();i++){

            PointListVO.PointHistoryVO historyVO=data.get(i);
            String date=historyVO.getDate();

            // 가장 마지막에 불러온 데이터의 날짜를 담기 위해서
            if(i==data.size()-1)
                lastDate=date;

            int usedpoint=historyVO.getUsedpoint();
            int savedpoint=historyVO.getSavedpoint();
            int totalpoint=historyVO.getTotalpoint();

            switch (radioBtnState){
                case 0:  // show both savedpoint and usedpoint in list
                    pointSavingContent.addItem(PointSavingContent.createDummyItem(date,0,savedpoint,totalpoint));
                    pointSavingContent.addItem(PointSavingContent.createDummyItem(date,1,usedpoint,totalpoint));
                    break;
                case 1:  // show usedpoint only in list
                    pointSavingContent.addItem(PointSavingContent.createDummyItem(date, 0, savedpoint,totalpoint));
                    break;
                case 2:  // show savedpoint only in list
                    pointSavingContent.addItem(PointSavingContent.createDummyItem(date, 1, usedpoint,totalpoint));
                    break;
            }
        }

    }
}
