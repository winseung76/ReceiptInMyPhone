package com.bukcoja.seung.receipt_inmyhand.Presenter;

import android.content.Context;

import com.bukcoja.seung.receipt_inmyhand.Callback.UserCallback;
import com.bukcoja.seung.receipt_inmyhand.Data.UserRepository;

public class SettingPresenter implements UserCallback {

    private SettingTaskContact.View view;
    private UserRepository repository;

    public SettingPresenter(SettingTaskContact.View view){
        this.view=view;
    }
    public void removeUserInfo(Context context,String userid){
        repository=new UserRepository(this);
        repository.removeData(context,userid);
    }

    @Override
    public void callbackRemoveResult(String result) {
        if(result.contains("1"))
            view.getRemoveUserResult(true);
        else
            view.getRemoveUserResult(false);
    }

    @Override
    public void callbackRegisterResult(int result) {

    }

    @Override
    public void callbackCheckResult(int result) {

    }


}
