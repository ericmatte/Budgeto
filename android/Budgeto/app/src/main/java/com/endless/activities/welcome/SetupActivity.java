package com.endless.activities.welcome;

import android.animation.ArgbEvaluator;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.endless.bank.BankResponse;
import com.endless.budgeto.R;
import com.endless.tools.Logger;

import java.util.List;

import static com.endless.budgeto.R.id.container;

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
    private SectionsPagerAdapter pagerAdapter;
    private ArgbEvaluator argbEvaluator = new ArgbEvaluator();

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        pagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        // Set up the ViewPager with the sections adapter.
        viewPager = (ViewPager) findViewById(container);
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(scrollingHandler);
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

            if (position == pagerAdapter.getCount()-1) checkSetup();
        }
    };

    /** Animation background color transition on page change */
    private void backgroundColorTransition(int position, float positionOffset) {
        int[] colors = getResources().getIntArray(R.array.colorsSetupActivity);

        if (position < (pagerAdapter.getCount()-1) && position < (colors.length-1)) {
            viewPager.setBackgroundColor((Integer) argbEvaluator.evaluate(positionOffset, colors[position], colors[position + 1]));
        } else {
            viewPager.setBackgroundColor(colors[colors.length-1]);
        }
    }

    /** Check if everything has been correctly completed */
    public void checkSetup() {
        View allSetView = pagerAdapter.getItem(3).getView();
        String PIN = ((PinFragment) pagerAdapter.getItem(1)).getPIN();
        if (PIN == null) {
            ((TextView) allSetView.findViewById(R.id.txtPIN)).setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_cross, 0, 0, 0);
        } else {
            ((TextView) allSetView.findViewById(R.id.txtPIN)).setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_done, 0, 0, 0);
        }

        BankAdapter bankAdapter = (BankAdapter) ((BanksFragment) pagerAdapter.getItem(2)).bankAdapter;
        List<BankResponse> bankResponses = bankAdapter.getBankResponses();
        ((LinearLayout) allSetView.findViewById(R.id.layBanks)).removeAllViews();
        if (bankResponses.size() == 0) {
            allSetView.findViewById(R.id.txtNoBanks).setVisibility(View.VISIBLE);
        } else {
            allSetView.findViewById(R.id.txtNoBanks).setVisibility(View.GONE);
        }

        for (int i=0; i<bankResponses.size(); i++) {
            BankResponse bankResponse = bankResponses.get(i);
            TextView txtBank = new TextView(this);
            txtBank.setText(bankResponse.getBank() + " - " + String.valueOf(bankResponse.getTransactions().size()) + " transactions");
            txtBank.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            ((LinearLayout) allSetView.findViewById(R.id.layBanks)).addView(txtBank);
        }

        int showDoneViews = (PIN != null) && (bankResponses.size() > 0) ? View.VISIBLE : View.GONE;
        allSetView.findViewById(R.id.layAllSet).setVisibility(showDoneViews);
        allSetView.findViewById(R.id.btnFinish).setVisibility(showDoneViews);
        allSetView.findViewById(R.id.txtRecap).setVisibility(showDoneViews==View.VISIBLE ? View.GONE : View.VISIBLE);
    }

    /** Get configuration data from all setup screen and start Budgeto */
    public void finishSetup() {
        Logger.print(this.getClass(), "Finishing setup...");

        String PIN = ((PinFragment) pagerAdapter.getItem(1)).getPIN();
        BankAdapter bankAdapter = (BankAdapter) ((BanksFragment) pagerAdapter.getItem(2)).bankAdapter;
        List<BankResponse> bankResponses = bankAdapter.getBankResponses();
        Logger.print(this.getClass(), PIN, "PIN");
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        Fragment[] pages = {PlaceholderFragment.newInstance(0),
                new PinFragment(),
                new BanksFragment(),
                new AllSetFragment()};

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position < pages.length) {
                return pages[position];
            } else {
                return null;
            }
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
            // getArguments().getInt(ARG_SECTION_NUMBER)
            return welcomeInflater(inflater, container);
        }

        private View welcomeInflater(LayoutInflater inflater, ViewGroup container) {
            return inflater.inflate(R.layout.fragment_welcome, container, false);
        }
    }
}
