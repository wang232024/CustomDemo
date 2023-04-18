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

public class FragmentPwdReset extends Fragment {
    private static final String TAG = "wtx_FragmentPwdChange";
    private EditText mAccount;
    private EditText mPwd_old;
    private EditText mPwd_new;
    private EditText mPwdCheck;
    private Button mSureButton;
    private Button mCancelButton;
    private LoginActivity mActivity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_fragment_pwd_reset, container, false);

        mAccount = (EditText) view.findViewById(R.id.resetpwd_edit_name);
        mPwd_old = (EditText) view.findViewById(R.id.resetpwd_edit_pwd_old);
        mPwd_new = (EditText) view.findViewById(R.id.resetpwd_edit_pwd_new);
        mPwdCheck = (EditText) view.findViewById(R.id.resetpwd_edit_pwd_check);
        mSureButton = (Button) view.findViewById(R.id.resetpwd_btn_sure);
        mCancelButton = (Button) view.findViewById(R.id.resetpwd_btn_cancel);

        mSureButton.setOnClickListener(mOnClickListener);
        mCancelButton.setOnClickListener(mOnClickListener);

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

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.resetpwd_btn_sure:
                    if (null != mActivity) {
                        Toast.makeText(mActivity, "Register check.", Toast.LENGTH_SHORT).show();
                    }
                    Log.i(TAG, "Reset pwd.");
                    break;
                case R.id.resetpwd_btn_cancel:
                    if (null != mActivity) {
                        mActivity.switchFragment(0);
                    }
                    break;
            }
        }
    };

}
