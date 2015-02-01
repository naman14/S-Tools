package com.naman14.stools.location;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.Drawable;
import android.hardware.GeomagneticField;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBarActivity;
import android.telephony.CellLocation;
import android.telephony.NeighboringCellInfo;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.naman14.stools.R;
import com.naman14.stools.location.widgets.GpsSnrView;
import com.naman14.stools.location.widgets.GpsStatusView;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static android.telephony.PhoneStateListener.LISTEN_CELL_INFO;
import static android.telephony.PhoneStateListener.LISTEN_CELL_LOCATION;
import static android.telephony.PhoneStateListener.LISTEN_NONE;
import static android.telephony.PhoneStateListener.LISTEN_SIGNAL_STRENGTHS;
import static android.telephony.TelephonyManager.PHONE_TYPE_CDMA;
import static android.telephony.TelephonyManager.PHONE_TYPE_GSM;

/**
 * Created by naman on 31/01/15.
 */
public class LocationActivity extends ActionBarActivity implements ActionBar.TabListener, GpsStatus.Listener, LocationListener, SensorEventListener, ViewPager.OnPageChangeListener {


    SectionsPagerAdapter mSectionsPagerAdapter;


    ViewPager mViewPager;


    private static final int iSensorRate = 200000;

    private LocationManager mLocationManager;
    private SensorManager mSensorManager;
    private Sensor mOrSensor;

    private long mOrLast = 0;

    private static TelephonyManager mTelephonyManager;
    private static WifiManager mWifiManager;


    protected static boolean isGpsViewReady = false;
    protected static LinearLayout gpsRootLayout;
    protected static GpsStatusView gpsStatusView;
    protected static GpsSnrView gpsSnrView;
    protected static TextView gpsLat;
    protected static TextView gpsLon;
    protected static TextView orDeclination;
    protected static TextView gpsSpeed;
    protected static TextView gpsAlt;
    protected static TextView gpsTime;
    protected static TextView gpsBearing;
    protected static TextView gpsAccuracy;
    protected static TextView gpsOrientation;
    protected static TextView gpsSats;
    protected static TextView gpsTtff;


    protected static boolean isRadioViewReady = false;
    protected static TextView rilMcc;
    protected static TextView rilMnc;
    protected static TextView rilCellId;
    protected static TextView rilLac;
    protected static TextView rilAsu;
    protected static TableLayout rilCells;
    protected static TextView rilSid;
    protected static TextView rilNid;
    protected static TextView rilBsid;
    protected static TextView rilCdmaAsu;
    protected static TableLayout rilCdmaCells;
    protected static TableLayout wifiAps;


    private final static Integer orFromRot[] = {
            ActivityInfo.SCREEN_ORIENTATION_PORTRAIT,
            ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE,
            ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT,
            ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE};

    @SuppressLint("UseSparseArrays")
    private final static HashMap<Integer, Integer> channelsFrequency = new HashMap<Integer, Integer>() {

        private static final long serialVersionUID = 6793015643527778045L;

        {

            this.put(2412, 1);
            this.put(2417, 2);
            this.put(2422, 3);
            this.put(2427, 4);
            this.put(2432, 5);
            this.put(2437, 6);
            this.put(2442, 7);
            this.put(2447, 8);
            this.put(2452, 9);
            this.put(2457, 10);
            this.put(2462, 11);
            this.put(2467, 12);
            this.put(2472, 13);
            this.put(2484, 14);

            //5 GHz (802.11 a/h/j/n/ac)
            this.put(4915, 183);
            this.put(4920, 184);
            this.put(4925, 185);
            this.put(4935, 187);
            this.put(4940, 188);
            this.put(4945, 189);
            this.put(4960, 192);
            this.put(4980, 196);

            this.put(5035, 7);
            this.put(5040, 8);
            this.put(5045, 9);
            this.put(5055, 11);
            this.put(5060, 12);
            this.put(5080, 16);

            this.put(5170, 34);
            this.put(5180, 36);
            this.put(5190, 38);
            this.put(5200, 40);
            this.put(5210, 42);
            this.put(5220, 44);
            this.put(5230, 46);
            this.put(5240, 48);
            this.put(5260, 52);
            this.put(5280, 56);
            this.put(5300, 60);
            this.put(5320, 64);

            this.put(5500, 100);
            this.put(5520, 104);
            this.put(5540, 108);
            this.put(5560, 112);
            this.put(5580, 116);
            this.put(5600, 120);
            this.put(5620, 124);
            this.put(5640, 128);
            this.put(5660, 132);
            this.put(5680, 136);
            this.put(5700, 140);
            this.put(5745, 149);
            this.put(5765, 153);
            this.put(5785, 157);
            this.put(5805, 161);
            this.put(5825, 165);
        }
    };


