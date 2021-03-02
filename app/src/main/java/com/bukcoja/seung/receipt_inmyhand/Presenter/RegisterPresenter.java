package com.bukcoja.seung.receipt_inmyhand.Presenter;

import android.content.Context;

import com.bukcoja.seung.receipt_inmyhand.Callback.UserCallback;
import com.bukcoja.seung.receipt_inmyhand.Data.UserRepository;

public class RegisterPresenter implements RegisterTaskContact.Presenter, UserCallback {

    private RegisterTaskContact.View view;
    private UserRepository repository;

    public RegisterPresenter(RegisterTaskContact.View view){
        this.view=view;
        view.setPresenter(this);
    }

    @Override
    public void start() {

    }

    public void registerUser(Context context,String id,String password,String name,String phone){
        repository=new UserRepository(this);
        repository.sendData(context,id,password,name,phone);
    }

    public void checkIDOverlay(Context context,String id){
        repository=new UserRepository(this);
        repository.getResult(context,id);
    }
    @Override
    public void callbackRegisterResult(int result) {

        view.registerUserResult(result);
    }

    @Override
    public void callbackCheckResult(int result) {
        view.checkIDResult(result);
    }

    @Override
    public void callbackRemoveResult(String result) {

    }
}
