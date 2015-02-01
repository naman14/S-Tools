

package com.naman14.stools.sensors;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;


class Ticker extends Thread implements SensorEventListener {


  private SensorEvent currentEvent;


  private SensorGraphActivity activity;
  

  private Ticker worker;


  private static final int SLEEPTIME = (int) 1000/SensorGraphActivity.SAMPLERATE;

  public Ticker(SensorGraphActivity activity) {
    worker = new Ticker();
    worker.activity = activity;
    this.activity=activity;
  }
  

  private Ticker() {}
  

  public void onAccuracyChanged(Sensor sensor, int accuracy) {
  }


  public void onSensorChanged(SensorEvent event) {
    worker.currentEvent = event;
  }
  
  @Override
  public void run() {

    if (worker!=null) {

      try {
        while(true) {
          Thread.sleep(SLEEPTIME);
          activity.runOnUiThread(worker);
        }
      }
      catch (Exception e) {

      }
    }
    else {

      if (currentEvent!=null) {
        activity.onTick(currentEvent);
      }
    }
  }
  
  
}
