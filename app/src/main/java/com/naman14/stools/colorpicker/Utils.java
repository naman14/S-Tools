
package com.naman14.stools.colorpicker;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.widget.ImageView;


public class Utils {

	public static String beautyHexString(String hexString) {
		if (hexString.length() < 2) {
			return "0".concat(hexString);
		} else {
			return hexString;
		}
	}
	

	public static int findColor(View view, int x, int y) 
	throws NullPointerException {
		
		int red = 0;
    	int green = 0;
    	int blue = 0;
    	int color = 0;
    	
    	int offset = 1; // 3x3 Matrix
    	int pixelsNumber = 0;
    	
    	int xImage = 0;
    	int yImage = 0;
    	

    	ImageView imageView = (ImageView)view;
    	BitmapDrawable bitmapDrawable = (BitmapDrawable)imageView.getDrawable();
    	Bitmap imageBitmap = bitmapDrawable.getBitmap();


    	xImage = (int)(x * ((double)imageBitmap.getWidth() / (double)imageView.getWidth()));
    	yImage = (int)(y * ((double)imageBitmap.getHeight() / (double)imageView.getHeight()));
    	

    	for (int i = xImage - offset; i <= xImage + offset; i++) {
    		for (int j = yImage - offset; j <= yImage + offset; j++) {
    			try {
        			color = imageBitmap.getPixel(i, j);
        			red += Color.red(color);
        			green += Color.green(color);
        			blue += Color.blue(color);
        			pixelsNumber += 1;
        		} catch(Exception e) {
        			//Log.w(TAG, "Error picking color!");
        		}	
    		}
    	}
    	red = red / pixelsNumber;
    	green = green / pixelsNumber;
    	blue = blue / pixelsNumber;
    	
    	return Color.rgb(red, green, blue); 
	}
}
