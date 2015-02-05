package com.naman14.stools.fragments;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.naman14.stools.R;
import com.naman14.stools.colorpicker.RalColor;
import com.naman14.stools.colorpicker.Utils;
import com.shamanland.fab.FloatingActionButton;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by naman on 30/01/15.
 */
public class ColorPickerFragment extends Fragment {

    private static final String APPLICATION_NAME = "ColorPicker";

    private static final int CAPTURE_ACTIVITY_REQUEST_CODE = 100;
    private static final int SELECT_ACTIVITY_REQUEST_CODE = 200;

    private static final String KEY_PHOTO_PATH = "photoUri";
    private static final String KEY_COLOR_COMPONENTS = "rgb";
    private static final CharSequence KEY_COLOR_COMPONENTS2 = "rgb2";

    private Uri photoUri;
    private ImageView imageView;
    private RalColor ralColor = null;
    private RalColor ralColor2;
    FloatingActionButton fab;

    FloatingActionButton fab2;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        final View v = inflater.inflate(R.layout.fragment_color_picker, container, false);

        final TextView hex =(TextView) v.findViewById(R.id.textViewHex);
        FloatingActionButton fab = (FloatingActionButton)v.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentGallery = new Intent();
                intentGallery.setType("image/*");
                intentGallery.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(
                        Intent.createChooser(
                                intentGallery,
                                getString(R.string.select_picture)),
                        SELECT_ACTIVITY_REQUEST_CODE);


            }


        });
        FloatingActionButton fab2 = (FloatingActionButton)v.findViewById(R.id.fab2);
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String hexString = hex.getText().toString();
                //removing "HEX:" from the string returned by getText
                hexString = hexString.replace("HEX:","");
                ClipboardManager _clipboard=
                        (ClipboardManager)
                                getActivity().getSystemService(Context.CLIPBOARD_SERVICE);


                ClipData clip=
                        ClipData.newPlainText("hex",hexString );
                _clipboard.setPrimaryClip(clip);
                Toast.makeText(getActivity().getApplicationContext(), hex.getText() + " Copied to Clipboard", Toast.LENGTH_SHORT).show();


            }


        });




        if (imageView == null) {
            imageView = (ImageView)v.findViewById(R.id.imageView);
            if (imageView != null) {
                ViewTreeObserver viewTreeObserver = imageView.getViewTreeObserver();
                viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

                    @SuppressWarnings("deprecation")
                    @Override
                    public void onGlobalLayout() {
                        if (photoUri != null) {
                            try {
                                showCapturedImage();
                                updateResultData();
                            } catch (FileNotFoundException e) {
                                Log.e(APPLICATION_NAME, "File ".
                                        concat(photoUri.getPath()).concat(" not found!"));
                            } catch (Exception e) {
                                // Ignore
                            }
                        }


                        imageView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    }
                });
            }
        }

        if (savedInstanceState != null) {
            String photoUriPath = savedInstanceState.getString(KEY_PHOTO_PATH);
            if (photoUriPath != null) {
                photoUri = Uri.fromFile(new File(photoUriPath));
            }

            if (savedInstanceState.containsKey(KEY_COLOR_COMPONENTS)) {
                ralColor = new RalColor(
                        savedInstanceState.getInt(KEY_COLOR_COMPONENTS));
            }
        }
        return v;

    }

    protected void updateResultData() {
        int index = 0;
        int red = Color.red(ralColor.getColor());
        int green = Color.green(ralColor.getColor());
        int blue = Color.blue(ralColor.getColor());


        index = ralColor.getIndex();
        String[] colorNames = getResources().getStringArray(R.array.color_names);

        TextView textViewColorName = (TextView) getView().findViewById(R.id.textViewColorName);
        try {
            textViewColorName.setText(colorNames[index]);
        } catch (ArrayIndexOutOfBoundsException e) {

            textViewColorName.setText(colorNames[colorNames.length-1]);
        }


        ImageView imageViewColor = (ImageView)getView().findViewById(R.id.imageViewColor);
        FloatingActionButton fab = (FloatingActionButton)getView().findViewById(R.id.fab);
        imageViewColor.setBackgroundColor(ralColor.getColor());

        TextView textViewRal = (TextView)getView().findViewById(R.id.textViewRal);
        textViewRal.setText(
                "RAL: ".concat(Integer.toString(ralColor.getCode(), 10)));

        TextView textViewRgb = (TextView)getView().findViewById(R.id.textViewRgb);
        textViewRgb.setText(
                "RGB: ".concat(Integer.toString(red , 10)).
                        concat(", ").concat(Integer.toString(green, 10)).
                        concat(", ").concat(Integer.toString(blue, 10)));

        TextView textViewHex = (TextView)getView().findViewById(R.id.textViewHex);
        textViewHex.setText(
                "HEX: #".concat(Utils.beautyHexString(Integer.toHexString(red))).
                        concat(Utils.beautyHexString(Integer.toHexString(green))).
                        concat(Utils.beautyHexString(Integer.toHexString(blue))));
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(APPLICATION_NAME, "onResume method entered");

        imageView = (ImageView) getView().findViewById(R.id.imageView);
        if (imageView != null) {
            ViewTreeObserver viewTreeObserver = imageView.getViewTreeObserver();
            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

                @SuppressWarnings("deprecation")
                @Override
                public void onGlobalLayout() {
                    if (photoUri != null) {
                        try {
                            showCapturedImage();
                            updateResultData();
                        } catch (FileNotFoundException e) {
                            Log.e(APPLICATION_NAME, "File ".
                                    concat(photoUri.getPath()).concat(" not found!"));
                        } catch (Exception e) {
                            // Ignore
                        }
                    }


                    imageView.setOnTouchListener(onTouchListener);
                    imageView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
            });
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(APPLICATION_NAME, "onStop method entered");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(APPLICATION_NAME, "onDestroy method entered");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(APPLICATION_NAME, "onSaveInstanceState");

        if (photoUri != null) {
            String realPath;
            try {
                realPath = getRealPathFromURI(photoUri);
            } catch (UnsupportedEncodingException e) {
                realPath = null;
            }
            outState.putString(KEY_PHOTO_PATH, realPath);
        }

        if (ralColor != null) {
            outState.putInt(KEY_COLOR_COMPONENTS, ralColor.getColor());
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {

        switch (menuItem.getItemId()) {



            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }

    View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {

            int action = motionEvent.getAction();
            switch(action) {
                case(MotionEvent.ACTION_DOWN):
                    int x = (int)motionEvent.getX();
                    int y = (int)motionEvent.getY();
                    int color;


                    try {
                        color = Utils.findColor(view, x, y);
                    } catch (NullPointerException e) {
                        return false;
                    }


                    if (ralColor == null) {
                        ralColor = new RalColor(color);
                    } else {
                        ralColor.setColor(color);
                    }

                    updateResultData();
            }
            return false;
        }
    };

    @Override
    public void onActivityResult(
            int requestCode,
            int resultCode,
            Intent data) {
        if (requestCode == CAPTURE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                Log.i(APPLICATION_NAME, "Capture result OK");
                try {
                    showCapturedImage();
                } catch (FileNotFoundException fileNotFoundException) {

                } catch (NullPointerException nullPointerException) {
                    imageView = (ImageView)getView().findViewById(R.id.imageView);
                    try {
                        showCapturedImage();
                    } catch (Exception exception) {
                        // Do nothing
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {

                Toast.makeText(getActivity(), R.string.action_canceled,
                        Toast.LENGTH_SHORT).show();
            } else {

            }
        }

        if (requestCode == SELECT_ACTIVITY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                Log.i(APPLICATION_NAME, "Select result OK");
                try {
                    photoUri = data.getData();
                    showCapturedImage();
                } catch (FileNotFoundException e) {

                } catch (NullPointerException e) {
                    imageView = (ImageView)getView().findViewById(R.id.imageView);
                    try {
                        showCapturedImage();
                    } catch (Exception exception) {

                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {

                Toast.makeText(getActivity(), R.string.action_canceled,
                        Toast.LENGTH_SHORT).show();
            } else {

            }
        }

        if (resultCode == Activity.RESULT_OK) {
            imageView.setOnTouchListener(onTouchListener);
        }
    }


    private void showCapturedImage() throws
            FileNotFoundException, NullPointerException {

        if (imageView == null) {
            throw new NullPointerException();
        }


        FrameLayout frameLayoutImage = (FrameLayout)getView().findViewById(R.id.frameLayoutImage);
        int targetW = frameLayoutImage.getWidth();
        int targetH = frameLayoutImage.getHeight();


        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(
                getActivity().getContentResolver().openInputStream(photoUri),
                null, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;


        int scaleFactor = 1;
        try {
            scaleFactor = Math.min(photoW/targetW, photoH/targetH);
        } catch (ArithmeticException arithmeticException) {
            Log.w(APPLICATION_NAME, "frameLayout not yet inflated, no scaling");
        }


        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        try {
            Bitmap bitmap = BitmapFactory.decodeStream(
                    getActivity().getContentResolver().openInputStream(photoUri),
                    null, bmOptions);

            int bitmapSize = bitmap.getRowBytes() * bitmap.getHeight();


            if ( bitmapSize > 20000000) {
                bmOptions.inSampleSize = 2;
                bitmap = BitmapFactory.decodeStream(
                        getActivity().getContentResolver().openInputStream(photoUri),
                        null, bmOptions);
            }

            imageView.setImageBitmap(bitmap);
        } catch (OutOfMemoryError e) {
            Log.e(APPLICATION_NAME, e.getLocalizedMessage());
        }

    }


    private static Uri getOutputMediaFileUri(){
        try {
            return Uri.fromFile(getOutputMediaFile());
        } catch(NullPointerException e) {
            return null;
        }
    }


    private static File getOutputMediaFile(){


        File mediaStorageDir = new File(
                Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_PICTURES), APPLICATION_NAME);


        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.e(APPLICATION_NAME, "failed to create directory");
                return null;
            }
        }

        // Create a media file pseudo random name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).
                format(new Date());
        File mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                "IMG_"+ timeStamp + ".jpg");

        return mediaFile;
    }


    @SuppressWarnings("deprecation")
    private String getRealPathFromURI(Uri contentUri) throws UnsupportedEncodingException {
        String realPath;

        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = getActivity().getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor == null) {
            realPath = contentUri.toString();
        } else {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            realPath = cursor.getString(column_index);
        }


        if (realPath.startsWith("file://")) {
            realPath = realPath.substring("file://".length());
        }
        return URLDecoder.decode(realPath, "UTF-8");
    }


}
