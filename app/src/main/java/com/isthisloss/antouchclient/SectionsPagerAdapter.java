package com.isthisloss.antouchclient;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
class SectionsPagerAdapter extends FragmentPagerAdapter {

    SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a TabFragmentFactory (defined as a static inner class below).
        return TabFragmentFactory.newInstance(position + 1);
    }

    @Override
    public int getCount() {
        // Show 2 total pages.
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "0";
            case 1:
                return "1";
        }
        return null;
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class TabFragmentFactory extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public TabFragmentFactory() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static TabFragmentFactory newInstance(int sectionNumber) {
            TabFragmentFactory fragment = new TabFragmentFactory();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = null;
            int num = getArguments().getInt(ARG_SECTION_NUMBER);
            if (num == 1) {
                rootView = inflater.inflate(R.layout.main_buttons_fragment, container, false);
            } else if (num == 2) {
                rootView = inflater.inflate(R.layout.extended_buttons_fragment, container, false);
            }
            return rootView;
        }
    }
}
