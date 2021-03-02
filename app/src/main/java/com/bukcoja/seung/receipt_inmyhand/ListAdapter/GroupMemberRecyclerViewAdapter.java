package com.bukcoja.seung.receipt_inmyhand.ListAdapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bukcoja.seung.receipt_inmyhand.R;
import com.bukcoja.seung.receipt_inmyhand.dummy.GroupMemberContent;
import com.squareup.picasso.Picasso;

import java.util.List;

public class GroupMemberRecyclerViewAdapter extends RecyclerView.Adapter<GroupMemberRecyclerViewAdapter.ViewHolder> {

    private final List<GroupMemberContent.DummyItem> mValues;
    private Context context;
    //private final DinnerFragment.OnListFragmentInteractionListener mListener;

    public GroupMemberRecyclerViewAdapter(List<GroupMemberContent.DummyItem> items, Context context) {
        mValues = items;
        this.context=context;
        //mListener = listener;
    }

    @Override
    public GroupMemberRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.group_members_list, parent, false);
        return new GroupMemberRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final GroupMemberRecyclerViewAdapter.ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        //holder.imageView.setImageDrawable(mValues.get(position).image);
        Picasso.with(context).load(mValues.get(position).imageUri).into(holder.imageView);
        holder.userID.setText(mValues.get(position).userid);


    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView imageView;
        public final TextView userID;
        public GroupMemberContent.DummyItem mItem;

        public ViewHolder(View view) {
            super(view);

            mView = view;
            imageView=view.findViewById(R.id.imageView);
            userID=view.findViewById(R.id.userID);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + userID.getText() + "'";
        }
    }
}
