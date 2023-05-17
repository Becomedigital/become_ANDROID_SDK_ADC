package com.example.demosdk;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.RequiresApi;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.becomedigital.sdk.identity.becomedigitalsdk.callback.BecomeCallBackManager;
import com.becomedigital.sdk.identity.becomedigitalsdk.callback.BecomeInterfaseCallback;
import com.becomedigital.sdk.identity.becomedigitalsdk.callback.BecomeResponseManager;
import com.becomedigital.sdk.identity.becomedigitalsdk.callback.LoginError;
import com.becomedigital.sdk.identity.becomedigitalsdk.models.BDIVConfig;
import com.becomedigital.sdk.identity.becomedigitalsdk.models.ResponseIV;

public class IdentificationActivity extends TimerActivity implements SensorEventListener, View.OnTouchListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    private final BecomeCallBackManager mCallbackManager = BecomeCallBackManager.createNew ( );
    // sensor manager.
    private static Sensor mSensorAccelerometer;
    private static Sensor mSensorMagnetometer;
    // gesture detector
    private GestureDetector gestureDetector, gestureDetectorSingle;
    // Current data from accelerometer & magnetometer.  The arrays hold values
// for X, Y, and Z.
    private float[] mAccelerometerData = new float[3];
    private float[] mMagnetometerData = new float[3];
    // System display. Need this for determining rotation.
    private Display mDisplay;
    // System sensor manager instance.
    private SensorManager mSensorManager;
    public static Button btnReloadQR;
    public ResponseIV responseIV;
    public String idUser;

    public ResponseIV getResponseIV() {
        return responseIV;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_identification);
        Bundle extras = getIntent().getExtras();
        InitialSetups();
        if (extras != null) {
            responseIV = (ResponseIV) extras.getSerializable("responseIV");
            idUser = extras.getString("idUser");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        starTimer();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mSensorManager.unregisterListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopTimer();
    }

    /**
     * cargas iniciales
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    protected void InitialSetups() {
        //fragment manager - se inicia con identidad digital
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new IdentificationFragment()).commit();

        //SensorManager
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensorAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorMagnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        mDisplay = wm.getDefaultDisplay();
        //define el servicio de tiempo como activo
        serviceBound = true;
        String validatiopnTypes = "VIDEO/PASSPORT/DNI/LICENSE";
        String clientSecret = "";
        String clientId = "";
        String contractId = "";
        btnReloadQR = findViewById(R.id.btnReloadQR);
        btnReloadQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                BecomeResponseManager.getInstance().startAutentication(IdentificationActivity.this,
                        new BDIVConfig(clientId,
                                clientSecret,
                                contractId,
                                validatiopnTypes,
                                true,
                                null,
                                idUser
                        ));

            }
        });
        ImageButton imgBtnBack = findViewById(R.id.imgBtnBack);
        imgBtnBack.setOnClickListener(v -> IdentificationActivity.super.onBackPressed());

        BecomeResponseManager.getInstance().registerCallback(mCallbackManager, new BecomeInterfaseCallback() {
            @Override
            public void onSuccess(final ResponseIV responseIV) {
                IdentificationFragment.timeOver = false;
                IdentificationFragment.loadQR();
                stopTimer();
                starTimer();
            }

            @Override
            public void onCancel() {
                Log.d("cancel", "cancel by user");
            }

            @Override
            public void onError(LoginError pLoginError) {
                IdentificationFragment.timeOver = false;
                IdentificationFragment.loadQR();
                stopTimer();
                starTimer();
            }
        });
    }

    //region movement sensors
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        int sensorType = sensorEvent.sensor.getType();

        switch (sensorType) {
            case Sensor.TYPE_ACCELEROMETER:
                mAccelerometerData = sensorEvent.values.clone();
                if (sensorEvent.values[0] < -9) {
                    IdentificationFragment.shakeFlip();
                    Log.d(TAG, "shake x");
                }
//                Log.d (TAG, "Acelerotemeter: x " + sensorEvent.values[0] + ", y " + sensorEvent.values[1] + "z " + sensorEvent.values[2]);
                break;
            case Sensor.TYPE_MAGNETIC_FIELD:
                mMagnetometerData = sensorEvent.values.clone();
                break;
            default:
                return;
        }

        float[] rotationMatrix = new float[9];
        boolean rotationOK = SensorManager.getRotationMatrix(rotationMatrix,
                null, mAccelerometerData, mMagnetometerData);

        float[] rotationMatrixAdjusted = new float[9];
        switch (mDisplay.getRotation()) {
            case Surface.ROTATION_0:
                rotationMatrixAdjusted = rotationMatrix.clone();
                break;
            case Surface.ROTATION_90:
                SensorManager.remapCoordinateSystem(rotationMatrix,
                        SensorManager.AXIS_Y, SensorManager.AXIS_MINUS_X,
                        rotationMatrixAdjusted);
                break;
            case Surface.ROTATION_180:
                SensorManager.remapCoordinateSystem(rotationMatrix,
                        SensorManager.AXIS_MINUS_X, SensorManager.AXIS_MINUS_Y,
                        rotationMatrixAdjusted);
                break;
            case Surface.ROTATION_270:
                SensorManager.remapCoordinateSystem(rotationMatrix,
                        SensorManager.AXIS_MINUS_Y, SensorManager.AXIS_X,
                        rotationMatrixAdjusted);
                break;
        }

        // Get the orientation of the device (azimuth, pitch, roll) based
        // on the rotation matrix. Output units are radians.
        float orientationValues[] = new float[3];
        if (rotationOK) {
            SensorManager.getOrientation(rotationMatrixAdjusted,
                    orientationValues);
            IdentificationFragment.setAlpha(orientationValues);
        }
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
//endregion

    //region slide gestures
    public static final class GestureListener extends GestureDetector.SimpleOnGestureListener {

        private static final int SWIPE_THRESHOLD = 100;
        private static final int SWIPE_VELOCITY_THRESHOLD = 100;

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            boolean result = false;
            float orientationValues[] = new float[3];
            try {
                float diffY = e2.getY() - e1.getY();
                float diffX = e2.getX() - e1.getX();
                orientationValues[1] = diffY / 1000;
                orientationValues[2] = diffX / 1000;

                Log.d("gesture x", Float.toString(orientationValues[1]));
                Log.d("gesture y", Float.toString(orientationValues[2]));
                onSwipe(orientationValues);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            return result;
        }

    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int action = event.getAction();
        int x = (int) event.getX();
        int y = (int) event.getY();
        if (gestureDetectorSingle.onTouchEvent(event)) {
            IdentificationFragment.onClickToFlip();
            return true;
        } else {
            return gestureDetector.onTouchEvent(event);
        }
    }

    public static void onSwipe(float[] diff) {
//        IdentificationFragment.setAlpha (diff);
    }

    public void setlistenersAndActivateSensors() {

        if (mSensorAccelerometer != null) {
            mSensorManager.registerListener(this, mSensorAccelerometer,
                    SensorManager.SENSOR_DELAY_NORMAL);
        }
        if (mSensorMagnetometer != null) {
            mSensorManager.registerListener(this, mSensorMagnetometer,
                    SensorManager.SENSOR_DELAY_NORMAL);
        }

        //activa los gestos de single tap para activar flip los cuales siempre se utilizaran
        gestureDetector = new GestureDetector(getApplicationContext(), new GestureListener());
        gestureDetectorSingle = new GestureDetector(this, new SingleTapConfirm());
        IdentificationFragment.imgFront.setOnTouchListener(this);
    }

    private class SingleTapConfirm extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onSingleTapUp(MotionEvent event) {
            return true;
        }
    }
//endregion
}