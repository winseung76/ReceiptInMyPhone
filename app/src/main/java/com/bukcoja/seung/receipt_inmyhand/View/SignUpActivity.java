package com.bukcoja.seung.receipt_inmyhand.View;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bukcoja.seung.receipt_inmyhand.Presenter.RegisterPresenter;
import com.bukcoja.seung.receipt_inmyhand.Presenter.RegisterTaskContact;
import com.bukcoja.seung.receipt_inmyhand.R;

import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity implements View.OnFocusChangeListener, RegisterTaskContact.View {

    Drawable checkicon;
    EditText name,id,password,checkPassword,phone;
    Button checkId,signUp;
    FormChecker formChecker;
    RegisterPresenter presenter;
    TextView login;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        formChecker.cancel(true);
    }

    @Override
    public void onFocusChange(View v, boolean b) {
        if(b){
            v.setBackgroundResource(R.drawable.edittext_touchmode);
        }
        else{
            v.setBackgroundResource(R.drawable.buttonlayout);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        presenter=new RegisterPresenter(this);
        formChecker=new FormChecker();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            formChecker.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, null);
        }
        else
            formChecker.execute();

        initView();

    }

    public void initView(){
        name=findViewById(R.id.name);
        id=findViewById(R.id.id);
        checkId=findViewById(R.id.checkId);
        password=findViewById(R.id.password);
        checkPassword=findViewById(R.id.checkPassword);
        phone=findViewById(R.id.phone);
        signUp=findViewById(R.id.signUp);
        login=findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
            }
        });

        name.setOnFocusChangeListener(this);
        id.setOnFocusChangeListener(this);
        password.setOnFocusChangeListener(this);
        checkPassword.setOnFocusChangeListener(this);
        phone.setOnFocusChangeListener(this);
        checkicon = getApplicationContext().getResources().getDrawable( R.drawable.check4 );
        id.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                //이미 중복체크 한경우
                if(formChecker.getId_ok()){
                    id.setCompoundDrawablesWithIntrinsicBounds(null,null,null,null);
                }
            }
        });
        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if(name.getText().toString().length()>1) {
                    name.setCompoundDrawablesWithIntrinsicBounds(null, null, checkicon, null);
                    formChecker.setName_ok(true);
                }
                else {
                    name.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                    formChecker.setName_ok(false);
                }
            }
        });
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String pwd=password.getText().toString();
                String pwd2=checkPassword.getText().toString();
                if(pwd.length()>4){
                    password.setCompoundDrawablesWithIntrinsicBounds(null,null,checkicon,null);
                    //pwdhint.setVisibility(View.INVISIBLE);
                    formChecker.setPwd_ok(true);
                }
                else {
                    password.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                    //pwdhint.setVisibility(View.VISIBLE);
                    formChecker.setPwd_ok(false);
                }

                if(pwd.length()>4 && pwd2.equals(pwd)){
                    checkPassword.setCompoundDrawablesWithIntrinsicBounds(null,null,checkicon,null);
                    formChecker.setPwd2_ok(true);
                }
                else{
                    checkPassword.setCompoundDrawablesWithIntrinsicBounds(null,null,null,null);
                    formChecker.setPwd2_ok(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                String pwd=password.getText().toString();
                String pwd2=checkPassword.getText().toString();
                if(pwd.length()>4){
                    password.setCompoundDrawablesWithIntrinsicBounds(null,null,checkicon,null);
                    //pwdhint.setVisibility(View.INVISIBLE);
                    formChecker.setPwd_ok(true);
                }
                else {
                    password.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                    //pwdhint.setVisibility(View.VISIBLE);
                    formChecker.setPwd_ok(false);
                }

                if(pwd.length()>4 && pwd2.equals(pwd)){
                    checkPassword.setCompoundDrawablesWithIntrinsicBounds(null,null,checkicon,null);
                    formChecker.setPwd2_ok(true);
                }
                else{
                    checkPassword.setCompoundDrawablesWithIntrinsicBounds(null,null,null,null);
                    formChecker.setPwd2_ok(false);
                }
            }
        });
        checkPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String pwd=password.getText().toString();
                String pwd2= checkPassword.getText().toString();
                if(pwd.length()>4 && pwd2.equals(pwd)){
                    checkPassword.setCompoundDrawablesWithIntrinsicBounds(null,null,checkicon,null);
                    formChecker.setPwd2_ok(true);
                }
                else{
                    checkPassword.setCompoundDrawablesWithIntrinsicBounds(null,null,null,null);
                    formChecker.setPwd2_ok(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                String pwd=password.getText().toString();
                String pwd2= checkPassword.getText().toString();
                if(pwd.length()>4 && pwd2.equals(pwd)){
                    checkPassword.setCompoundDrawablesWithIntrinsicBounds(null,null,checkicon,null);
                }
                else{
                    checkPassword.setCompoundDrawablesWithIntrinsicBounds(null,null,null,null);
                }
            }
        });
        /*
        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String email_str=email.getText().toString();
                if(android.util.Patterns.EMAIL_ADDRESS.matcher(email_str).matches()){
                    email.setCompoundDrawablesWithIntrinsicBounds(null,null,checkicon,null);
                    formChecker.setEmail_ok(true);
                }
                else{
                    email.setCompoundDrawablesWithIntrinsicBounds(null,null,null,null);
                    formChecker.setEmail_ok(false);
                }

            }
        });
        */
        phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                String phone_num=phone.getText().toString();
                if(Pattern.matches("(01[016789])(\\d{3,4})(\\d{4})", phone_num)){
                    phone.setCompoundDrawablesWithIntrinsicBounds(null,null,checkicon,null);
                    formChecker.setPhone_ok(true);
                }
                else {
                    phone.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                    formChecker.setPhone_ok(false);
                }
            }
        });
        checkId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                presenter.checkIDOverlay(getApplicationContext(),id.getText().toString());
                /*
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    new MemberIdChecker(id.getText().toString()).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, null);
                }
                else
                    new MemberIdChecker(id.getText().toString()).execute();
                    */

            }
        });
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                formChecker.cancel(true);

                String userid=id.getText().toString();
                String pwd=password.getText().toString();
                String username=name.getText().toString();
                String phonenum=phone.getText().toString();
                //String useremail=email.getText().toString();

                presenter.registerUser(getApplicationContext(),userid,pwd,username,phonenum);

            }
        });

    }
    @Override
    public void registerUserResult(int result) {
        if(result==1){
            showRegisterOKDialog();
        }
        else if(result==0){
            showRegisterFLDialog();
        }
    }

    @Override
    public void checkIDResult(int result) {
        // 아이디가 중복되지 않은 경우
        if(result==1){
            // 아이디 입력칸이 빈칸일 경우
            if(id.getText().toString().equals("")){
                showAskDialog();
                id.setCompoundDrawablesWithIntrinsicBounds(null,null,null,null);
                formChecker.setId_ok(false);
            }
            else {
                showOKDialog();
                //id.setText(idstr);
                id.setCompoundDrawablesWithIntrinsicBounds(null, null, checkicon, null);
                formChecker.setId_ok(true);
            }
        }
        // 중복된 아이디가 있는 경우
        else {
            showNotOKDialog();
            id.setCompoundDrawablesWithIntrinsicBounds(null,null,null,null);
            formChecker.setId_ok(false);
        }
    }

    @Override
    public void setPresenter(RegisterTaskContact.Presenter presenter) {
        this.presenter=(RegisterPresenter)presenter;
    }
    public void showRegisterOKDialog(){
        AlertDialog.Builder builder=new AlertDialog.Builder(SignUpActivity.this);
        builder.setTitle("Sign Up");
        builder.setMessage("가입이 완료되었습니다.");
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Intent intent=new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });
        AlertDialog dialog=builder.create();
        dialog.show();
    }
    public void showRegisterFLDialog(){
        AlertDialog.Builder builder=new AlertDialog.Builder(SignUpActivity.this);
        builder.setTitle("Sign Up");
        builder.setMessage("가입에 실패하였습니다.다시 시도해주세요.");
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog=builder.create();
        dialog.show();
    }
    public void showNotOKDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
        builder.setTitle("아이디 중복 체크");
        builder.setMessage("이미 존재하는 아이디입니다.");
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
    public void showAskDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
        builder.setTitle("아이디 중복 체크");
        builder.setMessage("아이디를 입력해주세요!");
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
    public void showOKDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
        builder.setTitle("아이디 중복 체크");
        builder.setMessage("사용 가능한 아이디입니다.");
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    class FormChecker extends AsyncTask<Void,Void,Void>{
        private boolean id_ok=false,pwd_ok=false,pwd2_ok=false,name_ok=false,email_ok=false,phone_ok=false;

        @Override
        protected Void doInBackground(Void... voids) {

            while(true){
                try {
                    if(isCancelled())break;
                    publishProgress();
                    Thread.sleep(2000);
                }catch (Exception e){

                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            if(isPermission()){
                signUp.setBackground(getResources().getDrawable(R.drawable.linear_gradient));
                signUp.setEnabled(true);
            }
            else {
                signUp.setBackgroundColor(Color.parseColor("#A6A6A6"));
                signUp.setEnabled(false);
            }
        }

        public boolean isPermission(){

            return id_ok && pwd_ok && pwd2_ok && name_ok && phone_ok;
        }

        public void setId_ok(boolean id_ok) {
            this.id_ok = id_ok;
        }
        public boolean getId_ok(){return this.id_ok;}

        public void setPwd_ok(boolean pwd_ok) {
            this.pwd_ok = pwd_ok;
        }

        public void setPwd2_ok(boolean pwd2_ok) {
            this.pwd2_ok = pwd2_ok;
        }

        public void setName_ok(boolean name_ok) {
            this.name_ok = name_ok;
        }

        public void setEmail_ok(boolean email_ok) {
            this.email_ok = email_ok;
        }

        public void setPhone_ok(boolean phone_ok) {
            this.phone_ok = phone_ok;
        }
    }
}
