package com.bukcoja.seung.receipt_inmyhand.View;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bukcoja.seung.receipt_inmyhand.ListAdapter.ChartDetailRecyclerViewAdapter;
import com.bukcoja.seung.receipt_inmyhand.Presenter.ChartPresenter;
import com.bukcoja.seung.receipt_inmyhand.Presenter.ChartTaskContact;
import com.bukcoja.seung.receipt_inmyhand.R;
import com.bukcoja.seung.receipt_inmyhand.UserInfo;
import com.bukcoja.seung.receipt_inmyhand.VO.ChartDetailVO;
import com.bukcoja.seung.receipt_inmyhand.VO.ChartListVO;
import com.bukcoja.seung.receipt_inmyhand.VO.ProductVO;
import com.bukcoja.seung.receipt_inmyhand.VO.UpperLimitVO;
import com.bukcoja.seung.receipt_inmyhand.dummy.ChartDetailContent;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.YAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ChartDetailActivity extends AppCompatActivity implements ChartTaskContact.View {

    private BarChart barChart;
    private RecyclerView recyclerView;
    private ChartDetailRecyclerViewAdapter adapter;
    private int ctgid;
    private String startdate,enddate;
    private UserInfo userInfo;
    private TextView date;
    private ChartPresenter presenter;
    private ArrayList<BarEntry> entries=new ArrayList<>();
    private ArrayList<String> labels=new ArrayList<>();
    private MonthPicker picker;
    private ChartDetailVO chartDetailVO;
    private int mColumnCount = 1;
    private TextView maxDate,minDate,maxPrice,minPrice;
    private Button upperEditBtn;
    private Toolbar toolbar;
    private int upperlimit;
    private LimitLine upper;
    SimpleDateFormat format1 = new SimpleDateFormat("yyyy/MM/dd");
    SimpleDateFormat format2 = new SimpleDateFormat("yyyy년 MM월 dd일");
    SimpleDateFormat format3 = new SimpleDateFormat("yyyy-MM-dd");
    DecimalFormat formatter=new DecimalFormat("###,###");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart_detail);

        userInfo=(UserInfo)getApplication();

        Intent intent=getIntent();
        ctgid=intent.getIntExtra("ctgid",-1);
        startdate=intent.getStringExtra("startdate");
        enddate=intent.getStringExtra("enddate");

        Log.e("ChartDetailActivity","ctgid : "+ctgid);


        initView();
        presenter=new ChartPresenter(getApplicationContext(),this);
        // 먼저 upperlimit을 불러온 후에
        presenter.getUpperLimit(getApplicationContext(),userInfo.getId(),ctgid);
        presenter.getChartDetailData(getApplicationContext(),userInfo.getId(),userInfo.getGroup(),
                picker.getStartDate(),picker.getEndDate(),ctgid);



    }
    public void initView(){

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setBackground(getResources().getDrawable(R.drawable.linear_gradient));
        toolbar.setTitle("소비 차트");
        setSupportActionBar(toolbar);

        barChart=findViewById(R.id.barchart);
        date=findViewById(R.id.date);

        maxDate=findViewById(R.id.maxDate);
        minDate=findViewById(R.id.minDate);
        maxPrice=findViewById(R.id.maxPrice);
        minPrice=findViewById(R.id.minPrice);

        upperEditBtn=findViewById(R.id.upperedit);
        upperEditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });
        upperEditBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction()==MotionEvent.ACTION_DOWN){
                    upperEditBtn.setBackground(getResources().getDrawable(R.drawable.buttonlayout5));
                }
                else if(motionEvent.getAction()==MotionEvent.ACTION_UP){
                    upperEditBtn.setBackground(getResources().getDrawable(R.drawable.buttonlayout));
                }
                return false;
            }
        });

        recyclerView=findViewById(R.id.recyclerview);
        if (mColumnCount <= 1) {
            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        }
        else {
            recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), mColumnCount));
        }

        adapter=new ChartDetailRecyclerViewAdapter(ChartDetailContent.ITEMS);
        ChartDetailContent.ITEMS.clear();
        recyclerView.setAdapter(adapter);

        picker=new MonthPicker();

    }
    public void showDialog(){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        final View dialogView=View.inflate(getApplicationContext(),R.layout.upperline_edit_dialog,null);
        final EditText editText=dialogView.findViewById(R.id.edittext);
        editText.setText(String.valueOf(upperlimit));
        TextView yes=dialogView.findViewById(R.id.yes);
        TextView no=dialogView.findViewById(R.id.no);
        ImageButton delete=dialogView.findViewById(R.id.delete);
        yes.setText("변경");
        no.setText("취소");

        builder.setView(dialogView);

        final AlertDialog dialog=builder.create();
        dialog.show();

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str=editText.getText().toString();
                presenter.saveUpperLimit(getApplicationContext(),userInfo.getId(),ctgid,Integer.parseInt(str));
                //setBarChart();
                // 변경한 upper limit을 로컬 DB에 저장 필요

                dialog.dismiss();
            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editText.setText("");
            }
        });

    }
    public void setBarChart(){
        //barChart=findViewById(R.id.barchart);

        /*상한선 지정 */
        upper= new LimitLine(upperlimit, "Upper Limit");
        upper.setLineWidth(2f);  //상한선 선 두께
        upper.setLineColor(Color.parseColor("#FF0000"));  //상한선 선
        upper.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);  //상한선 라벨 위치
        upper.setTextSize(13f);  //상한선 텍스트 크기
        upper.setTextColor(Color.parseColor("#FF0000"));

        /* chart value 클릭 시 나타나는 markerview */
        MarkerView mv = new MarkerView(getApplicationContext(), R.layout.markerview) {
            TextView tvContent = (TextView) findViewById(R.id.tvContent);

            @Override
            public void refreshContent(Entry e, Highlight highlight) {
                tvContent.setText(formatter.format((int)e.getVal())+" 원");
            }

            @Override
            public int getXOffset(float xpos) {

                // this will center the marker-view horizontally
                int min_offset = 60;
                if (xpos < min_offset)
                    return 0;

                WindowManager wm = (WindowManager)getApplication().getSystemService(Context.WINDOW_SERVICE);
                DisplayMetrics metrics = new DisplayMetrics();
                wm.getDefaultDisplay().getMetrics(metrics);
                //For right hand side
                if (metrics.widthPixels - xpos < min_offset)
                    return -getWidth();
                    //For left hand side
                else if (metrics.widthPixels - xpos < 0)
                    return -getWidth();
                return -(getWidth() / 2);
            }

            @Override
            public int getYOffset(float ypos) {

                return -getHeight();
            }
        };

        barChart.setVisibility(View.VISIBLE);

        //barChart.setMarkerView(mv);
        barChart.setGridBackgroundColor(Color.parseColor("#E83E2E"));  //chart background color
        barChart.setPinchZoom(true);
        barChart.zoomOut();
        barChart.setMarkerView(mv);
        barChart.setExtraTopOffset(30);
        barChart.setHighlightPerTapEnabled(true);
        barChart.setHighlightPerDragEnabled(false);
        barChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {

                int index=e.getXIndex();

                try {
                    String orgDate = labels.get(index);
                    SimpleDateFormat format1 = new SimpleDateFormat("yyyy/MM/dd");
                    SimpleDateFormat format2 = new SimpleDateFormat("yyyy년 MM월 dd일");

                    Date tempDate = format1.parse(Calendar.getInstance().get(Calendar.YEAR) + "/" + orgDate);
                    String newDate = format2.format(tempDate);
                    date.setText(newDate);

                    ChartDetailContent.ITEMS.clear();
                    ChartDetailVO.ProdPerDate prodPerDate=chartDetailVO.get(index);


                    for(int i=0;i<prodPerDate.getProducts().size();i++){
                        ProductVO productVO=prodPerDate.getProducts().get(i);
                        ChartDetailContent.addItem(ChartDetailContent.createDummyItem(productVO));
                    }
                    adapter.notifyDataSetChanged();


                }catch (Exception ex){
                    ex.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected() {

            }
        });

        BarData barData = new BarData(labels);
        //ArrayList<BarDataSet> barDataSets = new ArrayList<>();

        BarDataSet barDataSet = new BarDataSet(entries, "주간 지출 현황");  // add entries to dataset
        //System.out.println("사이즈 : "+entries.get(i).size());

        barDataSet.setColor(Color.parseColor("#E84C3D"));
        barDataSet.setBarSpacePercent(50f);
        //barDataSet.setBarBorderWidth(0.5f);
        barDataSet.setDrawValues(false);
        barDataSet.setHighlightEnabled(true);
        barDataSet.setHighLightColor(Color.parseColor("#FFE400"));
        barDataSet.setHighLightAlpha(255);
        //barDataSets.add(barDataSet);
        barData.addDataSet(barDataSet);


        barChart.setData(barData); // set the data and list of lables into chart

        YAxis byaxis = barChart.getAxisLeft();
        byaxis.setTextColor(Color.BLACK);
        byaxis.removeAllLimitLines();

        class GraphYAxisValueFormatter implements YAxisValueFormatter {
            private String[] mEmojis;



            @Override
            public String getFormattedValue(float value, YAxis yAxis) {
                String res;
                if(value>=10000){
                    res=(int)value/10000+"만원";
                }
                else
                    res=String.valueOf(formatter.format(value));

                return res;
            }
        }
        byaxis.setTextSize(9);
        byaxis.setValueFormatter(new GraphYAxisValueFormatter());
        byaxis.addLimitLine(upper);

        YAxis brightYAxis = barChart.getAxisRight();
        brightYAxis.setEnabled(false);

        XAxis bxaxis = barChart.getXAxis();
        bxaxis.setTextColor(Color.BLACK);
        //bxaxis.setLabelRotationAngle(-45);
        bxaxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        bxaxis.setDrawGridLines(false);

        Legend blegend = barChart.getLegend();
        blegend.setTextColor(Color.BLACK);

        barChart.animateXY(2000, 2000); //애니메이션 기능 활성화
        barChart.invalidate();

    }

    @Override
    public void getSaveLimitResult(int result) {
        if(result==1){
            presenter.getUpperLimit(getApplicationContext(),userInfo.getId(),ctgid);
        }
        else
            Toast.makeText(getApplicationContext(),"변경 실패",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setPresenter(ChartTaskContact.Presenter presenter) {
        this.presenter=(ChartPresenter)presenter;
    }
    @Override
    public void setPieChartData(ChartListVO chartListVO) { }
    @Override
    public void setList(ChartListVO chartListVO) { }

    @Override
    public void setDetailView(ChartDetailVO vo) {

        int maxprice=-1;int minprice=Integer.MAX_VALUE;
        int maxindex=0;
        String mindate=null,maxdate=null;

        entries.clear();
        labels.clear();
        ChartDetailContent.ITEMS.clear();

        this.chartDetailVO=vo;

        if(chartDetailVO!=null && chartDetailVO.size()==7){
            //Log.e("setDetailView","ChartDetailVO size is "+chartDetailVO.size());

            for(int i=0;i<7;i++){
                ChartDetailVO.ProdPerDate ppd=chartDetailVO.get(i);
                //Log.e("setDetailView",ppd.getDate());

                try {
                    SimpleDateFormat original_format = new SimpleDateFormat("yyyy-MM-dd");
                    SimpleDateFormat new_format = new SimpleDateFormat("MM/dd");

                    Date date = original_format.parse(ppd.getDate());

                    labels.add(new_format.format(date));
                }catch (Exception e){

                }
                entries.add(new BarEntry(ppd.getTotalprice(),i));

                if(i==0){
                    //Log.e("ppd size",String.valueOf(ppd.getProducts().size()));

                    for(int j=0;j<ppd.getProducts().size();j++) {
                        ChartDetailContent.addItem(ChartDetailContent.createDummyItem(ppd.getProducts().get(j)));
                    }
                    adapter.notifyDataSetChanged();
                }
                if(maxprice<ppd.getTotalprice()){
                    maxprice=ppd.getTotalprice();
                    maxdate=ppd.getDate();
                    maxindex=i;
                }
                if(minprice>ppd.getTotalprice()){
                    minprice=ppd.getTotalprice();
                    mindate=ppd.getDate();
                }

            }
            maxPrice.setText(formatter.format(maxprice));
            minPrice.setText(formatter.format(minprice));
            try {
                maxDate.setText(format2.format(format3.parse(maxdate)));
                minDate.setText(format2.format(format3.parse(mindate)));
            }catch (Exception e){
                e.printStackTrace();
            }
            presenter.getUpperLimit(getApplicationContext(),userInfo.getId(),ctgid);
            setBarChart();
            barChart.highlightValue(maxindex,0);
        }
        else{
            Log.e("setDetailView","ChartDetailVO is null");
        }

    }

    @Override
    public void setUpperLimit(UpperLimitVO upperLimitVO) {
        upperlimit=upperLimitVO.getUpperlimit();
        setBarChart();
    }

    class MonthPicker{
        private SimpleDateFormat original_format = new SimpleDateFormat("yyyy-MM-dd");
        private SimpleDateFormat format = new SimpleDateFormat("MM월 dd일");
        Calendar calendar = Calendar.getInstance();
        private TextView startDate,endDate;
        private ImageButton left_arrow,right_arrow;

        MonthPicker(){

            startDate=findViewById(R.id.startDate);
            endDate=findViewById(R.id.endDate);
            setInitDate();
            left_arrow=findViewById(R.id.left_arrow);
            right_arrow=findViewById(R.id.right_arrow);
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
            calendar = Calendar.getInstance();
            try {
                endDate.setText(format.format(original_format.parse(enddate)));
                calendar.setTime(format.parse(endDate.getText().toString()));

                calendar.add(calendar.DATE, -6);

                startDate.setText(format.format(calendar.getTime()));
            }catch (ParseException e){ }
        }
        // 화살표 버튼을 클릭함에 따라 선택된 연도와 월을 세팅해주는 메소드
        public void setMonthNDate(ImageButton btn) {
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
                    cal1.add(Calendar.DATE,-6);
                    cal1.add(Calendar.DATE,-1);
                    cal2.add(Calendar.DATE,-6);
                    cal2.add(Calendar.DATE,-1);
                    break;
                case R.id.right_arrow:
                    cal1.add(Calendar.DATE,+6);
                    cal1.add(Calendar.DATE,+1);
                    cal2.add(Calendar.DATE,+6);
                    cal2.add(Calendar.DATE,+1);
                    break;
            }
            startDate.setText(format.format(cal1.getTime()));
            endDate.setText(format.format(cal2.getTime()));
            setPickerState();
            presenter.getChartDetailData(getApplicationContext(),userInfo.getId(),userInfo.getGroup(),
                    picker.getStartDate(),picker.getEndDate(),ctgid);

        }
        // picker 상태(화살표 버튼을 활성화여부, 연,월 표시) 등을 결정하는 메소드
        public void setPickerState(){
            calendar=Calendar.getInstance();
            // 현재 연,월 일때는 그 이후로 갈 수 없으므로 오른쪽 화살표를 disable 처리한다.
            if(endDate.getText().toString().equals(enddate)){
                right_arrow.setEnabled(false);
                right_arrow.setImageDrawable(getResources().getDrawable(R.drawable.right_arrow_disable));
            }
            else {
                right_arrow.setEnabled(true);
                right_arrow.setImageDrawable(getResources().getDrawable(R.drawable.right_arrow));
            }

            /*
            if(startDate.getText().toString().equals(startdate)){
                left_arrow.setEnabled(false);
                left_arrow.setImageDrawable(getResources().getDrawable(R.drawable.left_arrow_disable));
            }
            else {
                left_arrow.setEnabled(true);
                left_arrow.setImageDrawable(getResources().getDrawable(R.drawable.left_arrow));
            }
            */
        }
        public String getStartDate(){
            SimpleDateFormat original_format = new SimpleDateFormat("yyyy년 MM월 dd일");
            SimpleDateFormat new_format = new SimpleDateFormat("yyyy-MM-dd");
            String newDate=null;
            try {
                Date originalDate = original_format.parse(Calendar.getInstance().get(Calendar.YEAR)+"년 "+startDate.getText().toString());
                newDate=new_format.format(originalDate);

            }catch (Exception e){
                e.printStackTrace();
            }

            Log.e("getStartDate",newDate);

            return newDate;
        }
        public String getEndDate(){
            SimpleDateFormat original_format = new SimpleDateFormat("yyyy년 MM월 dd일");
            SimpleDateFormat new_format = new SimpleDateFormat("yyyy-MM-dd");
            String newDate=null;
            try {
                Date originalDate = original_format.parse(Calendar.getInstance().get(Calendar.YEAR)+"년 "+endDate.getText().toString());
                newDate=new_format.format(originalDate);

            }catch (Exception e){
                e.printStackTrace();
            }

            Log.e("getEndDate",newDate);

            return newDate;
        }
    }

}
