package com.mobilecoderz.smartmeeting.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mobilecoderz.smartmeeting.R;

import butterknife.ButterKnife;

/**
 * Created by intel on 28-03-2018.
 */

public class DemoFragment extends Fragment {
    private Context context;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context=context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = null;
        int viewDount= (int) getArguments().get("frag");
        if(viewDount==0){
        view = inflater.inflate(R.layout.demo_fragment, container, false);
            ButterKnife.bind(this, view);
            return view;
        }else if(viewDount==1){
            view = inflater.inflate(R.layout.wallk2_fragment, container, false);
            ButterKnife.bind(this, view);
            return view;
        }else {
            view = inflater.inflate(R.layout.wallk3_fragment, container, false);
            ButterKnife.bind(this, view);
            return view;
        }
    }

    public static DemoFragment newInstance(int id) {
        Bundle args = new Bundle();
        args.putInt("frag", id);
        DemoFragment fragment = new DemoFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
