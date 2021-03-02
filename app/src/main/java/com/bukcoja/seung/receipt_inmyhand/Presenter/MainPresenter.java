package com.bukcoja.seung.receipt_inmyhand.Presenter;

import android.content.Context;
import android.util.Log;

import com.bukcoja.seung.receipt_inmyhand.Callback.GroupCallback;
import com.bukcoja.seung.receipt_inmyhand.Callback.ImageFileCallback;
import com.bukcoja.seung.receipt_inmyhand.Callback.UserCallback;
import com.bukcoja.seung.receipt_inmyhand.Data.CategoryRepository;
import com.bukcoja.seung.receipt_inmyhand.Data.GroupRepository;
import com.bukcoja.seung.receipt_inmyhand.Data.ImageFileRepository;
import com.bukcoja.seung.receipt_inmyhand.Data.ReceiptRepository;
import com.bukcoja.seung.receipt_inmyhand.Data.UserPointRepository;
import com.bukcoja.seung.receipt_inmyhand.Data.UserRepository;
import com.bukcoja.seung.receipt_inmyhand.DigitalReceipt.ReceiptPayloadItem.BasicInfo;
import com.bukcoja.seung.receipt_inmyhand.DigitalReceipt.ReceiptPayloadItem.Tag0x21;
import com.bukcoja.seung.receipt_inmyhand.VO.GroupListVO;
import com.bukcoja.seung.receipt_inmyhand.VO.GroupMemberListVO;
import com.bukcoja.seung.receipt_inmyhand.VO.GroupVO;

public class MainPresenter implements MainTaskContact.Presenter, GroupCallback, UserCallback , ImageFileCallback {

    private MainTaskContact.View view;
    private GroupRepository repository;
    private CategoryRepository categoryRepository;
    private ReceiptRepository receiptRepository;
    private UserPointRepository userPointRepository;
    private UserRepository userRepository;
    private ImageFileRepository imageFileRepository;

    public MainPresenter(MainTaskContact.View view){
        this.view=view;
        view.setPresenter(this);
    }

    @Override
    public void start() {

    }

    public void registerGroup(Context context,String id,int groupid,String group){
        repository=new GroupRepository(this);
        repository.register(context,id,groupid,group);
    }

    public void loadGroup(Context context,String id){
        repository=new GroupRepository(this);
        repository.load(context,id);
    }
    public void updateCategory(Context context){
        categoryRepository=new CategoryRepository(this);
        categoryRepository.getCtgData(context);
    }

    public void updateToken(Context context,String userid,String token){
        userRepository=new UserRepository(this);
        userRepository.updateToken(context,userid,token);
    }


    public void saveReceipt(Context context, int receiptNum, String userid, int groupid,  BasicInfo item, Tag0x21 tag0x21){
        receiptRepository=new ReceiptRepository(this);

        //receiptRepository.saveDataToLocal(context,receiptNum,userid,groupid,item,tag0x21);
        receiptRepository.saveDataToCenter(context,receiptNum,userid,groupid,item,tag0x21);
    }


    public void updateUserPoint(Context context,int receiptid,int groupid, String id,String date,int usedpoint){
        userPointRepository=new UserPointRepository();
        userPointRepository.updateData(context,receiptid,groupid,id,date,usedpoint);
    }
    public void isGroupExist(Context context,String userid,int groupid){
        repository=new GroupRepository(this);
        repository.dataExistCheck(context,userid,groupid);
    }

    public void removeGroup(Context context,String userid,int groupid){
        repository=new GroupRepository(this);
        repository.remove(context,userid,groupid);
    }

    public void uploadImage(Context context,String uploadImage,String encodedImage,String userid){
        imageFileRepository=new ImageFileRepository(this);
        imageFileRepository.uploadReceiptFile(context,uploadImage,encodedImage,userid);
    }

    public void uploadProfileImage(Context context,String uploadImage,String encodedImage,String userid){
        imageFileRepository=new ImageFileRepository(this);
        imageFileRepository.uploadProfile(context,uploadImage,encodedImage,userid);
    }
    public void getProfileImage(Context context,String userid){
        imageFileRepository=new ImageFileRepository(this);
        imageFileRepository.getProfile(context,userid);
    }

    public void changeDefaultImage(Context context,String userid){
        imageFileRepository=new ImageFileRepository(this);
        imageFileRepository.changeDefaultImage(context,userid);
    }


    @Override
    public void callbackResult(String result) {
        view.showGroupResultDialog(result);
    }

    @Override
    public void callbackGroupList(GroupListVO groupVO) {
        view.setUserGroup(groupVO);
    }

    @Override
    public void callbackSaveResult(boolean success) {
        view.setReceiptSaveResult(success);
    }

    @Override
    public void callbackExistResult(GroupVO groupVO) {
        view.getGroupExistResult(groupVO);
    }

    @Override
    public void callbackRemoveResult(boolean success) {
        view.getGroupRemoveResult(success);
    }

    @Override
    public void callbackRegisterResult(int result) {

    }

    @Override
    public void callbackGetMembersResult(GroupMemberListVO listVO) {

    }

    @Override
    public void callbackCheckResult(int result) {

    }

    @Override
    public void callbackRemoveResult(String result) {

    }

    @Override
    public void callbackImageUploadResult(int result,int flag) {

        // 영수증 사진을 업로드한 경우
        if(flag==0) {
            if (result == 1)
                view.getImageUploadResult(true);
            else
                view.getGroupRemoveResult(false);
        }
        // 프로필 사진을 업로드한 경우
        else if(flag==1){
            if (result == 1)
                view.getProfileUploadResult(true);
            else
                view.getProfileUploadResult(false);
        }
    }

    @Override
    public void callbackGetProfileImage(String imageUri) {
        Log.e("callbackGetProfileImage",imageUri);
        view.setProfileImage(imageUri);
    }

    @Override
    public void callbackDefaultProfile(int result) {
        if(result==1)
            view.setDefaultProfileImage(true);
        else
            view.setDefaultProfileImage(false);
    }
}
