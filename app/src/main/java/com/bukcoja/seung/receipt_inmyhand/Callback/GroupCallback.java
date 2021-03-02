package com.bukcoja.seung.receipt_inmyhand.Callback;

import com.bukcoja.seung.receipt_inmyhand.VO.GroupListVO;
import com.bukcoja.seung.receipt_inmyhand.VO.GroupMemberListVO;
import com.bukcoja.seung.receipt_inmyhand.VO.GroupVO;

public interface GroupCallback {

    void callbackResult(String result);
    void callbackGroupList(GroupListVO groupVO);
    void callbackSaveResult(boolean success);
    void callbackExistResult(GroupVO groupVO);
    void callbackRemoveResult(boolean success);
    void callbackGetMembersResult(GroupMemberListVO listVO);

}
