package com.bukcoja.seung.receipt_inmyhand.View;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bukcoja.seung.receipt_inmyhand.Presenter.SettingPresenter;
import com.bukcoja.seung.receipt_inmyhand.Presenter.SettingTaskContact;
import com.bukcoja.seung.receipt_inmyhand.R;
import com.bukcoja.seung.receipt_inmyhand.UserInfo;

public class SettingActivity extends AppCompatActivity implements SettingTaskContact.View {

    private Toolbar toolbar;
    private LinearLayout rimp_setting;
    private SettingPresenter presenter;
    private UserInfo userInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        userInfo=(UserInfo)getApplication();
        presenter=new SettingPresenter(this);

        initView();


    }
    public void initView(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setBackground(getResources().getDrawable(R.drawable.linear_gradient));
        toolbar.setTitle("설정");
        setSupportActionBar(toolbar);


        rimp_setting=findViewById(R.id.rimp_setting);
        rimp_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createWithdrawalDialog();
            }
        });
    }

    @Override
    public void setPresenter(SettingTaskContact.Presenter presenter) {
        this.presenter=(SettingPresenter)presenter;
    }

    @Override
    public void getRemoveUserResult(boolean result) {

        // 회원 탈퇴가 성공적으로 이루어진 경우
        if(result){
            // 로그인 화면으로 이동
            finish();
            Intent intent=new Intent(getApplicationContext(),LoginActivity.class);
            startActivity(intent);
        }
        else
            createAlertDialog();
    }

    public void createWithdrawalDialog(){
        AlertDialog.Builder builder=new AlertDialog.Builder(SettingActivity.this);
        final View dialogView=View.inflate(getApplicationContext(), R.layout.dialog_with_two_button, null);

        TextView msg=dialogView.findViewById(R.id.msg);
        TextView yes=dialogView.findViewById(R.id.yes);
        TextView no=dialogView.findViewById(R.id.no);

        builder.setView(dialogView);

        final AlertDialog dialog=builder.create();
        dialog.show();

        msg.setText("정말 탈퇴하시겠습니까?\n탈퇴 시 개인정보는 모두 삭제되며 복구하실 수 없습니다.");
        yes.setText("확인");
        no.setText("취소");
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.removeUserInfo(getApplicationContext(),userInfo.getId());
                dialog.dismiss();
            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }
    public void createAlertDialog(){
        AlertDialog.Builder builder=new AlertDialog.Builder(SettingActivity.this);
        final View dialogView=View.inflate(getApplicationContext(), R.layout.dialog_with_two_button, null);

        TextView msg=dialogView.findViewById(R.id.msg);
        TextView ok=dialogView.findViewById(R.id.yes);

        builder.setView(dialogView);

        final AlertDialog dialog=builder.create();
        dialog.show();

        msg.setText("문제가 발생하였습니다.\n다시 시도하시기 바랍니다.");
        ok.setText("확인");
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

    }
}
