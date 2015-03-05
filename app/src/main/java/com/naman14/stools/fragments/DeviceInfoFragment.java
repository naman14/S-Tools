package com.naman14.stools.fragments;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.app.Activity;

import com.naman14.stools.R;

/**
 * Created by naman on 30/01/15.
 */
public class DeviceInfoFragment extends Fragment {

    public DeviceInfoFragment(){}
    private String mReport = "Empty";
    private LinearLayout mLayout;
    private String mAppName = "Device info";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_deviceinfo, container, false);
        Activity activity = getActivity();

        mLayout = (LinearLayout)rootView.findViewById(R.id.layout);
        mReport = activity.getString(R.string.report) + "\n";


        setTextOfLabel(true, "\n" + activity.getString(R.string.device));
        setTextOfLabel(false, String.format(activity.getString(R.string.device_board), android.os.Build.BOARD));
        setTextOfLabel(false, String.format(activity.getString(R.string.device_brand), android.os.Build.BRAND));
        setTextOfLabel(false, String.format(activity.getString(R.string.device_device), android.os.Build.DEVICE));
        setTextOfLabel(false, String.format(activity.getString(R.string.device_model), android.os.Build.MODEL));
        setTextOfLabel(false, String.format(activity.getString(R.string.device_product), android.os.Build.PRODUCT));
        setTextOfLabel(false, String.format(activity.getString(R.string.device_tags), android.os.Build.TAGS));

        setTextOfLabel(true, "\n" + activity.getString(R.string.os));
        setTextOfLabel(false, String.format(activity.getString(R.string.os_build), android.os.Build.VERSION.RELEASE, android.os.Build.VERSION.INCREMENTAL));
        setTextOfLabel(false, String.format(activity.getString(R.string.os_build_display), android.os.Build.DISPLAY));
        setTextOfLabel(false, String.format(activity.getString(R.string.os_build_fingerprint), android.os.Build.FINGERPRINT));
        setTextOfLabel(false, String.format(activity.getString(R.string.os_build_id), android.os.Build.ID));
        setTextOfLabel(false, String.format(activity.getString(R.string.os_time), android.os.Build.TIME));
        setTextOfLabel(false, String.format(activity.getString(R.string.os_type), android.os.Build.TYPE));
        setTextOfLabel(false, String.format(activity.getString(R.string.os_user), android.os.Build.USER));

        setTextOfLabel(true, "\n" + activity.getString(R.string.density));
        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        setTextOfLabel(false, String.format(activity.getString(R.string.density_density), metrics.density));
        setTextOfLabel(false, String.format(activity.getString(R.string.density_dpi), metrics.densityDpi));
        setTextOfLabel(false, String.format(activity.getString(R.string.density_scaled), metrics.scaledDensity));
        setTextOfLabel(false, String.format(activity.getString(R.string.density_xdpi), metrics.xdpi));
        setTextOfLabel(false, String.format(activity.getString(R.string.density_ydpi), metrics.ydpi));
        setTextOfLabel(true, "\n" + activity.getString(R.string.density_reference));
        setTextOfLabel(false, String.format(activity.getString(R.string.density_reference_default), DisplayMetrics.DENSITY_DEFAULT));
        setTextOfLabel(false, String.format(activity.getString(R.string.density_reference_low), DisplayMetrics.DENSITY_LOW));
        setTextOfLabel(false, String.format(activity.getString(R.string.density_reference_medium), DisplayMetrics.DENSITY_MEDIUM));
        setTextOfLabel(false, String.format(activity.getString(R.string.density_reference_high), DisplayMetrics.DENSITY_HIGH));

        setTextOfLabel(true, "\n" + activity.getString(R.string.screen));
        setTextOfLabel(false, String.format(activity.getString(R.string.screen_height), metrics.heightPixels));
        setTextOfLabel(false, String.format(activity.getString(R.string.screen_width), metrics.widthPixels));


	/*
	@todo This code does not provides any benefits. The strings are currently undefined.
	In case this should provide the chosen resources, please note, that the strings should be translatable,
	which increases the amount of required resource directories.
	
	Some of these values are unnecessary:
	@see http://stackoverflow.com/questions/3166501/getting-the-screen-density-programmatically-in-android
	
        setTextOfLabel(true, "** Resources:");
        setTextOfResource("values-nokeys", R.string.nokeys);
        setTextOfResource("values-12key", R.string.keys12);
        setTextOfResource("values-qwerty", R.string.qwerty);
        setTextOfResource("values-dpad", R.string.dpad);
        setTextOfResource("values-nonav", R.string.nonav);
        setTextOfResource("values-trackball", R.string.trackball);
        setTextOfResource("values-wheel", R.string.wheel);
        setTextOfResource("values-stylus", R.string.stylus);
        setTextOfResource("values-finger", R.string.finger);
        setTextOfResource("values-notouch", R.string.notouch);

        setTextOfResource("values-nodpi", R.string.nodpi);
        setTextOfResource("values-ldpi", R.string.ldpi);
        setTextOfResource("values-mdpi", R.string.mdpi);
        setTextOfResource("values-hdpi", R.string.hdpi);
        setTextOfResource("values-xhdpi", R.string.xhdpi);
        setTextOfResource("prefered density", R.string.pref_pdi);

        setTextOfResource("values-small", R.string.small);
        setTextOfResource("values-normal", R.string.normal);
        setTextOfResource("values-large", R.string.large);
        setTextOfResource("values-xlarge", R.string.xlarge);
        setTextOfResource("prefered screen", R.string.pref_screen);

        setTextOfResource("values-long", R.string.long_resource);
        setTextOfResource("values-notlong", R.string.notlong);

        setTextOfResource("values-keysexposed", R.string.keysexposed);
        setTextOfResource("values-keyssoft", R.string.keysoft);
        */

        return rootView;



    }

    private void setTextOfResource(String resName, int resId) {
        setTextOfLabel(false, String.format("%s : %s", resName, getText(resId)));
    }

    private String getPkgVersion(String packageName) {
        try {
            PackageInfo info = getActivity().getApplication().getPackageManager().getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            return packageName+" "+info.versionName+" ("+info.versionCode+")";
        } catch (PackageManager.NameNotFoundException e) {
            return "Failed to get '"+packageName+"' info: "+e.getMessage();
        }
    }

    private void setTextOfLabel(boolean bold, String text)
    {
        TextView label = new TextView(getActivity());
        label.setText(text);
        label.setAnimation(AnimationUtils.loadAnimation(this.getActivity(),
                R.anim.slide_out));
        label.setTypeface(Typeface.DEFAULT, bold?Typeface.BOLD : Typeface.NORMAL);
        mLayout.addView(label);
        mReport = mReport + "\n" + text;
    }

}

