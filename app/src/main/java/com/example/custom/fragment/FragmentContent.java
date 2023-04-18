package com.example.custom.fragment;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.example.custom.R;
import com.example.custom.view.CustomView;

public class FragmentContent extends Fragment {
    private static final String TAG = "wtx_FragmentContent";
    private CustomView customView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
        Log.i(TAG, "onCreateView");
        View view = inflater.inflate(R.layout.layout_fragment, container, false);
        customView = getActivity().findViewById(R.id.fragment_view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i(TAG, "onActivityCreated1");

//        customView.layout(100, 100, 300, 200);
        ConstraintLayout.LayoutParams lp = (ConstraintLayout.LayoutParams) customView.getLayoutParams();
        lp.leftMargin = 100;
        lp.topMargin = 100;
        lp.width = 200;
        lp.height = 100;
        customView.setLayoutParams(lp);
    }

    @Override
    public void onAttachFragment(@NonNull Fragment childFragment) {
        super.onAttachFragment(childFragment);
        Log.i(TAG, "onAttachFragment");
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Log.i(TAG, "onAttach");
    }
}
