

package com.naman14.stools.cpu;


import android.os.SystemClock;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CpuStateMonitor {

    public static final String TIME_IN_STATE_PATH =
        "/sys/devices/system/cpu/cpu0/cpufreq/stats/time_in_state";

    private static final String TAG = "CpuStateMonitor";

    private List<CpuState>      _states = new ArrayList<CpuState>();
    private Map<Integer, Long>  _offsets = new HashMap<Integer, Long>();


    public class CpuStateMonitorException extends Exception {
        public CpuStateMonitorException(String s) {
            super(s);
        }
    }


    public class CpuState implements Comparable<CpuState> {

        public CpuState(int a, long b) { freq = a; duration = b; }

        public int freq = 0;
        public long duration = 0;


        public int compareTo(CpuState state) {
            Integer a = new Integer(freq);
            Integer b = new Integer(state.freq);
            return a.compareTo(b);
        }
    }


    public List<CpuState> getStates() {
        List<CpuState> states = new ArrayList<CpuState>();


        for (CpuState state : _states) {
            long duration = state.duration;
            if (_offsets.containsKey(state.freq)) {
                long offset = _offsets.get(state.freq);
                if (offset <= duration) {
                    duration -= offset;
                } else {

                    _offsets.clear();
                    return getStates();
                }
            }

            states.add(new CpuState(state.freq, duration));
        }

        return states;
    }


    public long getTotalStateTime() {
        long sum = 0;
        long offset = 0;

        for (CpuState state : _states) {
            sum += state.duration;
        }

        for (Map.Entry<Integer, Long> entry : _offsets.entrySet()) {
            offset += entry.getValue();
        }

        return sum - offset;
    }


    public Map<Integer, Long> getOffsets() {
        return _offsets;
    }


    public void setOffsets(Map<Integer, Long> offsets) {
        _offsets = offsets;
    }


    public void setOffsets() throws CpuStateMonitorException {
        _offsets.clear();
        updateStates();

        for (CpuState state : _states) {
            _offsets.put(state.freq, state.duration);
        }
    }


    public void removeOffsets() {
        _offsets.clear();
    }


    public List<CpuState> updateStates()
        throws CpuStateMonitorException {

        try {
            InputStream is = new FileInputStream(TIME_IN_STATE_PATH);
            InputStreamReader ir = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(ir);
            _states.clear();
            readInStates(br);
            is.close();
        } catch (IOException e) {
            throw new CpuStateMonitorException(
                    "Problem opening time-in-states file");
        }


        long sleepTime = (SystemClock.elapsedRealtime()
                - SystemClock.uptimeMillis()) / 10;
        _states.add(new CpuState(0, sleepTime));

        Collections.sort(_states, Collections.reverseOrder());

        return _states;
    }

    private void readInStates(BufferedReader br)
        throws CpuStateMonitorException {
        try {
            String line;
            while ((line = br.readLine()) != null) {

                String[] nums = line.split(" ");
                _states.add(new CpuState(
                        Integer.parseInt(nums[0]),
                        Long.parseLong(nums[1])));
            }
        } catch (IOException e) {
            throw new CpuStateMonitorException(
                    "Problem processing time-in-states file");
        }
    }
}
