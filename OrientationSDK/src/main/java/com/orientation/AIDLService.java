package com.orientation;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;

public class AIDLService extends Service implements SensorEventListener {
    private SensorManager mSensorManager;
    private Sensor mRotationSensor;

    private RemoteCallbackList<ICallback> mCallbacks = new RemoteCallbackList<>();

    private static final int SENSOR_DELAY = 8000; // 8ms
    private static final int FROM_RADS_TO_DEGS = -57;


    public AIDLService() {
    }


    IMyAidlInterface.Stub mBinder = new IMyAidlInterface.Stub() {

        @Override
        public void start() {

        }

        @Override
        public void registerCallback(ICallback cb) {
            if (cb != null) {
                mCallbacks.register(cb);
            }
        }

        @Override
        public void unregisterCallback(ICallback cb) {
            if (cb != null) {
                mCallbacks.unregister(cb);
            }
        }
    };


    @Override
    public void onCreate() {
        super.onCreate();

        mSensorManager = (SensorManager) getSystemService(Activity.SENSOR_SERVICE);
        mRotationSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        mSensorManager.registerListener(this, mRotationSensor, SENSOR_DELAY);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {
            update(event.values);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }


    private void update(float[] vectors) {
        float[] rotationMatrix = new float[9];
        SensorManager.getRotationMatrixFromVector(rotationMatrix, vectors);
        int worldAxisX = SensorManager.AXIS_X;
        int worldAxisZ = SensorManager.AXIS_Z;
        float[] adjustedRotationMatrix = new float[9];
        SensorManager.remapCoordinateSystem(rotationMatrix, worldAxisX, worldAxisZ, adjustedRotationMatrix);
        float[] orientation = new float[3];
        SensorManager.getOrientation(adjustedRotationMatrix, orientation);
        float pitch = orientation[1] * FROM_RADS_TO_DEGS;
        float roll = orientation[2] * FROM_RADS_TO_DEGS;

        updateCallBack(pitch, roll);
    }

    private void updateCallBack(float pitch, float roll) {
        int N = mCallbacks.beginBroadcast();
        try {
            for (int i = 0; i < N; i++) {
                // send a notification to each callback object
                mCallbacks.getBroadcastItem(i).showResult(pitch, roll);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        // End sending notifications
        mCallbacks.finishBroadcast();
    }
}