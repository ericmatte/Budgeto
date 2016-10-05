package com.endless.activities.welcome;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.endless.budgeto.R;

/**
 * PIN chooser for the application
 *
 * @author  Eric Matte
 * @version 1.0
 */
public class PinFragment extends Fragment {
    private String PIN;
    public String getPIN() { return PIN; }

    public PinFragment() {
        PIN = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_pin_chooser, container, false);

        final TextView txtPIN = (TextView) rootView.findViewById(R.id.txtPIN);
        final TextView txtPINCheck = (TextView) rootView.findViewById(R.id.txtPINCheck);
        TextWatcher textWatcher = new TextWatcher(){
            public void afterTextChanged(Editable s) {
                if (txtPINCheck.getText().toString().equals(txtPIN.getText().toString())) {
                    // "pwd matches!"
                    PIN = txtPIN.getText().toString();
                } else {
                    // "pwd no the same..."
                    PIN = null;
                }
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after){}
            public void onTextChanged(CharSequence s, int start, int before, int count){}
        };
        txtPIN.addTextChangedListener(textWatcher);
        txtPINCheck.addTextChangedListener(textWatcher);

        return rootView;
    }
}