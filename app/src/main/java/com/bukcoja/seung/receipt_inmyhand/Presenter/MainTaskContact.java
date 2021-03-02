package com.bukcoja.seung.receipt_inmyhand.Presenter;

import com.bukcoja.seung.receipt_inmyhand.BasePresenter;
import com.bukcoja.seung.receipt_inmyhand.BaseView;
import com.bukcoja.seung.receipt_inmyhand.VO.GroupListVO;
import com.bukcoja.seung.receipt_inmyhand.VO.GroupVO;

public interface MainTaskContact {

    interface Presenter extends BasePresenter{

    }
    interface View extends BaseView<Presenter>{
        void showGroupResultDialog(String result);
        void setUserGroup(GroupListVO groupVO);
        void setReceiptSaveResult(boolean success);
        void  getGroupExistResult(GroupVO groupVO);
        void getGroupRemoveResult(boolean success);
        void getImageUploadResult(boolean success);
        void getProfileUploadResult(boolean success);
        void setProfileImage(String imageUri);
        void setDefaultProfileImage(boolean success);

    }
}
