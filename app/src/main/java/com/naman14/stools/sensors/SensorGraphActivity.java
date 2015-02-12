/*
   Copyright 2012 Patrick Ahlbrecht

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */

package com.naman14.stools.sensors;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.naman14.stools.R;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;


public class SensorGraphActivity extends Activity implements View.OnTouchListener {


	public static final String SENSORINDEX = "com.naman14.stools.SensorIndex";


	public static final int SAMPLERATE = 10;


	private Sensor sensor;


	private GraphicalView chartView;


	private XYMultipleSeriesDataset sensorData;


	private XYMultipleSeriesRenderer renderer;


	private SensorManager sensorManager;


	private XYSeries channel[];


	private Thread ticker;


	private int xTick = 0;


	private int lastMinX = 0;



	@Override
	public void onCreate(Bundle savedInstanceState) {


		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_PROGRESS);
		sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		int idx = getIntent().getIntExtra(SENSORINDEX, 0);
		sensor = sensorManager.getSensorList(Sensor.TYPE_ALL).get(idx);
		setTitle(sensor.getName());

		sensorData = new XYMultipleSeriesDataset();
		renderer = new XYMultipleSeriesRenderer();
		renderer.setGridColor(Color.DKGRAY);
		renderer.setShowGrid(true);
		renderer.setXAxisMin(0.0);
		renderer.setXTitle(getString(R.string.samplerate, 1000 / SAMPLERATE));
		renderer.setXAxisMax(10000 / (1000 / SAMPLERATE)); // 10 seconds wide
		renderer.setXLabels(10); // 1 second per DIV
		renderer.setChartTitle(" ");
		renderer.setYLabelsAlign(Paint.Align.RIGHT);

        chartView = ChartFactory.getLineChartView(this, sensorData, renderer);
		chartView.setOnTouchListener(this);
		float textSize = new TextView(this).getTextSize();
		float upscale = textSize / renderer.getLegendTextSize();
		renderer.setLabelsTextSize(textSize);
		renderer.setLegendTextSize(textSize);
        renderer.setApplyBackgroundColor(true);

        renderer.setBackgroundColor(Color.WHITE);
        renderer.setMarginsColor(Color.WHITE);
		renderer.setChartTitleTextSize(textSize);
		renderer.setAxisTitleTextSize(textSize);
		renderer.setFitLegend(true);
		int[] margins = renderer.getMargins();
		margins[0] *= upscale;
		margins[1] *= upscale;
		margins[2] = (int) (2*renderer.getLegendTextSize());
		renderer.setMargins(margins);

