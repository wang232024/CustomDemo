package com.example.customutil.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.customutil.R;

public class ResetpwdActivity extends Activity {
    private EditText mAccount;
    private EditText mPwd_old;
    private EditText mPwd_new;
    private EditText mPwdCheck;
    private Button mSureButton;
    private Button mCancelButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.resetpwd);

        mAccount = (EditText) findViewById(R.id.resetpwd_edit_name);
        mPwd_old = (EditText) findViewById(R.id.resetpwd_edit_pwd_old);
        mPwd_new = (EditText) findViewById(R.id.resetpwd_edit_pwd_new);
        mPwdCheck = (EditText) findViewById(R.id.resetpwd_edit_pwd_check);
        mSureButton = (Button) findViewById(R.id.resetpwd_btn_sure);
        mCancelButton = (Button) findViewById(R.id.resetpwd_btn_cancel);

        mSureButton.setOnClickListener(m_resetpwd_Listener);
        mCancelButton.setOnClickListener(m_resetpwd_Listener);
    }

    View.OnClickListener m_resetpwd_Listener = new View.OnClickListener() {
        public void onClick(View v) {
            int id = v.getId();
            if (id == R.id.resetpwd_btn_sure) {
                resetpwd_check();
            } else if (id == R.id.resetpwd_btn_cancel) {
                Intent intent_Resetpwd_to_Login = new Intent(ResetpwdActivity.this, LoginActivity.class);
                startActivity(intent_Resetpwd_to_Login);
                finish();
            }
        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent_Resetpwd_to_Login = new Intent(ResetpwdActivity.this, LoginActivity.class);
        startActivity(intent_Resetpwd_to_Login);
        finish();
    }

    public void resetpwd_check() {
        if (isUserNameAndPwdValid()) {
            String userName = mAccount.getText().toString().trim();
            String userPwd_old = mPwd_old.getText().toString().trim();
            String userPwd_new = mPwd_new.getText().toString().trim();
            String userPwdCheck = mPwdCheck.getText().toString().trim();

            if (userPwd_new.equals(userPwdCheck)) {
                resetPwdToServer(userName, userPwd_old, userPwd_new);
            } else {
                Toast.makeText(ResetpwdActivity.this, R.string.pwd_check_same, Toast.LENGTH_SHORT).show();
            }
        }
    }

    public boolean isUserNameAndPwdValid() {
        if (mAccount.getText().toString().trim().equals("")) {
            Toast.makeText(this, getString(R.string.account_empty), Toast.LENGTH_SHORT).show();
            return false;
        } else if (mPwd_old.getText().toString().trim().equals("")) {
            Toast.makeText(this, getString(R.string.pwd_empty), Toast.LENGTH_SHORT).show();
            return false;
        } else if (mPwd_new.getText().toString().trim().equals("")) {
            Toast.makeText(this, getString(R.string.pwd_new_empty), Toast.LENGTH_SHORT).show();
            return false;
        } else if (mPwdCheck.getText().toString().trim().equals("")) {
            Toast.makeText(this, getString(R.string.pwd_check_empty), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void resetPwdToServer(String userName, String userPwd_old, String userPwd_new) {
        // TODO
        // 与服务器进行通信来重新设置密码
    }

}

