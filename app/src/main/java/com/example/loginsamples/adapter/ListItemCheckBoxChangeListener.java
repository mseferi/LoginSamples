package com.example.loginsamples.adapter;

import android.widget.CompoundButton;

import com.example.loginsamples.entity.ListItem;

import java.util.List;

public class ListItemCheckBoxChangeListener implements CompoundButton.OnCheckedChangeListener {
    private int position;
    private final TrackedViewType type;
    private final List<ListItem> items;

    public ListItemCheckBoxChangeListener(TrackedViewType type, List<ListItem> items) {
        this.type = type;
        this.items = items;
    }

    public void updatePosition(int position) {
        this.position = position;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (type == TrackedViewType.IS_BOLD) {
            items.get(position).setBold(isChecked);
        } else if (type == TrackedViewType.IS_PRINTED) {
            items.get(position).setPrintable(isChecked);
        } else if (type == TrackedViewType.IS_LINED) {
            items.get(position ).setLined(isChecked);
        }
    }
}
