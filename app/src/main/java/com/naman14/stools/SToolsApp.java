package com.naman14.stools;

import android.app.Application;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.util.Log;

import com.naman14.stools.cpu.CpuStateMonitor;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

/**
 * Created by naman on 30/01/15.
 */
public class SToolsApp extends Application {

    private static final String KERNEL_VERSION_PATH = "/proc/version";

    private static final String TAG = "CpuSpyApp";
    private List<Sensor> mSensorList= new Vector<Sensor>();
    private SensorManager mSensorManager;

    private static final String PREF_NAME = "CpuSpyPreferences";
    private static final String PREF_OFFSETS = "offsets";


    private CpuStateMonitor _monitor = new CpuStateMonitor();

    private String _kernelVersion = "";
    public void setSensorManager(SensorManager mgr){
        this.mSensorManager = mgr;
    }

    public SensorManager getSensorManager(){
        return this.mSensorManager;
    }

    public void setSensorList(List<Sensor> list){
        this.mSensorList.clear();
        this.mSensorList.addAll(list);
    }

    public List<Sensor> getSensorList(){
        return this.mSensorList;
    }

    public Sensor getSensor(int index){
        if(index <0 || index >= mSensorList.size()) return null;
        else return mSensorList.get(index);
    }



    @Override public void onCreate(){
        loadOffsets();
        updateKernelVersion();
    }


    public String getKernelVersion() {
        return _kernelVersion;
    }


    public CpuStateMonitor getCpuStateMonitor() {
        return _monitor;
    }


    public void loadOffsets() {
        SharedPreferences settings = getSharedPreferences(
                PREF_NAME, MODE_PRIVATE);
        String prefs = settings.getString (PREF_OFFSETS, "");

        if (prefs == null || prefs.length() < 1) {
            return;
        }


        Map<Integer, Long> offsets = new HashMap<Integer, Long>();
        String[] sOffsets = prefs.split(",");
        for (String offset : sOffsets) {
            String[] parts = offset.split(" ");
            offsets.put (Integer.parseInt(parts[0]),
                    Long.parseLong(parts[1]));
        }

        _monitor.setOffsets(offsets);
    }


    public void saveOffsets() {
        SharedPreferences settings = getSharedPreferences(
                PREF_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();


        String str = "";
        for (Map.Entry<Integer, Long> entry :
                _monitor.getOffsets().entrySet()) {
            str += entry.getKey() + " " + entry.getValue() + ",";
        }

        editor.putString(PREF_OFFSETS, str);
        editor.commit();
    }


    public String updateKernelVersion() {
        try {
            InputStream is = new FileInputStream(KERNEL_VERSION_PATH);
            InputStreamReader ir = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(ir);

            String line;
            while ((line = br.readLine())!= null ) {
                _kernelVersion = line;
            }

            is.close();
        } catch (IOException e) {
            Log.e(TAG, "Problem reading kernel version file");
            return "";
        }


        return _kernelVersion;
    }
}



