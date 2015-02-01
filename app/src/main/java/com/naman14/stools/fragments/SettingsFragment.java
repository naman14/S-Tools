package com.naman14.stools.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.naman14.stools.R;
import com.naman14.stools.util.HelpUtils;

/**
 * Created by naman on 30/01/15.
 */
public class SettingsFragment extends Fragment {



    String Urlrate = "https://play.google.com/store/apps/details?id=com.naman14.stools";
    String Urlgithub="https://github.com/naman14/S-Tools";
    String Urldonate="https://play.google.com/store/apps/details?id=com.naman14.stoolsp";


    ImageView github,rate,share;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        View v = inflater.inflate(R.layout.fragment_settings, container,
                false);
        github=(ImageView) v.findViewById(R.id.github);
        rate=(ImageView) v.findViewById(R.id.rate);
        share=(ImageView) v.findViewById(R.id.share);

        ImageView photo1 = (ImageView)v.findViewById(R.id.photo1);
        photo1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HelpUtils.showThemes(getActivity());
            }


        });
        TextView text1 = (TextView)v.findViewById(R.id.text1);
        text1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HelpUtils.showThemes(getActivity());
            }


        });
        ImageView photo2 = (ImageView)v.findViewById(R.id.photo2);
        photo2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HelpUtils.showAbout(getActivity());
            }


        });
        TextView text2 = (TextView)v.findViewById(R.id.text2);
        text2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HelpUtils.showAbout(getActivity());
            }

        });

        ImageView photo3 = (ImageView)v.findViewById(R.id.photo3);
        photo3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent email =new
                        Intent(Intent.ACTION_SENDTO);
                email.setData(Uri.parse("mailto"));
                email.putExtra(Intent.EXTRA_EMAIL,new
                        String[]{"namandwivedi14@gmail.com"});
                email.setType("message/rfc822");
                startActivity(email);
            }


        });
        TextView text3 = (TextView)v.findViewById(R.id.text3);
        text3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent email =new
                        Intent(Intent.ACTION_SEND);

                email.putExtra(Intent.EXTRA_EMAIL,new
                        String[]{"namandwivedi14@gmail.com"});
                email.setType("message/rfc822");
                startActivity(email);
            }

        });

        github.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(Urlgithub));
                startActivity(i);
            }
        });
        rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(Urlrate));
                startActivity(i);
            }
        });
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_SUBJECT, "S Tools+");
                String sAux = "\nLet me recommend you this application for keeping track of your cpu,device sensors and much more\n\n";
                sAux = sAux + "https://play.google.com/store/apps/details?id=com.naman14.stools \n\n";
                i.putExtra(Intent.EXTRA_TEXT, sAux);
                startActivity(Intent.createChooser(i, "Choose one"));
            }
        });
        TextView donate =(TextView) v.findViewById(R.id.donate);
        donate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(Urldonate));
                startActivity(i);
            }
        });

        return v;
    }


}
