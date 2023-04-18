package com.example.customutil.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.customutil.R;

public class RegisterActivity extends Activity {
    private EditText mAccount;
    private EditText mPwd;
    private EditText mPwdCheck;
    private Button mSureButton;
    private Button mCancelButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        mAccount = (EditText) findViewById(R.id.resetpwd_edit_name);
        mPwd = (EditText) findViewById(R.id.resetpwd_edit_pwd_old);
        mPwdCheck = (EditText) findViewById(R.id.resetpwd_edit_pwd_new);
        mSureButton = (Button) findViewById(R.id.register_btn_sure);
        mCancelButton = (Button) findViewById(R.id.register_btn_cancel);

        mSureButton.setOnClickListener(m_register_Listener);
        mCancelButton.setOnClickListener(m_register_Listener);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent_Resetpwd_to_Login = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent_Resetpwd_to_Login);
        finish();
    }

    View.OnClickListener m_register_Listener = new View.OnClickListener() {    //不同按钮按下的监听事件选择
        public void onClick(View v) {
            int id = v.getId();
            if (id == R.id.register_btn_sure) {
                register_check();
            } else if (id == R.id.register_btn_cancel) {
                Intent intent_Register_to_Login = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent_Register_to_Login);
                finish();
            }
        }
    };

    public void register_check() {
        if (isUserNameAndPwdValid()) {
            String userName = mAccount.getText().toString().trim();
            String userPwd = mPwd.getText().toString().trim();
            String userPwdCheck = mPwdCheck.getText().toString().trim();

            if (userPwd.equals(userPwdCheck)) {
                registerToServer(userName, userPwd);
            } else {
                Toast.makeText(RegisterActivity.this, R.string.pwd_check_same, Toast.LENGTH_SHORT).show();
            }
        }
    }

    public boolean isUserNameAndPwdValid() {
        if (mAccount.getText().toString().trim().equals("")) {
            Toast.makeText(this, getString(R.string.account_empty),
                    Toast.LENGTH_SHORT).show();
            return false;
        } else if (mPwd.getText().toString().trim().equals("")) {
            Toast.makeText(this, getString(R.string.pwd_empty),
                    Toast.LENGTH_SHORT).show();
            return false;
        } else if (mPwdCheck.getText().toString().trim().equals("")) {
            Toast.makeText(this, getString(R.string.pwd_check_empty),
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void registerToServer(String userName, String userPwd) {
        // TODO
        // 与服务器进行通信来注册

    }

}
