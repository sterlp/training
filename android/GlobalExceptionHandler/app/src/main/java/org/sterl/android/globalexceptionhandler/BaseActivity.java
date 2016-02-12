package org.sterl.android.globalexceptionhandler;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Not to be bothered to attach our Exception handler in each and every activity, we do it once
 * in a base activity.
 */
public abstract class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new MyExceptionHandler(BaseActivity.this);
    }
}
