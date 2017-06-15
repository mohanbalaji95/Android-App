package com.garcon.garcon;

/**
 * Created by kritikagopalakrishnan on 6/2/16.
 */

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class TabFragment extends Fragment{

    public static TabLayout tabLayout;
    public static ViewPager viewPager;
    public static int int_items = 2;

    private MyAdapter myAdapter;

    public static TabFragment newInstance(String flip) {

        Bundle args = new Bundle();

        TabFragment fragment = new TabFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        /**
         *Inflate tab_layout and setup Views.
         */
        View x = inflater.inflate(R.layout.tab_layout, null);
        tabLayout = (TabLayout) x.findViewById(R.id.tabs);
        viewPager = (ViewPager) x.findViewById(R.id.viewpager);

        /**
         *Set an Apater for the View Pager
         */

        myAdapter = new MyAdapter(getChildFragmentManager());
        viewPager.setAdapter(myAdapter);

        /**
         * Now , this is a workaround ,
         * The setupWithViewPager dose't works without the runnable .
         * Maybe a Support Library Bug .
         */

        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                tabLayout.setupWithViewPager(viewPager);
            }
        });

        return x;

    }

    public Fragment getSelectedFragment(int position){
        return myAdapter.getRegisteredFragment(position);
    }

    class MyAdapter extends SmartFragmentStatePagerAdapter {

        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        /**
         * Return fragment with respect to Position .
         */

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new ContainerFragment();
                case 1:
                    return new RestaurantDetailFragment();
            }
            return null;
        }

        @Override
        public int getCount() {

            return int_items;

        }

        /**
         * This method returns the title of the tab according to the position.
         */

        @Override
        public CharSequence getPageTitle(int position) {

            switch (position) {
                case 0:
                    return "Map";
                case 1:
                    return "Restaurant";
//                case 2:
//                    return "Nearby";
            }
            return null;
        }
    }

    public void switchViewPagerPosition(int position){
        if(viewPager!=null) {
            viewPager.setCurrentItem(position);
        }
    }
}
