
package com.naman14.stools.colorpicker;

import android.graphics.Color;

public class RalColor {

	private int index = 0;

	private int color = 0;

	private double difference = 512;
	
	private static final int DEFAULT_COLOR_INDEX = 0;
	private static final int MAX_COLOR_DIFFERENCE = 512;
	

	public RalColor() {}
	


	public RalColor(int color) {
		this.setColor(color);
	}
	
	public int getCode() {
		if (this.index == 0) {
			return 0;
		} else {
			return RalSystem.code[this.index];
		}
	}
	

	public void setIndex(int index) {
		this.index = index;
	}


	public int getIndex() {
		return this.index;
	}


	public void setDifference(double difference) {
		this.difference = difference;
	}


	public double getDifference() {
		return this.difference;
	}


	public int getColor() {
		return this.color;
	}


	public void setColor(int color) {
		this.index = DEFAULT_COLOR_INDEX;
		this.color = color;
		this.difference = MAX_COLOR_DIFFERENCE;
		
		double differencetmp = 0;
		
		for (int i = 0; i < RalSystem.code.length; i++) {

			differencetmp = Math.sqrt(
				Math.pow(RalSystem.red[i] - Color.red(color), 2) +
				Math.pow(RalSystem.green[i] - Color.green(color), 2) +
				Math.pow(RalSystem.blue[i] - Color.blue(color), 2));
			
			if (differencetmp < this.difference) {
				this.difference = differencetmp;
				this.index = i;
			}
		}
	}
	
}
