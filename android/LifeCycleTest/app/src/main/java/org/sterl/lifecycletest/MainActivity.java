package org.sterl.lifecycletest;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import org.sterl.lifecycletest.dialog.DialogActivity;
import org.sterl.lifecycletest.dialogfragment.DialogFragmentActivity;
import org.sterl.lifecycletest.service.ExampleIntentService;

import java.util.Date;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void goDialogFragment(View view) {
        startActivity(new Intent(this, DialogFragmentActivity.class));
    }
    public void goDialog(View view) {
        startActivity(new Intent(this, DialogActivity.class));
    }
    public void goCoordinator(View view) {
        startActivity(new Intent(this, CoordinatorLayoutActivity.class));
    }

    public void doIntentService(View view) {
        doIntentService(false);
    }

    public void doIntentServiceAlive(View view) {
        doIntentService(true);
    }

    private void doIntentService(final boolean alive) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                Intent intent = new Intent(MainActivity.this, ExampleIntentService.class);
                intent.putExtra(ExampleIntentService.EXTRA_MESSAGE, alive ? "1. Alive "  + new Date() : "1. Stop at once " + new Date());
                intent.putExtra(ExampleIntentService.EXTRA_KEEP_ALIVE, alive);
                startService(intent);

                try {
                    Thread.sleep(500);
                } catch (InterruptedException ignored) {}

                intent = new Intent(MainActivity.this, ExampleIntentService.class);
                intent.putExtra(ExampleIntentService.EXTRA_MESSAGE, alive ? "2. Alive "  + new Date() : "2. Stop at once: " + new Date());
                intent.putExtra(ExampleIntentService.EXTRA_KEEP_ALIVE, alive);
                startService(intent);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                // update the UI (this is executed on UI thread)
                super.onPostExecute(aVoid);
            }
        }.execute();


    }
}
