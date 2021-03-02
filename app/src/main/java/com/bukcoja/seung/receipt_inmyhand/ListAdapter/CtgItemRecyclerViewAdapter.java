package com.bukcoja.seung.receipt_inmyhand.ListAdapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bukcoja.seung.receipt_inmyhand.R;
import com.bukcoja.seung.receipt_inmyhand.View.ChartDetailActivity;
import com.bukcoja.seung.receipt_inmyhand.View.ChartFragment;
import com.bukcoja.seung.receipt_inmyhand.View.ItemFragment.OnListFragmentInteractionListener;
import com.bukcoja.seung.receipt_inmyhand.dummy.CategoryContent.DummyItem;
import com.github.mikephil.charting.data.BarEntry;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class CtgItemRecyclerViewAdapter extends RecyclerView.Adapter<CtgItemRecyclerViewAdapter.ViewHolder> {

    final List<DummyItem> mValues;
    private int ctgid;
    private Context context;
    private ChartFragment.MonthPicker monthPicker;

    public CtgItemRecyclerViewAdapter(List<DummyItem> items, Context context, ChartFragment.MonthPicker monthPicker) {
        mValues = items;

        this.ctgid=ctgid;
        this.context=context;
        this.monthPicker=monthPicker;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ctgitem, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mItem = mValues.get(position);

        int color=mValues.get(position).getColor();
        int sum=mValues.get(position).getSum();
        int total=mValues.get(position).getTotal();
        holder.cbc.setColor(color);
        holder.cbc.setData(sum);
        //holder.cbc.setBarChart();
        holder.color.setBackgroundColor(color);

        double percentData;

        if(total!=0)
             percentData=(double)sum/(double)total*100;
        else
             percentData=0;

        holder.percent.setText(String.format("%.1f",percentData)+"%");
        holder.category.setText(String.valueOf(mValues.get(position).getCategory()));
        DecimalFormat formatter=new DecimalFormat("###,###");
        holder.sum.setText(formatter.format(sum));

        holder.itemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, ChartDetailActivity.class);
                intent.putExtra("ctgid",mValues.get(position).getCtgid());
                intent.putExtra("startdate",monthPicker.getStartDate());
                intent.putExtra("enddate",monthPicker.getEndDate());

                context.startActivity(intent);
            }
        });
        holder.itemLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction()==MotionEvent.ACTION_DOWN){
                    holder.itemLayout.setBackgroundColor(Color.parseColor("#EAEAEA"));
                }
                else if(motionEvent.getAction()!=MotionEvent.ACTION_DOWN){
                    holder.itemLayout.setBackgroundColor(Color.WHITE);
                }
                return false;
            }
        });

    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public DummyItem mItem;
        protected TextView category,sum;
        public TextView percent;
        public LinearLayout itemLayout;

        protected View color;
        protected CtgBarChart cbc;

        public ViewHolder(View view) {
            super(view);
            this.color=view.findViewById(R.id.color);
            this.category=view.findViewById(R.id.ctgname);
            this.sum=view.findViewById(R.id.sum);
            this.percent=view.findViewById(R.id.percent);
            this.itemLayout=view.findViewById(R.id.item_layout);
            //this.image=view.findViewById(R.id.image);
            cbc=new CtgBarChart(view);

        }

        class CtgBarChart{
            //private HorizontalBarChart barChart;
            private ArrayList<String> x_value=new ArrayList<>();  //x축 데이터
            private ArrayList<BarEntry> entries=new ArrayList<>(); // y값으로 나타날 엔트리들의 리스트
            private int data;
            private int color;

            CtgBarChart(View view){
                //this.barChart=view.findViewById(R.id.ctg_bar);
            }

            public void setColor(int color){this.color=color;}

            // BarEntry로 변결될 원래 int형 데이터를 가져옴
            public void setData(int data){
                this.data=data;
                entries.add(new BarEntry(data,0));
                x_value.add("0");
            }

        }

        @Override
        public String toString() {
            return super.toString();
        }


    }
}
