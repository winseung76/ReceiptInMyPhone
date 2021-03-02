package com.bukcoja.seung.receipt_inmyhand.Callback;

import com.bukcoja.seung.receipt_inmyhand.VO.PointListVO;
import com.bukcoja.seung.receipt_inmyhand.VO.PointVO;

public interface DataCallback {

    void callbackUserPoint(PointVO pointVO);

    void callbackPointHistory(PointListVO list);
}
