package com.bukcoja.seung.receipt_inmyhand.Callback;

import com.bukcoja.seung.receipt_inmyhand.VO.ChartDetailVO;
import com.bukcoja.seung.receipt_inmyhand.VO.ChartListVO;
import com.bukcoja.seung.receipt_inmyhand.VO.UpperLimitVO;

public interface ChartDataCallback {

    void callbackPieChartData(ChartListVO chartListVO);

    void callbackListData(ChartListVO chartListVO);

    void callbackDetailData(ChartDetailVO chartDetailVO);

    void callbackUpperLimitData(UpperLimitVO upperLimitVO);

    void callbackSaveLimitResult(int result);
}
