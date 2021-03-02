package com.bukcoja.seung.receipt_inmyhand.Presenter;

import android.content.Context;

import com.bukcoja.seung.receipt_inmyhand.Data.LoginRepository;
import com.bukcoja.seung.receipt_inmyhand.Callback.LoginCallback;
import com.bukcoja.seung.receipt_inmyhand.VO.LoginVO;

public class LoginPresenter implements LoginTaskContact.Presenter, LoginCallback {

    private LoginTaskContact.View view;
    private LoginRepository repository;

    public LoginPresenter(LoginTaskContact.View view){
        this.view=view;
    }


    // 사용자의 자동 로그인 상태를 업데이트
    public void update(Context context, int state, String id){
        repository=new LoginRepository(this);
        repository.updateUserState(context,state,id);
    }

    public void checkUserState(Context context){
        repository=new LoginRepository(this);
        repository.getState(context);
    }
    // 로그인 시 사용자 정보를 불러오는 메서드
    // 자동로그인 상태에서는 아이디 정보만을 가지고 사용자 정보를 가져옴
    public void getUserInfo(Context context,String id,String password,int state){
        repository=new LoginRepository(this);
        repository.getData(context,id,password,state);
    }


    @Override
    public void start() {

    }

    @Override
    public void callbackUserInfo(LoginVO result) {
        view.setUserInfo(result);
    }

    @Override
    public void callbackAutoLogin(String id) {
        view.getAutoLoginState(id);
    }

}
