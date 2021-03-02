package com.bukcoja.seung.receipt_inmyhand.View;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bukcoja.seung.receipt_inmyhand.ListAdapter.GroupMemberRecyclerViewAdapter;
import com.bukcoja.seung.receipt_inmyhand.ListAdapter.MyItemRecyclerViewAdapter;
import com.bukcoja.seung.receipt_inmyhand.Presenter.ReceiptPresenter;
import com.bukcoja.seung.receipt_inmyhand.Presenter.ReceiptTaskContact;
import com.bukcoja.seung.receipt_inmyhand.R;
import com.bukcoja.seung.receipt_inmyhand.ReceiptLayout;
import com.bukcoja.seung.receipt_inmyhand.SwipeController;
import com.bukcoja.seung.receipt_inmyhand.SwipeControllerActions;
import com.bukcoja.seung.receipt_inmyhand.UserInfo;
import com.bukcoja.seung.receipt_inmyhand.VO.GroupMemberListVO;
import com.bukcoja.seung.receipt_inmyhand.VO.ReceiptVO;
import com.bukcoja.seung.receipt_inmyhand.dummy.GroupMemberContent;
import com.bukcoja.seung.receipt_inmyhand.dummy.ReceiptContent;
import com.kakao.kakaolink.v2.KakaoLinkResponse;
import com.kakao.kakaolink.v2.KakaoLinkService;
import com.kakao.message.template.ButtonObject;
import com.kakao.message.template.ContentObject;
import com.kakao.message.template.FeedTemplate;
import com.kakao.message.template.LinkObject;
import com.kakao.network.ErrorResult;
import com.kakao.network.callback.ResponseCallback;
import com.kakao.network.storage.ImageUploadResponse;
import com.kakao.util.helper.log.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class ItemFragment extends Fragment implements ReceiptTaskContact.View {

    private EditText from,to;
    private  View view;
    private Button search;
    private MyItemRecyclerViewAdapter adapter;
    private GroupMemberRecyclerViewAdapter gadapter;
    private RecyclerView recyclerView;
    private SwipeController swipeController;
    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Period period;
    private UserInfo userInfo;
    private TextView total,group;
    private TextView addmember,groupMember,deleteAll;
    private int totalprice=0;
    private ReceiptPresenter presenter;
    private String linkImageURL;
    private LinearLayout member_setting;
    private ResponseCallback<KakaoLinkResponse> callback;
    private static final String SEGMENT_PROMOTION = "promotion";
    private static final int LOCAL_RECEIPT_MAX_COUNT=1;
    /**
     * BasicInfo empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static ItemFragment newInstance(int columnCount) {
        ItemFragment fragment = new ItemFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    // 프래그먼트가 처음 만들어 질때 호출됨
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("ItemFragment","onCreate");
        userInfo=(UserInfo)getActivity().getApplication();
        //FirebaseApp.initializeApp(getActivity());
        //handleDeepLink();

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
        presenter=new ReceiptPresenter(getContext(),this);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.e("ItemFragment","onCreateView");

        view = inflater.inflate(R.layout.fragment_item_list, container, false);
        initView();
        period=new Period();
        saveLogoImage();
        createKakaoLinkImage();
        //checkFirstRun();

        //presenter.getReceiptCount();
        ReceiptContent.ITEMS.clear();
        presenter.getReceiptData(userInfo.getId(),period.getStartDate(),period.getEndDate(),userInfo.getGroup(),userInfo.getName());


        if(userInfo.getGroup()==1) {
            //member_setting.setVisibility(View.INVISIBLE);
            addmember.setEnabled(false);
            addmember.setTextColor(Color.parseColor("#8C8C8C"));
            groupMember.setEnabled(false);
            groupMember.setTextColor(Color.parseColor("#8C8C8C"));
        }
        else {
            //member_setting.setVisibility(View.VISIBLE);
            addmember.setEnabled(true);
            addmember.setTextColor(Color.parseColor("#e84c3d"));
            groupMember.setEnabled(true);
            groupMember.setTextColor(Color.parseColor("#e84c3d"));
        }


        return view;
    }

    public void initView(){

        swipeRefreshLayout=view.findViewById(R.id.swipe_layout);
        //swipeRefreshLayout.setProgressBackgroundColorSchemeColor(Color.parseColor("#e84c3d"));
        swipeRefreshLayout.setColorSchemeColors(Color.parseColor("#e84c3d"));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                /*
                Fragment visible=getVisibleFragment();
                if(visible!=null){
                    fm.beginTransaction().detach(visible).attach(visible).commit();
                }

                 */
                totalprice=0;
                //presenter.getReceiptCount();
                ReceiptContent.ITEMS.clear();
                presenter.getReceiptData(userInfo.getId(),period.getStartDate(),period.getEndDate(),userInfo.getGroup(),userInfo.getName());
                //swipeRefreshLayout.setProgressViewEndTarget(true,30);
                swipeRefreshLayout.setRefreshing(false);

            }
        });

        total=view.findViewById(R.id.total);
        totalprice=0;
        search=view.findViewById(R.id.search);
        member_setting=view.findViewById(R.id.member_setting);
        //count=view.findViewById(R.id.count);

        // 조회 버튼 클릭 시 이벤트 처리
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                totalprice=0;
                //presenter.getReceiptCount();
                ReceiptContent.ITEMS.clear();
                presenter.getReceiptData(userInfo.getId(),period.getStartDate(),period.getEndDate(),userInfo.getGroup(),userInfo.getName());
            }
        });

        search.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                if(motionEvent.getAction()==MotionEvent.ACTION_DOWN){
                    search.setBackground(getResources().getDrawable(R.drawable.buttonlayout3));
                    search.setTextColor(Color.WHITE);
                }
                else if(motionEvent.getAction()!=MotionEvent.ACTION_DOWN){
                    search.setBackground(getResources().getDrawable(R.drawable.buttonlayout2));
                    search.setTextColor(Color.parseColor("#e84c3d"));
                }
                return false;
            }
        });

        recyclerView = (RecyclerView) view.findViewById(R.id.list);
        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                //super.onScrolled(recyclerView, dx, dy);

                int topRowVerticalPosition =
                        (recyclerView == null || recyclerView.getChildCount() == 0) ? 0 : recyclerView.getChildAt(0).getTop();
                swipeRefreshLayout.setEnabled(topRowVerticalPosition >= 0);
            }
        });
        addmember=view.findViewById(R.id.addmember);

        // 멤버 추가 버튼을 누를 때 발생하는 이벤트
        addmember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendLink();
                //onDynamicLinkClick();
            }
        });

        groupMember=view.findViewById(R.id.group_members);
        groupMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showGroupMemberDialog();
            }
        });

        deleteAll=view.findViewById(R.id.delete_all);
        deleteAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createDeleteAllDialog();
            }
        });

        //group=view.findViewById(R.id.group);
        //group.setText(userInfo.getGroupname());

        if (mColumnCount <= 1) {
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        }
        else {
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), mColumnCount));
        }

        adapter=new MyItemRecyclerViewAdapter(ReceiptContent.ITEMS, mListener,this);
        ReceiptContent.ITEMS.clear();
        recyclerView.setAdapter(adapter);
        setupRecyclerView();

        // 삭제 기능을 구현하기 위해 리사이클뷰에 helper를 attach 한다.
        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeController);
        itemTouchhelper.attachToRecyclerView(recyclerView);
    }

    public void showGroupMemberDialog(){
        AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
        View dialogView=View.inflate(getContext(),R.layout.group_members_dialog,null);
        builder.setView(dialogView);

        RecyclerView recyclerView=dialogView.findViewById(R.id.recyclerview);
        gadapter=new GroupMemberRecyclerViewAdapter(GroupMemberContent.ITEMS,getContext());

        if (mColumnCount <= 1) {
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        }
        else {
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), mColumnCount));
        }
        //adapter=new MyItemRecyclerViewAdapter(ReceiptContent.ITEMS, mListener,this);
        GroupMemberContent.ITEMS.clear();
        recyclerView.setAdapter(gadapter);

        presenter.getGroupMemberInfo(getContext(),userInfo.getGroup());

        final AlertDialog dialog=builder.create();
        dialog.show();

    }

    public void saveLogoImage(){
        //내부저장소 캐시 경로를 받아옵니다.
        File storage = getActivity().getCacheDir();
        Bitmap bitmap= BitmapFactory.decodeResource(getResources(), R.drawable.rimplogo);


        //저장할 파일 이름
        String fileName = "rimplogo.jpg";

        //storage 에 파일 인스턴스를 생성합니다.
        File tempFile = new File(storage, fileName);
        Log.e("tempFile",String.valueOf(tempFile));
        try {

            // 자동으로 빈 파일을 생성합니다.
            tempFile.createNewFile();

            // 파일을 쓸 수 있는 스트림을 준비합니다.
            FileOutputStream out = new FileOutputStream(tempFile);

            // compress 함수를 사용해 스트림에 비트맵을 저장합니다.
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);

            // 스트림 사용후 닫아줍니다.
            out.close();

        } catch (FileNotFoundException e) {
            Log.e("MyTag","FileNotFoundException : " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            Log.e("MyTag","IOException : " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void createKakaoLinkImage(){
        File imageFile = new File("data/user/0/com.example.seung.receipt_inmyhand/cache/rimplogo.jpg");

        Log.e("imageFile",String.valueOf(imageFile));

        KakaoLinkService.getInstance().uploadImage(getContext(), false, imageFile, new ResponseCallback<ImageUploadResponse>() {
            @Override
            public void onFailure(ErrorResult errorResult) {
                Logger.e(errorResult.toString());
            }

            @Override
            public void onSuccess(ImageUploadResponse result) {
                Logger.d(result.getOriginal().getUrl());
                linkImageURL=result.getOriginal().getUrl();
                Log.e("linkImageURL",linkImageURL);
            }
        });

    }
    public void createReceiptDialog(ReceiptLayout receiptLayout, final String receiptID){
        LayoutInflater inflater = LayoutInflater.from(getContext());
        //View dialogView = inflater.inflate(R.layout.receipt_dialog, null);

        // 디바이스 화면 해상도를 구하기 위해 DisplayMetrics 객체를 얻어옴
        final View dialogView=receiptLayout.getReceipt();
        LinearLayout linearLayout=dialogView.findViewById(R.id.linearLayout);

        DisplayMetrics metrics = getContext().getResources().getDisplayMetrics();

        final AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());

        // 전자 영수증 대화상자의 제목
        View customTitle = View.inflate(getContext(), R.layout.custom_title, null);
        TextView title=customTitle.findViewById(R.id.title);
        title.setText("영수증 번호 : "+receiptID);

        builder.setCustomTitle(customTitle);
        builder.setView(dialogView);

        TextView sharebtn=customTitle.findViewById(R.id.sharebtn);
        // '공유' 버튼 클릭 시에 이벤트 처리
        sharebtn.setOnClickListener(new View.OnClickListener() {

            int checkedGroupId;

            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialogBuiler=new AlertDialog.Builder(getActivity());

                dialogBuiler.setTitle("공유할 그룹을 선택하세요");
                View dialogView=View.inflate(getContext(), R.layout.receipt_share_dialog, null);
                dialogBuiler.setView(dialogView);

                LinearLayout scrolllayout=dialogView.findViewById(R.id.group_scroll);

                ArrayList<String> groups=userInfo.getGroups();
                final ArrayList<Integer> groupids=userInfo.getGroupids();
                final RadioGroup radioGroup=new RadioGroup(getActivity());

                for(int i=0;i<groups.size();i++){

                    if(!String.valueOf(userInfo.getGroup()).equals(groups.get(i))) {
                        final AppCompatCheckBox checkbox = new AppCompatCheckBox(getActivity());

                        final RadioButton radioButton=new RadioButton(getActivity());

                        /*
                        checkbox.setText(groups.get(i));
                        checkbox.setTextColor(Color.parseColor("#000000"));
                        checkbox.setHighlightColor(Color.parseColor("#e84c3d"));
                        checkbox.setTextSize(15);

                        scrolllayout.addView(checkbox);
                        */
                        radioButton.setText(groups.get(i));
                        radioButton.setTextColor(Color.parseColor("#000000"));
                        radioButton.setHighlightColor(Color.parseColor("#e84c3d"));
                        radioGroup.addView(radioButton);
                        final int index=i;
                        radioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                                if(b){
                                    checkedGroupId=groupids.get(index);
                                }
                            }
                        });
                        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                                if(b){
                                    checkedGroupId=groupids.get(index);

                                }

                            }
                        });
                    }

                }
                scrolllayout.addView(radioGroup);
                final AlertDialog dialog=dialogBuiler.create();
                dialog.show();

                TextView cancel=dialogView.findViewById(R.id.cancel);
                // '취소' 버튼을 누를 시에 이벤트 처리
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                TextView share=dialogView.findViewById(R.id.share);
                // '공유' 버튼을 누를 시에 이벤트 처리
                share.setOnClickListener(new View.OnClickListener() {

                    final int receiptid=Integer.parseInt(receiptID);

                    @Override
                    public void onClick(View view) {
                        Log.e("checkedGroupId",String.valueOf(checkedGroupId));
                        presenter.shareReceipt(getContext(),userInfo.getId(),checkedGroupId,receiptid);
                        dialog.dismiss();
                        //Toast.makeText(getActivity(),checkedGroupId+", "+receiptid,Toast.LENGTH_SHORT).show();
                    }

                });
            }
        });

        final AlertDialog dialog=builder.create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {

                //int height=dialog.getWindow().getDecorView().getHeight();
                int height=dialogView.getHeight();

                ViewGroup.LayoutParams clsParam = dialogView.getLayoutParams();
                DisplayMetrics metrics = getContext().getResources().getDisplayMetrics();

                //Log.e("height",String.valueOf(height));
                //Log.e("height2",String.valueOf(height));
                //Log.e("height3",String.valueOf(metrics.heightPixels*0.8));

                // 전자 영수증의 크기가 화면 크기의 80% 보다 큰 경우
                if(height>(int)(metrics.heightPixels*0.8)) {
                    // 전자 영수증의 최대 크기를
                    // 화면의 80%로 설정한다
                    clsParam.height = (int)(metrics.heightPixels*0.8);
                }
                // 전자 영수증의 크기가 화면 크기의 80% 보다 작은 경우
                else {
                    // 그 크기를 그대로 유지한다.
                    clsParam.height=height;
                }
                dialogView.setLayoutParams(clsParam);
            }
        });
        dialog.show();


        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        ImageView close=customTitle.findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

    }

    public void createDeleteAllDialog(){
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());

        View view=View.inflate(getContext(),R.layout.dialog_with_two_button,null);
        builder.setView(view);

        final AlertDialog dialog=builder.create();
        dialog.show();

        TextView msg=view.findViewById(R.id.msg);
        msg.setText("'"+userInfo.getGroupname()+"' 의 모든 영수증을 삭제하시겠습니까?");
        TextView yes=view.findViewById(R.id.yes);
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                List<Integer> list=new ArrayList<>();
                for(int i=0;i< ReceiptContent.ITEMS.size();i++){
                    list.add(adapter.mValues.get(i).getId());
                }
                presenter.deleteReceipt(list,userInfo.getId(),userInfo.getGroup());
                dialog.dismiss();

                adapter.mValues.clear();
                total.setText("0");
                /*
                for(int i=0;i< ReceiptContent.ITEMS.size();i++){

                    // 리스트에서 삭제
                    adapter.mValues.remove(i);
                    adapter.notifyItemRemoved(i);
                    //adapter.notifyItemRangeChanged(i, adapter.getItemCount());
                }
                */
                adapter.notifyDataSetChanged();

            }
        });
        TextView no=view.findViewById(R.id.no);
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }


    private void sendLink(){

        FeedTemplate params = FeedTemplate
                .newBuilder(ContentObject.newBuilder("RIMP에서 "+userInfo.getName()+"님이 초대장을 보냈습니다.",
                        linkImageURL,
                        LinkObject.newBuilder().setWebUrl("https://developers.kakao.com")
                                .setMobileWebUrl("https://developers.kakao.com").build())
                        .setDescrption("")
                        .build())
                //.setSocial(SocialObject.newBuilder().setLikeCount(10).setCommentCount(20)
                //        .setSharedCount(30).setViewCount(40).build())
                .addButton(new ButtonObject("초대장 보기", LinkObject.newBuilder()
                        .setWebUrl("'https://developers.kakao.com")
                        .setMobileWebUrl("'https://developers.kakao.com")
                        .setAndroidExecutionParams("group="+userInfo.getGroup())
                        .setIosExecutionParams("key1=value1")
                        .build()))
                .build();

        Map<String, String> serverCallbackArgs = new HashMap<String, String>();
        serverCallbackArgs.put("user_id", "${current_user_id}");
        serverCallbackArgs.put("product_id", "${shared_product_id}");


        KakaoLinkService.getInstance().sendDefault(getActivity(), params, serverCallbackArgs, new ResponseCallback<KakaoLinkResponse>() {
            @Override
            public void onFailure(ErrorResult errorResult) {
                Logger.e(errorResult.toString());
            }

            @Override
            public void onSuccess(KakaoLinkResponse result) {
            }
        });
    }


    public void showDeleteCheckDialog(final int position){
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        final View dialogView= View.inflate(getContext(), R.layout.dialog_with_two_button, null);
        builder.setView(dialogView);

        TextView msg=dialogView.findViewById(R.id.msg);
        msg.setText("정말 삭제하시겠습니까?");
        TextView yes=dialogView.findViewById(R.id.yes);
        TextView no=dialogView.findViewById(R.id.no);

        final AlertDialog dialog=builder.create();

        // '예'버튼을 누르면 리스트에서 삭제
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                totalprice=Integer.parseInt(total.getText().toString().replace(",",""));
                totalprice-=adapter.mValues.get(position).getReceipt().getTotalprice();
                total.setText(new DecimalFormat("###,###").format(totalprice));

                //dbHelper.delete(adapter.mValues.get(position).getId());
                List<Integer> list=new ArrayList<>();
                list.add(adapter.mValues.get(position).getId());
                presenter.deleteReceipt(list,userInfo.getId(),userInfo.getGroup());

                // 리스트에서 삭제
                adapter.mValues.remove(position);
                adapter.notifyItemRemoved(position);
                adapter.notifyItemRangeChanged(position, adapter.getItemCount());

                dialog.dismiss();
            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public void checkReceiptShareResult(int result) {
        AlertDialog.Builder builer=new AlertDialog.Builder(getActivity());
        View dialogView=View.inflate(getContext(),R.layout.dialog_with_one_button,null);
        builer.setView(dialogView);
        TextView msg=dialogView.findViewById(R.id.msg);
        TextView ok=dialogView.findViewById(R.id.ok);

        // 제대로 공유되었을 경우
        if(result==1){
            msg.setText("공유 되었습니다.");
        }
        else if(result==-1) {
            msg.setText("해당 영수증이 이미 존재합니다.");
        }
        else{
            msg.setText("공유를 실패하였습니다.\n다시 시도하시길 바랍니다.");
        }
        final AlertDialog dialog=builer.create();
        dialog.show();
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    @Override
    public void setReceiptCount(int receiptCount) {

        Log.e("로컬의 전자 영수증 개수",String.valueOf(receiptCount));

        // 로컬에 있는 전자 영수증의 개수가 30개를 넘는 경우
        if(receiptCount>=LOCAL_RECEIPT_MAX_COUNT){
            // 로컬에 있는 전자 영수증을 중앙으로 업데이트
            // 로컬에 있는 전자 영수증은 삭제됨
            //Toast.makeText(getContext(),"setReceiptCount :"+userInfo.getGroup(),Toast.LENGTH_SHORT).show();
            presenter.moveReceiptFromLocalToCenter(getContext(),userInfo.getId(),userInfo.getGroup());
        }

        // 전자 영수증 불러오기
        // 로컬에 있는 전자 영수증의 수만큼 로컬에서 불러오기
        // 로컬과 중앙에서 전자 영수증 정보가 중복되는 일은 없음
        ReceiptContent.ITEMS.clear();
        presenter.getReceiptData(userInfo.getId(),period.getStartDate(),period.getEndDate(),userInfo.getGroup(),userInfo.getName());
        // 나머지는 중앙에서 불러오기
        //presenter.getReceiptData(1,userInfo.getId(),period.getStartDate(),period.getEndDate(),userInfo.getGroup(),userInfo.getName());

    }

    @Override
    public void setGroupMembers(GroupMemberListVO listVO) {

        Log.e("group member number",String.valueOf(listVO.size()));

        if(listVO!=null){
            for(int i=0;i<listVO.size();i++){
                GroupMemberListVO.GroupMemberVO memberVO =listVO.get(i);
                Log.e("group member userid",memberVO.getUserid());
                Log.e("group member imageuri",memberVO.getImageUri());
                GroupMemberContent.addItem(GroupMemberContent.createDummyItem(memberVO.getImageUri(),memberVO.getUserid()));
            }
        }
        gadapter.notifyDataSetChanged();

    }

    @Override
    public void setReceiptList(ReceiptVO list) {
        ReceiptContent.ITEMS.clear();
        //totalprice+=Integer.parseInt(map.get("price"));

        //Log.e("receipt size ",String.valueOf(list.size()));
        //Toast.makeText(getContext(),"receipt size : "+list.size(),Toast.LENGTH_SHORT).show();

        for(int i=0;i<list.size();i++){
            ReceiptVO.Receipt receipt=list.get(i);
            ReceiptContent.addItem(ReceiptContent.createDummyItem(receipt));
            totalprice+=list.get(i).getTotalprice();
            //Log.e("receipt product",String.valueOf(receipt.getReceiptid()));
        }
        DecimalFormat formatter=new DecimalFormat("###,###");
        total.setText(formatter.format(totalprice));
        //ReceiptContent.addItem(ReceiptContent.createDummyItem(regnum,map,prods));
        adapter.notifyDataSetChanged();
        //Toast.makeText(getContext(),"setReceiptList",Toast.LENGTH_SHORT).show();
    }


    @Override
    public void setPresenter(ReceiptTaskContact.Presenter presenter) {
        this.presenter=(ReceiptPresenter)presenter;
    }

    private void setupRecyclerView() {

        // Delete 버튼을 클릭하는 경우
        swipeController = new SwipeController(new SwipeControllerActions() {
            @Override
            public void onRightClicked(int position) {
                // DB에서 데이터 삭제

                showDeleteCheckDialog(position);


            }
        });

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeController);
        itemTouchhelper.attachToRecyclerView(recyclerView);

        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                swipeController.onDraw(c);
            }
        });
    }

    class Period{
        private Button oneMonth,oneYear,setting;
        private ImageButton startdate_btn,enddate_btn;  // 시작날짜와 마지막날짜 설정가능한 이미지버튼
        Calendar calendar = Calendar.getInstance();
        private int fromYear,fromMonth,fromDate; // 시작연도,월,날짜를 저장하는 변수
        private int toYear=calendar.get(Calendar.YEAR);
        private int toMonth=calendar.get(Calendar.MONTH)+1;
        private int toDate=calendar.get(Calendar.DATE);

        Period(){
            startdate_btn=view.findViewById(R.id.startdate_btn);
            startdate_btn.setEnabled(false);
            startdate_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    createDatePicker((ImageButton)view);
                }
            });
            enddate_btn=view.findViewById(R.id.enddate_btn);
            enddate_btn.setEnabled(false);
            enddate_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    createDatePicker((ImageButton)view);
                }
            });
            from=view.findViewById(R.id.from);
            to=view.findViewById(R.id.to);
            oneMonth=view.findViewById(R.id.oneMonth);
            oneMonth.setBackground(getResources().getDrawable(R.drawable.btn_design));
            oneMonth.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startdate_btn.setImageResource(R.drawable.calendar3);
                    enddate_btn.setImageResource(R.drawable.calendar3);
                    oneMonth.setBackground(getResources().getDrawable(R.drawable.btn_design));
                    oneYear.setBackgroundColor(Color.parseColor("#00ffffff"));
                    setting.setBackgroundColor(Color.parseColor("#00ffffff"));

                    startdate_btn.setEnabled(false);
                    enddate_btn.setEnabled(false);
                    calendar.clear();
                    calendar = Calendar.getInstance();
                    calendar.add(Calendar.MONTH,-1);
                    fromYear=calendar.get(Calendar.YEAR);
                    fromMonth=calendar.get(Calendar.MONTH)+1;
                    fromDate=calendar.get(Calendar.DATE);
                    from.setText(fromYear+"-"+String.format("%02d", fromMonth)+"-"+String.format("%02d", fromDate));
                }
            });
            oneYear=view.findViewById(R.id.oneYear);
            oneYear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startdate_btn.setImageResource(R.drawable.calendar3);
                    enddate_btn.setImageResource(R.drawable.calendar3);
                    oneYear.setBackground(getResources().getDrawable(R.drawable.btn_design));
                    oneMonth.setBackgroundColor(Color.parseColor("#00ffffff"));
                    setting.setBackgroundColor(Color.parseColor("#00ffffff"));
                    startdate_btn.setEnabled(false);
                    enddate_btn.setEnabled(false);
                    calendar.clear();
                    calendar = Calendar.getInstance();
                    calendar.add(Calendar.YEAR,-1);
                    fromYear=calendar.get(Calendar.YEAR);
                    fromMonth=calendar.get(Calendar.MONTH)+1;
                    fromDate=calendar.get(Calendar.DATE);
                    from.setText(fromYear+"-"+String.format("%02d", fromMonth)+"-"+String.format("%02d", fromDate));
                }
            });
            setting=view.findViewById(R.id.setting);
            setting.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startdate_btn.setImageResource(R.drawable.calendar2);
                    enddate_btn.setImageResource(R.drawable.calendar2);
                    setting.setBackground(getResources().getDrawable(R.drawable.btn_design));
                    oneYear.setBackgroundColor(Color.parseColor("#00ffffff"));
                    oneMonth.setBackgroundColor(Color.parseColor("#00ffffff"));
                    startdate_btn.setEnabled(true);
                    enddate_btn.setEnabled(true);
                }
            });
            to.setText(toYear+"-"+String.format("%02d", toMonth)+"-"+String.format("%02d", toDate));
            calendar = Calendar.getInstance();
            calendar.add(Calendar.MONTH,-1);
            fromYear=calendar.get(Calendar.YEAR);
            fromMonth=calendar.get(Calendar.MONTH)+1;
            fromDate=calendar.get(Calendar.DATE);
            from.setText(fromYear+"-"+String.format("%02d", fromMonth)+"-"+String.format("%02d", fromDate));
        }
        // datepicker를 생성하는 메소드
        public void createDatePicker(final ImageButton btn){
           int year=0,month=0,date=0;

            switch(btn.getId()){
                case R.id.startdate_btn:
                    year=fromYear;
                    month=fromMonth-1;
                    date=fromDate;
                    break;
                case R.id.enddate_btn:
                    year=toYear;
                    month=toMonth-1;
                    date=toDate;
                    break;
            }
            DatePickerDialog dialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                    switch (btn.getId()){
                        case R.id.startdate_btn:
                            fromYear=i;
                            fromMonth=i1+1;
                            fromDate=i2;
                            from.setText(fromYear+"-"+String.format("%02d", fromMonth)+"-"+String.format("%02d", fromDate));
                            break;
                        case R.id.enddate_btn:
                            toYear=i;
                            toMonth=i1+1;
                            toDate=i2;
                            to.setText(toYear+"-"+String.format("%02d", toMonth)+"-"+String.format("%02d", toDate));
                            break;
                    }
                }
            }, year, month, date);

            dialog.show();
        }
        public String getStartDate(){ return from.getText().toString(); }
        public String getEndDate(){ return to.getText().toString(); }

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

    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(ReceiptContent.DummyItem item);
    }
}
