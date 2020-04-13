package com.example.amogha.newsupdates;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class helpline extends android.support.v4.app.Fragment {
    TextView number,pdf,whoweb,cdcweb,mohfweb;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.helpline_fragment, container, false);
        number=(TextView) view.findViewById(R.id.cnt_hlp_num);
        pdf=(TextView) view.findViewById(R.id.pdf);
        whoweb=(TextView)view.findViewById(R.id.who);
        cdcweb=(TextView)view.findViewById(R.id.cdc);
        mohfweb=(TextView) view.findViewById(R.id.mohfw);
        number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri u= Uri.parse("tel:"+number.getText());
                Intent i=new Intent(Intent.ACTION_DIAL,u);
                startActivity(i);
            }
        });
        pdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Intent.ACTION_VIEW,Uri.parse(pdf.getText().toString()));
                startActivity(i);
            }
        });
        whoweb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri u= Uri.parse(""+whoweb.getText());
                Intent i=new Intent(Intent.ACTION_VIEW,u);
                startActivity(i);
            }
        });
        cdcweb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri u= Uri.parse(""+cdcweb.getText());
                Intent i=new Intent(Intent.ACTION_VIEW,u);
                startActivity(i);
            }
        });
        mohfweb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri u= Uri.parse(""+mohfweb.getText());
                Intent i=new Intent(Intent.ACTION_VIEW,u);
                startActivity(i);
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Helpline");
    }
}