    private final PhoneStateListener mPhoneStateListener = new PhoneStateListener() {


        public void onCellLocationChanged (CellLocation location) {
            if (isRadioViewReady) {
                showCellLocation(location);
                if (mTelephonyManager.getPhoneType() == PHONE_TYPE_GSM) {

                    List<NeighboringCellInfo> neighboringCells = mTelephonyManager.getNeighboringCellInfo();
                    showNeighboringCellInfo(neighboringCells);
                }
            }
        }

        public void onSignalStrengthsChanged (SignalStrength signalStrength) {
            if (isRadioViewReady) {
                int pt = mTelephonyManager.getPhoneType();
                if (pt == PHONE_TYPE_GSM) {
                    rilAsu.setText(String.valueOf(signalStrength.getGsmSignalStrength() * 2 - 113));

                    List<NeighboringCellInfo> neighboringCells = mTelephonyManager.getNeighboringCellInfo();
                    showNeighboringCellInfo(neighboringCells);
                } else if (pt == PHONE_TYPE_CDMA) {
                    rilCdmaAsu.setText(String.valueOf(signalStrength.getCdmaDbm()));
                }
            }
        }
    };

    private final BroadcastReceiver mWifiScanReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context c, Intent intent) {
            if (intent.getAction() == WifiManager.SCAN_RESULTS_AVAILABLE_ACTION) {
                List <ScanResult> scanResults = mWifiManager.getScanResults();
                if ((isRadioViewReady) && (scanResults != null)) {
                    wifiAps.removeAllViews();
                    for (ScanResult result : scanResults) {
                        TableRow row0 = new TableRow(wifiAps.getContext());
                        View divider = new View(wifiAps.getContext());
                        divider.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1, 1));
                        divider.setBackgroundColor(getResources().getColor(android.R.color.tertiary_text_dark));
                        row0.addView(divider);
                        wifiAps.addView(row0, new TableLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

                        TableRow row1 = new TableRow(wifiAps.getContext());
                        //row.setPadding(0, (int) getResources().getDimension(R.dimen.activity_horizontal_margin), 0, 0);
                        TextView newMac = new TextView(wifiAps.getContext());
                        newMac.setLayoutParams(new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 14));
                        newMac.setTextAppearance(wifiAps.getContext(), android.R.style.TextAppearance_Medium);
                        newMac.setText(result.BSSID);
                        row1.addView(newMac);
                        TextView newCh = new TextView(wifiAps.getContext());
                        newCh.setLayoutParams(new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 2));
                        newCh.setTextAppearance(wifiAps.getContext(), android.R.style.TextAppearance_Medium);
                        newCh.setText(getChannelFromFrequency(result.frequency));
                        row1.addView(newCh);
                        TextView newLevel = new TextView(wifiAps.getContext());
                        newLevel.setLayoutParams(new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 3));
                        newLevel.setTextAppearance(wifiAps.getContext(), android.R.style.TextAppearance_Medium);
                        newLevel.setText(String.valueOf(result.level));
                        row1.addView(newLevel);
                        wifiAps.addView(row1,new TableLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

                        TableRow row2 = new TableRow(wifiAps.getContext());
                        TextView newSSID = new TextView(wifiAps.getContext());
                        newSSID.setLayoutParams(new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 17));
                        newSSID.setTextAppearance(wifiAps.getContext(), android.R.style.TextAppearance_Small);
                        newSSID.setText(result.SSID);
                        row2.addView(newSSID);
                        wifiAps.addView(row2, new TableLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    }
                }
            } else {

                mWifiManager.startScan();
            }
        }
    };



    public String formatOrientation(float bearing) {
        return
                (bearing < 11.25) ? getString(R.string.value_N) :
                        (bearing < 33.75) ? getString(R.string.value_NNE) :
                                (bearing < 56.25) ? getString(R.string.value_NE) :
                                        (bearing < 78.75) ? getString(R.string.value_ENE) :
                                                (bearing < 101.25) ? getString(R.string.value_E) :
                                                        (bearing < 123.75) ? getString(R.string.value_ESE) :
                                                                (bearing < 146.25) ? getString(R.string.value_SE) :
                                                                        (bearing < 168.75) ? getString(R.string.value_SSE) :
                                                                                (bearing < 191.25) ? getString(R.string.value_S) :
                                                                                        (bearing < 213.75) ? getString(R.string.value_SSW) :
                                                                                                (bearing < 236.25) ? getString(R.string.value_SW) :
                                                                                                        (bearing < 258.75) ? getString(R.string.value_WSW) :
                                                                                                                (bearing < 280.25) ? getString(R.string.value_W) :
                                                                                                                        (bearing < 302.75) ? getString(R.string.value_WNW) :
                                                                                                                                (bearing < 325.25) ? getString(R.string.value_NW) :
                                                                                                                                        (bearing < 347.75) ? getString(R.string.value_NNW) :
                                                                                                                                                getString(R.string.value_N);
    }



    public static String getChannelFromFrequency(int frequency) {
        if (channelsFrequency.containsKey(frequency)) {
            return String.valueOf(channelsFrequency.get(frequency));
        }
        else {
            return "?";
        }
    }




    public void onAccuracyChanged(Sensor sensor, int accuracy) {}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_main);


        final ActionBar actionBar = getSupportActionBar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(0);


        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);


        setEmbeddedTabs(actionBar, true);


        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());


        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOnPageChangeListener(this);


        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            actionBar.addTab(
                    actionBar.newTab()

                            .setIcon(mSectionsPagerAdapter.getPageIcon(i))
                            .setTabListener(this));
        }

        mLocationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        mSensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);

        mOrSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);

        mTelephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        mWifiManager = (WifiManager)getSystemService(Context.WIFI_SERVICE);


    }


    public void onGpsStatusChanged (int event) {
        if (isGpsViewReady) {
            GpsStatus status = mLocationManager.getGpsStatus(null);
            int satsInView = 0;
            int satsUsed = 0;
            Iterable<GpsSatellite> sats = status.getSatellites();
            for (GpsSatellite sat : sats) {
                satsInView++;
                if (sat.usedInFix()) {
                    satsUsed++;
                }
            }
            gpsSats.setText(String.valueOf(satsUsed) + "/" + String.valueOf(satsInView));
            gpsTtff.setText(String.valueOf(status.getTimeToFirstFix() / 1000));
            gpsStatusView.showSats(sats);
            gpsSnrView.showSats(sats);
        }
    }


    public void onLocationChanged(Location location) {

        if (isGpsViewReady) {
            if (location.hasAccuracy()) {
                gpsAccuracy.setText(String.format("%.0f", location.getAccuracy()));
            } else {
                gpsAccuracy.setText(getString(R.string.value_none));
            }

            gpsLat.setText(String.format("%.5f%s", location.getLatitude(), getString(R.string.unit_degree)));
            gpsLon.setText(String.format("%.5f%s", location.getLongitude(), getString(R.string.unit_degree)));
            gpsTime.setText(String.format("%1$tY-%1$tm-%1$td %1$tH:%1$tM:%1$tS", location.getTime()));

            if (location.hasAltitude()) {
                gpsAlt.setText(String.format("%.0f", location.getAltitude()));
                orDeclination.setText(String.format("%.0f%s", new GeomagneticField(
                        (float) location.getLatitude(),
                        (float) location.getLongitude(),
                        (float) location.getAltitude(),
                        location.getTime()
                ).getDeclination(), getString(R.string.unit_degree)));
            } else {
                gpsAlt.setText(getString(R.string.value_none));
                orDeclination.setText(getString(R.string.value_none));
            }

            if (location.hasBearing()) {
                gpsBearing.setText(String.format("%.0f%s", location.getBearing(), getString(R.string.unit_degree)));
                gpsOrientation.setText(formatOrientation(location.getBearing()));
            } else {
                gpsBearing.setText(getString(R.string.value_none));
                gpsOrientation.setText(getString(R.string.value_none));
            }

            if (location.hasSpeed()) {
                gpsSpeed.setText(String.format("%.0f", (location.getSpeed()) * 3.6));
            } else {
                gpsSpeed.setText(getString(R.string.value_none));
            }


        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:
                this.finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onPageSelected(int position) {

        getSupportActionBar().setSelectedNavigationItem(position);

    }


    public void onProviderDisabled(String provider) {}


    public void onProviderEnabled(String provider) {}

    @Override
    protected void onResume() {
        super.onResume();

        if (mLocationManager.getAllProviders().indexOf(LocationManager.GPS_PROVIDER) >= 0) {
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        } else {
            Log.w("MainActivity", "No GPS location provider found. GPS data display will not be available.");
        }
        mLocationManager.addGpsStatusListener(this);
        mSensorManager.registerListener(this, mOrSensor, iSensorRate);

        mTelephonyManager.listen(mPhoneStateListener, (LISTEN_CELL_INFO | LISTEN_CELL_LOCATION | LISTEN_SIGNAL_STRENGTHS));


        registerReceiver(mWifiScanReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));


        registerReceiver(mWifiScanReceiver, new IntentFilter(WifiManager.NETWORK_STATE_CHANGED_ACTION));


        registerReceiver(mWifiScanReceiver, new IntentFilter(WifiManager.RSSI_CHANGED_ACTION));


        registerReceiver(mWifiScanReceiver, new IntentFilter(WifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION));
    }


    public void onSensorChanged(SensorEvent event) {

        boolean isRateElapsed = false;

        switch (event.sensor.getType()) {

            case Sensor.TYPE_ORIENTATION:
                isRateElapsed = (event.timestamp / 1000) - mOrLast >= iSensorRate;
                break;

        }

        
        if (isGpsViewReady && isRateElapsed) {
            switch (event.sensor.getType()) {
                case Sensor.TYPE_ORIENTATION:
                    gpsStatusView.setYaw(event.values[0]);
                    break;
            }
        }
    }


    public void onStatusChanged(String provider, int status, Bundle extras) {}

    @Override
    protected void onStop() {
        mLocationManager.removeUpdates(this);
        mLocationManager.removeGpsStatusListener(this);
        mSensorManager.unregisterListener(this);
        mTelephonyManager.listen(mPhoneStateListener, LISTEN_NONE);
        unregisterReceiver(mWifiScanReceiver);
        super.onStop();
    }




    @Override
    public void onTabReselected(Tab tab, FragmentTransaction ft) {

    }

    @Override
    public void onTabSelected(Tab tab, FragmentTransaction ft) {

        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(Tab tab, FragmentTransaction ft) {

    }

    private void setEmbeddedTabs(Object actionBar, Boolean embed_tabs) {
        try {

            Method setHasEmbeddedTabsMethod = actionBar.getClass()
                    .getDeclaredMethod("setHasEmbeddedTabs", boolean.class);
            setHasEmbeddedTabsMethod.setAccessible(true);
            setHasEmbeddedTabsMethod.invoke(actionBar, embed_tabs);
        } catch (Exception e) {
            Log.e("", "Error marking actionbar embedded", e);
        }
    }


    protected static void showCellLocation (CellLocation location) {
        if (isRadioViewReady) {
            if (location instanceof GsmCellLocation) {
                String networkOperator = mTelephonyManager.getNetworkOperator();

                int cid = ((GsmCellLocation) location).getCid();
                int lac = ((GsmCellLocation) location).getLac();

                if (networkOperator.length() >= 3) {
                    rilMcc.setText(networkOperator.substring(0, 3));
                    rilMnc.setText(networkOperator.substring(3));
                } else {
                    rilMcc.setText(rilMcc.getContext().getString(R.string.value_none));
                    rilMnc.setText(rilMnc.getContext().getString(R.string.value_none));
                }
                rilCellId.setText(String.valueOf(cid));
                rilLac.setText(String.valueOf(lac));
            } else if (location instanceof CdmaCellLocation) {
                int sid = ((CdmaCellLocation) location).getSystemId();
                int nid = ((CdmaCellLocation) location).getNetworkId();
                int bsid = ((CdmaCellLocation) location).getBaseStationId();
                rilSid.setText(String.valueOf(sid));
                rilNid.setText(String.valueOf(nid));
                rilBsid.setText(String.valueOf(bsid));
            }
        }
    }


    protected static void showNeighboringCellInfo (List <NeighboringCellInfo> neighboringCells) {
        if ((isRadioViewReady) && (neighboringCells != null)) {
            rilCells.removeAllViews();
            for (NeighboringCellInfo cell : neighboringCells) {
                TableRow row = new TableRow(rilCells.getContext());
                TextView newMcc = new TextView(rilCells.getContext());
                newMcc.setLayoutParams(new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 3));
                newMcc.setTextAppearance(rilCells.getContext(), android.R.style.TextAppearance_Medium);
                newMcc.setText(rilCells.getContext().getString(R.string.value_none));
                row.addView(newMcc);
                TextView newMnc = new TextView(rilCells.getContext());
                newMnc.setLayoutParams(new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 3));
                newMnc.setTextAppearance(rilCells.getContext(), android.R.style.TextAppearance_Medium);
                newMnc.setText(rilCells.getContext().getString(R.string.value_none));
                row.addView(newMnc);
                TextView newLac = new TextView(rilCells.getContext());
                newLac.setLayoutParams(new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 7));
                newLac.setTextAppearance(rilCells.getContext(), android.R.style.TextAppearance_Medium);
                newLac.setText(String.valueOf(cell.getLac()));
                TextView newCid = new TextView(rilCells.getContext());
                newCid.setLayoutParams(new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 9));
                newCid.setTextAppearance(rilCells.getContext(), android.R.style.TextAppearance_Medium);
                newCid.setText(String.valueOf(cell.getCid()));
                row.addView(newCid);
                row.addView(newLac);
                TextView newDbm = new TextView(rilCells.getContext());
                newDbm.setLayoutParams(new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 2));
                newDbm.setTextAppearance(rilCells.getContext(), android.R.style.TextAppearance_Medium);
                newDbm.setText(String.valueOf(cell.getRssi() * 2 - 113));
                row.addView(newDbm);
                rilCells.addView(row,new TableLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            }
        }
    }



    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            Fragment fragment;
            switch (position) {
                case 0:
                    fragment = new GpsSectionFragment();
                    return fragment;
                case 1:
                    fragment = new RadioSectionFragment();
                    return fragment;


            }
            return null;
        }

        @Override
        public int getCount() {

            return 2;
        }

        public Drawable getPageIcon(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getResources().getDrawable(R.drawable.ic_action_gps);
                case 1:
                    return getResources().getDrawable(R.drawable.ic_action_radio);

            }
            return null;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.title_section1).toUpperCase(l);
                case 1:
                    return getString(R.string.title_section3).toUpperCase(l);

            }
            return null;
        }
    }


    public static class GpsSectionFragment extends Fragment {

        public static final String ARG_SECTION_NUMBER = "section_number";

        public GpsSectionFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main_gps, container, false);



            gpsRootLayout = (LinearLayout) rootView.findViewById(R.id.gpsRootLayout);
            gpsSnrView = (GpsSnrView) rootView.findViewById(R.id.gpsSnrView);
            gpsStatusView = new GpsStatusView(rootView.getContext());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.gravity = Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL;
            params.weight = 1;
            gpsRootLayout.addView(gpsStatusView, 0, params);
            gpsLat = (TextView) rootView.findViewById(R.id.gpsLat);
            gpsLon = (TextView) rootView.findViewById(R.id.gpsLon);
            orDeclination = (TextView) rootView.findViewById(R.id.orDeclination);
            gpsSpeed = (TextView) rootView.findViewById(R.id.gpsSpeed);
            gpsAlt = (TextView) rootView.findViewById(R.id.gpsAlt);
            gpsTime = (TextView) rootView.findViewById(R.id.gpsTime);
            gpsBearing = (TextView) rootView.findViewById(R.id.gpsBearing);
            gpsAccuracy = (TextView) rootView.findViewById(R.id.gpsAccuracy);
            gpsOrientation = (TextView) rootView.findViewById(R.id.gpsOrientation);
            gpsSats = (TextView) rootView.findViewById(R.id.gpsSats);
            gpsTtff = (TextView) rootView.findViewById(R.id.gpsTtff);

            isGpsViewReady = true;

            return rootView;
        }

        @Override
        public void onDestroyView() {
            super.onDestroyView();
            isGpsViewReady = false;
        }
    }






    public static class RadioSectionFragment extends Fragment {

        public static final String ARG_SECTION_NUMBER = "section_number";

        public RadioSectionFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main_radio, container, false);

            // Initialize controls
            rilMcc = (TextView) rootView.findViewById(R.id.rilMcc);
            rilMnc = (TextView) rootView.findViewById(R.id.rilMnc);
            rilCellId = (TextView) rootView.findViewById(R.id.rilCellId);
            rilLac = (TextView) rootView.findViewById(R.id.rilLac);
            rilAsu = (TextView) rootView.findViewById(R.id.rilAsu);
            rilCells = (TableLayout) rootView.findViewById(R.id.rilCells);

            rilSid = (TextView) rootView.findViewById(R.id.rilSid);
            rilNid = (TextView) rootView.findViewById(R.id.rilNid);
            rilBsid = (TextView) rootView.findViewById(R.id.rilBsid);
            rilCdmaAsu = (TextView) rootView.findViewById(R.id.rilCdmaAsu);
            rilCdmaCells = (TableLayout) rootView.findViewById(R.id.rilCdmaCells);

            wifiAps = (TableLayout) rootView.findViewById(R.id.wifiAps);

            isRadioViewReady = true;


            CellLocation cellLocation = mTelephonyManager.getCellLocation();
            showCellLocation(cellLocation);


            List<NeighboringCellInfo> neighboringCells = mTelephonyManager.getNeighboringCellInfo();
            showNeighboringCellInfo(neighboringCells);



            mWifiManager.startScan();

            return rootView;
        }

        @Override
        public void onDestroyView() {
            super.onDestroyView();
            isRadioViewReady = false;
        }
    }
}
