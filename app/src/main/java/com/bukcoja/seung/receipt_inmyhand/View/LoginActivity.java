package com.bukcoja.seung.receipt_inmyhand.View;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bukcoja.seung.receipt_inmyhand.Presenter.LoginPresenter;
import com.bukcoja.seung.receipt_inmyhand.Presenter.LoginTaskContact;
import com.bukcoja.seung.receipt_inmyhand.R;
import com.bukcoja.seung.receipt_inmyhand.UserInfo;
import com.bukcoja.seung.receipt_inmyhand.VO.LoginVO;

public class LoginActivity extends AppCompatActivity implements View.OnFocusChangeListener, LoginTaskContact.View {

    private Button login;
    private TextView signUp;
    private TextView id,password;
    private TextView alertmsg;
    private UserInfo userInfo;
    private LoginPresenter presenter;

    @Override
    public void onFocusChange(View v, boolean b) {
        if(b){
            ((EditText)v).setHintTextColor(Color.parseColor("#ffffff"));
        }
        else{
            ((EditText)v).setHighlightColor(Color.parseColor("#ffffff"));
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e("LoginActivity","onStart");
        //presenter.checkUserState(getApplicationContext());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("LoginActivity","onCreate");
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_login);

        userInfo=(UserInfo)getApplication();
        initView();
        presenter=new LoginPresenter(this);

        Intent intent=getIntent();

        // 사용자가 로그아웃해서 MainActivity에서 인텐트로 넘어오는 경우
        if(intent!=null){
            Log.e("LoginActivity","intent");
            String id=intent.getStringExtra("id");
            int state=intent.getIntExtra("state",0);
            presenter.update(getApplicationContext(),state,id);
        }

        signUp.setText(Html.fromHtml("<u>" + "가입하기" + "</u>"));
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(intent);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                presenter.getUserInfo(getApplicationContext(),id.getText().toString(),password.getText().toString(),0);

            }
        });
    }

    public void initView(){
        alertmsg=findViewById(R.id.alertmsg);
        login=findViewById(R.id.login);
        signUp=findViewById(R.id.signUp);
        id=findViewById(R.id.id);
        password=findViewById(R.id.pwd);
        id.setOnFocusChangeListener(this);
        password.setOnFocusChangeListener(this);
    }

    @Override
    public void getAutoLoginState(String id) {

    }

    @Override
    public void setUserInfo(LoginVO result) {

        try {
            if (result!=null) {

                // 로그인한 사용자 정보를 저장
                userInfo.setId(result.getId());
                userInfo.setName(result.getName());
                userInfo.setEmail(result.getEmail());
                userInfo.setGroup(1);
                userInfo.setGroupname("나의 영수증");

                // 로그인이 되었으므로 자동 로그인 상태로 DB의 내용을 변경
                presenter.update(getApplicationContext(),1, result.getId());

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                alertmsg.setVisibility(View.VISIBLE);
                alertmsg.setText("아이디 혹은 비밀번호가 일치하지 않습니다.");
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
