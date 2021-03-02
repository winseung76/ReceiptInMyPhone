package com.bukcoja.seung.receipt_inmyhand.Presenter;

import com.bukcoja.seung.receipt_inmyhand.BasePresenter;
import com.bukcoja.seung.receipt_inmyhand.BaseView;
import com.bukcoja.seung.receipt_inmyhand.VO.ChartDetailVO;
import com.bukcoja.seung.receipt_inmyhand.VO.ChartListVO;
import com.bukcoja.seung.receipt_inmyhand.VO.UpperLimitVO;

public interface ChartTaskContact {

    interface Presenter extends BasePresenter{

    }

    interface View extends BaseView<Presenter>{
        void setPieChartData(ChartListVO chartListVO);
        void setList(ChartListVO chartListVO);
        void setDetailView(ChartDetailVO chartDetailVO);
        void setUpperLimit(UpperLimitVO upperLimitVO);
        void getSaveLimitResult(int result);
    }
}
