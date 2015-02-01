package com.naman14.stools.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.naman14.stools.R;
import com.naman14.stools.SToolsApp;
import com.naman14.stools.cpu.CpuStateMonitor;
import com.naman14.stools.cpu.CpuStateMonitor.CpuState;
import com.naman14.stools.cpu.CpuStateMonitor.CpuStateMonitorException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by naman on 30/01/15.
 */
public class CpuFragment extends Fragment {

    public CpuFragment(){}
    private SToolsApp _app = null;


    private GridLayout _uiStatesView = null;
    private TextView _uiAdditionalStates = null;
    private TextView        _uiTotalStateTime = null;
    private TextView        _uiHeaderAdditionalStates = null;
    private TextView        _uiHeaderTotalStateTime = null;
    private TextView        _uiStatesWarning = null;
    private TextView        _uiKernelString = null;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        final View v = inflater.inflate(R.layout.fragment_cpu, container, false);

        _app = (SToolsApp)getActivity().getApplicationContext();
        setHasOptionsMenu(true);


        updateData();

        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findViews();
        setHasOptionsMenu(true);
    }

    @Override public void onResume () {
        super.onResume();
        updateView();

    }

    private void findViews() {
        _uiStatesView = (GridLayout)getView().findViewById(R.id.ui_states_view);
        _uiKernelString = (TextView)getView().findViewById(R.id.ui_kernel_string);
        _uiAdditionalStates = (TextView)getView().findViewById(
                R.id.ui_additional_states);
        _uiHeaderAdditionalStates = (TextView)getView().findViewById(
                R.id.ui_header_additional_states);
        _uiHeaderTotalStateTime = (TextView)getView().findViewById(
                R.id.ui_header_total_state_time);
        _uiStatesWarning = (TextView)getView().findViewById(R.id.ui_states_warning);
        _uiTotalStateTime = (TextView)getView().findViewById(R.id.ui_total_state_time);

    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.home_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
    @Override public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.menu_refresh:
                updateData();
                updateView();
                break;
            case R.id.menu_reset:
                try {
                    _app.getCpuStateMonitor().setOffsets();
                } catch (CpuStateMonitor.CpuStateMonitorException e) {
                    // TODO: something
                }

                _app.saveOffsets();
                updateView();
                break;
            case R.id.menu_restore:
                _app.getCpuStateMonitor().removeOffsets();
                _app.saveOffsets();
                updateView();
                break;
        }


        return true;
    }

    public void updateView() {

        CpuStateMonitor monitor = _app.getCpuStateMonitor();
        _uiStatesView.removeAllViews();
        List<String> extraStates = new ArrayList<String>();
        for (CpuStateMonitor.CpuState state : monitor.getStates()) {
            if (state.duration > 0) {
                generateStateRow(state, _uiStatesView);
            } else {
                if (state.freq == 0) {
                    extraStates.add("Deep Sleep");
                } else {
                    extraStates.add(state.freq/1000 + " MHz");
                }
            }
        }


        if ( monitor.getStates().size() == 0) {
            _uiStatesWarning.setVisibility(View.VISIBLE);
            _uiHeaderTotalStateTime.setVisibility(View.GONE);
            _uiTotalStateTime.setVisibility(View.GONE);
            _uiStatesView.setVisibility(View.GONE);
        }


        long totTime = monitor.getTotalStateTime() / 100;
        _uiTotalStateTime.setText(sToString(totTime));


        if (extraStates.size() > 0) {
            int n = 0;
            String str = "";

            for (String s : extraStates) {
                if (n++ > 0)
                    str += ", ";
                str += s;
            }

            _uiAdditionalStates.setVisibility(View.VISIBLE);
            _uiHeaderAdditionalStates.setVisibility(View.VISIBLE);
            _uiAdditionalStates.setText(str);
        } else {
            _uiAdditionalStates.setVisibility(View.GONE);
            _uiHeaderAdditionalStates.setVisibility(View.GONE);
        }


        _uiKernelString.setText(_app.getKernelVersion());
    }


    public void updateData() {
        CpuStateMonitor monitor = _app.getCpuStateMonitor();
        try {
            monitor.updateStates();
        } catch (CpuStateMonitorException e) {

        }
    }


    private static String sToString(long tSec) {
        long h = (long)Math.floor(tSec / (60*60));
        long m = (long)Math.floor((tSec - h*60*60) / 60);
        long s = tSec % 60;
        String sDur;
        sDur = h + ":";
        if (m < 10)
            sDur += "0";
        sDur += m + ":";
        if (s < 10)
            sDur += "0";
        sDur += s;

        return sDur;
    }


    private View generateStateRow(CpuState state, ViewGroup parent) {

        LayoutInflater inf = LayoutInflater.from((Context)_app);
        LinearLayout theRow = (LinearLayout)inf.inflate(
                R.layout.state_row, parent, false);


        CpuStateMonitor monitor = _app.getCpuStateMonitor();
        float per = (float)state.duration * 100 /
                monitor.getTotalStateTime();
        String sPer = (int)per + "%";


        String sFreq;
        if (state.freq == 0) {
            sFreq = "Deep Sleep";
        } else {
            sFreq = state.freq / 1000 + " MHz";
        }


        long tSec = state.duration / 100;
        String sDur = sToString(tSec);


        TextView freqText = (TextView)theRow.findViewById(R.id.ui_freq_text);
        TextView durText = (TextView)theRow.findViewById(
                R.id.ui_duration_text);
        TextView perText = (TextView)theRow.findViewById(
                R.id.ui_percentage_text);
        ProgressBar bar = (ProgressBar)theRow.findViewById(R.id.ui_bar);


        freqText.setText(sFreq);
        freqText.setAnimation(AnimationUtils.loadAnimation(this.getActivity(),
                R.anim.slide_out));
        perText.setText(sPer);
        perText.setAnimation(AnimationUtils.loadAnimation(this.getActivity(),
                R.anim.slide_out));
        durText.setText(sDur);
        durText.setAnimation(AnimationUtils.loadAnimation(this.getActivity(),
                R.anim.slide_out));



        bar.setProgress((int)per);

        parent.addView(theRow);
        return theRow;
    }

}

