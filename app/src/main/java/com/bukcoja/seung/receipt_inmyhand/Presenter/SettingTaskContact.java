package com.bukcoja.seung.receipt_inmyhand.Presenter;

import com.bukcoja.seung.receipt_inmyhand.BasePresenter;
import com.bukcoja.seung.receipt_inmyhand.BaseView;

public interface SettingTaskContact {

    interface Presenter extends BasePresenter{

    }
    interface View extends BaseView<Presenter>{
        void getRemoveUserResult(boolean result);
    }
}
