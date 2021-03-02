package com.bukcoja.seung.receipt_inmyhand.ListAdapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bukcoja.seung.receipt_inmyhand.R;
import com.bukcoja.seung.receipt_inmyhand.dummy.ChartDetailContent;

import java.text.DecimalFormat;
import java.util.List;

public class ChartDetailRecyclerViewAdapter extends RecyclerView.Adapter<ChartDetailRecyclerViewAdapter.ViewHolder>  {

    final List<ChartDetailContent.DummyItem> mValues;
    private Context context;

    public ChartDetailRecyclerViewAdapter(List<ChartDetailContent.DummyItem> items) {
        mValues = items;

    }

    @Override
    public ChartDetailRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chartdetaillist_item_layout, parent, false);

        return new ChartDetailRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ChartDetailRecyclerViewAdapter.ViewHolder holder, final int position) {
        holder.mItem = mValues.get(position);

        holder.prodname.setText(mValues.get(position).productname);
        //Log.e("prodname",mValues.get(position).productname);
        DecimalFormat formatter=new DecimalFormat("###,###");
        holder.unitprice.setText(formatter.format(mValues.get(position).unitprice));
        holder.count.setText(String.valueOf(mValues.get(position).count));

    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ChartDetailContent.DummyItem mItem;
        public TextView prodname,unitprice,count;

        public ViewHolder(View view) {
            super(view);

            this.prodname=view.findViewById(R.id.product_name);
            this.unitprice=view.findViewById(R.id.unitprice);
            this.count=view.findViewById(R.id.count);

        }


        @Override
        public String toString() {
            return super.toString();
        }


    }
}
