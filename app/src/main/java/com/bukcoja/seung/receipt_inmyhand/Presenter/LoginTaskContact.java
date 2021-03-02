package com.bukcoja.seung.receipt_inmyhand.Presenter;

import com.bukcoja.seung.receipt_inmyhand.BasePresenter;
import com.bukcoja.seung.receipt_inmyhand.BaseView;
import com.bukcoja.seung.receipt_inmyhand.VO.LoginVO;

public interface LoginTaskContact {

    interface Presenter extends BasePresenter{

    }
    interface View extends BaseView<Presenter>{
        void setUserInfo(LoginVO result);
        // 자동 로그인 상태여부 확인
        void getAutoLoginState(String id);
    }
}
