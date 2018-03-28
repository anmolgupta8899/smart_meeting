package com.mobilecoderz.smartmeeting.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.mobilecoderz.smartmeeting.R;
import com.mobilecoderz.smartmeeting.fragment.DemoFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OnBoardActivity extends AppCompatActivity {
    private Context context;

    @BindView(R.id.flParent)
    FrameLayout flParent;

    @BindView(R.id.vpIntroduction)
    ViewPager vpIntroduction;

    @BindView(R.id.tvSignUp)
    TextView tvSignUp;

    @BindView(R.id.tvLogin)
    TextView tvLogin;

    @BindView(R.id.ivIndicatorFirst)
    ImageView ivIndicatorFirst;
    @BindView(R.id.ivIndicatorSecond)
    ImageView ivIndicatorSecond;
    @BindView(R.id.ivIndicatorThird)
    ImageView ivIndicatorThird;
    @BindView(R.id.ivIndicatorForth)
    ImageView ivIndicatorForth;
    @BindView(R.id.ivIndicatorFifth)
    ImageView ivIndicatorFifth;
    @BindView(R.id.ivIndicatorSixth)
    ImageView ivIndicatorSixth;
    private PicturePreViewAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_board);
        context = OnBoardActivity.this;
        ButterKnife.bind(this);
        setAdapter();
    }

    private void setAdapter() {
        mAdapter = new PicturePreViewAdapter(getSupportFragmentManager());
        vpIntroduction.setAdapter(mAdapter);
        vpIntroduction.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                highlightIndicator(position);
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    class PicturePreViewAdapter extends FragmentStatePagerAdapter {

        public PicturePreViewAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return DemoFragment.newInstance(position);
        }

        @Override
        public int getCount() {
            return 6;
        }
    }

    private void highlightIndicator(int count) {
        if (count == 0) {
            setGrayBackground();
            ivIndicatorFirst.setImageResource(R.drawable.ic_fiber_manual_record_black_24dp);
        } else if (count == 1) {
            setGrayBackground();
            ivIndicatorSecond.setImageResource(R.drawable.ic_fiber_manual_record_black_24dp);
        } else if (count == 2) {
            setGrayBackground();
            ivIndicatorThird.setImageResource(R.drawable.ic_fiber_manual_record_black_24dp);
        } else if (count == 3) {
            setGrayBackground();
            ivIndicatorForth.setImageResource(R.drawable.ic_fiber_manual_record_black_24dp);
        } else if (count == 4) {
            setGrayBackground();
            ivIndicatorFifth.setImageResource(R.drawable.ic_fiber_manual_record_black_24dp);
        } else if (count == 5) {
            setGrayBackground();
            ivIndicatorSixth.setImageResource(R.drawable.ic_fiber_manual_record_black_24dp);

        }
    }

    private void setGrayBackground() {
        ivIndicatorFirst.setImageResource(R.drawable.circular_gray_shape);
        ivIndicatorSecond.setImageResource(R.drawable.circular_gray_shape);
        ivIndicatorThird.setImageResource(R.drawable.circular_gray_shape);
        ivIndicatorForth.setImageResource(R.drawable.circular_gray_shape);
        ivIndicatorFifth.setImageResource(R.drawable.circular_gray_shape);
        ivIndicatorSixth.setImageResource(R.drawable.circular_gray_shape);
    }
}
