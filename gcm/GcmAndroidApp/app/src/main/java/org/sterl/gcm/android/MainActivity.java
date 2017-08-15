package org.sterl.gcm.android;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.iid.InstanceID;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Receiver;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.rest.RestService;
import org.androidannotations.annotations.sharedpreferences.Pref;
import org.sterl.gcm.android.backend.GcmClient;
import org.sterl.gcm.android.backend.RestBackend;
import org.sterl.gcm.android.service.GcmMessageReceiver;
import org.sterl.gcm.android.twocolumnlist.TwoColumnListAdapter;

import java.io.IOException;

@EActivity(R.layout.activity_main)
public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    @ViewById Toolbar toolbar;
    @ViewById ListView listView;
    private TwoColumnListAdapter listAdapter = new TwoColumnListAdapter();

    @Pref GcmPreferences_ gcmPreferences;
    @RestService RestBackend restBackend;

    private Handler handler;

    @AfterViews
    protected void init() {
        setSupportActionBar(toolbar);

        listView.setAdapter(listAdapter);

        final long start = System.currentTimeMillis();
        listAdapter.add("Instance Id", InstanceID.getInstance(this).getId());
        Log.d(TAG, "InstanceID time: " + (System.currentTimeMillis() - start) + "ms");

    }
    @Click(R.id.fab)
    protected void sendMessage(final View view) {
        new SendMessageDialogFragment().show(getFragmentManager(), "send-message-dialog");
    }

    @Override
    protected void onResume() {
        super.onResume();
        handler =  new Handler(Looper.getMainLooper());
        generateToken();
    }

    protected void generateToken() {
        generateToken(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "Adding token: " + gcmPreferences.token().get());
                listAdapter.add("Token", gcmPreferences.token().get());
            }
        }, new Runnable() {
            @Override
            public void run() {
                Snackbar.make(listView, "Failed to generate Token", Snackbar.LENGTH_INDEFINITE)
                        .setAction("RETRY", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                generateToken();
                            }
                        }).show();
            }
        });
    }

    @Receiver(actions = GcmMessageReceiver.BROADCAST_GCM_MESSAGE_RECEIVED, registerAt = Receiver.RegisterAt.OnResumeOnPause)
    protected void gcmMessageReceived(
            @Receiver.Extra(GcmMessageReceiver.BROADCAST_EXTRA_TEXT) String text,
            @Receiver.Extra(GcmMessageReceiver.BROADCAST_EXTRA_NOTIFICATION) Bundle bundle) {
        if (bundle == null) {
            Toast.makeText(this, "GCM Message: " + text, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "GCM Notification: " + bundle.getString("body"), Toast.LENGTH_LONG).show();
        }
    }


    @Background
    protected void generateToken(Runnable onSuccess, Runnable onFailed) {
        try {
            String token;
            //deleteToken();

            if (gcmPreferences.registered().get()) {
                token = gcmPreferences.token().get();
            } else {
                Log.d(TAG, "generateToken... ");
                token = InstanceID.getInstance(this).getToken(getString(R.string.gcm_project_number), null);
                gcmPreferences.edit().registered().put(true).token().put(token).apply();
            }
            restBackend.addClient(new GcmClient(token)); // post it to the server ...
            if (onSuccess != null) handler.post(onSuccess);
            Log.d(TAG, "generateToken... success.");
        } catch (Exception e) {
            Log.w(TAG, "Failed to generate the message sender token ...", e);
            gcmPreferences.edit().registered().put(false).token().put(null).apply();
            if (null != onFailed) handler.post(onFailed);
        }
    }

    protected void deleteToken() throws IOException {
        if (gcmPreferences.registered().get()) {
            final String token = gcmPreferences.token().get();
            Log.d(TAG, "delete Token... : " + token);
            InstanceID.getInstance(this).deleteToken(token, null);
            gcmPreferences.edit().registered().put(false);
        }
    }
}
