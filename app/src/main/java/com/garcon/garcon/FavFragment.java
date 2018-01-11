package com.garcon.garcon;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by siddhiparekh11 on 7/24/17.
 */

public class FavFragment extends Fragment {

    public static TabLayout tabLayout;
    public static ViewPager viewPager;
    public static int int_items = 2;

    private FavFragment.MyAdapter myAdapter;

    public static FavFragment newInstance(String flip) {

        Bundle args = new Bundle();

        FavFragment fragment = new FavFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        /**
         *Inflate tab_layout and setup Views.
         */
        View x = inflater.inflate(R.layout.fav_tab_layout, null);
        tabLayout = (TabLayout) x.findViewById(R.id.tabs);
        viewPager = (ViewPager) x.findViewById(R.id.viewpager);

        /**
         *Set an Apater for the View Pager
         */

        myAdapter = new FavFragment.MyAdapter(getChildFragmentManager());
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
                    //interchanging Map and RestaurantList fragments
                    //  return new ContainerFragment();
                   // return new SecondFragment();
                     return new FavResFragment();

                case 1:
                    //return new SecondFragment();
                    //   return new RestaurantDetailFragment();
                    return new FavMenuFragment();

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
                //interchanging maps and restaurantlist tabs
                case 0:
                    //  return "Map";
                    return "Restaurants";
                case 1:
                    //return "Restlist";
                    return "Menu";
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
