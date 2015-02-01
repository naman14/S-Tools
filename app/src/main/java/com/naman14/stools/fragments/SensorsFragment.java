package com.naman14.stools.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.naman14.stools.R;
import com.naman14.stools.sensors.SensorAdapter;
import com.naman14.stools.sensors.SensorGraphActivity;

/**
 * Created by naman on 30/01/15.
 */
public class SensorsFragment  extends ListFragment implements AdapterView.OnItemLongClickListener{

    public SensorsFragment(){}
    private ListView mListView;
    private BaseAdapter mAdapter;




    @Override public void onActivityCreated(
            Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(
                this.getActivity(), R.anim.layout_controller_scale);
        getListView().setLayoutAnimation(controller);
        getListView().setOnItemLongClickListener(this);}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final  View v = inflater.inflate(R.layout.fragment_sensors, container, false);



        SensorManager sensorManager = (SensorManager)getActivity().getSystemService(Context.SENSOR_SERVICE);
        setListAdapter(new SensorAdapter(getActivity(), 0, sensorManager.getSensorList(Sensor.TYPE_ALL)));





        return v;
    }



    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {

        Intent intent = new Intent(getActivity(),SensorGraphActivity.class);
        intent.putExtra(SensorGraphActivity.SENSORINDEX,position);
        startActivity(intent);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

        Sensor sensor = (Sensor)parent.getItemAtPosition(position);
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.detail_sensor,null);
        ((TextView)layout.findViewById(R.id.vendor_value)).setText(sensor.getVendor());
        ((TextView)layout.findViewById(R.id.power_value)).setText(sensor.getPower()+getString(R.string.unit_consumption));
        ((TextView)layout.findViewById(R.id.resolution_value)).setText(sensor.getResolution()+"");
        ((TextView)layout.findViewById(R.id.version_value)).setText(sensor.getVersion()+"");
        ((TextView)layout.findViewById(R.id.delay_value)).setText(sensor.getMinDelay()+getString(R.string.unit_mindelay));
        ((TextView)layout.findViewById(R.id.range_value)).setText(sensor.getMaximumRange()+"");
        ScrollView scrollView = new ScrollView(getActivity());
        scrollView.addView(layout);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(sensor.getName());
        int imageResource = R.drawable.ic_sensor_unknown;
        try {

            imageResource = getResources().getIdentifier("drawable/ic_sensor_"+sensor.getType(), null, "com.naman14.stools");
            if (imageResource==0) {
                imageResource=R.drawable.ic_sensor_unknown;
            }
        }
        catch (Exception e) {}
        builder.setIcon(imageResource);
        builder.setView(scrollView);
        builder.create().show();

        return true;
    }

}






