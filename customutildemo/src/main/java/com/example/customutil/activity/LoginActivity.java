package com.example.customutil.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.customutil.R;

public class LoginActivity extends Activity {
    private EditText mAccount;
    private EditText mPwd;
    private Button mRegisterButton;
    private Button mLoginButton;
    private Button mCancleButton;
    private CheckBox mRememberCheck;

    private SharedPreferences login_sp;
    private String userNameValue, passwordValue;

    private View loginView;
    private View loginSuccessView;
    private TextView loginSuccessShow;
    private TextView mChangepwdText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        mAccount = (EditText) findViewById(R.id.login_edit_account);
        mPwd = (EditText) findViewById(R.id.login_edit_pwd);
        mRegisterButton = (Button) findViewById(R.id.login_btn_register);
        mLoginButton = (Button) findViewById(R.id.login_btn_login);
        mCancleButton = (Button) findViewById(R.id.login_btn_cancle);
        loginView = findViewById(R.id.login_view);
        loginSuccessView = findViewById(R.id.login_success_view);
        loginSuccessShow = (TextView) findViewById(R.id.login_success_show);
        mChangepwdText = (TextView) findViewById(R.id.login_text_change_pwd);
        mRememberCheck = (CheckBox) findViewById(R.id.Login_Remember);

        login_sp = getSharedPreferences("userInfo", 0);

        String name = login_sp.getString("USER_NAME", "");
        String pwd = login_sp.getString("PASSWORD", "");
        boolean choseRemember = login_sp.getBoolean("mRememberCheck", false);
        boolean choseAutoLogin = login_sp.getBoolean("mAutologinCheck", false);

        if (choseRemember) {
            mAccount.setText(name);
            mPwd.setText(pwd);
            mRememberCheck.setChecked(true);
        }

        mRegisterButton.setOnClickListener(mListener);
        mLoginButton.setOnClickListener(mListener);
        mCancleButton.setOnClickListener(mListener);
        mChangepwdText.setOnClickListener(mListener);

        ImageView image = (ImageView) findViewById(R.id.logo);
        image.setImageResource(R.drawable.ic_launcher_foreground);
    }

    OnClickListener mListener = new OnClickListener() {
        public void onClick(View v) {
            int id = v.getId();
            if (id == R.id.login_btn_login) {
                login();
            } else if (id == R.id.login_btn_cancle) {
                finish();
            } else if (id == R.id.login_btn_register) {
                Intent intent_Login_to_Register = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent_Login_to_Register);
                finish();
            } else if (id == R.id.login_text_change_pwd) {
                Intent intent_Login_to_reset = new Intent(LoginActivity.this, ResetpwdActivity.class);
                startActivity(intent_Login_to_reset);
                finish();
            }
        }
    };

    public void login() {
        if (isUserNameAndPwdValid()) {
            String userName = mAccount.getText().toString().trim();
            String userPwd = mPwd.getText().toString().trim();
            SharedPreferences.Editor editor = login_sp.edit();

            editor.putString("USER_NAME", userName);
            editor.putString("PASSWORD", userPwd);

            if (mRememberCheck.isChecked()) {
                editor.putBoolean("mRememberCheck", true);
            } else {
                editor.putBoolean("mRememberCheck", false);
            }
            editor.commit();

            // TODO
            // 与服务器进行通信以登录
            if (checkLogin()) {
                Intent intent = new Intent(LoginActivity.this, DownloadActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(LoginActivity.this, R.string.pwd_check_same, Toast.LENGTH_SHORT).show();
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
        }
        return true;
    }

    private boolean checkLogin() {
        return true;
    }

}
