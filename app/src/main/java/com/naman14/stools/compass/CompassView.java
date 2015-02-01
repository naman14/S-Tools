

package com.naman14.stools.compass;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

public class CompassView extends ImageView {
    private float mDirection;
    private Drawable compass;

    public CompassView(Context context) {
        super(context);
        mDirection = 0.0f;
        compass = null;
    }

    public CompassView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mDirection = 0.0f;
        compass = null;
    }

    public CompassView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mDirection = 0.0f;
        compass = null;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (compass == null) {
            compass = getDrawable();
            compass.setBounds(0, 0, getWidth(), getHeight());
        }

        canvas.save();
        canvas.rotate(mDirection, getWidth() / 2, getHeight() / 2);
        compass.draw(canvas);
        canvas.restore();
    }

    public void updateDirection(float direction) {
        mDirection = direction;
        invalidate();
    }

}
