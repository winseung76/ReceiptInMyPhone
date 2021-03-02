package com.bukcoja.seung.receipt_inmyhand.View;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;

import com.bukcoja.seung.receipt_inmyhand.DBHelper;
import com.bukcoja.seung.receipt_inmyhand.Presenter.LoginPresenter;
import com.bukcoja.seung.receipt_inmyhand.Presenter.LoginTaskContact;
import com.bukcoja.seung.receipt_inmyhand.R;
import com.bukcoja.seung.receipt_inmyhand.UserInfo;
import com.bukcoja.seung.receipt_inmyhand.VO.LoginVO;

public class IntroActivity extends AppCompatActivity implements LoginTaskContact.View {

    private LoginPresenter presenter;
    private UserInfo userInfo;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        userInfo=(UserInfo)getApplication();
        prefs = getSharedPreferences("isFirst", MODE_PRIVATE);
        presenter=new LoginPresenter(this);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                // 먼저 자동 로그인 상태인지 확인한다
                presenter.checkUserState(getApplicationContext());
                checkFirstRun();

            }
        },3000);

    }
    // 앱의 첫 실행인지를 확인하기 위해서
    public void checkFirstRun(){
        boolean first = prefs.getBoolean("isFirst", false);

        if(first==false){
            Log.d("Is first Time?", "first");
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("isFirst",true);
            editor.commit();
            //앱 최초 실행시 하고 싶은 작업
            DBHelper dbHelper=new DBHelper(getApplicationContext(),"rimp.db",null,3);
            dbHelper.onCreate(dbHelper.getWritableDatabase());
        }else{
            Log.d("Is first Time?", "not first");
        }

    }
    @Override
    public void getAutoLoginState(String id) {
        // 자동 로그인 상태가 아니면 LoginActivity로 이동
        if(id==null){
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        }
        // 자동 로그인 상태면 해당 아이디로 사용자 정보 읽어오기
        else{
            presenter.getUserInfo(getApplicationContext(),id,null,1);
        }
    }

    @Override
    public void setUserInfo(LoginVO result) {
        try {
            Log.e("setUserInfo",String.valueOf(result));
            // 자동로그인 상태의 사용자가 있는 경우
            if (result!=null) {

                // 로그인한 사용자 정보를 저장
                userInfo.setId(result.getId());
                userInfo.setName(result.getName());
                userInfo.setEmail(result.getEmail());
                userInfo.setGroup(1);
                userInfo.setGroupname("나의 영수증");

                presenter.update(getApplicationContext(),1, result.getId());

                // IntroActivity에서 바로 MainActivitiy로 이동
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void setPresenter(LoginTaskContact.Presenter presenter) {
        this.presenter=(LoginPresenter)presenter;
    }
}
