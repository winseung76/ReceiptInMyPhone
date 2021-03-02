package com.bukcoja.seung.receipt_inmyhand.Callback;

import com.bukcoja.seung.receipt_inmyhand.VO.ReceiptVO;

public interface ReceiptCallback {

    //void callbackReceiptData(int regnum, HashMap<String,String> map, HashMap<String, ArrayList<String>> prods);
    void callbackReceiptData(ReceiptVO list);
    //void callbackTotalPrice(int totalprice);
    void callbackReceiptCount(int count);
    void callbackShareResult(int result);
}
