package org.sterl.sliderdemo;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.SeekBar;
import android.widget.Toast;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_main)
public class MainActivity extends AppCompatActivity {

    @ViewById SeekBar seekBarSlider;

    @AfterViews
    protected void init() {
        //
        // Using the seek bar
        //
        seekBarSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            boolean open = true;
            /**
             * User needs to move the bar at least by 80%
             */
            private static final int MIN_MOVE = 80;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    if (seekBar.getProgress() >= MIN_MOVE) {
                        seekBar.setThumb(ContextCompat.getDrawable(MainActivity.this, R.drawable.slider_locked));
                        seekBar.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.seekbar_bg_locked));
                    } else if (seekBar.getProgress() <= (100 - MIN_MOVE)) {
                        seekBar.setThumb(ContextCompat.getDrawable(MainActivity.this, R.drawable.slider_open));
                        seekBar.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.seekbar_bg_open));
                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // closed?
                if (open && seekBar.getProgress() >= MIN_MOVE) {
                    open = false;
                    // todo event ...
                    Toast.makeText(MainActivity.this, "SeekBar Locked ...", Toast.LENGTH_SHORT).show();
                } else if (!open && seekBar.getProgress() <= (100 - MIN_MOVE)) {
                    // todo event ...
                    open = true;
                    Toast.makeText(MainActivity.this, "SeekBar Open ...", Toast.LENGTH_SHORT).show();
                }
                if (open) {
                    seekBar.setProgress(0);
                    seekBar.setThumb(ContextCompat.getDrawable(MainActivity.this, R.drawable.slider_open));
                    seekBar.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.seekbar_bg_open));
                } else {
                    seekBar.setProgress(100);
                    seekBar.setThumb(ContextCompat.getDrawable(MainActivity.this, R.drawable.slider_locked));
                    seekBar.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.seekbar_bg_locked));
                }
            }
        });
    }
}
