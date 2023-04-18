package com.example.custom.fragment;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.custom.R;
import com.example.custom.activity.LoginActivity;

public class FragmentLogin extends Fragment {
    private static final String TAG = "wtx_FragmentLogin";
    private LoginActivity mActivity;
    private EditText mEtAccount;
    private EditText mEtPwd;
    private Button mBtnLogin;
    private Button mBtnRegister;
    private CheckBox mCbRemember;
    private TextView mTvPwdChange;
    private CheckBox mCbEye;
    private SharedPreferences mSharedPreferencesLogin;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.w(TAG, "onCreateView");
        View view = inflater.inflate(R.layout.layout_fragment_login, container, false);

        mEtAccount = (EditText) view.findViewById(R.id.id_et_login_account);
        mEtPwd = (EditText) view.findViewById(R.id.id_et_login_pwd);
        mCbEye = view.findViewById(R.id.id_login_pwd_eye);
        mBtnLogin = (Button) view.findViewById(R.id.id_btn_login);
        mBtnRegister = (Button) view.findViewById(R.id.id_btn_register);
        mTvPwdChange = (TextView) view.findViewById(R.id.id_login_pwd_change);
        mCbRemember = (CheckBox) view.findViewById(R.id.id_login_remember);

        mBtnLogin.setOnClickListener(mListener);
        mBtnRegister.setOnClickListener(mListener);
        mTvPwdChange.setOnClickListener(mListener);

        mCbEye.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //设置密码为明文，并更改眼睛图标
                    mEtPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    //设置密码为暗文，并更改眼睛图标
                    mEtPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                //设置光标位置的代码需放在设置明暗文的代码后面
                mEtPwd.setSelection(mEtPwd.getText().toString().length());
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.w(TAG, "onActivityCreated");

        Activity activity = getActivity();
        if (activity instanceof LoginActivity) {
            mActivity = (LoginActivity) activity;
            mSharedPreferencesLogin = mActivity.getSharedPreferences("userInfo", 0);

            String name = mSharedPreferencesLogin.getString("USER_NAME", "");
            String pwd = mSharedPreferencesLogin.getString("PASSWORD", "");
            boolean choseRemember = mSharedPreferencesLogin.getBoolean("mRememberCheck", false);
            boolean choseAutoLogin = mSharedPreferencesLogin.getBoolean("mAutologinCheck", false);

            if (choseRemember) {
                mEtAccount.setText(name);
                mEtPwd.setText(pwd);
                mCbRemember.setChecked(true);
            }
        }
    }

    private View.OnClickListener mListener = new View.OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.id_btn_register:
                    if (null != mActivity) {
                        mActivity.switchFragment(1);
                    }
                    break;
                case R.id.id_btn_login:
                    Toast.makeText(mActivity, "Login.", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.id_login_pwd_change:
                    if (null != mActivity) {
                        mActivity.switchFragment(2);
                    }
                    break;
            }
        }
    };

}
