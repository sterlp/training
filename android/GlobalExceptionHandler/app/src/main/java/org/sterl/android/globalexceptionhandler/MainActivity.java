package org.sterl.android.globalexceptionhandler;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends BaseActivity {

    private ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        listView = (ListView)findViewById(R.id.listView);

        findViewById(R.id.cmdAuthError).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                throw new MyAuthException(); // we just fail with our custom exception ...
            }
        });
        findViewById(R.id.cmdRandomError).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (System.currentTimeMillis() % 2 == 0) {
                    throw new IllegalStateException("This state is not allowed.");
                } else {
                    throw new IllegalArgumentException("You can't click this Button now.");
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_list_item_1,
                LoggingExceptionHandler.readExceptions(this));
        listView.setAdapter(adapter);
    }
}