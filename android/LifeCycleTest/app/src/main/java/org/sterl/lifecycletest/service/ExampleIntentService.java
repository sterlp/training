package org.sterl.lifecycletest.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import java.util.concurrent.atomic.AtomicInteger;

public class ExampleIntentService extends IntentService {
    private static final String TAG = "ExampleIntentService";
    public static final String EXTRA_MESSAGE = "MESSAGE";
    public static final String EXTRA_KEEP_ALIVE = "ALIVE";
    private Handler uiHandler = new Handler(Looper.getMainLooper());

    private static final AtomicInteger CALLS_TO_THIS_SERVICE = new AtomicInteger(0);

    public ExampleIntentService() {
        super(TAG);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "create ...");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "destroy ...");
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        Log.d(TAG, "onStart called ... " + intent.getStringExtra(EXTRA_MESSAGE));
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        final int n = CALLS_TO_THIS_SERVICE.incrementAndGet();
        final String msg = intent.getStringExtra(EXTRA_MESSAGE) + " Call number: " + n;

        Log.d(TAG, "onHandleIntent called ... " + intent.getStringExtra(EXTRA_MESSAGE));

        try {
            Thread.sleep(250);
        } catch (InterruptedException ignored) {}

        uiHandler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
            }
        });

        // keep service alive
        if (intent.getBooleanExtra(EXTRA_KEEP_ALIVE, false)) {
            try {
                Thread.sleep(2500);
            } catch (InterruptedException ignored) {
                Log.w("ExampleIntentService", "Sleep interrupt.");
            }
        }
        Log.d("ExampleIntentService", "Ended " + msg);
    }
}
