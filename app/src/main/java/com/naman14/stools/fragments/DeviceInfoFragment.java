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

        mLayout = (LinearLayout)rootView.findViewById(R.id.layout);
        mReport = "Device information report:\n";


        setTextOfLabel(true, "** Device:");
        setTextOfLabel(false, "Board: "+android.os.Build.BOARD);
        setTextOfLabel(false, "Brand: "+android.os.Build.BRAND);
        setTextOfLabel(false, "Device: "+android.os.Build.DEVICE);
        setTextOfLabel(false, "Model: "+android.os.Build.MODEL);
        setTextOfLabel(false, "Product: "+android.os.Build.PRODUCT);
        setTextOfLabel(false, "TAGS: "+android.os.Build.TAGS);

        setTextOfLabel(true, "** OS:");
        setTextOfLabel(false, "Build release "+android.os.Build.VERSION.RELEASE + ", Inc: '"+android.os.Build.VERSION.INCREMENTAL+"'");
        setTextOfLabel(false, "Display build: "+android.os.Build.DISPLAY);
        setTextOfLabel(false, "Finger print: "+android.os.Build.FINGERPRINT);
        setTextOfLabel(false, "Build ID: "+android.os.Build.ID);
        setTextOfLabel(false, "Time: "+android.os.Build.TIME);
        setTextOfLabel(false, "Type: "+android.os.Build.TYPE);
        setTextOfLabel(false, "User: "+android.os.Build.USER);

        setTextOfLabel(true, "** Density:");
        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        setTextOfLabel(false, "density: "+metrics.density);
        setTextOfLabel(false, "densityDpi: "+metrics.densityDpi);
        setTextOfLabel(false, "scaledDensity: "+metrics.scaledDensity);
        setTextOfLabel(false, "xdpi: "+metrics.xdpi);
        setTextOfLabel(false, "ydpi: "+metrics.ydpi);
        setTextOfLabel(true, "** Density reference:");
        setTextOfLabel(false, "DENSITY_DEFAULT: "+DisplayMetrics.DENSITY_DEFAULT);
        setTextOfLabel(false, "DENSITY_LOW: "+DisplayMetrics.DENSITY_LOW);
        setTextOfLabel(false, "DENSITY_MEDIUM: "+DisplayMetrics.DENSITY_MEDIUM);
        setTextOfLabel(false, "DENSITY_HIGH: "+DisplayMetrics.DENSITY_HIGH);

        setTextOfLabel(true, "** Screen:");
        setTextOfLabel(false, "heightPixels: "+metrics.heightPixels);
        setTextOfLabel(false, "widthPixels: "+metrics.widthPixels);


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

