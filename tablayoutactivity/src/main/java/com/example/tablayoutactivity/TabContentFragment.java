package com.example.tablayoutactivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class TabContentFragment extends Fragment {

    public TabContentFragment() {

    }

    public static Fragment newInstance(String string) {
        Bundle bundle = new Bundle();
        bundle.putString("content", string);
        TabContentFragment tabContentFragment = new TabContentFragment();
        tabContentFragment.setArguments(bundle);
        return tabContentFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_content, container, false);
        ((TextView) contentView.findViewById(R.id.fragment_content_tv)).setText(getArguments().getString("content"));
        return contentView;
    }
}
