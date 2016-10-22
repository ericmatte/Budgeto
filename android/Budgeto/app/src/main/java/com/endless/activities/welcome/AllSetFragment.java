package com.endless.activities.welcome;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.endless.budgeto.R;

/**
 * Check if all configuration has been properly completed and redirect to Budgeto
 *
 * @author  Eric Matte
 * @version 1.0
 */
public class AllSetFragment extends Fragment {
    public AllSetFragment() {}
    private View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (this.rootView == null) {
            View rootView = inflater.inflate(R.layout.fragment_all_set, container, false);

            Button btnFinish = (Button) rootView.findViewById(R.id.btnFinish);
            btnFinish.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    // Perform action on click
                    ((SetupActivity) getActivity()).finishSetup();
                }
            });
            this.rootView = rootView;
        }
        return this.rootView;
    }
}