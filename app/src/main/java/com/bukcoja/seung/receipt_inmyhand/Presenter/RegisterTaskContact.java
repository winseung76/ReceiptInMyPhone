package com.bukcoja.seung.receipt_inmyhand.Presenter;

import com.bukcoja.seung.receipt_inmyhand.BasePresenter;
import com.bukcoja.seung.receipt_inmyhand.BaseView;

public interface RegisterTaskContact {

    interface Presenter extends BasePresenter{

    }
    interface View extends BaseView<Presenter>{

        void registerUserResult(int result);
        void checkIDResult(int result);

    }
}
