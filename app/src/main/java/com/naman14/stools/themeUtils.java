package com.naman14.stools;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;


public class themeUtils extends Activity

{

private static int cTheme;

 public static final String MyPREFERENCES = "MyPrefs";


public final static int THEME1 = 0;

public final static int THEME2 = 1;

public final static int THEME3 = 2;

public final static int THEME4 = 3;

public final static int THEME5 = 4;

public final static int THEME6 = 5;



public static void changeToTheme(Activity activity, int theme)

{


SharedPreferences.Editor editor = activity.getSharedPreferences(MyPREFERENCES, MODE_PRIVATE).edit();
editor.putInt("theme",theme);
editor.commit();

activity.startActivity(new Intent(activity, activity.getClass()));


activity.finish();





}

public static void onActivityCreateSetTheme(Activity activity,ActionBar actionBar,Context context)

{

    SharedPreferences preferences = activity.getSharedPreferences(MyPREFERENCES,MODE_PRIVATE);
    cTheme=preferences.getInt("theme",0);

switch (cTheme)

{

default:

case THEME1:

activity.setTheme(R.style.AppTheme);
actionBar.setBackgroundDrawable(context.getResources().getDrawable(R.color.theme1));



break;

case THEME2:

activity.setTheme(R.style.AppTheme2);
actionBar.setBackgroundDrawable(context.getResources().getDrawable(R.color.theme2));


break;


case THEME3:

    activity.setTheme(R.style.AppTheme3);
    actionBar.setBackgroundDrawable(context.getResources().getDrawable(R.color.theme3));

break;

case THEME4:
    activity.setTheme(R.style.AppTheme4);
    actionBar.setBackgroundDrawable(context.getResources().getDrawable(R.color.theme4));

break;

case THEME5:

activity.setTheme(R.style.AppTheme5);
    actionBar.setBackgroundDrawable(context.getResources().getDrawable(R.color.theme5));
break;


case THEME6:

activity.setTheme(R.style.AppTheme6);
    actionBar.setBackgroundDrawable(context.getResources().getDrawable(R.color.theme6));
break;



}

}

}