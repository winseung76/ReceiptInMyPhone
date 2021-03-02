package com.bukcoja.seung.receipt_inmyhand.View;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;

import com.bukcoja.seung.receipt_inmyhand.ListAdapter.PointSavingItemRecyclerViewAdapter;
import com.bukcoja.seung.receipt_inmyhand.Presenter.PointPresenter;
import com.bukcoja.seung.receipt_inmyhand.Presenter.PointTaskContact;
import com.bukcoja.seung.receipt_inmyhand.R;
import com.bukcoja.seung.receipt_inmyhand.UserInfo;
import com.bukcoja.seung.receipt_inmyhand.VO.PointListVO;
import com.bukcoja.seung.receipt_inmyhand.VO.PointVO;
import com.bukcoja.seung.receipt_inmyhand.dummy.PointSavingContent;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import java.text.SimpleDateFormat;
import java.util.Calendar;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MyPointFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MyPointFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyPointFragment extends Fragment implements RadioButton.OnCheckedChangeListener, PointTaskContact.View{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private PointSavingItemRecyclerViewAdapter adapter;
    private RecyclerView recyclerView;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View view;
    private TextView name,usable,total,more;
    private UserInfo userInfo;
    private RadioButton radioall,radiouse,radiosave;
    private int mColumnCount = 1;
    private ProgressDialog pd;
    private CircularProgressBar circularProgressBar;
    private String lastDate;
    private int datanum;
    private int itemSize=10;
    private int state;
    private PointPresenter presenter;
    private PointSavingContent pointSavingContent;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyPointFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyPointFragment newInstance(String param1, String param2){

        MyPointFragment fragment = new MyPointFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        userInfo=(UserInfo)getActivity().getApplication();
        presenter=new PointPresenter(this);

    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        switch (compoundButton.getId()){
            case R.id.radioall:
                state=0;
                break;
            case R.id.radiosave:
                state=1;
                break;
            case R.id.radiouse:
                state=2;
                break;
        }
        if(b) {
            Calendar calendar = Calendar.getInstance();
            lastDate=format.format(calendar.getTime());
            //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            pointSavingContent.ITEMS.clear(); // 목록 리스트 초기화하기
            Log.e("called","onCheckedChanged()");

            //itemSize=compoundButton.getId()==R.id.radioall?5:10;
            itemSize=compoundButton.getId()==R.id.radioall?3:6;
            Log.e("date",lastDate);
            presenter.getPointHistory(getContext(),userInfo.getId(),lastDate,itemSize);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_my_point, container, false);
        initView();
        pd=new ProgressDialog(getContext());
        presenter.getUserPoint(getContext(),userInfo.getId());

        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(), PointListActivity.class);
                intent.putExtra("radioBtnState",state);
                startActivity(intent);
            }
        });

        /*
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                Log.e("called","onScrolled");
                // recyclerview의 스크롤을 끝까지 내렸을 때
                if(recyclerView.canScrollVertically(1)){
                    Log.e("called","canScrollVertically");

                    // 전에 받아온 데이터의 개수가 10이면 새로운 데이터를 다시 로드
                    if(datanum==itemSize) {
                        Log.e("datanum",String.valueOf(datanum));
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

                        presenter.getPointHistory(userInfo.getId(),lastDate,itemSize);
                    }
                }
            }
        });
        */


        return view;
    }
    public void initView(){
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        recyclerView.setFocusable(false);
        circularProgressBar = (CircularProgressBar)view.findViewById(R.id.circularBar);
        //circularProgressBar.setProgressWithAnimation(30, 2500); // Default duration = 1500ms

        more=view.findViewById(R.id.more);  // 더보기
        name=view.findViewById(R.id.name);
        name.setText(userInfo.getName());
        usable=view.findViewById(R.id.usable);
        total=view.findViewById(R.id.total);
        radioall=view.findViewById(R.id.radioall);
        radiouse=view.findViewById(R.id.radiouse);
        radiosave=view.findViewById(R.id.radiosave);

        radioall.setOnCheckedChangeListener(this);
        radiouse.setOnCheckedChangeListener(this);
        radiosave.setOnCheckedChangeListener(this);

        pointSavingContent=new PointSavingContent();
        if (mColumnCount <= 1) {
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        }
        else {
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), mColumnCount));
        }
        pointSavingContent.ITEMS.clear(); // 목록 리스트 초기화하기
        adapter=new PointSavingItemRecyclerViewAdapter(pointSavingContent.ITEMS);
        recyclerView.setAdapter(adapter);
        radioall.setChecked(true);
    }

    @Override
    public void showCircularProgress(PointVO pointVO){

        int usablepoint = pointVO.getUsablepoint();
        int totalpoint = pointVO.getTotalpoint();

        usable.setText(String.valueOf(usablepoint));
        total.setText(String.valueOf(totalpoint));

        int progress=(int)((totalpoint/((double)10000))*100);
        if(progress>=100)
            progress=100;

        Log.e("progress",String.valueOf(progress));
        circularProgressBar.setProgressWithAnimation(progress, 2500); // Default duration = 1500ms
    }

    @Override
    public void showHistoryList(PointListVO data) {

        this.datanum=data.getDatanum();

        if(data.size()==0)
            lastDate=null;

        for(int i=0;i<data.size();i++){

            PointListVO.PointHistoryVO historyVO=data.get(i);
            String date=historyVO.getDate();

            // 가장 마지막에 불러온 데이터의 날짜를 담기 위해서
            if(i==data.size()-1)
                lastDate=date;

            int usedpoint=historyVO.getUsedpoint();
            int savedpoint=historyVO.getSavedpoint();
            int totalpoint=historyVO.getTotalpoint();

            if(radioall.isChecked()){   // show both savedpoint and usedpoint in list
                pointSavingContent.addItem(PointSavingContent.createDummyItem(date,0,savedpoint,totalpoint));
                pointSavingContent.addItem(PointSavingContent.createDummyItem(date,1,usedpoint,totalpoint));

            }
            else if(radiouse.isChecked()) {  // show usedpoint only in list
                pointSavingContent.addItem(PointSavingContent.createDummyItem(date, 1, usedpoint,totalpoint));
            }
            else if(radiosave.isChecked()) {  // show savedpoint only in list
                pointSavingContent.addItem(PointSavingContent.createDummyItem(date, 0, savedpoint,totalpoint));
            }

            adapter.notifyDataSetChanged();
            recyclerView.scrollToPosition(adapter.getItemCount()-1);
            Log.e("item size",String.valueOf(adapter.getItemCount()));
        }

    }

    @Override
    public void setPresenter(PointTaskContact.Presenter presenter) {
        this.presenter=(PointPresenter)presenter;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

}
