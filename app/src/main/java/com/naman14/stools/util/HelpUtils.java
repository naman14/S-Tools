/*
 * Copyright 2014 Google Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.naman14.stools.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.naman14.stools.R;
import com.naman14.stools.themeUtils;


public class HelpUtils {


    public static void showThemes(Activity activity) {
        FragmentManager fm = activity.getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Fragment prev = fm.findFragmentByTag("dialog_themes");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        new ThemesDialog().show(ft, "dialog_themes");
    }

    public static class ThemesDialog extends DialogFragment {


        ImageView imageButton1;
        ImageView   imageButton2;
        ImageView   imageButton3;
        ImageView  imageButton4;
        ImageView   imageButton5;
        ImageView   imageButton6;


        public ThemesDialog() {
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            LinearLayout aboutBodyView = (LinearLayout) layoutInflater.inflate(R.layout.dialog_themes, null);

            imageButton1=(ImageView) aboutBodyView.findViewById(R.id.imageButton1);
            imageButton1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    themeUtils.changeToTheme(getActivity(), themeUtils.THEME1);
                    Intent i1 = getActivity().getBaseContext().getPackageManager()
                            .getLaunchIntentForPackage( getActivity().getBaseContext().getPackageName() );
                    i1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i1);
                }
            });

            imageButton2=(ImageView) aboutBodyView.findViewById(R.id.imageButton2);
            imageButton2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    themeUtils.changeToTheme(getActivity(), themeUtils.THEME2);
                    Intent i2 = getActivity().getBaseContext().getPackageManager()
                            .getLaunchIntentForPackage( getActivity().getBaseContext().getPackageName() );
                    i2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i2);
                }
            });
            imageButton3=(ImageView) aboutBodyView.findViewById(R.id.imageButton3);
            imageButton3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    themeUtils.changeToTheme(getActivity(), themeUtils.THEME3);
                    Intent i3 = getActivity().getBaseContext().getPackageManager()
                            .getLaunchIntentForPackage( getActivity().getBaseContext().getPackageName() );
                    i3.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i3);
                }
            });
            imageButton4=(ImageView) aboutBodyView.findViewById(R.id.imageButton4);
            imageButton4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    themeUtils.changeToTheme(getActivity(), themeUtils.THEME4);
                    Intent i4 = getActivity().getBaseContext().getPackageManager()
                            .getLaunchIntentForPackage( getActivity().getBaseContext().getPackageName() );
                    i4.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i4);
                }
            });
            imageButton5=(ImageView) aboutBodyView.findViewById(R.id.imageButton5);
            imageButton5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    themeUtils.changeToTheme(getActivity(), themeUtils.THEME5);
                    Intent i5 = getActivity().getBaseContext().getPackageManager()
                            .getLaunchIntentForPackage( getActivity().getBaseContext().getPackageName() );
                    i5.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i5);
                }
            });
            imageButton6=(ImageView) aboutBodyView.findViewById(R.id.imageButton6);
            imageButton6.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    themeUtils.changeToTheme(getActivity(), themeUtils.THEME6);
                    Intent i6 = getActivity().getBaseContext().getPackageManager()
                            .getLaunchIntentForPackage( getActivity().getBaseContext().getPackageName() );
                    i6.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i6);
                }
            });



            return new AlertDialog.Builder(getActivity())
                    .setView(aboutBodyView)
                    .create();
        }

    }

    public static void showAbout(Activity activity) {
        FragmentManager fm = activity.getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Fragment prev = fm.findFragmentByTag("dialog_about");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        new AboutDialog().show(ft, "dialog_about");
    }

    public static class AboutDialog extends DialogFragment {

        String url1 = "https://plus.google.com/app/basic/+NamanDwivedi14";

        public AboutDialog() {
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            LinearLayout aboutBodyView = (LinearLayout) layoutInflater.inflate(R.layout.dialog_about, null);
            TextView follow = (TextView)aboutBodyView.findViewById(R.id.follow);
            follow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url1));
                    startActivity(i);
                }

            });

            return new AlertDialog.Builder(getActivity())
                    .setView(aboutBodyView)
                    .create();
        }

    }



}
