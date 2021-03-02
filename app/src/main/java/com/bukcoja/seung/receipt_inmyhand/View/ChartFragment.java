package com.bukcoja.seung.receipt_inmyhand.View;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.bukcoja.seung.receipt_inmyhand.ListAdapter.CtgItemRecyclerViewAdapter;
import com.bukcoja.seung.receipt_inmyhand.Presenter.ChartPresenter;
import com.bukcoja.seung.receipt_inmyhand.Presenter.ChartTaskContact;
import com.bukcoja.seung.receipt_inmyhand.R;
import com.bukcoja.seung.receipt_inmyhand.UserInfo;
import com.bukcoja.seung.receipt_inmyhand.VO.ChartDetailVO;
import com.bukcoja.seung.receipt_inmyhand.VO.ChartListVO;
import com.bukcoja.seung.receipt_inmyhand.VO.ChartVO;
import com.bukcoja.seung.receipt_inmyhand.VO.UpperLimitVO;
import com.bukcoja.seung.receipt_inmyhand.dummy.CategoryContent;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ChartFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ChartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChartFragment extends Fragment implements ChartTaskContact.View{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private  View view;
    private TextView total;
    private CtgItemRecyclerViewAdapter adapter;
    private RecyclerView recyclerView;
    private int mColumnCount = 1;
    private ChartPresenter presenter;
    private PieChart pieChart;
    private UserInfo userinfo;
    private TextView emptyMsg;
    private ArrayList<Entry> entries=new ArrayList<>();
    private ArrayList<String> labels=new ArrayList<>();
    public MonthPicker monthPicker;
    private NestedScrollView scrollView;
    /*private int[] colors={Color.parseColor("#E81300"),Color.parseColor("#E83E2E"),
            Color.parseColor("#E8685D"),Color.parseColor("#E8938B"),Color.parseColor("#E8BEBA")};
            */
    /*
    private int[] colors={Color.parseColor("#E81300"),Color.parseColor("#E84C3D"),
            Color.parseColor("#E8685D"),Color.parseColor("#E8938B"),Color.parseColor("#E8BEBA")};

     */
    private int[] colors={Color.parseColor("#dc5356"),Color.parseColor("#f0cb69"),
            Color.parseColor("#8ec3a7"),Color.parseColor("#5fb7e5"),Color.parseColor("#ab91c5")};

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ItemFragment.OnListFragmentInteractionListener mListener;

    public ChartFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChartFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChartFragment newInstance(String param1, String param2) {

        ChartFragment fragment = new ChartFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    // 프래그먼트가 생성될 떄 호출
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("ChartFragment","onCreate");
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        presenter=new ChartPresenter(getContext(),this);
        userinfo=(UserInfo)getActivity().getApplication();
    }
    // onCreate()후에 화면을 구성할 떄 호출됨
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_chart, container, false);

        monthPicker=new MonthPicker(view);
        initView();
        Log.e("ChartFragment","onCreateView");
        //presenter.getChartData(getContext(),userinfo.getId(),userinfo.getGroup(),monthPicker.getStartDate(),monthPicker.getEndDate());


        return view;
    }

    @Override
    public void setPresenter(ChartTaskContact.Presenter presenter) {
        this.presenter=(ChartPresenter)presenter;
    }

    @Override
    public void setPieChartData(ChartListVO chartListVO) {

        Log.e("setPieChartData",String.valueOf(chartListVO.size()));
        pieChart.clear();
        entries.clear();

        if(chartListVO.getTotalprice()==0){
            emptyMsg.setVisibility(View.VISIBLE);
            pieChart.setVisibility(View.GONE);
        }
        else if(chartListVO.getTotalprice()>0){
            emptyMsg.setVisibility(View.GONE);
            pieChart.setVisibility(View.VISIBLE);
        }

        for(int i = 0; i< chartListVO.size(); i++){
            ChartVO prod= chartListVO.get(i);

            entries.add(new Entry(prod.getPrice(), i));
            //Log.e("entries", String.valueOf(entries.get(i)));

            labels.add(prod.getCtgname());
            //Log.e("labels",String.valueOf(labels.get(i)));
        }
        //this.entries=entries;
        //this.labels=labels;
        setPieChart();
    }

    @Override
    public void setList(ChartListVO chartListVO) {

        int totalprice= chartListVO.getTotalprice();
        CategoryContent.ITEMS.clear();

        Log.e("setList",String.valueOf(chartListVO.size()));

        for(int i = 0; i< chartListVO.size(); i++){
            ChartVO prod= chartListVO.get(i);

            CategoryContent.addItem(CategoryContent.createDummyItem(colors[i], prod.getCtgname(), prod.getPrice(),totalprice,prod.getCtgid()));
            //totalprice+=prod.getPrice();
        }


        DecimalFormat formatter=new DecimalFormat("###,###");
        total.setText(formatter.format(totalprice)+" 원");
        Log.e("total",total.getText().toString());

        adapter.notifyDataSetChanged();
    }

    @Override
    public void setUpperLimit(UpperLimitVO upperLimitVO) {

    }

    @Override
    public void setDetailView(ChartDetailVO chartDetailVO) {

    }

    public void initView(){
        Log.e("ChartFragment","initView");

        entries=new ArrayList<>();
        labels=new ArrayList<>();

        pieChart=view.findViewById(R.id.piechart);
        total=view.findViewById(R.id.total);
        emptyMsg=view.findViewById(R.id.empty_msg);
        scrollView=view.findViewById(R.id.scrollView);
        //scrollView.scrollTo(0,0);

        recyclerView = (RecyclerView) view.findViewById(R.id.ctg_recyclerview);
        recyclerView.setFocusable(false);
        if (mColumnCount <= 1) {
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        }
        else {
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), mColumnCount));
        }

        adapter=new CtgItemRecyclerViewAdapter(CategoryContent.ITEMS,getContext(),monthPicker);
        CategoryContent.ITEMS.clear();
        recyclerView.setAdapter(adapter);
    }

    public void setPieChart(){
        //String label = machineinfo.getSensor(selected_sensors.get(0));
        //description.setText(label);

        pieChart.setDescription("");
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.TRANSPARENT);
        pieChart.setHoleRadius(35);               //안쪽 구멍 반지름
        pieChart.setTransparentCircleRadius(40);  //투명 반지름
        pieChart.setExtraOffsets(0.f, 5.f, 0.f, 5.f);
        pieChart.setDrawSliceText(false);
        pieChart.setUsePercentValues(false);
        pieChart.setRotationAngle(0);
        pieChart.setRotationEnabled(false);
        pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
                for(int i=0;i<recyclerView.getChildCount();i++){
                    if(i==e.getXIndex()){
                        recyclerView.getChildAt(i).findViewById(R.id.item_layout).setBackgroundColor(Color.argb(50,255,0,0));
                    }
                    else {
                        recyclerView.getChildAt(i).findViewById(R.id.item_layout).setBackgroundColor(Color.argb(255, 255, 255, 255));
                    }
                }

            }

            @Override
            public void onNothingSelected() {
                for(int i=0;i<recyclerView.getChildCount();i++){
                    recyclerView.getChildAt(i).findViewById(R.id.item_layout).setBackgroundColor(Color.argb(255, 255, 255, 255));

                }
            }
        });

        // customize legends
        Legend l = pieChart.getLegend();
        l.setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);
        //l.setXEntrySpace(7);
        l.setYEntrySpace(5);

        // create pie data set
        PieDataSet dataSet = new PieDataSet(entries,"");
        dataSet.setSliceSpace(1);
        dataSet.setSelectionShift(7);
        dataSet.setSelectionShift(10);

        //dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        dataSet.setValueLinePart1OffsetPercentage(100.f);
        dataSet.setValueLinePart1Length(0.3f);
        dataSet.setValueLinePart2Length(0.3f);
        dataSet.setColors(colors);
        dataSet.setDrawValues(false);


        // instantiate pie data object now
        PieData data = new PieData(labels, dataSet);
        //data.setValueFormatter(new PercentFormatter());
        //data.setValueTextSize(15f);
        //data.setValueTextColor(Color.BLACK);

        pieChart.setData(data);
        pieChart.setBackgroundColor(Color.TRANSPARENT);

        // undo all highlights
        pieChart.highlightValues(null);
        pieChart.animateXY(2000, 2000); //애니메이션 기능 활성화
        pieChart.invalidate();
    }
    public class MonthPicker{
        private SimpleDateFormat format = new SimpleDateFormat("MM월 dd일");
        private Spinner spinner;
        private ArrayAdapter<String> spinner_adapter;
        Calendar calendar = Calendar.getInstance();
        private TextView startDate,endDate;
        private ImageButton left_arrow,right_arrow;
        private int scope; // 선택한 범위가 월 간인지 주 간인지에 따라 월 간은 0, 주 간은 1로 변경됨
        private String nowDate=format.format(calendar.getTime()); // 현재 날짜

        MonthPicker(View view){
            Log.e("MonthPicker","MonthPicker");

            spinner=view.findViewById(R.id.spinner);
            startDate=view.findViewById(R.id.startDate);
            endDate=view.findViewById(R.id.endDate);
            spinner_adapter=new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item,
                    new String[]{"월 간","주 간"}) {
                @Override
                public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                    View view = super.getDropDownView(position, convertView, parent);
                    // 드롭다운으로 메뉴들이 나타났을 때의 텍스트 크기와 색상을 변경함
                    TextView tv = (TextView) view;
                    tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15); //dp
                    tv.setTextColor(Color.BLACK);

                    return view;
                }
            };
            spinner.setAdapter(spinner_adapter);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                    // spinner의 텍스트 크기 및 gravity 설정
                    if(adapterView.getChildAt(0)!=null) {
                        // 첫 번째 자식의 텍스트 크기와 gravity 변경
                        // 첫 번째 자식은 처음 초기화되었을 때의 선택된 자식과 일치
                        ((TextView) adapterView.getChildAt(0)).setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
                        ((TextView) adapterView.getChildAt(0)).setGravity(Gravity.CENTER);
                        scope = position;
                        setInitDate();
                    }
                }
                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                }
            });

            setInitDate();
            left_arrow=view.findViewById(R.id.left_arrow);
            right_arrow=view.findViewById(R.id.right_arrow);
            left_arrow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setMonthNDate(left_arrow);
                }
            });
            right_arrow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setMonthNDate(right_arrow);
                }
            });
            setPickerState();
        }
        // startDate와 endDate를 초기화하는 메소드
        public void setInitDate(){
            Log.e("ChartFragment","setInitDate");
            calendar = Calendar.getInstance();
            try {
                endDate.setText(format.format(calendar.getTime()));
                calendar.setTime(format.parse(endDate.getText().toString()));
                if (scope == 0) {
                    calendar.add(calendar.MONTH, -1);
                    calendar.add(calendar.DATE,+1);
                } else if (scope == 1) {
                    calendar.add(calendar.DATE, -6);
                }
                startDate.setText(format.format(calendar.getTime()));

                presenter.getChartData(getContext(),userinfo.getId(),userinfo.getGroup(),getStartDate(),getEndDate());
            }catch (ParseException e){ }
        }
        // 화살표 버튼을 클릭함에 따라 선택된 연도와 월을 세팅해주는 메소드
        public void setMonthNDate(ImageButton btn) {
            Log.e("ChartFragment","setMonthNDate");
            Date date1=null,date2=null;
            Calendar cal1=Calendar.getInstance(),cal2=Calendar.getInstance();
            try {
                date1 = format.parse(startDate.getText().toString());
                date2 = format.parse(endDate.getText().toString());
                cal1.setTime(date1);
                cal2.setTime(date2);
            }catch(ParseException e){}
            switch(btn.getId()){
                //calendar = Calendar.getInstance();
                case R.id.left_arrow:
                    if(scope==0){
                        cal1.add(Calendar.MONTH,-1);
                        //cal1.add(Calendar.DATE,-1);
                        cal2.add(Calendar.MONTH,-1);
                        //cal2.add(Calendar.DATE,-1);
                    }
                    else if(scope==1){
                        cal1.add(Calendar.DATE,-6);
                        cal1.add(Calendar.DATE,-1);
                        cal2.add(Calendar.DATE,-6);
                        cal2.add(Calendar.DATE,-1);
                    }
                    break;
                case R.id.right_arrow:
                    if(scope==0){
                        cal1.add(Calendar.MONTH,+1);
                        //cal1.add(Calendar.DATE,+1);
                        cal2.add(Calendar.MONTH,+1);
                        //cal2.add(Calendar.DATE,+1);
                    }
                    else if(scope==1){
                        cal1.add(Calendar.DATE,+6);
                        cal1.add(Calendar.DATE,+1);
                        cal2.add(Calendar.DATE,+6);
                        cal2.add(Calendar.DATE,+1);
                    }
                    break;
            }
            startDate.setText(format.format(cal1.getTime()));
            endDate.setText(format.format(cal2.getTime()));
            presenter.getChartData(getContext(),userinfo.getId(),userinfo.getGroup(),getStartDate(),getEndDate());
            setPickerState();

        }
        // picker 상태(화살표 버튼을 활성화여부, 연,월 표시) 등을 결정하는 메소드
        public void setPickerState(){
            calendar=Calendar.getInstance();
            // 현재 연,월 일때는 그 이후로 갈 수 없으므로 오른쪽 화살표를 disable 처리한다.
            if(endDate.getText().toString().equals(nowDate)){
                right_arrow.setEnabled(false);
                right_arrow.setImageDrawable(getResources().getDrawable(R.drawable.right_arrow_disable));
            }
            else {
                right_arrow.setEnabled(true);
                right_arrow.setImageDrawable(getResources().getDrawable(R.drawable.right_arrow));
            }
        }
        public String getStartDate(){
            SimpleDateFormat original_format = new SimpleDateFormat("yyyy년 MM월 dd일");
            SimpleDateFormat new_format = new SimpleDateFormat("yyyy-MM-dd");
            String newDate=null;
            try {
                Log.e("getStartDate",startDate.getText().toString());
                Date originalDate = original_format.parse(Calendar.getInstance().get(Calendar.YEAR)+"년 "+startDate.getText().toString());
                newDate=new_format.format(originalDate);

            }catch (Exception e){
                e.printStackTrace();
            }

            return newDate;
        }
        public String getEndDate(){
            SimpleDateFormat original_format = new SimpleDateFormat("yyyy년 MM월 dd일");
            SimpleDateFormat new_format = new SimpleDateFormat("yyyy-MM-dd");
            String newDate=null;
            try {
                Log.e("getEnddDate",endDate.getText().toString());
                Date originalDate = original_format.parse(Calendar.getInstance().get(Calendar.YEAR)+"년 "+endDate.getText().toString());
               newDate=new_format.format(originalDate);

            }catch (Exception e){
                e.printStackTrace();
            }
            return newDate;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (ItemFragment.OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void getSaveLimitResult(int result) {

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