		setContentView(R.layout.sensor_loading);


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
	protected void onResume() {
		super.onResume();


		switch (getResources().getConfiguration().orientation) {
			case Configuration.ORIENTATION_PORTRAIT: {
				setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
				break;
			}
			case Configuration.ORIENTATION_LANDSCAPE: {
				setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
				break;
			}
		}

		if (xTick == 0) {
			ticker = new Ticker(this);
			ticker.start();
			sensorManager.registerListener((SensorEventListener) ticker, sensor,
					SensorManager.SENSOR_DELAY_UI);
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		stopSampling();
	}


	@Override
	public boolean onTouch(View v, MotionEvent event) {

		if (v == chartView && ticker != null && channel != null) {

			stopSampling();
		}
		return v.onTouchEvent(event);
	}


	protected void onTick(SensorEvent currentEvent) {

		if (xTick == 0) {

			configure(currentEvent);
			setContentView(chartView);
		}

		if (xTick > renderer.getXAxisMax()) {
			renderer.setXAxisMax(xTick);
			renderer.setXAxisMin(++lastMinX);
		}

		fitYAxis(currentEvent);

		for (int i = 0; i < channel.length; i++) {
			if (channel[i] != null) {
				channel[i].add(xTick, currentEvent.values[i]);
			}
		}

		xTick++;

		switch (currentEvent.accuracy) {
			case SensorManager.SENSOR_STATUS_ACCURACY_HIGH: {
				renderer.setChartTitle(getString(R.string.sensor_accuracy_high));
				break;
			}
			case SensorManager.SENSOR_STATUS_ACCURACY_MEDIUM: {
				renderer.setChartTitle(getString(R.string.sensor_accuracy_medium));
				break;
			}
			case SensorManager.SENSOR_STATUS_ACCURACY_LOW: {
				renderer.setChartTitle(getString(R.string.sensor_accuracy_low));
				break;
			}
			default: {
				renderer.setChartTitle(getString(R.string.sensor_accuracy_unreliable));
				break;
			}
		}
		chartView.repaint();
	}


	private void stopSampling() {
		try {
			sensorManager.unregisterListener((SensorEventListener) ticker);
			ticker.interrupt();
			ticker.join();
			ticker = null;
			Toast.makeText(this, R.string.msg_stopped, Toast.LENGTH_SHORT).show();
		}
		catch (Exception e) {
		}
	}


	private void fitYAxis(SensorEvent event) {
		double min = renderer.getYAxisMin(), max = renderer.getYAxisMax();
		for (int i = 0; i < channel.length; i++) {
			if (event.values[i] < min) {
				min = event.values[i];
			}
			if (event.values[i] > max) {
				max = event.values[i];
			}
		}
		float sum = 0;
		for (int i = 0; i < event.values.length; i++) {
			sum += event.values[i];
		}
		double half = 0;
		if (xTick == 0 && sum == event.values[0] * event.values.length) {

			half = event.values[0] * 0.5 + 1;
		}
		renderer.setYAxisMax(max + half);
		renderer.setYAxisMin(min - half);
	}


	private void configure(SensorEvent event) {
		String[] channelNames = new String[event.values.length];
		channel = new XYSeries[event.values.length];
		for (int i = 0; i < channelNames.length; i++) {
			channelNames[i] = getString(R.string.channel_default) + i;
		}

		switch (event.sensor.getType()) {
			case Sensor.TYPE_ACCELEROMETER: {
				channelNames[0]=getString(R.string.channel_x_axis);
				channelNames[1]=getString(R.string.channel_y_axis);
				channelNames[2]=getString(R.string.channel_z_axis);
				renderer.setYTitle(getString(R.string.unit_acceleration));
				break;
			}
			case Sensor.TYPE_GRAVITY: {
				channelNames[0]=getString(R.string.channel_x_axis);
				channelNames[1]=getString(R.string.channel_y_axis);
				channelNames[2]=getString(R.string.channel_z_axis);
				renderer.setYTitle(getString(R.string.unit_acceleration));
				break;
			}
			case Sensor.TYPE_GYROSCOPE: {
				channelNames[0]=getString(R.string.channel_x_axis);
				channelNames[1]=getString(R.string.channel_y_axis);
				channelNames[2]=getString(R.string.channel_z_axis);
				renderer.setYTitle(getString(R.string.unit_gyro));
				break;
			}
			case Sensor.TYPE_LIGHT: {
				channel = new XYSeries[1];
				channelNames[0]=getString(R.string.channel_light);
				renderer.setYTitle(getString(R.string.unit_light));
				break;
			}
			case Sensor.TYPE_LINEAR_ACCELERATION: {
				channelNames[0]=getString(R.string.channel_x_axis);
				channelNames[1]=getString(R.string.channel_y_axis);
				channelNames[2]=getString(R.string.channel_z_axis);
				renderer.setYTitle(getString(R.string.unit_acceleration));
				break;
			}
			case Sensor.TYPE_MAGNETIC_FIELD: {
				channelNames[0]=getString(R.string.channel_x_axis);
				channelNames[1]=getString(R.string.channel_y_axis);
				channelNames[2]=getString(R.string.channel_z_axis);
				renderer.setYTitle(getString(R.string.unit_magnetic));
				break;
			}
			case Sensor.TYPE_PRESSURE: {
				channel = new XYSeries[1];
				channelNames[0]=getString(R.string.channel_pressure);
				renderer.setYTitle(getString(R.string.unit_pressure));
				break;
			}
			case Sensor.TYPE_PROXIMITY: {
				channel = new XYSeries[1];
				channelNames[0]=getString(R.string.channel_distance);
				renderer.setYTitle(getString(R.string.unit_distance));
				break;
			}
			case Sensor.TYPE_ROTATION_VECTOR: {
				channelNames[0]=getString(R.string.channel_x_axis);
				channelNames[1]=getString(R.string.channel_y_axis);
				channelNames[2]=getString(R.string.channel_z_axis);
				break;
			}
			case Sensor.TYPE_ORIENTATION: {
				channelNames[0]=getString(R.string.channel_azimuth);
				channelNames[1]=getString(R.string.channel_pitch);
				channelNames[2]=getString(R.string.channel_roll);
				break;
			}
			case 7:
			case 13: {

				renderer.setYTitle(getString(R.string.unit_temperature));
				break;
			}
		}

		int[] colors = { Color.RED, Color.GREEN, Color.BLUE, Color.GREEN,
				Color.MAGENTA, Color.CYAN };
		for (int i = 0; i < channel.length; i++) {
			channel[i] = new XYSeries(channelNames[i]);
			sensorData.addSeries(channel[i]);
			XYSeriesRenderer r = new XYSeriesRenderer();
			r.setColor(colors[i % colors.length]);
			renderer.addSeriesRenderer(r);
		}
	}

}
