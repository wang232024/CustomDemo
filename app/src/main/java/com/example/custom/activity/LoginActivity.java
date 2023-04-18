package com.example.custom.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.custom.R;
import com.example.custom.fragment.FragmentLogin;
import com.example.custom.fragment.FragmentPwdReset;
import com.example.custom.fragment.FragmentRegister;

public class LoginActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_activity_login);

        switchFragment(0);
    }

    public void switchFragment(int index) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        Fragment fragment = null;
        switch (index) {
            case 0:
                fragment = new FragmentLogin();
                break;
            case 1:
                fragment = new FragmentRegister();
                break;
            case 2:
                fragment = new FragmentPwdReset();
                break;
        }
        if (null != fragment) {
            fragmentTransaction.replace(R.id.id_login_container, fragment);
            fragmentTransaction.commitAllowingStateLoss();
        }
    }

}
