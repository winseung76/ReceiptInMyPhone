package com.bukcoja.seung.receipt_inmyhand.ListAdapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bukcoja.seung.receipt_inmyhand.R;
import com.bukcoja.seung.receipt_inmyhand.ReceiptLayout;
import com.bukcoja.seung.receipt_inmyhand.VO.ReceiptVO;
import com.bukcoja.seung.receipt_inmyhand.View.ItemFragment;
import com.bukcoja.seung.receipt_inmyhand.View.ItemFragment.OnListFragmentInteractionListener;
import com.bukcoja.seung.receipt_inmyhand.dummy.ReceiptContent.DummyItem;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyItemRecyclerViewAdapter extends RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder> {

    public final List<DummyItem> mValues;
    private final OnListFragmentInteractionListener mListener;
    private ReceiptLayout receiptLayout;
    private Fragment fragment;
    private String receiptID;
    private HashMap<String,String> map;
    private HashMap<String, ArrayList<String>> prods;

    public MyItemRecyclerViewAdapter(List<DummyItem> items, OnListFragmentInteractionListener listener, ItemFragment fragment) {
        mValues = items;
        mListener = listener;
        this.fragment=fragment;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final ReceiptVO.Receipt receipt=mValues.get(position).getReceipt();

        holder.mItem = mValues.get(position);
        //holder.mIdView.setText(mValues.get(position).id);
        //holder.mContentView.setText(mValues.get(position).content);
        map=mValues.get(position).getMap();
        prods=mValues.get(position).getProds();

        receiptID=String.valueOf(mValues.get(position).getId());
        // '상세보기' 클릭 시에 이벤트 처리
        holder.detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                //Toast.makeText(context, "테스트 메시지", Toast.LENGTH_LONG).show();
                //receiptLayout=new ReceiptLayout(context,map,prods);
                receiptLayout=new ReceiptLayout(context,mValues.get(position).getReceipt());
                receiptLayout.insertInfo();
                //createDialog(context);
                ((ItemFragment)fragment).createReceiptDialog(receiptLayout,String.valueOf(mValues.get(position).getId()));
            }
        });


        holder.id.setText("NUM: "+receiptID);

        //holder.name.setText(map.get("cn"));
        holder.name.setText(receipt.getCn());
        //holder.address.setText(String.valueOf(mValues.get(position).getAddress()));
        //holder.date.setText(map.get("date"));
        holder.date.setText(receipt.getDate());
        DecimalFormat formatter=new DecimalFormat("###,###");
        holder.price.setText(formatter.format(receipt.getTotalprice()));
    }


    @Override
    public int getItemCount() {
        return mValues.size();
    }

    // 홀더에 뷰를 꼽아놓은듯 보관하는 객체
    public class ViewHolder extends RecyclerView.ViewHolder {
        //public final View mView;
        //public final TextView mIdView;
        //public final TextView mContentView;
        private TextView id;
        public DummyItem mItem;
        protected TextView name,address,category,price;
        protected TextView date;
        protected TextView detail; // 영수증 상세보기 클릭 부분

        public ViewHolder(View view) {
            super(view);
            this.id=view.findViewById(R.id.id);
            this.date=view.findViewById(R.id.date);
            this.name=view.findViewById(R.id.name);
            this.address=view.findViewById(R.id.address);
            //this.category=view.findViewById(R.id.category);
            this.price=view.findViewById(R.id.price);
            this.detail=view.findViewById(R.id.detail);
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
