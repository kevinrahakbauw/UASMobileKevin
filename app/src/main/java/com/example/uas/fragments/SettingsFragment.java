package com.example.uas.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.uas.Preferences;
import com.example.uas.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends Fragment {
    TextView fontSizeValue;
    SeekBar fontSize;

    public SettingsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        RadioGroup radioGroup = view.findViewById(R.id.radiogroup);
        fontSize = view.findViewById(R.id.sb_font_size);
        fontSizeValue = view.findViewById(R.id.tv_value_seekbar);
        fontSize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                fontSizeValue.setText(String.valueOf(progress));
                Preferences.setFontSize(getContext(), progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            public void onCheckedChanged(RadioGroup group, int checkedId) {
                String colourSelected = "";
                switch (checkedId) {
                    case R.id.radioButton1 :
                        colourSelected = "GREEN";
                        Preferences.setBackgroundColor(getContext(), colourSelected);
                        break;
                    case R.id.radioButton2 :
                        colourSelected = "YELLOW";
                        Preferences.setBackgroundColor(getContext(), colourSelected);
                        break;

                    case R.id.radioButton3 :
                        colourSelected = "BLUE";
                        Preferences.setBackgroundColor(getContext(), colourSelected);
                        break;

                    case R.id.radioButton4 :
                        colourSelected = "WHITE";
                        Preferences.setBackgroundColor(getContext(), colourSelected);
                        break;
                }
            }
        });
        return view;
    }
}
