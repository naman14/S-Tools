

package com.naman14.stools.sensors;

import android.content.Context;
import android.hardware.Sensor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.naman14.stools.R;

import java.util.List;


public class SensorAdapter extends ArrayAdapter<Sensor> {

  public SensorAdapter(Context context, int textViewResourceId, List<Sensor> sensors) {
    super(context, textViewResourceId, sensors);
  }
  
  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    View ret;
    
    Sensor sensor = getItem(position);
    LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    ret = inflater.inflate(R.layout.sensor_item,null);
    ((TextView)ret.findViewById(R.id.sensor_name)).setText(sensor.getName());
    
    TextView description = (TextView)ret.findViewById(R.id.sensor_description);
    ImageView icon = (ImageView)ret.findViewById(R.id.sensor_icon);
    
    switch(sensor.getType()) {

      case 1: {
        icon.setImageResource(R.drawable.ic_sensor_1);
        description.setText(R.string.sensor_desc_1);
        break;
      }
      case 2: {
        icon.setImageResource(R.drawable.ic_sensor_2);
        description.setText(R.string.sensor_desc_2);
        break;
      }
      case 3: {
        icon.setImageResource(R.drawable.ic_sensor_3);
        description.setText(R.string.sensor_desc_3);
        break;
      }
      case 4: {
        icon.setImageResource(R.drawable.ic_sensor_4);
        description.setText(R.string.sensor_desc_4);
        break;
      }
      case 5: {
        icon.setImageResource(R.drawable.ic_sensor_5);
        description.setText(R.string.sensor_desc_5);
        break;
      }
      case 6: {
        icon.setImageResource(R.drawable.ic_sensor_6);
        description.setText(R.string.sensor_desc_6);
        break;
      }
      case 7: {

        icon.setImageResource(R.drawable.ic_sensor_13);
        description.setText(R.string.sensor_desc_13);
        break;
      }
      case 8: {
        icon.setImageResource(R.drawable.ic_sensor_8);
        description.setText(R.string.sensor_desc_8);
        break;
      }
      case 9: {
        icon.setImageResource(R.drawable.ic_sensor_9);
        description.setText(R.string.sensor_desc_9);
        break;
      }
      case 10: {
        icon.setImageResource(R.drawable.ic_sensor_10);
        description.setText(R.string.sensor_desc_10);
        break;
      }
      case 11: {
        icon.setImageResource(R.drawable.ic_sensor_11);
        description.setText(R.string.sensor_desc_11);
        break;
      }
      case 12: {
        icon.setImageResource(R.drawable.ic_sensor_12);
        description.setText(R.string.sensor_desc_12);
        break;
      }
      case 13: {
        icon.setImageResource(R.drawable.ic_sensor_13);
        description.setText(R.string.sensor_desc_13);
        break;
      }
      default: {

      }
    }
    
    return ret;
  }
}
