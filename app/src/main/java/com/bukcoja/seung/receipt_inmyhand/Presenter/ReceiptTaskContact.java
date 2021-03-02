package com.bukcoja.seung.receipt_inmyhand.Presenter;

import com.bukcoja.seung.receipt_inmyhand.BasePresenter;
import com.bukcoja.seung.receipt_inmyhand.BaseView;
import com.bukcoja.seung.receipt_inmyhand.VO.GroupMemberListVO;
import com.bukcoja.seung.receipt_inmyhand.VO.ReceiptVO;

public interface ReceiptTaskContact {

    interface Presenter extends BasePresenter{

    }
    interface View extends BaseView<Presenter>{
        //void setReceiptList(int regnum, HashMap<String,String> map,HashMap<String, ArrayList<String>> prods);
        void setReceiptList(ReceiptVO list);
        //void setTotalPrice(int totalprice);
        void setReceiptCount(int count);

        void checkReceiptShareResult(int result);
        void setGroupMembers(GroupMemberListVO listVO);

    }
}
