package com.bukcoja.seung.receipt_inmyhand.View;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bukcoja.seung.receipt_inmyhand.ListAdapter.LunchRecyclerViewAdapter;
import com.bukcoja.seung.receipt_inmyhand.Presenter.RcmdPresenter;
import com.bukcoja.seung.receipt_inmyhand.Presenter.RcmdTaskContact;
import com.bukcoja.seung.receipt_inmyhand.R;
import com.bukcoja.seung.receipt_inmyhand.VO.DateVO;
import com.bukcoja.seung.receipt_inmyhand.VO.ServiceVO;
import com.bukcoja.seung.receipt_inmyhand.dummy.LunchContent;
import com.bukcoja.seung.receipt_inmyhand.dummy.LunchContent.DummyItem;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class LunchFragment extends Fragment implements RcmdTaskContact.View {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    private RcmdPresenter presenter;
    private RecyclerView recyclerView;
    private LunchRecyclerViewAdapter adapter;
    private View view;
    private TextView updateDate;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public LunchFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static LunchFragment newInstance(int columnCount) {
        LunchFragment fragment = new LunchFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        presenter=new RcmdPresenter(this);


        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_lunch_list, container, false);

        initView();
        presenter.getRcmdData(getContext(),1);
        presenter.getUpdateDate(getContext());

        return view;
    }

    public void initView(){

        updateDate=view.findViewById(R.id.update_date);

        // Set the adapter
        recyclerView = (RecyclerView) view.findViewById(R.id.list);
        if (mColumnCount <= 1) {
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        }
        else {
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), mColumnCount));
        }
        adapter=new LunchRecyclerViewAdapter(LunchContent.ITEMS,mListener,getContext());
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void setRcmdData(ServiceVO serviceVO) {
        LunchContent.ITEMS.clear();

        //Log.e("setRcmdData","Called");
        ServiceVO.RcmdStore rcmdStore1=serviceVO.getRank1();
        ServiceVO.RcmdStore rcmdStore2=serviceVO.getRank2();
        ServiceVO.RcmdStore rcmdStore3=serviceVO.getRank3();

        Drawable medal1=getResources().getDrawable(R.drawable.first_medal);
        Drawable medal2=getResources().getDrawable(R.drawable.second_medal);
        Drawable medal3=getResources().getDrawable(R.drawable.third_medal);

        LunchContent.addItem(LunchContent.createDummyItem(medal1,rcmdStore1));
        LunchContent.addItem(LunchContent.createDummyItem(medal2,rcmdStore2));
        LunchContent.addItem(LunchContent.createDummyItem(medal3,rcmdStore3));

        adapter.notifyDataSetChanged();
    }

    @Override
    public void setUpdateDate(DateVO dateVO) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy년 MM월 dd일");
        SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");

        try {
            Date date=format2.parse(dateVO.getDate());
            String datestr=format.format(date);
            updateDate.setText(datestr+" 기준");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void setPresenter(RcmdTaskContact.Presenter presenter) {
        this.presenter=(RcmdPresenter)presenter;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(DummyItem item);
    }
}
