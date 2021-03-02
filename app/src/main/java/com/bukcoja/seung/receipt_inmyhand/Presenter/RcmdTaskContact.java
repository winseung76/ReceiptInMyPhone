package com.bukcoja.seung.receipt_inmyhand.Presenter;

import com.bukcoja.seung.receipt_inmyhand.BasePresenter;
import com.bukcoja.seung.receipt_inmyhand.BaseView;
import com.bukcoja.seung.receipt_inmyhand.VO.DateVO;
import com.bukcoja.seung.receipt_inmyhand.VO.ServiceVO;

public interface RcmdTaskContact {

    interface Presenter extends BasePresenter{

    }
    interface View extends BaseView<Presenter>{
        void setRcmdData(ServiceVO serviceVO);
        void setUpdateDate(DateVO dateVO);
    }
}
