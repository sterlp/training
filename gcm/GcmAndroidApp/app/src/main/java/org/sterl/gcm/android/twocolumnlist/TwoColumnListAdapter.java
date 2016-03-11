package org.sterl.gcm.android.twocolumnlist;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

public class TwoColumnListAdapter extends BaseAdapter {

    private List<TwoColumnEntry> values = new ArrayList<>();

    public static class TwoColumnEntry {
        public final String label;
        public String value;

        public TwoColumnEntry(String label, String value) {
            this.label = label;
            this.value = value;
        }
    }

    @Override
    public int getCount() {
        return values.size();
    }

    @Override
    public TwoColumnEntry getItem(int position) {
        return values.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TwoColumnEntry entry = getItem(position);
        TwoColumnView view;
        if (convertView != null) {
            view = (TwoColumnView)convertView;
        } else {
            view = TwoColumnView_.build(parent.getContext());
        }
        view.set(entry.label, entry.value);
        return view;
    }

    public TwoColumnListAdapter add(String label, String value) {
        TwoColumnEntry entry = getEntry(label);
        if (entry == null) {
            values.add(new TwoColumnEntry(label, value));
            notifyDataSetChanged();
        } else {
            if (entry.value != null && !entry.value.equals(value)) {
                entry.value = value;
                notifyDataSetChanged();
            }
        }
        return this;
    }

    public TwoColumnEntry getEntry(String label) {
        for (TwoColumnEntry e : values) {
            if (label.equals(e.label)) return e;
        }
        return null;
    }
}