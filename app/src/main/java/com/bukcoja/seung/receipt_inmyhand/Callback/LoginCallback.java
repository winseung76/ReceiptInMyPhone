package com.bukcoja.seung.receipt_inmyhand.Callback;

import com.bukcoja.seung.receipt_inmyhand.VO.LoginVO;

public interface LoginCallback {

    void callbackUserInfo(LoginVO result);
    void callbackAutoLogin(String id);

}
