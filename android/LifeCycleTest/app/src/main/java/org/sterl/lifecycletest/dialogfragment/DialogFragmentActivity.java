package org.sterl.lifecycletest.dialogfragment;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import org.sterl.lifecycletest.R;

public class DialogFragmentActivity extends AppCompatActivity implements MyDialogFragment.OnFragmentInteractionListener {
    private static final String TAG = DialogFragmentActivity.class.getSimpleName();

    final MyDialogFragment reusedFragment = MyDialogFragment.newInstance("reused fragment");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog_fragment);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //if (reusedFragment.isAdded()) reusedFragment.dismiss();
    }
    public void openReusedFragment(View view) {
        if (!reusedFragment.isAdded()) reusedFragment.show(getFragmentManager(), null);
    }

    public void doOpenFragment(View view) {
        Log.i(TAG, "doOpenFragment");
        MyDialogFragment dialogFragment = MyDialogFragment.newInstance("init");
        dialogFragment.show(getFragmentManager(), "dialog");
    }

    @Override
    public void onButtonDismiss(String value, boolean addToBackStack) {
        Log.i(TAG, "onFragmentInteraction: " + value + " addToBackStack: " + addToBackStack);
        if (reusedFragment.isVisible()) reusedFragment.dismiss();

        Fragment dialog = getFragmentManager().findFragmentByTag("dialog");
        if (dialog != null) {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.remove(dialog);
            if (addToBackStack) ft.addToBackStack(null);
            ft.commit();
        }
    }

    public void openFragmentTwice(View view) {
        final MyDialogFragment first = MyDialogFragment.newInstance("first");
        final MyDialogFragment second = MyDialogFragment.newInstance("second");
        first.show(getFragmentManager(), "dialog");
        second.show(getFragmentManager(), "dialog");

        Log.d(TAG, "First visible: " + first.isVisible());
        Log.d(TAG, "Second visible: " + second.isVisible());

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "Delay visible:");
                Log.d(TAG, "First visible: " + first.isVisible());
                Log.d(TAG, "Second visible: " + second.isVisible());
            }
        }, 1500);
    }
}