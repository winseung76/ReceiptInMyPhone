package com.bukcoja.seung.receipt_inmyhand.Callback;

import com.bukcoja.seung.receipt_inmyhand.VO.DateVO;
import com.bukcoja.seung.receipt_inmyhand.VO.ServiceVO;

public interface RcmdCallback {

    void callbackRcmdData(ServiceVO serviceVO);
    void callbackUpdateDate(DateVO dateVO);
}
