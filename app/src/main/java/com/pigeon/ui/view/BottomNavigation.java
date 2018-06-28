package com.pigeon.ui.view;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.pigeon.R;
import com.stone.base.ArgsUtils;
import com.stone.base.GMInvoker;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Joseph.Yan.
 */
public class BottomNavigation extends LinearLayout implements View.OnClickListener {

    private FrameLayout[] guiderArray=new FrameLayout[4];
    private List<Fragment> fragments;
    private InnerAdapter adapter;
    private ViewPager viewPager;
    private int currentPosition;

    public BottomNavigation(Context context) {
        super(context);
        init();
    }

    public BottomNavigation(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BottomNavigation(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        guiderArray = new FrameLayout[4];
        mappingGuiderWithFragment();

        LayoutInflater.from(getContext()).inflate(R.layout.view_bottom_navigation, this);


        guiderArray[0] = this.findViewById(R.id.view_guider_bar_hall_flyt);
        guiderArray[1] = this.findViewById(R.id.view_guider_bar_message_flyt);
        guiderArray[2] = this.findViewById(R.id.view_guider_bar_contacts_flyt);
        guiderArray[3] = this.findViewById(R.id.view_guider_bar_me_flyt);

        GMInvoker.newInstance(OnClickListener.class).wrap((Object[]) guiderArray).set(this);
    }

    private void mappingGuiderWithFragment() {
        fragments = new ArrayList<>();
//        fragments.add(new HallFragment());
//        fragments.add(new MessageFragment());
//        fragments.add(new ContactsFragment());
//        fragments.add(new MeFragment());
    }

    public void setCurrentItem(int position) {
        guiderArray[position].performClick();
    }

    public void touchViewPagerUnderFragmentManager(FragmentManager fragmentManager, ViewPager viewPager) {
        ArgsUtils.notNull(fragmentManager, "FragmentManager");
        ArgsUtils.notNull(viewPager, "ViewPager");
        if (adapter == null) {
            adapter = new InnerAdapter(fragmentManager, fragments);
        }
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                invalidateGuiders(guiderArray[position]);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        invalidateGuiders(guiderArray[currentPosition]);
        this.viewPager = viewPager;
    }

    @Override
    public void onClick(View v) {
        try {
            int position = positionByView(v);
            viewPager.setCurrentItem(position, false);
            this.currentPosition = position;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void invalidateGuiders(View view) {
        for (View v : guiderArray) {
            v.setSelected(view == v);
        }
    }

    private int positionByView(View view) {
        int position = 0;
        if (view != null) {
            for (int i = 0; i < guiderArray.length; i++) {
                View v = guiderArray[i];
                if (v.getId() == view.getId()) {
                    position = i;
                    break;
                }
            }
        }
        return position;
    }

    public FrameLayout getGuider(int position) {
        return guiderArray[position];
    }

    public int getCount() {
        return 4;
    }

    private static class InnerAdapter extends FragmentPagerAdapter {
        private List<Fragment> fragments;

        InnerAdapter(FragmentManager fm, List<Fragment> fragments) {
            super(fm);
            this.fragments = fragments;
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "title";
        }
    }
}
