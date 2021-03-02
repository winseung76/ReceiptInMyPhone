package com.bukcoja.seung.receipt_inmyhand.Presenter;

import android.content.Context;

import com.bukcoja.seung.receipt_inmyhand.Callback.GroupCallback;
import com.bukcoja.seung.receipt_inmyhand.Callback.ReceiptCallback;
import com.bukcoja.seung.receipt_inmyhand.Data.GroupRepository;
import com.bukcoja.seung.receipt_inmyhand.Data.ReceiptRepository;
import com.bukcoja.seung.receipt_inmyhand.VO.GroupListVO;
import com.bukcoja.seung.receipt_inmyhand.VO.GroupMemberListVO;
import com.bukcoja.seung.receipt_inmyhand.VO.GroupVO;
import com.bukcoja.seung.receipt_inmyhand.VO.ReceiptVO;

import java.util.List;

public class ReceiptPresenter implements ReceiptTaskContact.Presenter, ReceiptCallback, GroupCallback {

    private ReceiptTaskContact.View view;
    private ReceiptRepository repository;
    private GroupRepository groupRepository;
    private Context context;

    public ReceiptPresenter(Context context,ReceiptTaskContact.View view){
        this.context=context;
        this.view=view;
        view.setPresenter(this);
    }
    @Override
    public void start() {

    }

    public void getReceiptData(String userid,String startdate,String enddate,int groupid,String name){
        repository=new ReceiptRepository(this);
        repository.getData(context,userid,startdate,enddate,groupid);
        //repository.getDataFromCenter(userid);
        //repository.getDataFromLocal();
    }

    public void getReceiptCount(){
        repository=new ReceiptRepository(this);
        repository.getDataCount(context);
    }
    public void deleteReceipt(List<Integer> receiptids, String userid, int groupid){
        repository=new ReceiptRepository(this);
        repository.deleteData(context,receiptids,userid,groupid);
    }
    public void moveReceiptFromLocalToCenter(Context context,String userid,int groupid){
        repository=new ReceiptRepository(this);
        repository.moveDataFromLocalToCenter(context,0,userid,groupid);
    }

    public void shareReceipt(Context context,String userid,int groupid,int receiptid){
        repository=new ReceiptRepository(this);
        repository.shareDataToCenter(context,userid,groupid,receiptid);
    }

    public void getGroupMemberInfo(Context context,int groupid){
        groupRepository=new GroupRepository(this);
        groupRepository.getMembers(context,groupid);
    }

    @Override
    public void callbackReceiptData(ReceiptVO list) {
        view.setReceiptList(list);
    }



    @Override
    public void callbackReceiptCount(int count) {
        view.setReceiptCount(count);
    }

    @Override
    public void callbackShareResult(int result) {
        view.checkReceiptShareResult(result);
    }

    @Override
    public void callbackResult(String result) {

    }

    @Override
    public void callbackGroupList(GroupListVO groupVO) {

    }

    @Override
    public void callbackSaveResult(boolean success) {

    }

    @Override
    public void callbackExistResult(GroupVO groupVO) {

    }

    @Override
    public void callbackRemoveResult(boolean success) {

    }

    @Override
    public void callbackGetMembersResult(GroupMemberListVO listVO) {
        view.setGroupMembers(listVO);
    }
}
