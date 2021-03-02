package com.bukcoja.seung.receipt_inmyhand.ListAdapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bukcoja.seung.receipt_inmyhand.R;
import com.bukcoja.seung.receipt_inmyhand.dummy.PointSavingContent;

import java.util.List;

public class PointSavingItemRecyclerViewAdapter extends RecyclerView.Adapter<PointSavingItemRecyclerViewAdapter.ViewHolder>{
    final List<PointSavingContent.DummyItem> mValues;
    //private final ItemFragment.OnListFragmentInteractionListener mListener;

    public PointSavingItemRecyclerViewAdapter(List<PointSavingContent.DummyItem> items) {
        mValues = items;
       // mListener = listener;
    }

    @Override
    public PointSavingItemRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.pointsavingitem, parent, false);
        return new PointSavingItemRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //Log.e("mValues",String.valueOf(mValues.size()));
        holder.mItem = mValues.get(position);

        //holder.date.setText("");
        holder.date.setText(mValues.get(position).getDate());

        if(mValues.get(position).getState()==0){
            holder.state.setImageResource(R.drawable.saved);
        }
        else if(mValues.get(position).getState()==1){
            holder.state.setImageResource(R.drawable.used);
        }
        holder.point.setText(String.valueOf(mValues.get(position).getPoint()));
        holder.total.setText(String.valueOf(mValues.get(position).getTotal()));
    }


    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public PointSavingContent.DummyItem mItem;
        protected TextView date, point,total;
        protected ImageView state;

        public ViewHolder(View view) {
            super(view);
            this.date = view.findViewById(R.id.date);
            this.state =  view.findViewById(R.id.state);
            this.point= view.findViewById(R.id.point);
            this.total=view.findViewById(R.id.total);
            //mView = view;
            //mIdView = (TextView) view.findViewById(R.id.item_number);
            //mContentView = (TextView) view.findViewById(R.id.content);

        }

        @Override
        public String toString() {
            return super.toString();
        }
    }
}
