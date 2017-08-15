package org.sterl.gcm.android.service;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.gcm.GcmListenerService;

/**
 * Service which receives messages from the backend.
 */
public class GcmMessageReceiver extends GcmListenerService {
    private static final String TAG = GcmMessageReceiver.class.getSimpleName();

    public static final String BROADCAST_GCM_MESSAGE_RECEIVED = "org.sterl.gcm.BROADCAST_GCM_MESSAGE_RECEIVED";
    public static final String BROADCAST_EXTRA_FROM = "BROADCAST_GCM_MESSAGE_RECEIVED_EXTRA_FROM";
    public static final String BROADCAST_EXTRA_TEXT = "BROADCAST_GCM_MESSAGE_RECEIVED_EXTRA_TEXT";
    public static final String BROADCAST_EXTRA_NOTIFICATION = "BROADCAST_GCM_MESSAGE_RECEIVED_EXTRA_NOTIFICATION";

    @Override
    public void onMessageReceived(String from, Bundle data) {

        Log.d(TAG, "GCM Message received --> from: " + from + " data: " + data);

        final Intent i =  new Intent(BROADCAST_GCM_MESSAGE_RECEIVED);
        i.putExtra(BROADCAST_EXTRA_FROM, from);
        i.putExtra(BROADCAST_EXTRA_TEXT, data.getString("text"));
        i.putExtra(BROADCAST_EXTRA_NOTIFICATION, data.getBundle("notification"));
        this.sendBroadcast(i);
    }
}