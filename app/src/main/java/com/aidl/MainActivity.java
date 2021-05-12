package com.aidl;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.orientation.AIDLService;
import com.orientation.ICallback;
import com.orientation.IMyAidlInterface;

public class MainActivity extends AppCompatActivity implements ServiceConnection {

    private TextView textView;
    private IMyAidlInterface binder = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView);

        Intent intent = new Intent(getApplicationContext(), AIDLService.class);
        bindService(intent, this, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (binder != null) {
            try {
                binder.unregisterCallback(mCallback);
            } catch (RemoteException e) {
                Log.e("TAG", "", e);
            }
        }
        unbindService(this);
        binder = null;
    }

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        binder = IMyAidlInterface.Stub.asInterface(iBinder);
        try {
            binder.registerCallback(mCallback);
            binder.start();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {

    }


    private final ICallback mCallback = new ICallback.Stub() {

        @Override
        public void showResult(float pitch, float roll) {
            runOnUiThread(() -> {
                textView.setText("pitch : " + pitch + "\n roll : " + roll);
            });
        }
    };
}