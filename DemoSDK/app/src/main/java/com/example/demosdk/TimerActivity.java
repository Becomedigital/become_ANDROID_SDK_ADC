package com.example.demosdk;

import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.demosdk.utils.TimerService;

public class TimerActivity extends AppCompatActivity {

    private static final String TAG = TimerActivity.class.getSimpleName();

    private static TimerService timerService;
    protected static boolean serviceBound;
    public static String miliseconsTimer;
    public static String miliseconsTimer1d;
    // Handler to update the UI every second when the timer is running
    private final Handler mUpdateTimeHandler = new UIUpdateHandler(this, miliseconsTimer);

    // Message type for the handler
    private final static int MSG_UPDATE_TIME = 0;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        timerService = new TimerService();
        TimerActivity.initResources(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
//        if (Log.isLoggable (TAG, Log.VERBOSE)) {
//            Log.v (TAG, "Starting and binding service");
//        }
//        Intent i = new Intent (this, TimerService.class);
//        startService (i);
//        bindService (i, mConnection, 0);
    }

    public static String APP_NAME;

    public static void initResources(Context context) {
        miliseconsTimer = context.getResources().getString(R.string.milisecons_timer);
        miliseconsTimer1d = context.getResources().getString(R.string.secons_timer_1d);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    public void starTimer() {
        if (serviceBound && !timerService.isTimerRunning()) {
            if (Log.isLoggable(TAG, Log.VERBOSE)) {
                Log.v(TAG, "Starting timer");
            }
            timerService.startTimer();
            updateUIStartRun();
        }
    }


    public void stopTimer() {
        if (serviceBound && timerService.isTimerRunning()) {
            if (Log.isLoggable(TAG, Log.VERBOSE)) {
                Log.v(TAG, "Stopping timer");
            }
            timerService.stopTimer();
            updateUIStopRun();
        }

    }

    /**
     * Updates the UI when a run starts
     */
    private void updateUIStartRun() {
        mUpdateTimeHandler.sendEmptyMessage(MSG_UPDATE_TIME);
    }

    /**
     * Updates the UI when a run stops
     */
    private void updateUIStopRun() {
        mUpdateTimeHandler.removeMessages(MSG_UPDATE_TIME);
    }

    /**
     * Updates the timer readout in the UI; the service must be bound
     */
    public static void updateControl() {
        if (serviceBound) {
            IdentificationFragment.refreshControls(timerService.elapsedTime(), miliseconsTimer1d);
        }
    }

    /**
     * Callback for service binding, passed to bindService()
     */
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            if (Log.isLoggable(TAG, Log.VERBOSE)) {
                Log.v(TAG, "Service bound");
            }

            TimerService.RunServiceBinder binder = (TimerService.RunServiceBinder) service;
            timerService = binder.getService();
            serviceBound = true;
            // Ensure the service is not in the foreground when bound
            timerService.background();
            // Update the UI if the service is already running the timer
            if (timerService.isTimerRunning()) {
                updateUIStartRun();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            if (Log.isLoggable(TAG, Log.VERBOSE)) {
                Log.v(TAG, "Service disconnect");
            }
            serviceBound = false;
        }
    };

    /**
     * When the timer is running, use this handler to update
     * the UI every second to show timer progress
     */
    static class UIUpdateHandler extends Handler {


        UIUpdateHandler(TimerActivity activity, String miliseconsTimer) {

        }

        @Override
        public void handleMessage(Message message) {
            if (MSG_UPDATE_TIME == message.what) {
                if (Log.isLoggable(TAG, Log.VERBOSE)) {
                    Log.v(TAG, "updating time");
                }

                updateControl();
                sendEmptyMessageDelayed(MSG_UPDATE_TIME, Integer.parseInt(miliseconsTimer));
            }
        }
    }
}

