package com.bukcoja.seung.receipt_inmyhand.Callback;

public interface UserCallback {

    void callbackRegisterResult(int result);
    void callbackCheckResult(int result);
    void callbackRemoveResult(String result);
}
