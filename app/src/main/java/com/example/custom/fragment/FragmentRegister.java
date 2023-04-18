package com.example.custom.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.custom.R;
import com.example.custom.activity.LoginActivity;

public class FragmentRegister extends Fragment {
    private static final String TAG = "wtx_FragmentRegister";
    private EditText mEtAccount;
    private EditText mEtPwd;
    private EditText mCbPwd;
    private Button mBtnSure;
    private Button mBtnCancel;
    private LoginActivity mActivity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_fragment_register, container, false);

        mEtAccount = (EditText) view.findViewById(R.id.resetpwd_edit_name);
        mEtPwd = (EditText) view.findViewById(R.id.resetpwd_edit_pwd_old);
        mCbPwd = (EditText) view.findViewById(R.id.resetpwd_edit_pwd_new);
        mBtnSure = (Button) view.findViewById(R.id.register_btn_sure);
        mBtnCancel = (Button) view.findViewById(R.id.register_btn_cancel);

        mBtnSure.setOnClickListener(mOnClickListener);
        mBtnCancel.setOnClickListener(mOnClickListener);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.w(TAG, "onActivityCreated");

        Activity activity = getActivity();
        if (activity instanceof LoginActivity) {
            mActivity = (LoginActivity) activity;
        }
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {    //不同按钮按下的监听事件选择
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.register_btn_sure:
                    if (null != mActivity) {
                        Toast.makeText(mActivity, "Register check.", Toast.LENGTH_SHORT).show();
                    }
                    Log.i(TAG, "Register check.");
                    break;
                case R.id.register_btn_cancel:
                    if (null != mActivity) {
                        mActivity.switchFragment(0);
                    }
                    break;
            }
        }
    };

}
