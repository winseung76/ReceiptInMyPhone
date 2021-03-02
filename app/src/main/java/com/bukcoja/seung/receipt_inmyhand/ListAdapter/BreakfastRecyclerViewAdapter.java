package com.bukcoja.seung.receipt_inmyhand.ListAdapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bukcoja.seung.receipt_inmyhand.R;
import com.bukcoja.seung.receipt_inmyhand.View.BreakfastFragment.OnListFragmentInteractionListener;
import com.bukcoja.seung.receipt_inmyhand.dummy.BreakfastContent.DummyItem;

import java.io.IOException;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class BreakfastRecyclerViewAdapter extends RecyclerView.Adapter<BreakfastRecyclerViewAdapter.ViewHolder>{

    private final List<DummyItem> mValues;
    private final OnListFragmentInteractionListener mListener;
    private final Context context;


    public BreakfastRecyclerViewAdapter(List<DummyItem> items, OnListFragmentInteractionListener listener, Context context) {
        mValues = items;
        mListener = listener;
        this.context=context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_breakfast, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);

        holder.imageView.setImageDrawable(mValues.get(position).image);
        holder.storename.setText(mValues.get(position).store);
        holder.address.setText(mValues.get(position).address);
        holder.menu.setText(mValues.get(position).menu);
        holder.count.setText(String.valueOf(mValues.get(position).count));

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });

        // '위치 보기'버튼 클릭 시에 이벤트 처리
        holder.location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                convertAddress(holder.address.getText().toString(),holder.storename.getText().toString());

            }
        });
        holder.location.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction()==MotionEvent.ACTION_DOWN){
                    holder.location.setBackground(context.getResources().getDrawable(R.drawable.buttonlayout3));
                    holder.location.setTextColor(Color.WHITE);
                }
                else{
                    holder.location.setBackground(context.getResources().getDrawable(R.drawable.buttonlayout2));
                    holder.location.setTextColor(Color.parseColor("#e84c3d"));
                }
                return false;
            }
        });

    }

    public void convertAddress(String address,String label){
        // 주소입력후 지도2버튼 클릭시 해당 위도경도값의 지도화면으로 이동
        List<Address> list = null;
        final Geocoder geocoder = new Geocoder(context);

        try {
            list = geocoder.getFromLocationName(address, 10); // 읽을 개수
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("test","입출력 오류 - 서버에서 주소변환시 에러발생");
        }

        if (list != null) {
            if (list.size() == 0) {
                Toast.makeText(context,"해당되는 주소 정보는 없습니다",Toast.LENGTH_SHORT).show();
            } else {
                // 해당되는 주소로 인텐트 날리기
                Toast.makeText(context,"구글맵으로 이동합니다",Toast.LENGTH_SHORT).show();

                Address addr = list.get(0);
                double lat = addr.getLatitude();
                double lon = addr.getLongitude();

                //String sss = String.format("geo:%f,%f", lat, lon);

                String uriBegin = "geo:" + lat + "," + lon;
                String query = lat + "," + lon + "(" + label + ")";
                String encodedQuery = Uri.encode(query);
                String uriString = uriBegin + "?q=" + encodedQuery + "&z=16";

                Uri uri = Uri.parse(uriString);
                Intent it = new Intent(Intent.ACTION_VIEW,uri);
                context.startActivity(it);
            }
        }
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView imageView;
        public final TextView storename;
        public final TextView address;
        public final TextView menu;
        public final TextView count;
        public final Button location;
        public DummyItem mItem;

        public ViewHolder(View view) {
            super(view);

            mView = view;
            imageView=view.findViewById(R.id.medal);
            storename = view.findViewById(R.id.storename);
            address =  view.findViewById(R.id.address);
            menu=view.findViewById(R.id.menu);
            count=view.findViewById(R.id.count);
            location=view.findViewById(R.id.location);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + address.getText() + "'";
        }
    }
}
