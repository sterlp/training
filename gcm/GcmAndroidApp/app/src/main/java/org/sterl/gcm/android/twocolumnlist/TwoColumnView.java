package org.sterl.gcm.android.twocolumnlist;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;
import org.sterl.gcm.android.R;

@EViewGroup(R.layout.two_column_list_item)
public class TwoColumnView extends LinearLayout {

    @ViewById protected TextView txtLabel;
    @ViewById protected TextView txtValue;

    public TwoColumnView(Context context) {
        super(context);
    }

    public TwoColumnView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void set(String label, String value) {
        this.txtLabel.setText(label);
        this.txtValue.setText(value);
    }
}