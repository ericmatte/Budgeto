package com.endless.activities.welcome;

import android.animation.ArgbEvaluator;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.endless.budgeto.R;
import com.endless.tools.Logger;

/**
 * Setup and welcome screens handling user first configuration
 *
 * @author  Eric Matte
 * @version 1.0
 */
public class SetupActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ArgbEvaluator argbEvaluator = new ArgbEvaluator();

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.addOnPageChangeListener(scrollingHandler);
    }

    /** Allow Background color transition and dots indicator */
    ViewPager.SimpleOnPageChangeListener scrollingHandler = new ViewPager.SimpleOnPageChangeListener() {
        public void onPageScrollStateChanged(int state) {}
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            backgroundColorTransition(position, positionOffset);
        }
        public void onPageSelected(int position) {
            LinearLayout viewPagerCountDots = (LinearLayout) findViewById(R.id.viewPagerCountDots);

            for (int i = 0; i < viewPagerCountDots.getChildCount(); i++) {
                ImageView dot = (ImageView) viewPagerCountDots.getChildAt(i);
                dot.setImageResource(i == position ? R.drawable.dot_selected : R.drawable.dot);
            }
        }
    };

    /** Animation background color transition on page change */
    private void backgroundColorTransition(int position, float positionOffset) {
        int[] colors = getResources().getIntArray(R.array.colorsSetupActivity);

        if (position < (mSectionsPagerAdapter.getCount() -1) && position < (colors.length - 1)) {
            mViewPager.setBackgroundColor((Integer) argbEvaluator.evaluate(positionOffset, colors[position], colors[position + 1]));
        } else {
            mViewPager.setBackgroundColor(colors[colors.length - 1]);
        }
    }

    /** Get configuration data from all setup screen and start Budgeto */
    public void finishSetup() {
        Logger.print(this, "finishSetup!", "Test");
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            return 4; // Show 4 total pages.
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        public PlaceholderFragment() {}
        /** Fragment argument representing the section number for this fragment. */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView;
            switch (getArguments().getInt(ARG_SECTION_NUMBER)) {
                case 1:
                    // Welcome
                    return welcomeInflater(inflater, container);
                case 2:
                    // Pin
                    return pinInflater(inflater, container);
                case 3:
                    // Banks
                    return banksInflater(inflater, container);
                case 4:
                    // All set
                    return allSetInflater(inflater, container);
                default:
                    //textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
                    return null;
            }
        }

        private View welcomeInflater(LayoutInflater inflater, ViewGroup container) {
            return inflater.inflate(R.layout.fragment_welcome, container, false);
        }

        private View pinInflater(LayoutInflater inflater, ViewGroup container) {
            View rootView = inflater.inflate(R.layout.fragment_pin_chooser, container, false);

            final TextView txtPIN = (TextView) rootView.findViewById(R.id.txtPIN);
            final TextView txtPINCheck = (TextView) rootView.findViewById(R.id.txtPINCheck);
            TextWatcher textWatcher = new TextWatcher(){
                public void afterTextChanged(Editable s) {
                    if (txtPINCheck.getText().toString().equals(txtPIN.getText().toString())) {
                        // btnPinCheck.setText("pwd matches!");
                    } else {
                        // btnPinCheck.setText("pwd no the same...");
                    }
                }
                public void beforeTextChanged(CharSequence s, int start, int count, int after){}
                public void onTextChanged(CharSequence s, int start, int before, int count){}
            };
            txtPIN.addTextChangedListener(textWatcher);
            txtPINCheck.addTextChangedListener(textWatcher);

            return rootView;
        }

        private View banksInflater(LayoutInflater inflater, ViewGroup container) {
            View rootView = inflater.inflate(R.layout.fragment_banks, container, false);

            RecyclerView mRecyclerView = (RecyclerView) rootView.findViewById(R.id.banks_recycler_view);
            RecyclerView.Adapter mAdapter;
            RecyclerView.LayoutManager mLayoutManager;

            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            mRecyclerView.setHasFixedSize(true);

            // use a linear layout manager
            mLayoutManager = new LinearLayoutManager(getContext());
            mRecyclerView.setLayoutManager(mLayoutManager);

            // specify an adapter (see also next example)
            String[] myDataset = {"Tangerine", "Desjardins", "BNC"};
            mAdapter = new BankAdapter(myDataset);
            mRecyclerView.setAdapter(mAdapter);

            return rootView;
        }

        private View allSetInflater(LayoutInflater inflater, ViewGroup container) {
            final View rootView = inflater.inflate(R.layout.fragment_all_set, container, false);

            Button btnFinish = (Button) rootView.findViewById(R.id.btnFinish);
            btnFinish.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    // Perform action on click
                    ((SetupActivity) getActivity()).finishSetup();
                }
            });

            return rootView;
        }
    }
}
