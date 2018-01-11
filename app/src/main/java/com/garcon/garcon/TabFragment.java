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

    // Code added to Change the words "Map" and "Restaurant" on bottom two tabs and put the Maps and Restaurant icons instead
    public int[] tabIcons = {
            R.drawable.maap,
            R.drawable.rest
    };
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
                // Code added to Change the words "Map" and "Restaurant" on bottom two tabs and put the Maps and Restaurant icons instead
                setupTabIcons();
            }
        });

        return x;

    }

    // Code added to Change the words "Map" and "Restaurant" on bottom two tabs and put the Maps and Restaurant icons instead
    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
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
                    //interchanging Map and RestaurantList fragments
                  //  return new ContainerFragment();
                    return new SecondFragment();

                case 1:
                    //return new SecondFragment();
                //   return new RestaurantDetailFragment();
                     return new ContainerFragment();

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
                    //return "Restaurant"
                    // Code added to Change the words "Map" and "Restaurant" on bottom two tabs and put the Maps and Restaurant icons instead
                    return  "";

                case 1:
                    //return "Map"
                    // Code added to Change the words "Map" and "Restaurant" on bottom two tabs and put the Maps and Restaurant icons instead
                    return "";
//              case 2:
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
