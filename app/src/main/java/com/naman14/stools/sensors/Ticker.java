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
