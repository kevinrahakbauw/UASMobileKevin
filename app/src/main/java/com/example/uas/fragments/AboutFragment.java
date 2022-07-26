package com.example.uas.fragments;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.uas.Preferences;
import com.example.uas.R;
import com.example.uas.activities.LoginActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class AboutFragment extends Fragment {
    Button logout;
    TextView backgroundColor, login, fontSize, title;

    public AboutFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_about, container, false);
        String color = Preferences.getBackgroundColor(getContext());
        LinearLayout ll = view.findViewById(R.id.ll_about);
        if (color.equalsIgnoreCase("White")){
            ll.setBackgroundColor(Color.WHITE);
        } else if (color.equalsIgnoreCase("Yellow")){
            ll.setBackgroundColor(Color.YELLOW);
        } else if (color.equalsIgnoreCase("Green")){
            ll.setBackgroundColor(Color.GREEN);
        } else if (color.equalsIgnoreCase("Blue")){
            ll.setBackgroundColor(Color.BLUE);
        }
        logout = view.findViewById(R.id.button_logout);
        login = view.findViewById(R.id.tv_info_login);
        backgroundColor = view.findViewById(R.id.tv_info_bg_color);
        fontSize = view.findViewById(R.id.tv_info_font_size);

        login.setText(Preferences.getLoggedInUser(getContext()));
        backgroundColor.setText(Preferences.getBackgroundColor(getContext()));
        fontSize.setText(String.valueOf(Preferences.getFontSize(getContext())));

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Preferences.clearLoggedInUser(getContext());
                startActivity(new Intent(getContext(), LoginActivity.class));getActivity().finish();
            }
        });

        return view;
    }
}