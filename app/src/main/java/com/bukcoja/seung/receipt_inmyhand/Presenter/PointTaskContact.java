package com.bukcoja.seung.receipt_inmyhand.Presenter;

import com.bukcoja.seung.receipt_inmyhand.BasePresenter;
import com.bukcoja.seung.receipt_inmyhand.BaseView;
import com.bukcoja.seung.receipt_inmyhand.VO.PointListVO;
import com.bukcoja.seung.receipt_inmyhand.VO.PointVO;

public interface PointTaskContact {

    interface Presenter extends BasePresenter {

    }
    interface View extends BaseView<Presenter> {
        void showCircularProgress(PointVO pointVO);
        void showHistoryList(PointListVO data);
    }
}
